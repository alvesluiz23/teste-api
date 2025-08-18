package com.cadastroVeiculos.CadastroVeiculos

import com.cadastroVeiculos.CadastroVeiculos.Entity.VeiculoEntity
import com.cadastroVeiculos.CadastroVeiculos.Service.RepositoryService
import com.cadastroVeiculos.CadastroVeiculos.Service.VeiculoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*



class veiculosServiceTest {
    private val repositoryService = mock(RepositoryService::class.java)
    private val veiculoService = VeiculoService(repositoryService)
    @Test
    fun testGetTodosCarros() {
        val mockEmployee = listOf(
            VeiculoEntity(
                1,
                "Civic",
                "Honda",
                2020,
                "Preto",
                "Sedan médio com bom consumo",
                false,
                Date(),
                Date()
            ),
            VeiculoEntity(
                2,
                "Corolla",
                "Toyota",
                2021,
                "Branco",
                "Sedan confiável e confortável",
                true,
                Date(),
                Date()
            ),
            VeiculoEntity(
                3,
                "Gol",
                "Volkswagen",
                2018,
                "Prata",
                "Compacto popular",
                false,
                Date(),
                Date()
            ),
            VeiculoEntity(
                4,
                "Onix",
                "Chevrolet",
                2019,
                "Vermelho",
                "Hatchback econômico",
                false,
                Date(),
                Date()
            )
        )

        Mockito.`when`(veiculoService.consultaTodosVeiculos()).thenReturn(mockEmployee)

        val result = veiculoService.consultaTodosVeiculos()
        assertEquals(4, result.size)
    }

    @Test
    fun `deve cadastrar veiculo com marca permitida`() {
        val veiculo = VeiculoEntity(
            id = 1,
            marca = "Honda",
            veiculo = "Civic",
            ano = 2020,
            cor = "Preto",
            descricao = "Carro teste",
            vendido = false,
            created = null,
            updated = null
        )
        veiculoService.cadastraVeiculo(veiculo)

        verify(repositoryService, times(1)).save(veiculo)
    }

    @Test
    fun `nao deve cadastrar veiculo com marca nao permitida`() {
        val veiculo = VeiculoEntity(
            id = 2,
            marca = "Ferrari",
            veiculo = "F8",
            ano = 2022,
            cor = "Vermelho",
            descricao = "Carro de luxo",
            vendido = false,
            created = null,
            updated = null
        )

        assertThrows<IllegalArgumentException> {
            veiculoService.cadastraVeiculo(veiculo)
        }
        verify(repositoryService, never()).save(veiculo)
    }
}
