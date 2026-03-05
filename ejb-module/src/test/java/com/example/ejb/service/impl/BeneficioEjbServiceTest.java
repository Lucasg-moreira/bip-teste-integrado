package com.example.ejb.service.impl;

import com.example.ejb.exception.DomainException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.repository.impl.JpaBeneficioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da camada EJB - BeneficioEjbService")
class BeneficioEjbServiceTest {

    // =====================================================
    // CONSTANTES
    // =====================================================

    private static final Long   ID_1  = 1L;
    private static final Long   ID_2  = 2L;
    private static final String NOME  = "Vale Alimentação";
    private static final BigDecimal VALOR        = new BigDecimal("100.00");
    private static final BigDecimal VALOR_FROM   = new BigDecimal("100.00");
    private static final BigDecimal VALOR_TO     = new BigDecimal("50.00");
    private static final BigDecimal VALOR_TRANSF = new BigDecimal("30.00");

    // =====================================================
    // MOCKS
    // =====================================================

    @Mock
    private JpaBeneficioRepository repository;

    @InjectMocks
    private BeneficioEjbService service;

    @Captor
    private ArgumentCaptor<Beneficio> beneficioCaptor;

    // =====================================================
    // CRIAR
    // =====================================================

    @Test
    @DisplayName("Deve criar benefício com valor normalizado")
    void deveCriarBeneficioComValorNormalizado() {
        when(repository.criar(any()))
                .thenAnswer(invocation -> {
                    Beneficio b = invocation.getArgument(0);
                    b.setId(ID_1);
                    return b;
                });

        var resultado = service.criar(NOME, VALOR);

        verify(repository).criar(beneficioCaptor.capture());

        var capturado = beneficioCaptor.getValue();
        assertEquals(NOME, capturado.getNome());
        assertEquals(VALOR, capturado.getValor());
        assertNotNull(resultado.getId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve lançar exceção quando nome for nulo ou vazio")
    void deveLancarExcecaoQuandoNomeInvalido(String nome) {
        assertThrows(DomainException.class,
                () -> service.criar(nome, VALOR));

        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10", "0"})
    @DisplayName("Deve lançar exceção quando valor for negativo ou zero")
    void deveLancarExcecaoQuandoValorInvalido(String valor) {
        assertThrows(DomainException.class,
                () -> service.criar(NOME, new BigDecimal(valor)));

        verifyNoInteractions(repository);
    }

    // =====================================================
    // ATUALIZAR
    // =====================================================

    @Test
    @DisplayName("Deve atualizar benefício existente com novos dados")
    void deveAtualizarBeneficio() {
        var novoNome  = "Novo Nome";
        var novoValor = new BigDecimal("200.00");
        var beneficio = umBeneficioComId(ID_1);

        when(repository.buscarPorId(ID_1)).thenReturn(beneficio);

        service.atualizar(ID_1, novoNome, novoValor, null, true);

        verify(repository).buscarPorId(ID_1);
        assertEquals(novoNome,  beneficio.getNome());
        assertEquals(novoValor, beneficio.getValor());
    }

    // =====================================================
    // TRANSFERÊNCIA — validações de entrada
    // =====================================================

    @Test
    @DisplayName("Deve lançar exceção quando fromId for nulo")
    void deveLancarExcecaoQuandoFromIdNulo() {
        assertThrows(DomainException.class,
                () -> service.transfer(null, ID_2, VALOR_TRANSF));

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando toId for nulo")
    void deveLancarExcecaoQuandoToIdNulo() {
        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, null, VALOR_TRANSF));

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando fromId e toId forem iguais")
    void deveLancarExcecaoQuandoIdsForemIguais() {
        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_1, VALOR_TRANSF));

        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    @DisplayName("Deve lançar exceção quando valor da transferência for zero ou negativo")
    void deveLancarExcecaoQuandoValorTransferenciaInvalido(String valor) {
        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, new BigDecimal(valor)));

        verifyNoInteractions(repository);
    }

    // =====================================================
    // TRANSFERÊNCIA — regras de negócio
    // =====================================================

    @Test
    @DisplayName("Deve lançar exceção quando benefício de origem não existir")
    void deveLancarExcecaoQuandoOrigemNaoExistir() {
        when(repository.buscarPorId(ID_1)).thenReturn(null);
        when(repository.buscarPorId(ID_2)).thenReturn(umBeneficioComId(ID_2));

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, VALOR_TRANSF));

        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for insuficiente")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        when(repository.buscarPorId(ID_1)).thenReturn(umBeneficio(ID_1, "50.00"));
        when(repository.buscarPorId(ID_2)).thenReturn(umBeneficio(ID_2, "10.00"));

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, new BigDecimal("100")));

        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve transferir valor e atualizar saldos corretamente")
    void deveTransferirValorCorretamente() {
        var from = umBeneficio(ID_1, VALOR_FROM.toPlainString());
        var to   = umBeneficio(ID_2, VALOR_TO.toPlainString());

        when(repository.buscarPorId(ID_1)).thenReturn(from);
        when(repository.buscarPorId(ID_2)).thenReturn(to);

        service.transfer(ID_1, ID_2, VALOR_TRANSF);

        assertEquals(VALOR_FROM.subtract(VALOR_TRANSF), from.getValor());
        assertEquals(VALOR_TO.add(VALOR_TRANSF),        to.getValor());

        verify(repository).atualizar(from);
        verify(repository).atualizar(to);
        verify(repository, times(2)).atualizar(any());
    }

    // =====================================================
    // BUILDERS
    // =====================================================

    private Beneficio umBeneficioComId(Long id) {
        return umBeneficio(id, VALOR.toPlainString());
    }

    private Beneficio umBeneficio(Long id, String valor) {
        return Beneficio.builder()
                .id(id)
                .nome(NOME)
                .valor(new BigDecimal(valor))
                .build();
    }
}