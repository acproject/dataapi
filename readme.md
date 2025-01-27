## DataApi
This is a simple API that allows you to get data from a database.

## 增强kong的安全
### 使用Kong的Admin API安装JWT插件
```shell
curl -X POST http://localhost:8001/plugins \
  --data "name=jwt" \
  --data "config.uri_param_names=jwt" \
  --data "config.key_claim_name=iss" \
  --data "config.secret_is_base64=false" \
  --data "config.claims_to_verify=exp" \
  --data "config.run_on_preflight=true"
```
### 配置JWT插件关联到路由
当前路由ID：528ef6b6-a0ad-4490-adbb-8406f7154f32，name: users, paths: /my-service001/api/users
```shell
# 将JWT插件应用到指定路由（替换ROUTE_ID）
curl -X POST http://localhost:8001/routes/528ef6b6-a0ad-4490-adbb-8406f7154f32/plugins \
  --data "name=jwt" \
  --data "config.claims_to_verify=iss,aud" \
  --data "config.issuer=http://localhost:9080/auth/realms/myrealm" \
  --data "config.consumer_claim=sub" \
  --data "config.jwks_uri=http://localhost:9080/auth/realms/myrealm/protocol/openid-connect/certs"
```

### （可选）使用Kong OpenID Connect插件（若可用）
```shell
curl -X POST http://localhost:8001/plugins \
  --data "name=openid-connect" \
  --data "config.issuer=http://localhost:9080/auth/realms/myrealm" \
  --data "config.client_id=myclient" \
  --data "config.client_secret=ftHSBVz8KboFk5lIlKsvOKtFnQbhR9X7" \
  --data "config.auth_methods=bearer" \
  --data "config.scopes=openid,roles" \
  --data "config.verify_parameters=off" \
  --data "config.consumer_optional=true"
```

curl -X POST http://localhost:8001/plugins \
  --data "name=openid-connect" \
  --data "config.issuer=https://<keycloak-host>/auth/realms/<realm>" \
  --data "config.client_id=<client-id>" \
  --data "config.client_secret=<client-secret>" \
  --data "config.auth_methods=bearer" \
  --data "config.scopes=openid,roles" \
  --data "config.verify_parameters=off" \
  --data "config.consumer_optional=true"

### 选择下面的方案
```shell
# 创建全局 JWT 插件（关联 Keycloak 的 JWKS）
curl -X POST http://localhost:8001/plugins \
  --data "name=jwt" \
  --data "config.run_on_preflight=true" \
  --data "config.claims_to_verify=exp" \
  --data "config.key_claim_name=iss" \
  --data "config.uri_param_names=jwt" \
  --data "config.secret_is_base64=false" \
  --data "config.issuer=http://localhost:9080/auth/realms/myrealm" \
  --data "config.jwks_uri=http://localhost:9080/auth/realms/myrealm/protocol/openid-connect/certs"

```