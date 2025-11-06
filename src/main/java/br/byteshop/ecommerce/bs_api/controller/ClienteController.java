package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.model.Cliente;
import br.byteshop.ecommerce.bs_api.model.Produto;
import br.byteshop.ecommerce.bs_api.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/cliente")

public class ClienteController {

    private ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteDetalhes) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);

        if(clienteOptional.isPresent()) {
            Cliente clienteExistente = clienteOptional.get();
            clienteExistente.setCpf(clienteDetalhes.getCpf());
            clienteExistente.setNomeCliente(clienteDetalhes.getNomeCliente());
            clienteExistente.setDataNascimento(clienteDetalhes.getDataNascimento());
            clienteExistente.setEmail(clienteDetalhes.getEmail());
            clienteExistente.setCep(clienteDetalhes.getCep());
            clienteExistente.setEndereco(clienteDetalhes.getEndereco());
            clienteExistente.setEstado(clienteDetalhes.getEstado());

            Cliente clienteAtualizado = clienteService.salvar(clienteExistente);
            return ResponseEntity.ok(clienteAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Integer id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


}
