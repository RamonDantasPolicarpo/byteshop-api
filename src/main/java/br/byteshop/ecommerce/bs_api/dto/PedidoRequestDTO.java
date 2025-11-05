package br.byteshop.ecommerce.bs_api.dto;

import java.util.List;

public class PedidoRequestDTO {

    private Integer clienteid;
    private List<ItemPedidoRequestDTO> itens;

    public Integer getClienteid() {
        return clienteid;
    }

    public void setClienteid(Integer clienteid) {
        this.clienteid = clienteid;
    }

    public List<ItemPedidoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens) {
        this.itens = itens;
    }
}
