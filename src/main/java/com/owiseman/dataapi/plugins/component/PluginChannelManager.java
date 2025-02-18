package com.owiseman.dataapi.plugins.component;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class PluginChannelManager {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(PluginChannelManager.class);
    private final Selector selector;
    private final ExecutorService nioExecutor;

    public PluginChannelManager() throws IOException {
        this.selector = Selector.open();
        this.nioExecutor = Executors.newSingleThreadExecutor();
        startSelectorLoop();
    }

    // 启动Selector监听循环
    private void startSelectorLoop() {
        nioExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) continue;

                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        if (!key.isValid()) continue;
                        if (key.isConnectable()) {
                            handleConnection(key);
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 处理读事件
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String pluginId = key.attachment().toString();
        ByteBuffer buffer = ByteBuffer.allocate(1024); // 可以根据业务调整缓冲区大小
        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                log.info("插件断开连接: " + pluginId);
                channel.close();
                key.cancel();
                return;
            }

            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String message = new String(data, StandardCharsets.UTF_8);
                log.info("收到插件消息: " + pluginId + ": " + message);
                //  TODO: 将消息传递到业务层处理（如通过事件或回调）
                //  例如：pluginMessageHandler.onMessage(pluginId, message);
            }
        } catch (Exception e) {
            log.error("处理插件消息时发生错误: " + pluginId, e);
            channel.close();
            key.cancel();
        }
    }

    // 关闭资源（例如在Spring销毁时调用）
    public void shutdown() throws IOException {
        nioExecutor.shutdownNow();
        selector.close();
    }

    // 连接到插件进程（非阻塞）
    public void connectToPlugin(String host, int port, String pluginId) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new java.net.InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT, pluginId);
    }

    private void handleConnection(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.finishConnect()) {
            String pluginId = key.attachment().toString();
            channel.register(selector, SelectionKey.OP_READ, pluginId);
            log.info("与插件建立连接: " + pluginId);
        }
    }
}
