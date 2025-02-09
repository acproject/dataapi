package com.owiseman.dataapi.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(UUID uuid) {
        if (uuid == null) {
            return null;
        } else {
            return ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits())
                    .putLong(uuid.getLeastSignificantBits()).array();
        }
    }

    @Override
    public UUID convertToEntityAttribute(byte[] bytes) {
        if (bytes == null || bytes.length != 16) return null;
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

}
