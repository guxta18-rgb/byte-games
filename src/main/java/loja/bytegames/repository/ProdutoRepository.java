package loja.bytegames.repository;

import loja.bytegames.model.Produto;
import loja.bytegames.model.Categoria;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // O Spring Data cria essa busca por aproximação de nome automaticamente pelo
    // nome do método!
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // --- Métodos de compatibilidade (para não quebrar o código existente) ---

    // Retorna todos os produtos usando o findAll() que já vem pronto no
    // JpaRepository
    default List<Produto> buscarTodosProdutos() {
        return findAll();
    }

    // Busca o produto pelo ID usando o findById(id) que já vem pronto no
    // JpaRepository
    default Optional<Produto> buscarProdutoPorId(Long id) {
        return findById(id);
    }

    // Insere um novo produto criando o objeto Produto e associando a categoria pelo
    // ID
    default void inserirProduto(String nome,
            String descricao,
            BigDecimal preco,
            Integer estoque,
            Long categoriaId,
            String imagem) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        produto.setImagem(imagem);

        // Cria e associa a categoria através do ID informado
        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        produto.setCategoria(categoria);

        save(produto); // Salva o produto automaticamente no banco de dados!
    }

    // Atualiza um produto existente buscando por ID e salvando com save()
    default void atualizarProduto(Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            Integer estoque,
            Long categoriaId,
            String imagem) {
        findById(id).ifPresent(produto -> {
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            produto.setImagem(imagem);

            // Cria e associa a categoria através do ID informado
            Categoria categoria = new Categoria();
            categoria.setId(categoriaId);
            produto.setCategoria(categoria);

            save(produto); // O save() atualiza o registro existente porque ele já possui um ID!
        });
    }

    // Deleta um produto pelo ID usando o deleteById(id) que já vem pronto no
    // JpaRepository
    default void deletarProdutoPorId(Long id) {
        deleteById(id);
    }
}
