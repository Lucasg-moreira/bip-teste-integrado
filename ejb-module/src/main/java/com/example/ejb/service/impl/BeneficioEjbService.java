package com.example.ejb.service.impl;

import com.example.ejb.exception.DomainException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.repository.BeneficioRepository;
import com.example.ejb.service.IBeneficioEjbService;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Stateless
@Remote
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEjbService implements IBeneficioEjbService {

    private static final  RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int SCALE = 2;

    @EJB
    BeneficioRepository beneficioRepository;

    @Override
    public Beneficio criar(String nome, BigDecimal valor) {

        validaSave(nome, valor);

        Beneficio beneficio = Beneficio.builder()
                .nome(nome)
                .valor(normalizaValor(valor))
                .build();

        return beneficioRepository.criar(beneficio);
    }

    @Override
    public Beneficio atualizar(Long id, String nome, BigDecimal valor, String descricao, Boolean ativo) {

        if (id == null) {
            throw new IllegalArgumentException("ID obrigatório");
        }

        validaSave(nome, valor);

        Beneficio beneficio = beneficioRepository.buscarPorId(id);

        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado");
        }

        beneficio.setNome(nome);
        beneficio.setValor(valor);
        beneficio.setDescricao(descricao);
        beneficio.setAtivo(ativo);

        return beneficioRepository.atualizar(beneficio);
    }

    @Override
    public List<Beneficio> buscarTodos() {
        return beneficioRepository.buscarTodos();
    }

    @Override
    public void desativar(Long id) {
        Beneficio beneficio = beneficioRepository.buscarPorId(id);
        if  (beneficio == null) {
            return;
        }

        if (Boolean.FALSE.equals(beneficio.getAtivo())) {
            throw new DomainException("Benefício já está desativado.");
        }

        beneficio.setAtivo(false);

        beneficioRepository.atualizar(beneficio);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new DomainException("IDs não podem ser nulos");
        }

        if (fromId.equals(toId)) {
            throw new DomainException("Conta origem e destino não podem ser iguais");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Valor da transferência deve ser positivo");
        }

        amount = normalizaValor(amount);

        Beneficio from = beneficioRepository.buscarPorId(fromId);
        Beneficio to = beneficioRepository.buscarPorId(toId);

        if (from == null || to == null) {
            throw new DomainException("Conta não encontrada");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new DomainException("Saldo insuficiente");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        beneficioRepository.atualizar(from);
        beneficioRepository.atualizar(to);
    }

    private BigDecimal normalizaValor(BigDecimal valor) {
        return valor.setScale(SCALE, ROUNDING_MODE);
    }


    private void validaSave(String nome, BigDecimal valor) {
        validarNome(nome);
        validarValor(valor);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome é obrigatório");
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new DomainException("Valor é obrigatório");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Valor não pode ser negativo");
        }
    }
}
