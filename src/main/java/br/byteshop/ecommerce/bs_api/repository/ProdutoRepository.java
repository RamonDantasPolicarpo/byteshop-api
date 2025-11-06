package br.byteshop.ecommerce.bs_api.repository;

import br.byteshop.ecommerce.bs_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findByCategoria(String categoria);
}