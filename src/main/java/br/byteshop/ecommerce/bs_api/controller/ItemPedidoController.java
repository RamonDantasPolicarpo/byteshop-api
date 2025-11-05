package br.byteshop.ecommerce.bs_api.controller;


import br.byteshop.ecommerce.bs_api.model.ItemPedido;
import br.byteshop.ecommerce.bs_api.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/itempedido")

public class ItemPedidoController {
    private ItemPedidoService itemPedidoService;

    @Autowired
    public void setItemPedidoRepository(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping
    public List<ItemPedido> listarItemPedidos() {
        return itemPedidoService.listarItemPedidos();
    }
}
