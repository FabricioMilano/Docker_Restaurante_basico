Sistema de Gerenciamento de Comida & Pedidos

Aplicação Full-Stack desenvolvida para gerenciamento de produtos e controle de pedidos (Kanban), totalmente containerizada com Docker e Docker Compose.

Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate
- **Banco de Dados:** MySQL 8.0
- **Frontend:** HTML5, CSS3, JavaScript (Vanilla JS / Fetch API)
- **Infraestrutura:** Docker, Multi-Stage Dockerfile, Docker Compose

 Funcionalidades

- CRUD completo de Produtos (Cadastro, Listagem e Remoção).
- Criação de Pedidos com seleção múltipla de produtos.
- Transição de status de pedidos em tempo real (Recebido ➔ Preparando ➔ Pronto).
- Persistência de dados em volume Docker e carga inicial automatizada via `data.sql`.

