package com.example.ejb.service.impl;

import com.example.ejb.DomainException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.IBeneficioEjbService;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Stateless
@Remote
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEjbService implements IBeneficioEjbService {

    @PersistenceContext(unitName = "beneficioPU")
    private EntityManager em;

    private static final  RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int SCALE = 2;

    @Override
    public Beneficio criar(String nome, BigDecimal valor) {

        validaSave(nome, valor);

        Beneficio beneficio = Beneficio.builder()
                .nome(nome)
                .valor(normalizaValor(valor))
                .build();

        em.persist(beneficio);
        em.flush();

        return getBeneficio(beneficio.getId()) ;
    }

    @Override
    public void atualizar(Long id, String nome, BigDecimal valor) {

        if (id == null) {
            throw new IllegalArgumentException("ID obrigatório");
        }

        validaSave(nome, valor);

        Beneficio beneficio = getBeneficio(id);

        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado");
        }

        beneficio.setNome(nome);
        beneficio.setValor(valor);

        em.merge(beneficio);
    }

    @Override
    public List<Beneficio> buscarTodos() {
        return em.createQuery(
                "SELECT b FROM Beneficio b",
                Beneficio.class
        ).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs não podem ser nulos");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Conta origem e destino não podem ser iguais");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        amount = normalizaValor(amount);

        Beneficio from = getBeneficio(fromId);
        Beneficio to = getBeneficio(toId);

        if (from == null || to == null) {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));
    }

    private Beneficio getBeneficio(Long fromId) {
        return em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
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

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
    }
}
