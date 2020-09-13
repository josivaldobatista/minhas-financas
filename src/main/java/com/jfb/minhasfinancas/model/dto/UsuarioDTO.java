package com.jfb.minhasfinancas.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    private String nome;
    private String email;
    private String senha;
}