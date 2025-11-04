package br.byteshop.ecommerce.bs_api.repository;

import br.byteshop.ecommerce.bs_api.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Long> {
}