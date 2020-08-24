package com.jfb.minhasfinancas.services;

import java.util.Optional;

import com.jfb.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario usuario);

	void validarEmail(String email);

	Optional<Usuario> obtetPorId(Long id);
}
