package loja.bytegames.repository;

import loja.bytegames.model.Categoria;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // O Spring Data cria essa validação automaticamente pelo nome do método!
    boolean existsByNome(String nome);

    // O Spring Data cria essa busca automaticamente pelo nome do método!
    Optional<Categoria> findByNome(String nome);

    // --- Métodos de compatibilidade (para não quebrar o código existente) ---

    // Busca a categoria por nome usando o método automático do Spring Data
    default Optional<Categoria> buscarCategoriaPorNome(String nome) {
        return findByNome(nome);
    }

    // Retorna todas as categorias usando o findAll() que já vem pronto no
    // JpaRepository
    default List<Categoria> buscarTodasCategorias() {
        return findAll();
    }

    // Busca a categoria pelo ID usando o findById(id) que já vem pronto no
    // JpaRepository
    default Optional<Categoria> buscarCategoriaPorId(Long id) {
        return findById(id);
    }

    // Insere uma nova categoria criando o objeto Categoria e usando o save()
    default void inserirCategoria(String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        save(categoria); // O save() insere automaticamente no banco de dados!
    }

    // Atualiza uma categoria existente buscando por ID e salvando com save()
    default void atualizarCategoria(Long id, String nome, String descricao) {
        findById(id).ifPresent(categoria -> {
            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            save(categoria); // O save() atualiza o registro existente porque ele já possui um ID!
        });
    }

    // Deleta uma categoria pelo ID usando o deleteById(id) que já vem pronto no
    // JpaRepository
    default void deletarCategoriaPorId(Long id) {
        deleteById(id);
    }
}
