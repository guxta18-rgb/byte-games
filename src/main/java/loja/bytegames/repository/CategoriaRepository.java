package loja.bytegames.repository;

import loja.bytegames.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository indica que esta interface faz acesso e gravação no banco de dados
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

   
    boolean existsByNome(String nome);

    // Método que busca categoria pelo nome exato
    Optional<Categoria> findByNome(String nome);

    // Método padrão para buscar categoria por nome
    default Optional<Categoria> buscarCategoriaPorNome(String nome) {
        return findByNome(nome);
    }

    // Método padrão para listar todas as categorias do banco
    default List<Categoria> buscarTodasCategorias() {
        return findAll();
    }

    // Método padrão para buscar uma categoria pelo ID
    default Optional<Categoria> buscarCategoriaPorId(Long id) {
        return findById(id);
    }

    // Método personalizado para inserir uma nova categoria
    default void inserirCategoria(String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        save(categoria);
    }

    // Método personalizado para atualizar dados de uma categoria existente
    default void atualizarCategoria(Long id, String nome, String descricao) {
        findById(id).ifPresent(categoria -> {
            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            save(categoria);
        });
    }

    // Método padrão para deletar uma categoria pelo ID
    default void deletarCategoriaPorId(Long id) {
        deleteById(id);
    }
}
