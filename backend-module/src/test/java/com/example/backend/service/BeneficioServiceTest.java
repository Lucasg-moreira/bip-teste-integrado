package com.example.backend.service;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.service.impl.BeneficioService;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.impl.BeneficioEjbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioEjbService beneficioEjbService;

    @InjectMocks
    private BeneficioService beneficioService;

    @Test
    void deveCriarBeneficioComSucesso() {
        // given
        var dto = new BeneficioRequestDTO(
                "Vale Transporte",
                "Descricao",
                new BigDecimal("100.00")
        );

        var beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Vale Transporte");
        beneficio.setDescricao("Descricao");
        beneficio.setValor(new BigDecimal("100.00"));

        // simulando retorno do EJB (mesmo que não seja usado)
        when(beneficioEjbService.criar("Vale Transporte", new BigDecimal("100.00")))
                .thenReturn(beneficio);

        // when
        var response = beneficioService.criar(dto);

        // then
        verify(beneficioEjbService)
                .criar("Vale Transporte", new BigDecimal("100.00"));

        assertEquals("Vale Transporte", response.nome());
        assertEquals(new BigDecimal("100.00"), response.valor());
        assertNotNull(response.id());
    }

    @Test
    void devePropagarExcecaoQuandoEjbFalhar() {
        // given
        var dto = new BeneficioRequestDTO(
                "Vale Transporte",
                "Descricao",
                new BigDecimal("100.00")
        );

        when(beneficioEjbService.criar(anyString(), any()))
                .thenThrow(new RuntimeException("Erro ao criar"));

        // when / then
        assertThrows(RuntimeException.class, () ->
                beneficioService.criar(dto)
        );

        verify(beneficioEjbService)
                .criar("Vale Transporte", new BigDecimal("100.00"));
    }

    @Test
    void deveAtualizarBeneficioComSucesso() {
        // given
        Long id = 1L;

        var dto = new BeneficioRequestDTO(
                "Vale Alimentação",
                "Descricao",
                new BigDecimal("300.00")
        );

        // método atualizar é void → não precisa de when()
        doNothing().when(beneficioEjbService)
                .atualizar(id, "Vale Alimentação", new BigDecimal("300.00"));

        // when
        var response = beneficioService.atualizar(id, dto);

        // then
        verify(beneficioEjbService)
                .atualizar(id, "Vale Alimentação", new BigDecimal("300.00"));

        assertEquals(id, response.id());
        assertEquals("Vale Alimentação", response.nome());
        assertEquals(new BigDecimal("300.00"), response.valor());
    }

    @Test
    void devePropagarExcecaoQuandoAtualizacaoFalhar() {
        // given
        Long id = 1L;

        var dto = new BeneficioRequestDTO(
                "Vale Alimentação",
                "Descricao",
                new BigDecimal("300.00")
        );

        doThrow(new IllegalStateException("Erro ao atualizar"))
                .when(beneficioEjbService)
                .atualizar(id, "Vale Alimentação", new BigDecimal("300.00"));

        // when / then
        assertThrows(IllegalStateException.class, () ->
                beneficioService.atualizar(id, dto)
        );

        verify(beneficioEjbService)
                .atualizar(id, "Vale Alimentação", new BigDecimal("300.00"));
    }

    @Test
    void deveRetornarListaDeBeneficiosConvertidaParaDTO() {
        // given
        var beneficio1 = new Beneficio();
        beneficio1.setId(1L);
        beneficio1.setNome("Vale Alimentação");
        beneficio1.setValor(new BigDecimal("300.00"));

        var beneficio2 = new Beneficio();
        beneficio2.setId(2L);
        beneficio2.setNome("Vale Transporte");
        beneficio2.setValor(new BigDecimal("200.00"));

        when(beneficioEjbService.buscarTodos())
                .thenReturn(List.of(beneficio1, beneficio2));

        // when
        var resultado = beneficioService.buscarTodos();

        // then
        verify(beneficioEjbService).buscarTodos();

        assertEquals(2, resultado.size());

        assertEquals(1L, resultado.get(0).id());
        assertEquals("Vale Alimentação", resultado.get(0).nome());
        assertEquals(new BigDecimal("300.00"), resultado.get(0).valor());

        assertEquals(2L, resultado.get(1).id());
        assertEquals("Vale Transporte", resultado.get(1).nome());
        assertEquals(new BigDecimal("200.00"), resultado.get(1).valor());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremBeneficios() {
        // given
        when(beneficioEjbService.buscarTodos())
                .thenReturn(Collections.emptyList());

        // when
        var resultado = beneficioService.buscarTodos();

        // then
        verify(beneficioEjbService).buscarTodos();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void devePropagarExcecaoQuandoListagemFalhar() {
        // given
        when(beneficioEjbService.buscarTodos())
                .thenThrow(new RuntimeException("Erro interno"));

        // when / then
        assertThrows(RuntimeException.class,
                () -> beneficioService.buscarTodos());

        verify(beneficioEjbService).buscarTodos();
    }


}
