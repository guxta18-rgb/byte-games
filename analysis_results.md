# Relatório do Projeto Bytegames (Spring Boot)

Este documento apresenta uma análise técnica detalhada do estado atual do projeto **Bytegames**, uma aplicação web desenvolvida em Spring Boot.

---

## 1. Visão Geral do Projeto

O **Bytegames** é um sistema de e-commerce (loja virtual) focado na venda de jogos e produtos correlatos. A aplicação possui uma interface pública para navegação na vitrine e gerenciamento de carrinho de compras, além de uma área administrativa protegida para o gerenciamento de catálogo (CRUD de produtos).

### Tecnologias Utilizadas
* **Linguagem:** Java 17
* **Framework Backend:** Spring Boot 4.0.6
* **Persistência de Dados:** Spring Data JPA com banco de dados em memória **H2 Database**
* **Mecanismo de Template:** Thymeleaf
* **Validação de Dados:** Jakarta Validation (`spring-boot-starter-validation`)
* **Estilização Frontend:** CSS nativo (Vanilla CSS) com tema de estilo dark (gamer) nas cores roxo, verde e cinza escuro.

---

## 2. Arquitetura e Estrutura de Código

O código-fonte Java está estruturado no pacote principal `loja.bytegames` com as seguintes divisões de responsabilidade:

```text
loja.bytegames
├── BytegamesApplication.java (Classe de inicialização)
├── config
│   └── DataInitializer.java (Inicializador de dados de teste)
├── controller
│   ├── LojaController.java (Rotas públicas da vitrine e carrinho)
│   └── ProdutoController.java (Rotas administrativas de produtos)
├── model
│   ├── Categoria.java (Entidade de Categorias)
│   └── Produto.java (Entidade de Produtos)
└── repository
    ├── CategoriaRepository.java (Interface JPA para Categoria)
    └── ProdutoRepository.java (Interface JPA para Produto com busca personalizada)
```

### Detalhamento dos Componentes

#### Modelos de Dados (Entidades JPA)
* **`Categoria` (`Categoria.java`):**
  * Representa a tabela `categoria` no banco de dados.
  * Possui campos `id` (chave primária gerada automaticamente) e `nome` (obrigatório e único).
  * Possui uma relação `@OneToMany` com `Produto`, configurada com carregamento tardio (`FetchType.LAZY`) e remoção em cascata (`CascadeType.ALL`).
* **`Produto` (`Produto.java`):**
  * Representa a tabela `produto`.
  * Possui campos `id` (chave primária), `nome` (obrigatório, de 2 a 100 caracteres), `descricao` (obrigatória, até 500 caracteres), `preco` (obrigatório, maior que zero, com precisão monetária), e `estoque` (obrigatório, não negativo).
  * Possui uma relação `@ManyToOne` com `Categoria` através da coluna `categoria_id` (não nula).

#### Repositórios
* **`CategoriaRepository`:** Interface padrão que estende `JpaRepository`.
* **`ProdutoRepository`:** Interface que estende `JpaRepository` com o método customizado `findByNomeContainingIgnoreCase(String nome)` para realizar buscas textuais insensíveis a maiúsculas/minúsculas na vitrine.

#### Configurações e Carga Inicial
* **`DataInitializer`:** Implementa `CommandLineRunner` e insere quatro categorias de teste ("RPG", "Ação e Aventura", "FPS / Tiro", "Estratégia") caso a tabela de categorias esteja vazia no momento em que a aplicação é iniciada.

#### Controladores
* **`LojaController`:**
  * Controla a página pública `/` (vitrine), suportando filtros de busca através do parâmetro `search`.
  * Gerencia o carrinho de compras na sessão do usuário (`HttpSession`) através de um `Map<Long, Integer>` mapeando o ID do produto à quantidade selecionada.
  * Oferece endpoints para exibir o carrinho (`/carrinho`), adicionar itens (`/carrinho/adicionar`) e limpar a sessão (`/carrinho/limpar`).
* **`ProdutoController`:**
  * Controla as rotas mapeadas em `/produtos` para as operações administrativas.
  * Oferece endpoints para listagem de produtos, detalhamento, exibição de formulários de criação/edição, salvamento e exclusão (método POST). Utiliza `@Valid` e `BindingResult` para validação de integridade dos dados enviados.

---

## 3. Recursos do Frontend e Templates

Os arquivos estáticos e modelos de página estão contidos sob `src/main/resources`:

### Estilos Estáticos (`static/css/estilo.css`)
O projeto utiliza um visual com design moderno gamer com as seguintes características:
* Paleta de cores escura (fundo `#121214`, containers `#1f2029`).
* Destaques em roxo (`#9871ff`) e verde (`#04d361`) para indicar botões principais e preços de produtos.
* Grid responsivo para a exibição de cards de jogos.
* Estilos dedicados para tratamento de erros de validação de formulários.

### Templates Thymeleaf (`templates`)
* **`loja/`**:
  * `vitrine.html`: Página inicial com barra de busca, exibição de produtos em cards com marcação de categoria, preço e validação de estoque (botão "Comprar" ou indicador de "Esgotado").
  * `carrinho.html`: Tabela contendo os itens adicionados ao carrinho, quantidades, subtotal por item e total acumulado do pedido.
* **`produto/`**:
  * `listar.html`: Painel com tabela administrativa listando todos os produtos e opções de edição/exclusão.
  * `detalhar.html`: Tela de visualização dos atributos de um produto.
  * `form-criar.html`: Formulário de criação de produtos contendo campo de categorias dinâmico.
  * `form-editar.html`: Formulário de edição populado com os dados existentes.

---

## 4. Inconsistência Crítica Detectada ⚠️

Durante a análise da estrutura física e do fluxo dos controladores, foi identificada uma divergência que resultará em erro em tempo de execução:

> [!WARNING]
> **Inconsistência de Caminho de Templates de Produtos**
> * O controlador **`ProdutoController`** possui retornos que direcionam o Thymeleaf para a pasta pluralizada `"produtos/"` (exemplo: `return "produtos/listar"`, `return "produtos/form-criar"`).
> * No entanto, o diretório físico no sistema de arquivos está nomeado no singular: `src/main/resources/templates/produto/`.
> * **Consequência:** Sempre que o administrador acessar rotas como `/produtos` ou tentar cadastrar um novo produto, a aplicação falhará com erro de `TemplateInputException` (template não encontrado).

### Ações Recomendadas para Correção
Para sanar este problema, escolha uma das seguintes abordagens:
1. **Opção A:** Renomear a pasta física de `src/main/resources/templates/produto` para `src/main/resources/templates/produtos`.
2. **Opção B:** Atualizar os retornos das strings nos métodos do `ProdutoController` para utilizarem o singular (exemplo: de `"produtos/listar"` para `"produto/listar"`).
