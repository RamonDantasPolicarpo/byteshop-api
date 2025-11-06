package br.byteshop.ecommerce.bs_api.repository;

import br.byteshop.ecommerce.bs_api.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    @Query("SELECT ip FROM ItemPedido ip WHERE ip.idProduto.id = :produtoId")
    List<ItemPedido> findByProdutoId(@Param("produtoId") Integer produtoId);
}