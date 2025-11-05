package br.byteshop.ecommerce.bs_api.service;

import br.byteshop.ecommerce.bs_api.model.ItemPedido;
import br.byteshop.ecommerce.bs_api.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {

    private ItemPedidoRepository itemPedidoRepository;
    @Autowired

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> listarItemPedidos() {
        return itemPedidoRepository.findAll();
    }
}
