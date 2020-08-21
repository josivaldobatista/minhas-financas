package com.jfb.minhasfinancas.services;

import java.util.Optional;

import com.jayway.jsonpath.Option;
import com.jfb.minhasfinancas.exceptions.ErroAutenticacao;
import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.repositories.UsuarioRepository;
import com.jfb.minhasfinancas.services.impl.UsuarioServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	UsuarioService service;

	@MockBean
	UsuarioRepository repository;

	@Before 
	public void setUp() {
		service = new UsuarioServiceImpl(repository);
	}

	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		// Cenário
		String email = "usuario@email.com";
		String senha = "senha";

		Usuario obj = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(obj));

		// Ação/execução
		Usuario resultado = service.autenticar(email, senha);

		// Verificação
		Assertions.assertThat(resultado).isNotNull();
	}

	@Test(expected = ErroAutenticacao.class)
	public void develancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		// Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// Ação/execução
		service.autenticar("usuario@email.com", "senha");
	}

	@Test(expected = ErroAutenticacao.class)
	public void deveLancarErroQuandoASenhaNaoBater() {
		//Cenário
		String senha = "senha";
		Usuario obj = Usuario.builder().email("usuario@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(obj));

		// Ação/execução
		service.autenticar("usuario@email.com", "123");

	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		// Cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// Ação/execução
		service.validarEmail("usuario@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// Cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		// Ação/execução
		service.validarEmail("usuario@email.com");
	}
}
