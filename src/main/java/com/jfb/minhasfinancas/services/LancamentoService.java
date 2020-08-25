package com.jfb.minhasfinancas.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
    
     Lancamento salvar(Lancamento obj);

     Lancamento atualizar(Lancamento obj);

     void deletar(Lancamento obj);

     List<Lancamento> buscar(Lancamento objFiltro);
     
     void atualizarStatus(Lancamento obj, StatusLancamento status);

     void validar(Lancamento obj);

     Optional<Lancamento> obterPorId(Long id);

     BigDecimal obterSaldoPorUsuario(Long id);
}