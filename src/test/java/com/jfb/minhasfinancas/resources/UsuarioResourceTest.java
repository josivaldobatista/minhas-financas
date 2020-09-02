package com.jfb.minhasfinancas.resources;

import javax.swing.text.AbstractDocument.Content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfb.minhasfinancas.model.dto.UsuarioDTO;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.services.LancamentoService;
import com.jfb.minhasfinancas.services.UsuarioService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String BASE_API = "/api/usuarios";
	static final MediaType JSON_APP = MediaType.APPLICATION_JSON;

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	UsuarioService service;

	@MockBean
	LancamentoService lancamentoService;

	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		// Cenário
		String email = "usuario@email.com";
		String senha = "123";

		UsuarioDTO dto = UsuarioDTO.builder()
			.email(email)
			.senha(senha).build(); 

		Usuario obj = Usuario.builder()
			.id(1l)
			.email(email)
			.senha(senha).build();

		Mockito.when(service.autenticar(email, senha)).thenReturn(obj);
		String json = new ObjectMapper().writeValueAsString(dto);

		// Execução e Verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(BASE_API.concat("/autenticar"))
			.accept(JSON_APP)
			.contentType(JSON_APP)
			.content(json);

		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(obj.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(obj.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(obj.getEmail()));

	}
	
}
