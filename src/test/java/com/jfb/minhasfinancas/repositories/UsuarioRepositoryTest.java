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

import java.util.Optional;

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

	@Test
	public void deveVerificarAExistenciaDeEmail() {
		// Cenário
		Usuario obj = instanciaUsuario();
		entityManager.persist(obj);

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

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// Cenário
		Usuario obj = instanciaUsuario();

		// Ação/execução
		Usuario objSalvo = repository.save(obj);

		// Verificação
		Assertions.assertThat(objSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUusarioPorEmail() {
		// Cenário
		Usuario obj = instanciaUsuario();
		entityManager.persist(obj);
		
		// Ação/execução
		Optional<Usuario> resultado = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void deveRetornaVazioAoBuscarUsuarioPorEmailQuandoNaoExistirNaBase() {
		// Cenário
		
		// Ação/execução
		Optional<Usuario> resultado = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}

	private Usuario instanciaUsuario() {
		Usuario obj = Usuario.builder()
			.nome("usuario")
			.email("usuario@email.com")
			.senha("senha").build();
		entityManager.persist(obj);
		return obj;
	}
}
