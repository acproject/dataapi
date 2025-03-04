package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "sys_column_metadata",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"table_id", "column_name"})
})

public class SysColumnMetadata {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private SysTableMetadata sysTableMetadata;

      @Column(name = "column_name", nullable = false)
    private String columnName;

    @Column(name = "data_type", nullable = false, length = 100)
    private String dataType;

    @Column(name = "primary_key", nullable = false)
    private boolean primaryKey;

    @Column(nullable = false)
    private boolean nullable;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(length = 1000)
    private String description;

    @Column(name = "ordinal_position", nullable = false)
    private int ordinalPosition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SysTableMetadata getSysTableMetadata() {
        return sysTableMetadata;
    }

    public void setSysTableMetadata(SysTableMetadata sysTableMetadata) {
        this.sysTableMetadata = sysTableMetadata;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public SysColumnMetadata(String id, SysTableMetadata sysTableMetadata, String columnName, String dataType, boolean primaryKey, boolean nullable, String defaultValue, String description, int ordinalPosition) {
        this.id = id;
        this.sysTableMetadata = sysTableMetadata;
        this.columnName = columnName;
        this.dataType = dataType;
        this.primaryKey = primaryKey;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
        this.description = description;
        this.ordinalPosition = ordinalPosition;
    }

    public SysColumnMetadata() {
    }

    public static class Builder {
        private String id;
        private SysTableMetadata sysTableMetadata;
        private String columnName;
        private String dataType;
        private boolean primaryKey;
        private boolean nullable;
        private String defaultValue;
        private String description;
        private int ordinalPosition;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder sysTableMetadata(SysTableMetadata sysTableMetadata) {
            this.sysTableMetadata = sysTableMetadata;
            return this;
        }

        public Builder columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public Builder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder primaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public Builder nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder ordinalPosition(int ordinalPosition) {
            this.ordinalPosition = ordinalPosition;
            return this;
        }

        public SysColumnMetadata build() {
            SysColumnMetadata columnMetadata = new SysColumnMetadata();
            columnMetadata.setId(id);
            columnMetadata.setSysTableMetadata(sysTableMetadata);
            columnMetadata.setColumnName(columnName);
            columnMetadata.setDataType(dataType);
            columnMetadata.setPrimaryKey(primaryKey);
            columnMetadata.setNullable(nullable);
            columnMetadata.setDefaultValue(defaultValue);
            columnMetadata.setDescription(description);
            columnMetadata.setOrdinalPosition(ordinalPosition);
            return columnMetadata;
        }
    }
}
