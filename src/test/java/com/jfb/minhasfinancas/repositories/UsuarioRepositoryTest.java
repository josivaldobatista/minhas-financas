package com.jfb.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfb.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeEmail() {
		// Cenário
		Usuario usuario = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com").build();
		repository.save(usuario);
		
		// Ação/execução
		boolean resultado = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(resultado).isTrue();
	}
}
