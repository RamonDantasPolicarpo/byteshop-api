package br.byteshop.ecommerce.bs_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb01_cliente", schema = "loja")
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_cliente", nullable = false)
  private Long idCliente;

  @Column(name = "cpf", unique = true, nullable = false, length = 11)
  private String cpf;

  @Column(name = "nome_cliente", nullable = false, length = 100)
  private String nomeCliente;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "cep", nullable = false, length = 8)
  private String cep;

  @Column(name = "endereco", nullable = false)
  private String endereco;

  @Column(name = "cidade", nullable = false, length = 100)
  private String cidade;

  @Column(name = "estado", nullable = false, length = 2)
  private String estado;

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getNomeCliente() {
    return nomeCliente;
  }

  public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }
}