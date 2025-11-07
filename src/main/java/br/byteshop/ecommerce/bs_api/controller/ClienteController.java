package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.model.Cliente;
import br.byteshop.ecommerce.bs_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes no ByteShop")
@RestController
@RequestMapping("api/cliente")

public class ClienteController {

    private ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Lista todos os clientes", description = "Retorna a lista completa de todos os clientes.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @Operation(summary = "Busca cliente por ID", description = "Busca um cliente específico pelo seu ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Cliente encontrado",
            content = @Content(schema = @Schema(implementation = Cliente.class))
    )
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarClienteOuLancarExcecao(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Cria um novo cliente", description = "Salva um novo cliente no banco de dados.")
    @ApiResponse(
            responseCode = "200",
            description = "Cliente criado com sucesso",
            content = @Content(schema = @Schema(implementation = Cliente.class))
    )
    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @Operation(summary = "Atualiza cliente por ID", description = "Atualiza os dados de um cliente existente.")
    @ApiResponse(
            responseCode = "200",
            description = "Cliente atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = Cliente.class))
    )
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado para atualização", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteDetalhes) {
        Cliente clienteExistente = clienteService.buscarClienteOuLancarExcecao(id);

        clienteExistente.setCpf(clienteDetalhes.getCpf());
        clienteExistente.setNomeCliente(clienteDetalhes.getNomeCliente());
        clienteExistente.setDataNascimento(clienteDetalhes.getDataNascimento());
        clienteExistente.setEmail(clienteDetalhes.getEmail());
        clienteExistente.setCep(clienteDetalhes.getCep());
        clienteExistente.setEndereco(clienteDetalhes.getEndereco());
        clienteExistente.setEstado(clienteDetalhes.getEstado());

        Cliente clienteAtualizado = clienteService.salvar(clienteExistente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Operation(summary = "Deleta cliente por ID", description = "Remove um cliente do banco de dados.")
    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso (No Content)")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado para exclusão", content = @Content)
    @ApiResponse(responseCode = "400", description = "Erro na requisição (Ex: ID inválido, violação de integridade)", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Integer id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}

