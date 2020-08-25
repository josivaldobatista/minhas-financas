package com.jfb.minhasfinancas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import com.jfb.minhasfinancas.model.entity.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query(value = "select sum() from Lancamento l join l.usuario u "
        + "where u.id = :idUsuario and l.tipo = :tipo grounp by u")
    BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);
}
