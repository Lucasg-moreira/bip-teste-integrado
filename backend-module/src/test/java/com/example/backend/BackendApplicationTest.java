package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTest {
    @Test
    void contextLoads() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
        }, "O contexto da aplicação deve carregar sem exceções");
    }
}
