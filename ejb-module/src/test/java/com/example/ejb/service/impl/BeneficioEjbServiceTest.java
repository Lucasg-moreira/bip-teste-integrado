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
class BeneficioEjbServiceTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;

    @Mock
    private JpaBeneficioRepository repository;

    @InjectMocks
    private BeneficioEjbService service;

    @Captor
    private ArgumentCaptor<Beneficio> beneficioCaptor;

    // =========================
    // 🔹 CRIAR
    // =========================

    @Test
    @DisplayName("Deve criar benefício com valor normalizado")
    void deveCriarBeneficioComValorNormalizado() {

        when(repository.criar(any()))
                .thenAnswer(invocation -> {
                    Beneficio b = invocation.getArgument(0);
                    b.setId(ID_1);
                    return b;
                });

        var resultado = service.criar("Vale Alimentação", new BigDecimal("100.5"));

        verify(repository).criar(beneficioCaptor.capture());

        var capturado = beneficioCaptor.getValue();

        assertEquals("Vale Alimentação", capturado.getNome());
        assertEquals(new BigDecimal("100.50"), capturado.getValor());

        assertNotNull(resultado.getId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve lançar exceção quando nome for inválido")
    void deveLancarExcecaoQuandoNomeInvalido(String nome) {

        assertThrows(DomainException.class,
                () -> service.criar(nome, BigDecimal.TEN));

        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10"})
    @DisplayName("Deve lançar exceção quando valor for inválido")
    void deveLancarExcecaoQuandoValorInvalido(String valor) {

        assertThrows(DomainException.class,
                () -> service.criar("Vale", new BigDecimal(valor)));

        verifyNoInteractions(repository);
    }

    // =========================
    // 🔹 ATUALIZAR
    // =========================

    @Test
    @DisplayName("Deve atualizar benefício existente")
    void deveAtualizarBeneficio() {

        var beneficio = beneficio(ID_1, "Antigo", "100");

        when(repository.buscarPorId(ID_1)).thenReturn(beneficio);

        service.atualizar(ID_1, "Novo Nome", new BigDecimal("200.00"));

        assertEquals("Novo Nome", beneficio.getNome());
        assertEquals(new BigDecimal("200.00"), beneficio.getValor());

        verify(repository).buscarPorId(ID_1);
    }

    // =========================
    // 🔹 TRANSFERÊNCIA
    // =========================

    @Test
    void deveLancarExcecaoQuandoIdsForemInvalidos() {
        assertThrows(DomainException.class,
                () -> service.transfer(null, ID_2, BigDecimal.TEN));

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, null, BigDecimal.TEN));

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_1, BigDecimal.TEN));

        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void deveLancarExcecaoQuandoValorTransferenciaInvalido(String valor) {

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, new BigDecimal(valor)));
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExistir() {

        when(repository.buscarPorId(ID_1)).thenReturn(null);
        when(repository.buscarPorId(ID_2)).thenReturn(new Beneficio());

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, BigDecimal.TEN));

        verify(repository, never()).atualizar(any());
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {

        var from = beneficio(ID_1, null, "50");
        var to = beneficio(ID_2, null, "10");

        when(repository.buscarPorId(ID_1)).thenReturn(from);
        when(repository.buscarPorId(ID_2)).thenReturn(to);

        assertThrows(DomainException.class,
                () -> service.transfer(ID_1, ID_2, new BigDecimal("100")));

        verify(repository, never()).atualizar(any());
    }

    @Test
    void deveTransferirValorCorretamente() {

        var from = beneficio(ID_1, null, "100.00");
        var to = beneficio(ID_2, null, "50.00");

        when(repository.buscarPorId(ID_1)).thenReturn(from);
        when(repository.buscarPorId(ID_2)).thenReturn(to);

        service.transfer(ID_1, ID_2, new BigDecimal("30"));

        assertEquals(new BigDecimal("70.00"), from.getValor());
        assertEquals(new BigDecimal("80.00"), to.getValor());

        verify(repository).atualizar(from);
        verify(repository).atualizar(to);
        verify(repository, times(2)).atualizar(any());
    }

    // =========================
    // 🔹 MÉTODO AUXILIAR
    // =========================

    private Beneficio beneficio(Long id, String nome, String valor) {
        return Beneficio.builder()
                .id(id)
                .nome(nome)
                .valor(new BigDecimal(valor))
                .build();
    }
}