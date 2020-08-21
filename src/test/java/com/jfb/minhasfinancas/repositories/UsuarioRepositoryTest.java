package com.jfb.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfb.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	private void instanciaUsuario() {
		Usuario obj = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com").build();
		entityManager.persist(obj);
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
		
		// Ação/execução
		boolean resultado = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(resultado).isFalse();
	}
}
