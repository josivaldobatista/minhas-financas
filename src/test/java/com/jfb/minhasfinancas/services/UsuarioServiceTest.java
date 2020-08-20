package com.jfb.minhasfinancas.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.repositories.UsuarioRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	private void instanciaUsuario() {
		Usuario obj = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com").build();
		repository.save(obj);
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		// Cenário
		repository.deleteAll();
		
		// Ação/execução
		service.validarEmail("usuario@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// Cenário
		instanciaUsuario();
		
		// Ação/execução
		service.validarEmail("usuario@email.com");
	}

}
