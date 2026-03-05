package com.example.backend.service;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IBeneficioService {
    BeneficioResponseDTO criar(BeneficioRequestDTO dto);
    BeneficioResponseDTO atualizar(Long id, BeneficioRequestDTO dto);
    List<BeneficioResponseDTO> buscarTodos();
    void transfer(Long fromId, Long toId, BigDecimal valor);
}
