package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminUtils {

  private static final Logger log = LoggerFactory.getLogger(KeycloakAdminUtils.class);
  final String AUTH_SERVER_URL = "http://localhost:9080";
  final String REALM = "master";
  final String CLIENT_ID = "admin-cli";
  final String USER_NAME = "admin";
  final String PASSWORD = "owiseman.k.17";

  // acceccToken过期时间 12h (单位：s)
  final Integer ACCESS_TOKEN_LIFESPAN = 10*60;

  KeycloakTokenService tokenService;

  @Autowired
  public KeycloakAdminUtils() {
    this.tokenService = new KeycloakTokenService();
  }

  /**
   * 创建realm
   * @param newRealm
   */
  public boolean createRealm(String newRealm) {
    String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBeUdZbVB6RlE5TmdVWXAyandINFBiMXIydUw5UDNUZFlrZDlNZ2xiaTljIn0.eyJleHAiOjE3Mzk4OTAwODcsImlhdCI6MTczOTg4OTQ4NywianRpIjoiNGEyZTI4MGMtZTY1ZC00YjNlLThmNDEtM2RmODdkYjc5OWJkIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL3JlYWxtcy9tYXN0ZXIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jbGkiLCJzaWQiOiJhN2RjNzY3Ni0zZTY4LTQ4NTYtYWU4Yy1hNmM4MGU0MmUzMTEiLCJzY29wZSI6InByb2ZpbGUgZW1haWwifQ.H7SDRLbQX_a9HaTzeghrt-bIW9G9wHZShxyFMX253_0fe_st-JoW-olt8kC9UBBDu5mIA-h2Zp_xMb-jv5lQWBvcu1l09feq-SbxQM6eumMNxGQTjY0zr1Jha56lEVgvoBu_g0PDjH3p16KcCqZVLQGCSNj-s8aWGE1SR-7ZiNoHx-hSKX8RZbxOjJfCHMvyNrO1yMxJ1yWQRKgtDvhZQvpS7bufS12CzRdiwjbSmKkMEq_b6gc1dKmbnKudMUwPRq4FurDHuZOkW9tlGqatdYSR0t1F8glFBhpiLUl9O9j3XrmOYgPZNmUtkPZf1HwaF01k_UKcqzXYxL9s_U7yKg";

    Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(AUTH_SERVER_URL)
            .realm(REALM)
            .username(USER_NAME)
            .password(PASSWORD)
            .clientId(CLIENT_ID)
//            .clientSecret("OCFmD1ra2346Y9Uu6SKAvpGY9mjMk6A2")
            .grantType("password")
            .authorization(token)
            .build();
    try {
      RealmRepresentation realm = new RealmRepresentation();
      realm.setRealm(newRealm);
      realm.setEnabled(true);
//      realm.setSslRequired("NONE");
      realm.setAccessTokenLifespan(ACCESS_TOKEN_LIFESPAN);
      keycloak.realms().create(realm);
      return true;
    } catch (Exception e){
      log.error("create realm failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 修改realm名称
   * @param newRealm
   * @param oldRealm
   * @return
   */
  public boolean updateRealm(String newRealm,String oldRealm) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      RealmRepresentation realm = new RealmRepresentation();
      realm.setRealm(newRealm);
      realm.setEnabled(true);
      realm.setSslRequired("NONE");
      realm.setAccessTokenLifespan(ACCESS_TOKEN_LIFESPAN);
      keycloak.realm(oldRealm).update(realm);
      return true;
    } catch (Exception e){
      log.error("update realm failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 删除realm
   * @param realm
   * @return
   */
  public boolean removeRealm(String realm) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      keycloak.realm(realm).remove();
      return true;
    } catch (Exception e){
      log.error("remove realm failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 创建客户端(需要client_secret)
   * @param clientId
   * @param realmName
   * @return client_secret 客户端密钥
   */
  public String createClient(String clientId, String realmName) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      ClientRepresentation client = new ClientRepresentation();
      client.setClientId(clientId);
      // client.setSecret("");
      client.setRedirectUris(new ArrayList<String>());
      client.setBearerOnly(false);
      client.setPublicClient(false);
      client.setDirectAccessGrantsEnabled(true);
      Response response = keycloak.realm(realmName).clients().create(client);
      response.close();
      String cId = getCreatedId(response);
      log.info("client created with id: "+ cId);
      // 生成新密钥
      CredentialRepresentation credentialRepresentation = keycloak.realm(realmName).clients().get(cId).generateNewSecret();
      return credentialRepresentation.getValue();
    } catch (Exception e){
      log.error("create client failed,for",e);
      return null;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 创建客户端(Public)
   * @param clientId
   * @param realmName
   */
  public boolean createClientPublic(String clientId, String realmName) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      ClientRepresentation client = new ClientRepresentation();
      client.setClientId(clientId);
      client.setRedirectUris(new ArrayList<String>());
      client.setBearerOnly(false);
      client.setPublicClient(true);
      client.setDirectAccessGrantsEnabled(true);
      Response response = keycloak.realm(realmName).clients().create(client);
      response.close();
      if(!response.getStatusInfo().equals(Response.Status.CREATED)) {
        return false;
      }
      return true;
    } catch (Exception e){
      log.error("create client public failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 创建role
   * @param roleName
   * @param realmName
   * @return
   */
  public boolean createRole(String roleName, String realmName) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      RoleRepresentation roleRepresentation = new RoleRepresentation();
      roleRepresentation.setName(roleName);
      keycloak.realm(realmName).roles().create(roleRepresentation);
      return true;
    } catch (Exception e){
      log.error("create role failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 创建user
   * @param userName
   * @param password
   * @param realm
   */
  public boolean createUser(String userName, String password, String realm) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      UserRepresentation user = new UserRepresentation();
      // userName唯一且不可修改
      user.setUsername(userName);
      user.setEnabled(true);
      Response createUserResponse = keycloak.realm(realm).users().create(user);
      createUserResponse.close();
      String userId = getCreatedId(createUserResponse);
      log.info("User created with id: "+ userId);
      // 设置密码
      CredentialRepresentation passwordCred = new CredentialRepresentation();
      passwordCred.setTemporary(false);
      passwordCred.setValue(password);
      passwordCred.setType(CredentialRepresentation.PASSWORD);
      keycloak.realm(realm).users().get(userId).resetPassword(passwordCred);
      // 设置角色
      // RoleRepresentation userRealmRole = keycloak.realm(realm).roles().get(roleName).toRepresentation();
      // keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRealmRole));
      return true;
    } catch (Exception e){
      log.error("create user failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 删除user
   * @param userName
   * @param realm
   * @return
   */
  public boolean removeUser(String userName, String realm) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      // 用户不能重名, 精确搜索
      List<UserRepresentation> userRepresentationList = keycloak.realm(realm).users().search(userName,true);
      if(userRepresentationList.size() > 0){
        String id = userRepresentationList.get(0).getId();
        keycloak.realm(realm).users().get(id).remove();
        return true;
      } else {
        log.error("No Such User !");
        return false;
      }
    } catch (Exception e){
      log.error("remove user failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 更新密码
   * @param userName
   * @param password
   * @param realm
   */
  public boolean updatePassword(String userName, String password, String realm) {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      // 用户不能重名, 精确搜索
      List<UserRepresentation> userRepresentationList = keycloak.realm(realm).users().search(userName,true);
      if(userRepresentationList.size() > 0){
        String id = userRepresentationList.get(0).getId();
        // 设置密码
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setValue(password);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        keycloak.realm(realm).users().get(id).resetPassword(passwordCred);
        return true;
      } else {
        log.error("No Such User !");
        return false;
      }
    } catch (Exception e){
      log.error("update password failed,for",e);
      return false;
    } finally {
      keycloak.close();
    }
  }

  /**
   * 获取全部realms
   * @return
   */
  public List<String> getRealms() {
    Keycloak keycloak = Keycloak.getInstance(AUTH_SERVER_URL, REALM, USER_NAME, PASSWORD, CLIENT_ID);
    try {
      List<RealmRepresentation> realms = keycloak.realms().findAll();
      List<String> collect = realms.stream().map(RealmRepresentation::getRealm).collect(Collectors.toList());
      return collect;
    } catch (Exception e){
      log.error("get all realms failed,for",e);
      return null;
    } finally {
      keycloak.close();
    }
  }


  /**
   * keyclaok会把创建成功的id通过url返回
   * @param response
   * @return
   */
  public static String getCreatedId(Response response) {

    URI location = response.getLocation();

    if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
      Response.StatusType statusInfo = response.getStatusInfo();
      throw new WebApplicationException("Create method returned status " +
        statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)", response);
    }

    if (location == null) {
      return null;
    }

    String path = location.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }

  /**
   * 通过该方法使用admin用户给
   * @param realm
   * @param tokenUrl
   * @param clientId
   * @param masterAdminPassword
   * @return
   */
  public Keycloak getKeyCloak(String realm, String tokenUrl, String clientId,String masterAdminPassword,String token) {
    return KeycloakBuilder.builder()
            .grantType(OAuth2ConstantsExtends.PASSWORD)
            .clientId(clientId)
            .realm(realm)
            .serverUrl(tokenUrl)
            .username(OAuth2ConstantsExtends.ADMIN)
            .password(masterAdminPassword)
            .authorization(token)
            .build();
  }

}
