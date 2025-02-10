package com.owiseman.dataapi.util;

import java.util.Map;
import java.util.Set;

public class FileTypeUtil {
    private static final Map<String, String> MINE_TYPES = Map.of("pdf", "application/pdf",
        "png", "image/png",
        "jpg", "image/jpeg",
        "mp4", "video/mp4",
        "txt", "text/plain"
    );

    private static final Set<String> PREVIEWABLE_TYPES = Set.of(
           "pdf", "png", "jpg", "jpeg", "gif",
        "mp4", "webm", "ogg", "mp3", "wav", "txt");

    public static boolean isPreviewable(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return PREVIEWABLE_TYPES.contains(ext);
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String detectPreviewType(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return MINE_TYPES.getOrDefault(ext, "application/octet-stream");
    }
}
