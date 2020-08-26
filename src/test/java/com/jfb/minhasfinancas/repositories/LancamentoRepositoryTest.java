package com.jfb.minhasfinancas.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.jfb.minhasfinancas.model.entity.Lancamento;
import com.jfb.minhasfinancas.model.enums.StatusLancamento;
import com.jfb.minhasfinancas.model.enums.TipoLancamento;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    
    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento() {
        Lancamento obj = criarEPersisteUmLancamento();
        assertThat(obj.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento() {
    	Lancamento obj = criarLancamento();
    	entityManager.persist(obj); // Salva um lançamento.
    	obj = entityManager.find(Lancamento.class, obj.getId()); // Busca o lançamento que foi  salvo.
    	
    	repository.delete(obj);
    	Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, obj.getId());
    	assertThat(lancamentoInexistente).isNull();
    }
    
    @Test
    public void deveAtualizarUmLancamento() {
    	Lancamento obj = criarEPersisteUmLancamento();
    	obj.setAno(2021);
    	obj.setDescricao("Outra descrição qualquer.");
    	obj.setStatus(StatusLancamento.CANCELADO);
    	repository.save(obj);
    	
    	Lancamento objAtualizado = entityManager.find(Lancamento.class, obj.getId());
    	assertThat(objAtualizado.getAno()).isEqualTo(2021);
    	assertThat(objAtualizado.getDescricao()).isEqualTo("Outra descrição qualquer.");
    	assertThat(objAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }
    
    @Test
    public void deveBuscarUmLancamentoPorId() {
    	Lancamento obj = criarEPersisteUmLancamento();
    	Optional<Lancamento> objEncontrado = repository.findById(obj.getId());
    	assertThat(objEncontrado.isPresent()).isTrue();
    }
    
    // Métodos de apoio
    public static Lancamento criarLancamento() {
    	return Lancamento.builder()
    			.ano(2020)
    			.mes(1)
    			.descricao("Lancamento qualquer")
    			.valor(BigDecimal.valueOf(10))
    			.tipo(TipoLancamento.RECEITA)
    			.status(StatusLancamento.PENDENTE)
    			.dataCadastro(LocalDate.now())
    			.build();
    }
    
    private Lancamento criarEPersisteUmLancamento() {
    	Lancamento obj = criarLancamento();
    	obj = repository.save(obj);
    	return obj;
    }
}
