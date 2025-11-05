package br.byteshop.ecommerce.bs_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb03_status_pedido", schema = "loja")
public class StatusPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_pedido", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false, unique = true, length = 30)
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}