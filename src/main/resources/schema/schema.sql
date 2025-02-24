CREATE TABLE IF NOT EXISTS sys_audit_logs (
  id VARCHAR(255),
  user_id VARCHAR(255),
  action_type VARCHAR(255),
  target_type VARCHAR(255),
  target_id VARCHAR(255),
  details JSONB,
  timestamp TIMESTAMP,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_data_sources (
  id VARCHAR(255),
  type VARCHAR(255),
  config JSONB,
  created_by VARCHAR(255),
  last_test_result BOOLEAN,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_device_tokens (
  id VARCHAR(255),
  user_id VARCHAR(255),
  token VARCHAR(255),
  platform VARCHAR(255),
  last_active TIMESTAMP,
  active BOOLEAN,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_file_permissions (
  id VARCHAR(255),
  file_id VARCHAR(255),
  user_id VARCHAR(255),
  can_read VARCHAR(255),
  can_write VARCHAR(255),
  can_delete VARCHAR(255),
  can_share VARCHAR(255),
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_keycloak_clients (
  id VARCHAR(255),
  realm_name VARCHAR(255),
  client_id VARCHAR(255),
  secret VARCHAR(255),
  name VARCHAR(255),
  description VARCHAR(255),
  type VARCHAR(255),
  root_url VARCHAR(255),
  admin_url VARCHAR(255),
  base_url VARCHAR(255),
  surrogate_auth_required BOOLEAN,
  enabled BOOLEAN,
  always_display_in_console BOOLEAN,
  client_authenticator_type VARCHAR(255),
  registration_access_token VARCHAR(255),
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_keycloak_realm (
  id VARCHAR(255),
  realm VARCHAR(255),
  display_name VARCHAR(255),
  display_name_html VARCHAR(255),
  not_before INTEGER,
  default_signature_algorithm VARCHAR(255),
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
  ssl_required VARCHAR(255),
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_plugins (
  id VARCHAR(255),
  name VARCHAR(255),
  version VARCHAR(255),
  port INTEGER,
  status VARCHAR(255),
  wasm_path VARCHAR(255),
  last_heartbeat TIMESTAMP,
  config_schema JSONB,
  runtime_config JSONB,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_users (
  id VARCHAR(255),
  username VARCHAR(255),
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
  email_verified BOOLEAN,
  attributes JSONB,
  created_timestamp BIGINT,
  enabled BOOLEAN,
  realm_name VARCHAR(255),
  client_id VARCHAR(255),
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_user_config (
  id VARCHAR(255),
  keycloak_realm VARCHAR(255),
  keycloak_client_id VARCHAR(255),
  keycloak_client_secret VARCHAR(255),
  keycloak_auth_url VARCHAR(255),
  keycloak_token_url VARCHAR(255),
  apns_key_path VARCHAR(255),
  apns_team_id VARCHAR(255),
  apns_key_id VARCHAR(255),
  apns_bundle_id VARCHAR(255),
  apns_production BOOLEAN,
  firebase_project_id VARCHAR(255),
  firebase_private_key VARCHAR(255),
  firebase_client_email VARCHAR(255),
  firebase_client_id VARCHAR(255),
  firebase_service_account_path VARCHAR(255),
  database_table_name_prefix VARCHAR(255),
  user_id VARCHAR(255) NOT NULL,
  attributes JSONB,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS sys_user_files (
  id VARCHAR(255),
  user_id VARCHAR(255) NOT NULL,
  fid VARCHAR(64) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  size BIGINT,
  upload_time TIMESTAMP,
  parent_id VARCHAR(255),
  path VARCHAR(1024),
  is_directory BOOLEAN NOT NULL,
  PRIMARY KEY (id));
CREATE INDEX idx_parent_id ON sys_user_files (parent_id);
CREATE INDEX idx_path ON sys_user_files (path);


CREATE TABLE IF NOT EXISTS sys_workflows (
  id VARCHAR(255),
  name VARCHAR(255),
  definition JSONB,
  create_by VARCHAR(255),
  create_at TIMESTAMP,
  update_at TIMESTAMP,
  PRIMARY KEY (id));
