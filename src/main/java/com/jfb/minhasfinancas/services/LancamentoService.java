package com.jfb.minhasfinancas.services;

import java.util.List;

import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
    
     Lancamento salvar(Lancamento obj);

     Lancamento atualizar(Lancamento obj);

     void deletar(Lancamento obj);

     List<Lancamento> buscar(Lancamento objFiltro);
     
     void atualizarStatus(Lancamento obj, StatusLancamento status);
}