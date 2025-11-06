package br.byteshop.ecommerce.bs_api.service;

import br.byteshop.ecommerce.bs_api.dto.ItemPedidoRequestDTO;
import br.byteshop.ecommerce.bs_api.model.Pedido;
import br.byteshop.ecommerce.bs_api.dto.PedidoRequestDTO;
import br.byteshop.ecommerce.bs_api.model.*;
import br.byteshop.ecommerce.bs_api.repository.PedidoRepository;
import br.byteshop.ecommerce.bs_api.repository.StatusPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final StatusPedidoRepository statusPedidoRepository;


    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService,
                         ClienteService clienteService, StatusPedidoRepository statusPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.statusPedidoRepository = statusPedidoRepository;
    }

    @Transactional
    public Pedido atualizarStatusPedido(Integer idPedido, Integer idNovoStatus) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + idPedido));

        StatusPedido novoStatus = statusPedidoRepository.findById(idNovoStatus)
                .orElseThrow(() -> new RuntimeException("Status de Pedido não encontrado com ID: " + idNovoStatus));

        pedido.setStatusPedido(novoStatus);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido criarPedido(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteService.buscarPorId(requestDTO.getClienteid())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + requestDTO.getClienteid()));


        StatusPedido statusInicial = statusPedidoRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Status de pedido 'ID 1' não encontrado. Verifica a base de dados."));

        Pedido novoPedido = new Pedido();
        novoPedido.setId_cliente(cliente);
        novoPedido.setStatusPedido(statusInicial);

        novoPedido.setDataPedido(LocalDate.now());

        novoPedido.setNumeroPedido("PED-" + System.currentTimeMillis());

        for (ItemPedidoRequestDTO itemDTO : requestDTO.getItens()) {
            Produto produto = produtoService.buscarPorId(itemDTO.getProdutoid())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProdutoid()));

            if (produto.getEstoque() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Stock insuficiente para o produto: " + produto.getNomeProduto());
            }

            ItemPedido novoItem = new ItemPedido();

            novoItem.setIdPedido(novoPedido);

            novoItem.setIdProduto(produto);

            novoItem.setQuantidade(itemDTO.getQuantidade());

            novoItem.setValorUnitario(produto.getPreco());

            novoPedido.getItens().add(novoItem);
            produto.setEstoque(produto.getEstoque() - itemDTO.getQuantidade());
            produtoService.salvarProduto(produto);
        }
        return pedidoRepository.save(novoPedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPedidosPorCliente(Integer clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
}