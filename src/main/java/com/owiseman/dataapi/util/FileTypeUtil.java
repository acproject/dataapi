package com.owiseman.dataapi.util;

import java.util.Map;

public class FileTypeUtil {
    private static final Map<String, String> MINE_TYPES = Map.of("pdf", "application/pdf",
        "png", "image/png",
        "jpg", "image/jpeg",
        "mp4", "video/mp4",
        "txt", "text/plain"
    );

    public static String detectPreviewType(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return MINE_TYPES.getOrDefault(ext, "application/octet-stream");
    }
}
