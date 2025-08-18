package com.cadastroVeiculos.CadastroVeiculos.Service

import com.cadastroVeiculos.CadastroVeiculos.Entity.VeiculoEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class VeiculoService @Autowired constructor(private val repositoryService: RepositoryService) {
    fun consultaTodosVeiculos(): List<VeiculoEntity> {
        return repositoryService.findVeiculo()
    }
    fun consultaPorMarcaAnoCor(cor: String, marca: String, ano: Int): List<VeiculoEntity> {
        return repositoryService.findPorAnoCorMarca(cor,marca,ano);

    }
    fun consultaPorId(id: Int): VeiculoEntity{
        return repositoryService.findPorId(id).get();

    }
    fun cadastraVeiculo(veiculoEntity: VeiculoEntity){
        val marcasPermitidas = listOf("Honda", "Toyota", "Chevrolet", "Volkswagen", "Fiat")
        if (veiculoEntity.marca !in marcasPermitidas) {
            throw IllegalArgumentException("Marca '${veiculoEntity.marca}' não é permitida.")
        }
        veiculoEntity.created =  Date()
        veiculoEntity.updated =  Date()
        repositoryService.save(veiculoEntity);
    }

    fun atualizaCampos(id: Int, veiculoEntity: VeiculoEntity): VeiculoEntity {
        val veiculoOpt = repositoryService.findPorId(id)

        val marcasPermitidas = listOf("Honda", "Toyota", "Chevrolet", "Volkswagen", "Fiat")
        if(!veiculoOpt.isPresent){
            throw NoSuchElementException("Veículo $id não encontrado")
        }

        val veiculo = veiculoOpt.get()
        veiculoEntity.marca?.let {
            if (it !in marcasPermitidas) {
                throw IllegalArgumentException("Marca '$it' não é permitida.")
            }
            veiculo.marca = it
        }
        veiculoEntity.veiculo?.let { veiculo.veiculo = it }
        veiculoEntity.ano?.let { veiculo.ano = it }
        veiculoEntity.cor?.let { veiculo.cor = it }
        veiculoEntity.descricao?.let { veiculo.descricao = it }
        veiculoEntity.updated = Date()
        return repositoryService.save(veiculo)
    }

    fun deletaVeiculo(id: Int): Any {
        return repositoryService.deleteById(id);
    }
}