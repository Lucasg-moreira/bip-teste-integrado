package com.example.backend.service;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.service.impl.BeneficioService;
import com.example.ejb.exception.DomainException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.impl.BeneficioEjbService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da camada Spring - BeneficioService")
class BeneficioServiceTest {

    // =====================================================
    // CONSTANTES
    // =====================================================

    private static final Long ID       = 1L;
    private static final String NOME   = "Vale Transporte";
    private static final String DESC   = "Descricao";
    private static final BigDecimal VALOR = new BigDecimal("100.00");
    private static final boolean ATIVO = true;

    // =====================================================
    // MOCKS
    // =====================================================

    @Mock
    private BeneficioEjbService beneficioEjbService;

    @InjectMocks
    private BeneficioService beneficioService;

    // =====================================================
    // CRIAR
    // =====================================================

    @Test
    @DisplayName("Deve criar benefício com sucesso delegando ao EJB")
    void deveCriarBeneficioComSucesso() {
        when(beneficioEjbService.criar(NOME, VALOR))
                .thenReturn(umBeneficio());

        var response = beneficioService.criar(umRequestDTO());

        verify(beneficioEjbService).criar(NOME, VALOR);
        assertResponseIgualEntidade(response, umBeneficio());
    }

    @ParameterizedTest(name = "[{index}] Deve propagar ao criar: {0}")
    @MethodSource("exceptionProvider")
    void devePropagarExcecaoAoCriar(DomainException exception) {
        when(beneficioEjbService.criar(anyString(), any()))
                .thenThrow(exception);

        var thrown = assertThrows(DomainException.class,
                () -> beneficioService.criar(umRequestDTO()));

        assertEquals(exception.getMessage(), thrown.getMessage());
        verify(beneficioEjbService).criar(NOME, VALOR);
    }

    // =====================================================
    // ATUALIZAR
    // =====================================================

    @Test
    @DisplayName("Deve atualizar benefício com sucesso delegando ao EJB")
    void deveAtualizarBeneficioComSucesso() {
        when(beneficioEjbService.atualizar(ID, NOME, VALOR, DESC, ATIVO))
                .thenReturn(umBeneficio());

        var response = beneficioService.atualizar(ID, umRequestDTO());

        verify(beneficioEjbService).atualizar(ID, NOME, VALOR, DESC, ATIVO);
        assertResponseIgualEntidade(response, umBeneficio());
    }

    @ParameterizedTest(name = "[{index}] Deve propagar ao atualizar: {0}")
    @MethodSource("exceptionProvider")
    void devePropagarExcecaoAoAtualizar(DomainException exception) {
        when(beneficioEjbService.atualizar(any(), any(), any(), any(), anyBoolean()))
                .thenThrow(exception);

        var thrown = assertThrows(DomainException.class,
                () -> beneficioService.atualizar(ID, umRequestDTO()));

        assertEquals(exception.getMessage(), thrown.getMessage());
        verify(beneficioEjbService).atualizar(ID, NOME, VALOR, DESC, ATIVO);
    }

    // =====================================================
    // BUSCAR TODOS
    // =====================================================

    @Test
    @DisplayName("Deve retornar lista convertida para DTO")
    void deveRetornarListaConvertidaParaDTO() {
        var lista = List.of(
                umBeneficioComId(1L),
                umBeneficioComId(2L)
        );

        when(beneficioEjbService.buscarTodos()).thenReturn(lista);

        var resultado = beneficioService.buscarTodos();

        verify(beneficioEjbService).buscarTodos();
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).id());
        assertEquals(2L, resultado.get(1).id());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando EJB retornar vazio")
    void deveRetornarListaVazia() {
        when(beneficioEjbService.buscarTodos()).thenReturn(Collections.emptyList());

        var resultado = beneficioService.buscarTodos();

        verify(beneficioEjbService).buscarTodos();
        assertTrue(resultado.isEmpty());
    }

    @ParameterizedTest(name = "[{index}] Deve propagar ao listar: {0}")
    @MethodSource("exceptionProvider")
    void devePropagarExcecaoAoListar(DomainException exception) {
        when(beneficioEjbService.buscarTodos()).thenThrow(exception);

        var thrown = assertThrows(DomainException.class,
                () -> beneficioService.buscarTodos());

        assertEquals(exception.getMessage(), thrown.getMessage());
        verify(beneficioEjbService).buscarTodos();
    }

    // =====================================================
    // PROVIDERS
    // =====================================================

    static Stream<DomainException> exceptionProvider() {
        return Stream.of(
                new DomainException("Erro de validação"),
                new DomainException("Recurso não encontrado"),
                new DomainException("Erro interno")
        );
    }

    // =====================================================
    // BUILDERS
    // =====================================================

    private BeneficioRequestDTO umRequestDTO() {
        return new BeneficioRequestDTO(NOME, DESC, VALOR, ATIVO);
    }

    private Beneficio umBeneficio() {
        return umBeneficioComId(ID);
    }

    private Beneficio umBeneficioComId(Long id) {
        var b = new Beneficio();
        b.setId(id);
        b.setNome(NOME);
        b.setDescricao(DESC);
        b.setValor(VALOR);
        b.setAtivo(ATIVO);
        return b;
    }

    // =====================================================
    // ASSERTIONS
    // =====================================================

    private void assertResponseIgualEntidade(BeneficioResponseDTO response, Beneficio entidade) {
        assertEquals(entidade.getId(),   response.id());
        assertEquals(entidade.getNome(), response.nome());
        assertEquals(entidade.getValor(), response.valor());
    }
}