package com.cadastroVeiculos.CadastroVeiculos.Entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("veiculos")

data class VeiculoEntity(@Id var id: Int?, var veiculo: String, var marca: String, var ano: Int, var cor: String, var descricao: String,
                         val vendido: Boolean?, var created: Date?, var updated: Date?)


