package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.dto.PedidoRequestDTO;
import br.byteshop.ecommerce.bs_api.model.Pedido;
import br.byteshop.ecommerce.bs_api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarPedido() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        return pedido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(requestDTO);
            return ResponseEntity.ok(novoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idPedido}/status/{idNovoStatus}")
    public ResponseEntity<?> atualizarStatusDoPedido(
            @PathVariable Integer idPedido,
            @PathVariable Integer idNovoStatus) {

        try{
            Pedido pedidoAtualizado = pedidoService.atualizarStatusPedido(idPedido,idNovoStatus);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@PathVariable Integer clienteId) {

        List<Pedido> pedidosDoCliente = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidosDoCliente);
    }
}