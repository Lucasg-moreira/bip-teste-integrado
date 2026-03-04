package com.example.ejb.repository.impl;

import com.example.ejb.model.Beneficio;
import com.example.ejb.repository.BeneficioRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class JpaBeneficioRepository implements BeneficioRepository {
    @PersistenceContext(unitName = "beneficioPU")
    private EntityManager em;

    @Override
    public Beneficio buscarPorId(Long id) {
        return em.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public Beneficio criar(Beneficio beneficio) {
        em.persist(beneficio);
        em.flush();
        return beneficio;
    }

    @Override
    public Beneficio atualizar(Beneficio beneficio) {
        return em.merge(beneficio);
    }

    @Override
    public List<Beneficio> buscarTodos() {
        return em.createQuery(
                "SELECT b FROM Beneficio b",
                Beneficio.class
        ).getResultList();
    }
}
