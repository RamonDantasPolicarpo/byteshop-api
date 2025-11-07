package br.byteshop.ecommerce.bs_api.service;

import br.byteshop.ecommerce.bs_api.exception.BusinessRuleException;
import br.byteshop.ecommerce.bs_api.exception.ResourceNotFoundException;
import br.byteshop.ecommerce.bs_api.model.Cliente;
import br.byteshop.ecommerce.bs_api.model.Pedido;
import br.byteshop.ecommerce.bs_api.repository.ClienteRepository;
import br.byteshop.ecommerce.bs_api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClienteOuLancarExcecao(Integer id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID" + id));
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        List<Pedido> pedidosDoCliente = pedidoRepository.findByClienteId(id);

        if (!pedidosDoCliente.isEmpty()) {
            throw new BusinessRuleException("Não é possível apagar o cliente. Motivo: Cliente já possui pedidos associados.");
        }

        clienteRepository.deleteById(id);
    }
}
