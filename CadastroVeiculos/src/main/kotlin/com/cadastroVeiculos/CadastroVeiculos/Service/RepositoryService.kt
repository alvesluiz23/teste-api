package com.cadastroVeiculos.CadastroVeiculos.Service

import com.cadastroVeiculos.CadastroVeiculos.Entity.VeiculoEntity
import com.cadastroVeiculos.CadastroVeiculos.Repository.VeiculoRepository
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.stereotype.Service
import java.util.*

@Service
class RepositoryService(private val db: VeiculoRepository) {

    fun findVeiculo(): List<VeiculoEntity> = db.findAll().toList()

    fun findPorAnoCorMarca(cor: String, marca: String, ano:Int): List<VeiculoEntity> = db.findByAnoMarcaCorAno(cor, marca, ano).toList()

    fun findPorId(id: Int): Optional<VeiculoEntity> = db.findById(id)

    fun deleteById(id: Int): Unit = db.deleteById(id);

    fun save(veiculo: VeiculoEntity): VeiculoEntity = db.save(veiculo)

}