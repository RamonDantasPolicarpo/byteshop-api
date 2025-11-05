package br.byteshop.ecommerce.bs_api.dto;

public class ItemPedidoRequestDTO {

    private Integer produtoid;
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
