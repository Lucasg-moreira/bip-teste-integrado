package com.example.ejb.repository;

import com.example.ejb.model.Beneficio;

import java.util.List;


public interface BeneficioRepository {
    Beneficio buscarPorId(Long id);
    Beneficio criar(Beneficio beneficio);
    Beneficio atualizar(Beneficio beneficio);
    List<Beneficio> buscarTodos();
}

