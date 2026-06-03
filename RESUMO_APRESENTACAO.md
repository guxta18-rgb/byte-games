# 📢 Roteiro e Resumo de Apresentação: ByteGames

Este é um guia rápido estruturado em tópicos para você apresentar o projeto ao professor em **menos de 3 minutos**, demonstrando domínio completo do código, arquitetura de software e banco de dados.

---

## Tópico 1: 🎮 O que é a ByteGames?
*   **A Aplicação:** É um sistema de e-commerce (loja virtual) para venda de jogos digitais e físicos.
*   **O CRUD Principal:** O gerenciamento administrativo gerencia duas entidades principais de forma relacional:
    1.  **Produtos (Jogos):** Dados como nome, descrição, preço, estoque e a categoria associada.
    2.  **Categorias:** Gêneros dos jogos (RPG, Ação, FPS, Estratégia), com nomes únicos garantidos pelo banco.
*   **Interface do Cliente:** Vitrine pública de produtos com barra de busca por nome e sistema de **Carrinho de Compras** integrado de forma dinâmica à sessão HTTP.

---

## Tópico 2: 🛠️ Arquitetura e Stack de Produção
*   **Linguagem & Framework:** **Java 17** com **Spring Boot 3.2.5**.
*   **Banco de Dados:** **MySQL (Byteloja)** para persistência física e em disco.
*   **Segurança de Credenciais:** Separação profissional das variáveis de ambiente de acesso utilizando o arquivo `.env` (excluído do versionamento pelo `.gitignore`).
*   **Conexão e Performance:** Uso da biblioteca `spring-dotenv` para leitura do `.env` associada ao **HikariCP** (pool de conexões de alto desempenho) para otimizar acessos simultâneos no banco de dados.

---

## Tópico 3: 🔄 Funcionamento das Operações (Como o CRUD trabalha com o MySQL)
Explicar ao professor que o sistema adota a arquitetura **MVC** com **Spring Data JPA** e **Hibernate (ORM)**. Em vez de escrever código SQL bruto na aplicação, o ORM mapeia os objetos Java e gera as queries nativas do MySQL automaticamente:

| Operação CRUD | Método no Controller/Repository | Query Traduzida para o MySQL |
| :--- | :--- | :--- |
| **C**reate (Inserir) | `produtoRepository.save(produto)` | `INSERT INTO produto (nome, preco, ...) VALUES (?, ?, ...);` |
| **R**ead (Listar) | `produtoRepository.findAll()` | `SELECT * FROM produto;` |
| **R**ead (Detalhar) | `produtoRepository.findById(id)` | `SELECT * FROM produto WHERE id = ?;` |
| **U**pdate (Atualizar) | `produtoRepository.save(produto)` *(com ID)* | `UPDATE produto SET nome = ? WHERE id = ?;` |
| **D**elete (Remover) | `produtoRepository.deleteById(id)` | `DELETE FROM produto WHERE id = ?;` |

---

## Tópico 4: 🛡️ Validação em Camadas e Segurança de Código
*   **Validação no Model:** As propriedades das entidades são restritas por anotações do **Jakarta Validation** (ex: `@NotBlank`, `@Size(min=2)`, `@DecimalMin("0.01")`, `@Min(0)`).
*   **Validação no Controller:** Uso do `@Valid` associado ao `BindingResult` para capturar erros e exibir avisos amigáveis para o usuário na tela, impedindo que requisições com dados incorretos ou em branco cheguem ao MySQL.

---

## 🧪 Tópico 5: Fluxo de Teste de Integração MySQL
O fluxo completo do CRUD foi testado e validado de ponta a ponta no banco MySQL através de uma suite de testes unitários automatizada (`BytegamesDatabaseTests.java`):

1.  **Criação:** Uma categoria e um produto fictícios são salvos no MySQL (`INSERT`).
2.  **Busca:** O registro é recuperado por seu ID e listado no terminal (`SELECT`).
3.  **Alteração:** O preço é alterado e persistido com sucesso (`UPDATE`).
4.  **Remoção:** Os registros são deletados para limpar a base de dados (`DELETE`).

---

## 🎤 Roteiro de Fala Sugerido (Apresentação de 3 minutos)

1.  **Introdução:**
    > *"Olá, professor. Vou apresentar o sistema ByteGames, uma loja virtual de jogos que implementa um CRUD relacional de produtos e categorias desenvolvido em Spring Boot MVC com persistência real em MySQL."*
2.  **Segurança e Boas Práticas:**
    > *"Seguindo padrões profissionais da indústria, isolamos as credenciais de banco no arquivo `.env` para que as senhas locais não fiquem vulneráveis no código público. Utilizamos também o HikariCP como pool de conexões para otimizar os acessos."*
3.  **Persistência e ORM:**
    > *"A aplicação utiliza Spring Data JPA e Hibernate como ORM. Isso nos permite gerenciar o banco de forma orientada a objetos. Nas operações do CRUD, o Hibernate é responsável por traduzir automaticamente as chamadas dos métodos Java em comandos nativos do MySQL no console de execução."*
4.  **Conclusão e Teste:**
    > *"Por fim, validamos o fluxo completo do CRUD por meio de uma classe de teste de integração que insere, busca, altera e deleta registros de teste no MySQL, comprovando que a comunicação e a integridade referencial do banco de dados estão 100% corretas."*
