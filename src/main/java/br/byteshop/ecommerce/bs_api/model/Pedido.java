package br.byteshop.ecommerce.bs_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb04_pedido", schema = "loja")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false)
    private Integer idPedido;

    @Column(name = "numero_pedido", nullable = false, unique = true, length = 50)
    private String numeroPedido;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente id_cliente;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_status_pedido", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private StatusPedido statusPedido;

    @OneToMany(
            mappedBy = "idPedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<ItemPedido> itens = new ArrayList<>();



    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Cliente getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Cliente id_cliente) {
        this.id_cliente = id_cliente;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Integer getId() {
        return idPedido;
    }

    public void setId(Integer id) {
        this.idPedido = id;
    }
}