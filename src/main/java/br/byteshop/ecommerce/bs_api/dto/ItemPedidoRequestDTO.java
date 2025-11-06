package br.byteshop.ecommerce.bs_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemPedidoRequestDTO {

    @NotNull(message = "O ID do produto não pode ser nulo.")
    private Integer produtoid;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private Integer quantidade;

    public Integer getProdutoid() {
        return produtoid;
    }

    public void setProdutoid(Integer produtoid) {
        this.produtoid = produtoid;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}