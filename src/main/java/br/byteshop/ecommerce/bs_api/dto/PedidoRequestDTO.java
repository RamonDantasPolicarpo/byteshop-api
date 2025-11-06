package br.byteshop.ecommerce.bs_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente não pode ser nulo.")
    private Integer clienteid;

    @NotEmpty(message = "A lista de itens não pode estar vazia.")
    @Valid
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
