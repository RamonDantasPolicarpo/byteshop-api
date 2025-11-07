package br.byteshop.ecommerce.bs_api.controller;

import br.byteshop.ecommerce.bs_api.model.Produto;
import br.byteshop.ecommerce.bs_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Produtos", description = "Gerenciamento de produtos, estoque e catálogo")
@RestController
@RequestMapping("/api/produto")

public class ProdutoController {

    private ProdutoService produtoService;

    @Autowired
    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Lista todos os produtos (paginado)", description = "Retorna uma página de produtos, com suporte a paginação e ordenação.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<Produto>> listarProdutos(Pageable pageable) {
        Page<Produto> paginaDeProdutos = produtoService.listarProdutos(pageable);
        return ResponseEntity.ok(paginaDeProdutos);
    }

    @Operation(summary = "Busca produto por ID", description = "Busca um produto específico pelo seu ID. Retorna 404 se não encontrado.")
    @ApiResponse(
            responseCode = "200",
            description = "Produto encontrado",
            content = @Content(schema = @Schema(implementation = Produto.class))
    )
    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {

        Produto produto = produtoService.buscarProdutoOuLancarExcecao(id);
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Busca produtos por categoria", description = "Retorna todos os produtos que pertencem a uma categoria específica.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria retornada com sucesso")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Cria um novo produto", description = "Salva um novo produto no banco de dados.")
    @ApiResponse(
            responseCode = "201",
            description = "Produto criado com sucesso",
            content = @Content(schema = @Schema(implementation = Produto.class))
    )
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    @Operation(summary = "Atualiza produto por ID", description = "Atualiza os dados de um produto existente.")
    @ApiResponse(
            responseCode = "200",
            description = "Produto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = Produto.class))
    )
    @ApiResponse(responseCode = "404", description = "Produto não encontrado para atualização")
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoDetalhes) {

        Produto produtoExistente = produtoService.buscarProdutoOuLancarExcecao(id);

            produtoExistente.setNomeProduto(produtoDetalhes.getNomeProduto());
            produtoExistente.setCodigoProduto(produtoDetalhes.getCodigoProduto());
            produtoExistente.setPreco(produtoDetalhes.getPreco());
            produtoExistente.setCategoria(produtoDetalhes.getCategoria());
            produtoExistente.setEstoque(produtoDetalhes.getEstoque());

            Produto produtoAtualizado = produtoService.salvarProduto(produtoExistente);
            return ResponseEntity.ok(produtoAtualizado);

    }

    @Operation(summary = "Deleta produto por ID", description = "Remove um produto do banco de dados.")
    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso (No Content)")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado para exclusão")
    @ApiResponse(responseCode = "400", description = "Erro de integridade (ex: produto em um pedido ativo)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Integer id) {

        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

}
