package br.byteshop.ecommerce.bs_api.repository;

import br.byteshop.ecommerce.bs_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,Integer> {

    @Query("SElECT p FROM Pedido p WHERE p.id_cliente.idCliente = :clienteId")
    List<Pedido> findByClienteId(@Param("clienteId") Integer clienteId);
}