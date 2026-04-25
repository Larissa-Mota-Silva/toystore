# 🧸 ToyStore — Loja Virtual

Aplicação web de uma loja de brinquedos desenvolvida como projeto acadêmico na **FATEC Guarulhos**.  
Permite que clientes naveguem pelo catálogo de produtos e que funcionários gerenciem o estoque via painel administrativo.

---

## 🚀 Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Back-end | Java 17 · Spring Boot 4.0.5 · Spring MVC · Spring Data JPA |
| Front-end | Thymeleaf · HTML/CSS · JavaScript |
| Banco de dados | MySQL |
| Build | Maven |

---

## ✨ Funcionalidades

### Área Pública
- **Home** — exibe os 6 produtos mais recentes
- **Catálogo** — lista todas as categorias disponíveis
- **Categoria** — produtos filtrados por categoria com ordenação (menor preço, maior preço, A–Z)
- **Detalhe do produto** — informações completas e produtos relacionados da mesma categoria

### Área do Cliente
- Cadastro e login de usuários
- Perfil pessoal com edição de dados (nome, e-mail, CPF, telefone, data de nascimento)

### Painel Administrativo (Funcionário)
- Acesso restrito a usuários com perfil `FUNCIONARIO`
- Cadastro de novos produtos com upload de imagem
- Edição e exclusão de produtos
- Listagem completa do estoque

---

## 📁 Estrutura do Projeto

```
src/main/java/.../brinquedos/
├── controller/
│   ├── ProdutoController.java    # Rotas públicas e CRUD admin
│   ├── CategoriaController.java  # Filtragem e ordenação por categoria
│   ├── LoginController.java      # Autenticação, cadastro e perfil
│   └── PaginaController.java     # Páginas estáticas (sobre, etc.)
├── dao/
│   ├── ProdutoDAO.java           # Acesso a dados de produtos
│   └── UsuarioDAO.java           # Acesso a dados de usuários
└── model/
    ├── Produto.java              # Entidade produto
    └── Usuario.java              # Entidade usuário (CLIENTE / FUNCIONARIO)
```

---

## ⚙️ Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.9+
- MySQL 8+

### 1. Banco de dados

Crie o banco no MySQL:

```sql
CREATE DATABASE brinquedos_db;
```

### 2. Configure as credenciais

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/brinquedos_db
spring.datasource.username=root
spring.datasource.password=sua_senha
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

Acesse em: [http://localhost:8080](http://localhost:8080)

---

## 🔐 Perfis de Usuário

| Perfil | Acesso |
|--------|--------|
| `CLIENTE` | Navegação, perfil pessoal |
| `FUNCIONARIO` | Tudo acima + painel `/admin` (CRUD de produtos) |

> O cadastro de funcionário exige um código secreto interno.

---

## 📄 Licença

Projeto acadêmico — FATEC Guarulhos.
