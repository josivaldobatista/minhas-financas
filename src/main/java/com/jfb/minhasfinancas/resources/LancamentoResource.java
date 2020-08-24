package com.jfb.minhasfinancas.resources;

import java.util.List;
import java.util.Optional;

import com.jfb.minhasfinancas.exceptions.RegraNegocioException;
import com.jfb.minhasfinancas.model.dto.LancamentoDTO;
import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.entity.Usuario;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.model.enums.TipoLancamento;
import com.jfb.minhasfinancas.services.LancamentoService;
import com.jfb.minhasfinancas.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
    
    private final LancamentoService service;
    private final UsuarioService usuarioService;

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
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.",
            HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) {
        return  service.obterPorId(id).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity(
            "Lancamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
        @RequestParam(value = "descricao", required = false) String descricao,
        @RequestParam(value = "mes", required = false) Integer mes,
        @RequestParam(value = "ano", required = false) Integer ano,
        @RequestParam(value = "usuario", required = true) Long idUsuario
    ) {
        Lancamento objFiltro = new Lancamento();
        objFiltro.setDescricao(descricao);
        objFiltro.setMes(mes);
        objFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obtetPorId(idUsuario);
        if (usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado para o ID informado.");
        } else {
            objFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> obj = service.buscar(objFiltro);
        return ResponseEntity.ok(obj);
    }

    private Lancamento converteParaDto(LancamentoDTO objDto) {
        Lancamento obj = new Lancamento();
        obj.setDescricao(objDto.getDescricao());
        obj.setAno(objDto.getAno());
        obj.setMes(objDto.getMes());
        obj.setValor(objDto.getValor());

        Usuario usuario = usuarioService.obtetPorId(objDto.getUsuario())
            .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado."));

        obj.setUsuario(usuario);

        if (objDto.getTipo() != null) {
            obj.setTipo(TipoLancamento.valueOf(objDto.getTipo()));
        }

        if (objDto.getStatus() != null) {
            obj.setStatus(StatusLancamento.valueOf(objDto.getStatus()));
        }
        return obj;
    }
}