package com.jfb.minhasfinancas.resources;

import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.dto.LancamentoDTO;
import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.model.enums.TipoLancamento;
import com.jfb.minhasfinancas.services.LancamentoService;
import com.jfb.minhasfinancas.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
    
    @Autowired
    private LancamentoService service;

    private UsuarioService UsuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO objDto) {
        try {
            Lancamento entidade = converteParaDto(objDto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, LancamentoDTO objDto) {
            return service.obterPorId(id).map(entity -> {
                try {
                    Lancamento obj = converteParaDto(objDto);
                    obj.setId(entity.getId());
                    service.atualizar(obj);
                    return ResponseEntity.ok(obj);
                } catch (RegraNegocioException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados.",
            HttpStatus.BAD_REQUEST));
    }

    private Lancamento converteParaDto(LancamentoDTO objDto) {
        Lancamento obj = new Lancamento();
        obj.setDescricao(objDto.getDescricao());
        obj.setAno(objDto.getAno());
        obj.setMes(objDto.getMes());
        obj.setValor(objDto.getValor());

        Usuario usuario = UsuarioService.obtetPorId(objDto.getUsuario())
            .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado."));

        obj.setUsuario(usuario);
        obj.setTipo(TipoLancamento.valueOf(objDto.getTipo()));
        obj.setStatus(StatusLancamento.valueOf(objDto.getStatus()));
        return obj;
    }
}