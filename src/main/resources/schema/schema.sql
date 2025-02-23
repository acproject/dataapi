-- 设备令牌表
CREATE TABLE IF NOT EXISTS sys_device_tokens (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255),
    token TEXT,
    platform VARCHAR(20),
    last_active TIMESTAMP,
    active BOOLEAN
);

-- 工作流表
CREATE TABLE IF NOT EXISTS sys_workflows (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    definition JSONB,
    create_by VARCHAR(255),
    create_at TIMESTAMP,
    update_at TIMESTAMP
);

-- Keycloak客户端表
CREATE TABLE IF NOT EXISTS sys_keycloak_clients (
    id VARCHAR(255) PRIMARY KEY,
    realm_name VARCHAR(255),
    client_id VARCHAR(255),
    secret TEXT,
    name VARCHAR(255),
    description TEXT,
    type VARCHAR(50),
    root_url TEXT,
    admin_url TEXT,
    base_url TEXT,
    surrogate_auth_required BOOLEAN,
    enabled BOOLEAN,
    always_display_in_console BOOLEAN,
    client_authenticator_type VARCHAR(255),
    registration_access_token TEXT
);

-- Keycloak域表
CREATE TABLE IF NOT EXISTS sys_keycloak_realm (
    id VARCHAR(255) PRIMARY KEY,
    realm VARCHAR(255),
    display_name VARCHAR(255),
    display_name_html TEXT,
    not_before INTEGER,
    default_signature_algorithm VARCHAR(50),
    revoke_refresh_token BOOLEAN,
    refresh_token_max_reuse INTEGER,
    access_token_lifespan INTEGER,
    access_token_lifespan_for_implicit_flow INTEGER,
    sso_session_idle_timeout INTEGER,
    sso_session_max_lifespan INTEGER,
    sso_session_idle_timeout_remember_me INTEGER,
    sso_session_max_lifespan_remember_me INTEGER,
    offline_session_idle_timeout INTEGER,
    offline_session_max_lifespan_enabled BOOLEAN,
    offline_session_max_lifespan INTEGER,
    client_session_idle_timeout INTEGER,
    client_session_max_lifespan INTEGER,
    client_offline_session_idle_timeout INTEGER,
    client_offline_session_max_lifespan INTEGER,
    access_code_lifespan INTEGER,
    access_code_lifespan_user_action INTEGER,
    access_code_lifespan_login INTEGER,
    action_token_generated_by_admin_lifespan INTEGER,
    action_token_generated_by_user_lifespan INTEGER,
    oauth2_device_code_lifespan INTEGER,
    oauth2_device_polling_interval INTEGER,
    enabled BOOLEAN,
    ssl_required VARCHAR(50)
);

-- 插件表
CREATE TABLE IF NOT EXISTS sys_plugins (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    version VARCHAR(50),
    port INTEGER,
    status VARCHAR(20),
    wasm_path TEXT,
    last_heartbeat TIMESTAMP,
    config_schema JSONB,
    runtime_config JSONB
);

-- 审计日志表
CREATE TABLE IF NOT EXISTS sys_audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255),
    action_type VARCHAR(50),
    target_type VARCHAR(50),
    target_id VARCHAR(255),
    details JSONB,
    timestamp TIMESTAMP
);

-- 数据源表
CREATE TABLE IF NOT EXISTS sys_data_sources (
    id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(50),
    config JSONB,
    created_by VARCHAR(255),
    last_test_result BOOLEAN
);

-- 用户文件表
CREATE TABLE IF NOT EXISTS sys_user_files (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    fid VARCHAR(64) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT,
    upload_time TIMESTAMP
);

-- 用户表
CREATE TABLE IF NOT EXISTS sys_users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    email_verified BOOLEAN,
    attributes JSONB,
    created_timestamp BIGINT,
    enabled BOOLEAN,
    realm_name VARCHAR(255),
    client_id VARCHAR(255)
);

ALTER TABLE sys_user_files
  ADD COLUMN parent_id VARCHAR(255),
  ADD COLUMN is_directory BOOLEAN NOT NULL DEFAULT false,
  ADD COLUMN path VARCHAR(1024);
