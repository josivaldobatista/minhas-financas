package com.jfb.minhasfinancas.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.repositories.LancamentoRepository;
import com.jfb.minhasfinancas.repositories.LancamentoRepositoryTest;
import com.jfb.minhasfinancas.services.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		// cenário
		Lancamento objASalvar = LancamentoRepositoryTest.criarLancamento();
		doNothing().when(service).validar(objASalvar);
		
		Lancamento objSalvo = LancamentoRepositoryTest.criarLancamento();
		objSalvo.setId(1l);
		objSalvo.setStatus(StatusLancamento.PENDENTE);
		when(repository.save(objASalvar)).thenReturn(objSalvo);
		
		// Ação/execução
		Lancamento obj = service.salvar(objASalvar);
		
		// verificação
		assertThat( obj.getId() ).isEqualTo(objSalvo.getId());
		assertThat(obj.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		// Cenário
		Lancamento objASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(objASalvar);
		
		// Ação/execução
		Assertions.catchThrowableOfType(() -> service.salvar(objASalvar),
				RegraNegocioException.class);
		
		// Verificação
		Mockito.verify(repository, Mockito.never()).save(objASalvar);
	}
	
	@Test
	public void deveAtualizarUmlancamento() {
		// Cenário
		Lancamento objSalvo = LancamentoRepositoryTest.criarLancamento();
		objSalvo.setId(1l);
		objSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(objSalvo);
		Mockito.when(repository.save(objSalvo)).thenReturn(objSalvo);
		
		// Ação/execução
		service.atualizar(objSalvo);
		
		// Verificação
		Mockito.verify(repository, Mockito.times(1)).save(objSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		// Cenário
		Lancamento objASalvar = LancamentoRepositoryTest.criarLancamento();
		
		// Ação/execução e Execução
		Assertions.catchThrowableOfType(() -> service.atualizar(objASalvar), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(objASalvar);
	}
	
}













