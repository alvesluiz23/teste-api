package com.cadastroVeiculos.CadastroVeiculos.Repository
import com.cadastroVeiculos.CadastroVeiculos.Entity.VeiculoEntity
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface VeiculoRepository : CrudRepository<VeiculoEntity, Int>{
    @Query("select * from veiculos  where cor = :cor and ano = :ano and marca = :marca")
    fun findByAnoMarcaCorAno(cor: String, marca: String, ano: Int): List<VeiculoEntity>

    @Query("select * from veiculos groupBy marcas")
    fun findByMarca(marca: String): List<VeiculoEntity>




}