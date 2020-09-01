package com.jfb.minhasfinancas.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
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
		Lancamento obj = LancamentoRepositoryTest.criarLancamento();
		
		// Ação/execução e Execução
		Assertions.catchThrowableOfType(() -> service.atualizar(obj), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(obj);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		// Cenário
		Lancamento obj = LancamentoRepositoryTest.criarLancamento();
		obj.setId(1l);
		
		// Ação/execução
		service.deletar(obj);
		
		// Verificação
		Mockito.verify(repository).delete(obj);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		// Cenário
		Lancamento obj = LancamentoRepositoryTest.criarLancamento();
		
		// Ação/execução
		Assertions.catchThrowableOfType(() -> service.deletar(obj), NullPointerException.class);
		
		// Verificação
		Mockito.verify(repository, Mockito.never()).delete(obj);
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		// Cenário
		Lancamento obj = LancamentoRepositoryTest.criarLancamento();
		obj.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(obj);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		// Ação/execução
		List<Lancamento> resultado = service.buscar(obj);
		
		// Verificação
		Assertions.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(obj);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		// Cenário
		Lancamento obj = LancamentoRepositoryTest.criarLancamento();
		obj.setId(1l);
		obj.setStatus(StatusLancamento.PENDENTE);
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(obj).when(service).atualizar(obj);
		
		// Ação/execução 
		service.atualizarStatus(obj, novoStatus);
		
		// Verificação
		Assertions.assertThat(obj.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(obj);
	}
	
}













