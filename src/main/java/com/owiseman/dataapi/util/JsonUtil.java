package com.owiseman.dataapi.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class JsonUtil {
     /**
     * 将JSON对象转换为字符串列表的映射
     *
     * @param jsonNode 输入的JSON节点，格式示例：
     * {
     *   "fruits": ["apple", "banana"],
     *   "colors": "red",
     *   "emptyField": null
     * }
     * @return 转换后的映射，示例：
     * {
     *   "fruits": ["apple", "banana"],
     *   "colors": ["red"],
     *   "emptyField": []
     * }
     */
    public static Map<String, List<String>> convertToMap(JsonNode jsonNode) {
        Map<String, List<String>> resultMap = new LinkedHashMap<>();

        if (jsonNode == null || !jsonNode.isObject()) {
            return resultMap;
        }

        jsonNode.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode valueNode = entry.getValue();

            List<String> values = new ArrayList<>();

            if (valueNode != null) {
                if (valueNode.isArray()) {
                    valueNode.forEach(element ->
                        values.add(element.isNull() ? null : element.asText())
                    );
                } else if (!valueNode.isNull()) {
                    values.add(valueNode.asText());
                }
            }

            resultMap.put(key, values);
        });

        return resultMap;
    }
}
