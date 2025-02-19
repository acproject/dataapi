package com.owiseman.dataapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.repository.KeycloakRealmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DataapiApplicationTests {

//    @Test
//    void testDeserialization() throws Exception {
//        String json = "{\"name\":\"test001\",\"enabled\":true}";
//        KeycloakRealmDto dto = new ObjectMapper().readValue(json, KeycloakRealmDto.class);
//        assertThat(dto.getName()).isEqualTo("test001");  // 验证字段值
//    }
////
//    @Autowired
//    private KeycloakRealmRepository repository;
//
//    @Test
//    void shouldInjectRepository() {
//       assertNotNull(repository);
//    }
}
