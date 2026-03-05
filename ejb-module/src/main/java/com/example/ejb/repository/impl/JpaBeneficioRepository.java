package com.example.ejb.repository.impl;

import com.example.ejb.model.Beneficio;
import com.example.ejb.repository.BeneficioRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class JpaBeneficioRepository implements BeneficioRepository {
    @PersistenceContext(unitName = "beneficioPU")
    private EntityManager em;

    @Override
    public Beneficio buscarPorId(Long id) {
        Beneficio beneficio = em.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
        return Optional.ofNullable(beneficio).orElse(new Beneficio());
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
