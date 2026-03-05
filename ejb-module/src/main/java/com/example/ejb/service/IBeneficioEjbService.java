package com.example.ejb.service;

import com.example.ejb.model.Beneficio;
import jakarta.ejb.Remote;

import java.math.BigDecimal;
import java.util.List;

@Remote
public interface IBeneficioEjbService {
    void transfer(Long fromId, Long toId, BigDecimal amount);

    Beneficio criar(String nome, BigDecimal valor);

    void atualizar(Long id, String nome, BigDecimal valor);

    List<Beneficio> buscarTodos();

    void desativar(Long id);
}
