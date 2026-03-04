package com.example.backend.service.impl;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.service.IBeneficioService;
import com.example.ejb.service.IBeneficioEjbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BeneficioService implements IBeneficioService {

    private final IBeneficioEjbService beneficioEjbService;

    public BeneficioService(IBeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }

    public BeneficioResponseDTO criar(BeneficioRequestDTO dto) {

        var response = beneficioEjbService.criar(dto.nome(), dto.valor());

        return new BeneficioResponseDTO(response.getId(), dto.nome(), dto.valor());
    }

    public BeneficioResponseDTO atualizar(Long id, BeneficioRequestDTO dto) {

        beneficioEjbService.atualizar(id, dto.nome(), dto.valor());

        return new BeneficioResponseDTO(id, dto.nome(), dto.valor());
    }

    public List<BeneficioResponseDTO> buscarTodos() {

        return beneficioEjbService.buscarTodos()
                .stream()
                .map(b -> new BeneficioResponseDTO(
                        b.getId(),
                        b.getNome(),
                        b.getValor()))
                .toList();
    }

    @Override
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal valor) {
        beneficioEjbService.transfer(fromId, toId, valor);
    }
}
