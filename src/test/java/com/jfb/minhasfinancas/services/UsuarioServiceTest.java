package com.jfb.minhasfinancas.services;

import java.util.Optional;

import com.jfb.minhasfinancas.exceptions.ErroAutenticacao;
import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.repositories.UsuarioRepository;
import com.jfb.minhasfinancas.services.impl.UsuarioServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test(expected = Test.None.class)
	public void deveSavarUmUsuario() {
		// Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario obj = Usuario.builder()
			.id(1l)
			.nome("nome")
			.email("usuario@email.com")
			.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(obj);

		// Ação/execução
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

		// Verificação 
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}

	@Test(expected = RegraNegocioException.class)
	public void naoDeveCadastrarUmUsuarioComEmailJaCadastrado() {
		// Cenário
		String email = "usuario@email.com";
		Usuario obj = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service)
			.validarEmail(email);

		// Ação/execução
		service.salvarUsuario(obj);

		// Verificação
		Mockito.verify(repository, Mockito.never()).save(obj);
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

	@Test
	public void develancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		// Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// Ação/execução
		Throwable exception = Assertions.catchThrowable(
			() -> service.autenticar("usuario@email.com", "senha"));

		Assertions.assertThat(exception).isInstanceOf(
			ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado!");
	}

	@Test
	public void deveLancarErroQuandoASenhaNaoBater() {
		//Cenário
		String senha = "senha";
		Usuario obj = Usuario.builder().email("usuario@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(obj));

		// Ação/execução
		Throwable exception = Assertions.catchThrowable(
			() -> service.autenticar("usuario@email.com", "123"));

		Assertions.assertThat(exception).isInstanceOf(
			ErroAutenticacao.class).hasMessage("Senha inválida!");
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
