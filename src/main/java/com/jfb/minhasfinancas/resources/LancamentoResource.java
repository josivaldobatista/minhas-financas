package com.jfb.minhasfinancas.resources;

import com.jfb.minhasfinancas.model.dto.LancamentoDTO;
import com.jfb.minhasfinancas.services.LancamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
    
    @Autowired
    private LancamentoService service;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO objDto) {
        
        return null;
    }
}