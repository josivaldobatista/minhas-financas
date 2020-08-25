package com.jfb.minhasfinancas.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.model.enums.TipoLancamento;
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
        validar(obj);
        obj.setStatus(StatusLancamento.PENDENTE);
        return repository.save(obj);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento obj) {
        validar(obj);
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
                ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento obj, StatusLancamento status) {
        obj.setStatus(status);
        atualizar(obj);

    }

    @Override
    public void validar(Lancamento obj) {
        if (obj.getDescricao() == null || obj.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descrição válida.");
        }
        if (obj.getMes() == null || obj.getMes() < 1 || obj.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mês válido.");
        }
        if (obj.getAno() == null || obj.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um Ano válido.");
        }
        if (obj.getUsuario() == null || obj.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário válido.");
        }
        if (obj.getValor() == null || obj.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um Valor válido.");
        }
        if (obj.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de Lancamento.");
        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {
        BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA.name());
        BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA.name());
            if (receitas == null) {
                receitas = BigDecimal.ZERO;
            }
            if (despesas == null) {
                despesas = BigDecimal.ZERO;
            }
        return receitas.subtract(despesas);
    }

}