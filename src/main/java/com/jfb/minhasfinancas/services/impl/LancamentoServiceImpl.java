package com.jfb.minhasfinancas.services.impl;

import java.util.List;
import java.util.Objects;

import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.repositories.LancamentoRepository;
import com.jfb.minhasfinancas.services.LancamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento obj) {
        return repository.save(obj);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento obj) {
        Objects.requireNonNull(obj.getId());
        return repository.save(obj);
    }

    @Override
    @Transactional
    public void deletar(Lancamento obj) {
        Objects.requireNonNull(obj.getId()); 
        repository.delete(obj);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento objFiltro) {
        Example example = Example.of(objFiltro, 
            ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento obj, StatusLancamento status) {
        obj.setStatus(status);
        atualizar(obj);

    }
    
}