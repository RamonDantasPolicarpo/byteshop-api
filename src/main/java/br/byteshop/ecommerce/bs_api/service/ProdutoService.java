package br.byteshop.ecommerce.bs_api.service;

import br.byteshop.ecommerce.bs_api.model.ItemPedido;
import br.byteshop.ecommerce.bs_api.model.Produto;
import br.byteshop.ecommerce.bs_api.repository.ItemPedidoRepository;
import br.byteshop.ecommerce.bs_api.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public Page<Produto> listarProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Optional<Produto> buscarPorId(Integer id) {
        return produtoRepository.findById(id);
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        List<ItemPedido> itensComEsteProduto = itemPedidoRepository.findByProdutoId(id);
        if (!itensComEsteProduto.isEmpty()) {
            throw new RuntimeException("Não é possível apagar o produto. Motivo: Produto já está associado a pedidos.");
        }
        produtoRepository.deleteById(id);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

}
