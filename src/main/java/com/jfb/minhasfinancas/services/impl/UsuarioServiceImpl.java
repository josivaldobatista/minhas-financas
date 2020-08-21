package com.jfb.minhasfinancas.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jfb.minhasfinancas.exceptions.ErroAutenticacao;
import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
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
		Optional<Usuario> obj = repository.findByEmail(email);
		if (!obj.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado!");
		}
		if (!obj.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida!");
		}
		return obj.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario obj) {
		validarEmail(obj.getEmail());
		return repository.save(obj);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com esse email.");
		}
	}

}
