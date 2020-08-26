package com.jfb.minhasfinancas.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
		
	}
}













