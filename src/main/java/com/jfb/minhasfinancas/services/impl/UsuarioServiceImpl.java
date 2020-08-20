package com.jfb.minhasfinancas.services.impl;

import org.springframework.stereotype.Service;

import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.repositories.UsuarioRepository;
import com.jfb.minhasfinancas.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		
	}

}
