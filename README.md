# 🎮 ByteGames

Loja de games completa — vitrine pública, carrinho de compras e painel de
administração — construída com **Spring Boot 3** e **Java 17**.

Projeto acadêmico do curso de Análise e Desenvolvimento de Sistemas (FATEC
Itapetininga), feito para exercitar arquitetura em camadas, mapeamento
objeto-relacional e CRUD com validação.

---

## Funcionalidades

**Loja (público)**

- Vitrine com todos os jogos em catálogo
- Busca de jogos por nome, sem diferenciar maiúsculas e minúsculas
- Página de detalhes de cada jogo
- Carrinho de compras persistido na sessão HTTP, com cálculo do total

**Administração**

- CRUD completo de **Produtos** — criar, listar, detalhar, editar e excluir
- CRUD completo de **Categorias**, com nome único
- CRUD completo de **FAQ** (perguntas frequentes)
- Validação de formulário com mensagens de erro por campo

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 (Web, Data JPA, Validation) |
| Templates | Thymeleaf |
| ORM | Hibernate / JPA |
| Banco | MySQL 8 (padrão) ou H2 em memória |
| Pool de conexões | HikariCP |
| Build | Maven (com wrapper `mvnw`) |
| Testes | JUnit 5 + `@SpringBootTest` |

---

## Modelo de dados

```
categoria                          produto
─────────                          ───────
id        BIGINT  PK               id           BIGINT  PK
nome      VARCHAR(50)  UNIQUE      nome         VARCHAR(100)
descricao VARCHAR(200)             descricao    VARCHAR(500)
                                   preco        DECIMAL(10,2)  CHECK >= 0.01
                                   estoque      INT            CHECK >= 0
                                   categoria_id BIGINT  FK ──> categoria(id)
                                                        ON DELETE CASCADE
```

O relacionamento é `@ManyToOne` de `Produto` para `Categoria`, com carregamento
`LAZY`. As regras estão tanto nas anotações de validação da entidade quanto nas
`CONSTRAINT` do banco — o script SQL não depende do Hibernate para garantir
integridade.

---

## Como rodar

**Requisitos:** Java 17+ e MySQL 8 (ou use o H2, veja abaixo).

```bash
git clone https://github.com/guxta18-rgb/byte-games.git
cd byte-games
```

**1. Configure o acesso ao banco.** Copie o exemplo e preencha com as suas
credenciais:

```bash
cp .env.example .env
```

**2. Crie o banco e popule com dados de exemplo** (4 categorias e 5 jogos):

```bash
mysql -u root -p < script_criacao_banco.sql
```

**3. Suba a aplicação:**

```bash
./mvnw spring-boot:run
```

Acesse **http://localhost:8080**.

### Rodando sem MySQL

Para subir com banco em memória, sem instalar nada, descomente o bloco
`H2 EM MEMÓRIA` em `src/main/resources/application.properties` e comente o bloco
do MySQL logo abaixo.

---

## Estrutura

```
src/main/java/loja/bytegames/
├── BytegamesApplication.java      ponto de entrada
├── Produto.java                   entidades JPA
├── Categoria.java
├── Faq.java
├── LojaController.java            vitrine, busca e carrinho
├── ProdutoController.java         CRUD administrativo
├── CategoriaController.java
├── FaqController.java
└── repository/                    interfaces JPA + queries customizadas
    ├── ProdutoRepository.java
    ├── CategoriaRepository.java
    └── FaqRepository.java

src/main/resources/templates/      views Thymeleaf
src/test/java/                     teste de integração do fluxo CRUD
script_criacao_banco.sql           schema + dados de exemplo
```

---

## Testes

```bash
./mvnw test
```

`BytegamesDatabaseTests` sobe o contexto completo do Spring e executa um fluxo
CRUD real contra o banco: insere uma categoria, insere um produto associado a
ela, verifica a leitura e limpa os dados no final.

---

## Licença

Projeto acadêmico, disponibilizado para estudo.
