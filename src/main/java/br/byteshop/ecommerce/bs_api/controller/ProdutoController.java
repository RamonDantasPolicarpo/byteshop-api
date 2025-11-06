package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.model.Produto;
import br.byteshop.ecommerce.bs_api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produto")

public class ProdutoController {

    private ProdutoService produtoService;

    @Autowired
    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> listarProdutos(Pageable pageable) {
        Page<Produto> paginaDeProdutos = produtoService.listarProdutos(pageable);
        return ResponseEntity.ok(paginaDeProdutos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);

        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoDetalhes) {
        Optional<Produto> produtoOptional = produtoService.buscarPorId(id);

        if (produtoOptional.isPresent()) {
            Produto produtoExistente = produtoOptional.get();
            produtoExistente.setNomeProduto(produtoDetalhes.getNomeProduto());
            produtoExistente.setCodigoProduto(produtoDetalhes.getCodigoProduto());
            produtoExistente.setPreco(produtoDetalhes.getPreco());
            produtoExistente.setCategoria(produtoDetalhes.getCategoria());
            produtoExistente.setEstoque(produtoDetalhes.getEstoque());

            Produto produtoAtualizado = produtoService.salvarProduto(produtoExistente);
            return ResponseEntity.ok(produtoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Integer id) {
        try {
            produtoService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
