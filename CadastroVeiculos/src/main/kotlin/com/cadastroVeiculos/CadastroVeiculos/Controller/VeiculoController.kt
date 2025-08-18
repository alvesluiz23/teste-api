package com.cadastroVeiculos.CadastroVeiculos.Controller

import com.cadastroVeiculos.CadastroVeiculos.Entity.VeiculoEntity
import com.cadastroVeiculos.CadastroVeiculos.Service.VeiculoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody as OASRequestBody
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@Tag(name = "Veículos", description = "Operações de consulta e cadastro de veículos")
@RestController
@RequestMapping("/veiculos")
class VeiculoController @Autowired constructor(private val veiculoService: VeiculoService) {
    @Operation(
        summary = "Consulta veículos",
        description = "Busca por id OU por marca+ano+cor. Sem parâmetros, retorna todos.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                content = [Content(schema = Schema(implementation = Map::class))])
        ]
    )
    @GetMapping
    @CrossOrigin
    fun consultaVeiculos(
        @Parameter(description = "Marca exata", example = "Honda")  @RequestParam(required = false) marca: String?,
        @Parameter(description = "Ano do modelo", example = "2020") @RequestParam(required = false) ano: Int?,
        @Parameter(description = "Cor exata", example = "Preto")  @RequestParam(required = false) cor: String?,
        @Parameter(description = "Identificador do veículo", example = "1")  @RequestParam(required = false) id: Int?
    ): ResponseEntity<Any> = when {
        id != null -> {
            val veiculo = veiculoService.consultaPorId(id)
            ResponseEntity.ok(veiculo)
        }
        (marca != null || ano != null || cor != null) -> {
            if (marca != null && ano != null && cor != null) {
                val lista = veiculoService.consultaPorMarcaAnoCor(cor.trim(), marca.trim(), ano)
                ResponseEntity.ok(lista)
            } else {
                ResponseEntity.badRequest().body(
                    mapOf("error" to "Para filtrar, informe os três: marca, ano e cor.")
                )
            }
        }
        else -> ResponseEntity.ok(veiculoService.consultaTodosVeiculos())
    }

    @Operation(
        summary = "Cadastra veículos",
        description = "Cadastra um novo veiculo.",
        responses = [
            ApiResponse(responseCode = "200", description = "Veículo cadastrado com sucesso!"),
            ApiResponse(responseCode = "400", description = "\\\"Marca não é permitida.\\"),
            ApiResponse(responseCode = "400", description = "Erro inesperado: detalhe\\",

                content = [Content(schema = Schema(implementation = Map::class))])
        ]
    )
    @PostMapping
    @CrossOrigin
    fun cadastraVeiculo(    @OASRequestBody(
        required = true,
        description = "Payload do veículo a ser cadastrado",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = VeiculoEntity::class),
                examples = [ExampleObject(
                    name = "Exemplo",
                    value = """
                    {
                      "veiculo": "Civic",
                      "marca": "Honda",
                      "ano": 2020,
                      "cor": "Preto",
                      "descricao": "Sedan médio com bom consumo",
                      "vendido": false
                    }
                    """
                )]
            )
        ]
    )@RequestBody veiculoEntity: VeiculoEntity): ResponseEntity<Any> {

        return try {
            veiculoService.cadastraVeiculo(veiculoEntity)
            ResponseEntity.status(HttpStatus.CREATED).body("Veículo cadastrado com sucesso!")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: ${e.message}")
        }
    }

    @Operation(
        summary = "Atualiza veículo",
        description = "Atualiza todos os dados de um veículo existente. O corpo da requisição deve conter o ID do veículo e os novos valores."
    )

    @PutMapping
    @CrossOrigin
    fun atualizaVeiculo(    @OASRequestBody(
        required = true,
        description = "Dados completos do veículo a ser atualizado (incluindo ID).",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = VeiculoEntity::class),
                examples = [ExampleObject(
                    name = "Exemplo",
                    value = """
                    {
                      "id": 1,
                      "veiculo": "Civic",
                      "marca": "Honda",
                      "ano": 2021,
                      "cor": "Branco",
                      "descricao": "Sedan atualizado",
                      "vendido": false
                    }
                    """
                )]
            )
        ]
    )@RequestBody veiculoEntity: VeiculoEntity) = veiculoService.cadastraVeiculo(veiculoEntity);


    @Operation(
        summary = "Atualiza campos parciais de um veículo",
        description = "Atualiza apenas os campos enviados no corpo. É necessário informar o parâmetro de query `id`."
    )

    @PatchMapping
    @CrossOrigin
    fun atualizarCampos(
        @OASRequestBody(

            description = "Campos a serem atualizados (somente os enviados serão aplicados).",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = VeiculoEntity::class),
                examples = [ExampleObject(
                    name = "Atualização parcial",
                    value = """
                {
                  "cor": "Branco",
                  "descricao": "Pintura refeita"
                }
                """
                )]
            )]
        )
        @RequestParam id: Int,
        @RequestBody updates: VeiculoEntity
    ): ResponseEntity<Any> = try {
        ResponseEntity.ok(veiculoService.atualizaCampos(id, updates))
    } catch (e: NoSuchElementException) {
        ResponseEntity.notFound().build()
    } catch (e: IllegalArgumentException) {
        ResponseEntity.badRequest().body(mapOf("error" to e.message))
    }
    @Operation(
        summary = "Deleta um veículo",
        description = "Remove um veículo existente a partir do seu ID."
    )

    @DeleteMapping
    @CrossOrigin
    fun deletaVeiculo(@Parameter(description = "ID do veículo a ser removido", required = true, example = "1") @RequestParam("id") id: Int) = veiculoService.deletaVeiculo(id)


}