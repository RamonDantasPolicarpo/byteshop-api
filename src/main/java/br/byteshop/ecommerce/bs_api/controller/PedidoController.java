package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.dto.PedidoRequestDTO;
import br.byteshop.ecommerce.bs_api.model.Pedido;
import br.byteshop.ecommerce.bs_api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos e seus itens no ByteShop")
@RestController
@RequestMapping("api/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Lista todos os pedidos", description = "Retorna a lista completa de todos os pedidos.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    @GetMapping
    public List<Pedido> listarPedido() {
        return pedidoService.listarPedidos();
    }

    @Operation(summary = "Busca pedido por ID", description = "Busca um pedido específico pelo seu ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado",
            content = @Content(schema = @Schema(implementation = Pedido.class))
    )
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {
        Pedido pedido = pedidoService.buscarPedidoOuLancarExcecao(id);
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Cria um novo pedido", description = "Cria um pedido a partir de um DTO, validando itens e cliente.")
    @ApiResponse(
            responseCode = "200",
            description = "Pedido criado com sucesso",
            content = @Content(schema = @Schema(implementation = Pedido.class))
    )
    @ApiResponse(responseCode = "400", description = "Dados inválidos (DTO) ou violação de regra de negócio (ex: cliente/produto inexistente)")
    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        Pedido novoPedido = pedidoService.criarPedido(requestDTO);
        return ResponseEntity.ok(novoPedido);
    }

    @Operation(summary = "Atualiza status do pedido", description = "Altera o status de um pedido existente.")
    @ApiResponse(
            responseCode = "200",
            description = "Status do pedido atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = Pedido.class))
    )
    @ApiResponse(responseCode = "404", description = "Pedido ou Status não encontrado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Tentativa de mudar para um status inválido ou erro de negócio")
    @PutMapping("/{idPedido}/status/{idNovoStatus}")
    public ResponseEntity<?> atualizarStatusDoPedido(
            @PathVariable Integer idPedido,
            @PathVariable Integer idNovoStatus) {

        Pedido pedidoAtualizado = pedidoService.atualizarStatusPedido(idPedido, idNovoStatus);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Busca pedidos por cliente", description = "Retorna todos os pedidos feitos por um cliente específico.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos do cliente retornada.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado ou cliente não possui pedidos.")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@PathVariable Integer clienteId) {

        List<Pedido> pedidosDoCliente = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidosDoCliente);
    }
}