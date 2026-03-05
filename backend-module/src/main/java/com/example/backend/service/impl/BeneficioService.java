package com.example.backend.service.impl;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.service.IBeneficioService;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.IBeneficioEjbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class BeneficioService implements IBeneficioService {

    private final IBeneficioEjbService beneficioEjbService;

    public BeneficioService(IBeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }

    public BeneficioResponseDTO criar(BeneficioRequestDTO dto) {
        log.info("Criando novo beneficio. Nome: {}, Valor: {}", dto.nome(), dto.valor());

        var response = beneficioEjbService.criar(dto.nome(), dto.valor());

        return new BeneficioResponseDTO(
                response.getId(),
                response.getNome(),
                response.getValor(),
                response.getAtivo());
    }

    public BeneficioResponseDTO atualizar(Long id, BeneficioRequestDTO dto) {

        Beneficio beneficio = beneficioEjbService.atualizar(id,
                dto.nome(),
                dto.valor(),
                dto.descricao(),
                dto.ativo());

        return new BeneficioResponseDTO(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getValor(),
                beneficio.getAtivo());
    }

    //TODO: Implementar paginação
    public List<BeneficioResponseDTO> buscarTodos() {
        log.info("Buscando todos os beneficios");

        return beneficioEjbService.buscarTodos()
                .stream()
                .map(b -> new BeneficioResponseDTO(
                        b.getId(),
                        b.getNome(),
                        b.getValor(),
                        b.getAtivo()))
                .toList();
    }

    @Override
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal valor) {
        beneficioEjbService.transfer(fromId, toId, valor);
        log.info("Transferência realizada: {} de {} para {}", valor, fromId, toId);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Desativando beneficio de id: {}", id);
        beneficioEjbService.desativar(id);
    }
}
