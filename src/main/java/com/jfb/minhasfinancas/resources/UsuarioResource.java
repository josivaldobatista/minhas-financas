package com.jfb.minhasfinancas.resources;

import com.jfb.minhasfinancas.exceptions.ErroAutenticacao;
import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.dto.UsuarioDTO;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
    
    @Autowired
    private UsuarioService service;

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
        Usuario obj = Usuario.builder()
            .nome(dto.getNome())
            .email(dto.getEmail())
            .senha(dto.getSenha()).build();

            try {
                Usuario objSalvo = service.salvarUsuario(obj);
                return new ResponseEntity(objSalvo, HttpStatus.CREATED);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
}