package com.example.backend.repository;

import com.example.ejb.model.Beneficio;

public interface BeneficioRepository {
    Beneficio buscarPorId(Long id);
    Beneficio salvar(Beneficio beneficio);
}
