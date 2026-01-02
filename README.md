# ByteShop API - E-commerce

Este projeto consiste em uma **API REST** completa para gerenciamento de um e-commerce, desenvolvida como parte integrante da disciplina de **Programação Orientada a Objetos (POO)**.

A proposta do projeto foi aplicar os conceitos fundamentais de POO em um cenário real, utilizando tecnologias de mercado para criar um sistema robusto, com persistência de dados, containerização e documentação automatizada.

## 🚀 Funcionalidades Principais

A API gerencia o fluxo básico de um e-commerce através dos seguintes módulos:

- **Clientes:** Cadastro, consulta, atualização e exclusão de perfis de usuários.  
- **Produtos:** Gestão de estoque, categorias e preços com suporte a paginação.  
- **Pedidos:** Sistema de fechamento de pedidos com validação de estoque em tempo real.  
- **Regras de Negócio:** Tratamento de exceções personalizadas (ex: impedir exclusão de clientes com pedidos ativos ou venda de produtos sem estoque).  
- **Persistência e Seed:** Inicialização automática do banco de dados com dados de teste via script SQL.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21  <img src="https://img.icons8.com/?size=100&id=Pd2x9GWu9ovX&format=png&color=000000" alt="logo JAVA" width="30" align="top">
- **Framework:** Spring Boot 3.5.7  <img src="https://icon.icepanel.io/Technology/svg/Spring.svg" alt="logo Spring" width="30" align="top">
- **Persistência:** Spring Data JPA / Hibernate  <img src="https://img.icons8.com/?size=100&id=KZHjwwenS7oK&format=png&color=000000" alt="logo DATABASE" width="30" align="top">
- **Banco de Dados:** PostgreSQL 15  <img src="https://icon.icepanel.io/Technology/svg/PostgresSQL.svg" alt="logo Postegress" width="30" align="top">
- **Containerização:** Docker & Docker Compose  <img src="https://img.icons8.com/?size=100&id=cdYUlRaag9G9&format=png&color=000000" alt="logo Docker" width="30" align="top">
- **Documentação:** SpringDoc OpenAPI (Swagger)  <img src="https://icon.icepanel.io/Technology/svg/Swagger.svg" alt="logo Swagger" width="30" align="top">
- **Gerenciador de Dependências:** Maven  <img src="https://icon.icepanel.io/Technology/svg/Apache-Maven.svg" alt="logo Maven" width="30" align="top">

## <img src="https://icon.icepanel.io/Technology/svg/Swagger.svg" alt="logo Swagger" width="30" align="top"> Documentação e Testes (Swagger)

A API utiliza o **Swagger** para documentação interativa.

🔗 **Swagger UI:**  
http://localhost:8080/swagger-ui/index.html

## <img src="https://img.icons8.com/?size=100&id=cdYUlRaag9G9&format=png&color=000000" alt="logo Docker" width="30" align="top"> Execução com Docker (Recomendado)

O projeto está configurado para subir todo o ambiente (API + Banco de Dados) automaticamente utilizando **Docker Compose**.

O banco de dados é inicializado automaticamente através do script:

```
/sql/init.sql
```

### Passos para rodar:

1. Certifique-se de ter o **Docker** e o **Docker Compose** instalados.
2. Na raiz do projeto, execute:

```bash
docker-compose up --build
```

3. O Docker irá:
   - Criar o container do banco de dados (`byteshop-db`);
   - Executar o script de inicialização (`init.sql`);
   - Fazer o build da aplicação Java via *multi-stage build*;
   - Subir a API (`byteshop-api`) na porta **8080**.

## 💻 Execução Manual (Local)

1. Crie um banco com o nome `byteshop_ecommerce` no PostgreSQL e execute o arquivo:

```
sql/init.sql
```

2. Ajuste as credenciais em:

```
src/main/resources/application.properties
```

3. Execute:

```bash
./mvnw spring-boot:run
```

## 👥 Equipe de Desenvolvimento

<table border="0">
<tr>
<td><b>Ramon Dantas</b></td>
<td><a href="https://github.com/ramondantaspolicarpo"><img src="https://img.icons8.com/?size=100&id=YcrCjhmmhYOJ&format=png&color=000000" width="30" align="middle"></a></td>
<td><a href="https://www.linkedin.com/in/ramondantaspolicarpo/"><img src="https://img.icons8.com/?size=100&id=KVqty3o0BQsi&format=png&color=000000" width="30" align="middle"></a></td>
</tr>
<tr>
<td><b>Matheus Calônico</b></td>
<td><a href="https://github.com/Husty-09"><img src="https://img.icons8.com/?size=100&id=YcrCjhmmhYOJ&format=png&color=000000" width="30" align="middle"></a></td>
<td><a href="https://www.linkedin.com/in/matheus-dos-santos-calonico-2b02a320b/"><img src="https://img.icons8.com/?size=100&id=KVqty3o0BQsi&format=png&color=000000" width="30" align="middle"></a></td>
</tr>
<tr>
<td><b>Henrique Bittencourt</b></td>
<td><a href="https://github.com/henrique-bittencourt00"><img src="https://img.icons8.com/?size=100&id=YcrCjhmmhYOJ&format=png&color=000000" width="30" align="middle"></a></td>
<td><a href="https://www.linkedin.com/in/henrique-bittencourt-asevedo-oliveira-240510365/"><img src="https://img.icons8.com/?size=100&id=KVqty3o0BQsi&format=png&color=000000" width="30" align="middle"></a></td>
</tr>
</table>

## 📄 Licença

Este projeto é para fins estritamente acadêmicos.
