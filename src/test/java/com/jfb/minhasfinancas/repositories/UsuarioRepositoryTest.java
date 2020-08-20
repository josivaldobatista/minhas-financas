package com.jfb.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfb.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	private void instanciaUsuario() {
		Usuario usuario = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com").build();
		repository.save(usuario);
	}
	
	@Test
	public void deveVerificarAExistenciaDeEmail() {
		// Cenário
		instanciaUsuario();
		
		// Ação/execução
		boolean resultado = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(resultado).isTrue();
	}

	
	@Test
	public void deveRetornaFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// Cenário
		repository.deleteAll();
		
		// Ação/execução
		boolean resultado = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(resultado).isFalse();
	}
}
