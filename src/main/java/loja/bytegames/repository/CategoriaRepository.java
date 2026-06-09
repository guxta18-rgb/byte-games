package loja.bytegames.repository;

// Importação da entidade Categoria
import loja.bytegames.model.Categoria;

// Importações do Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * CONTEXTO DESTA INTERFACE:
 * Este é o Repositório de Categoria (CategoriaRepository). No Spring Data JPA, as interfaces 
 * de repositório servem de ponte direta com o Banco de Dados. Ao estender 'JpaRepository', 
 * o Spring cria por baixo dos panos toda a implementação com comandos SQL prontos de inserção, 
 * busca, atualização e exclusão, sem precisarmos escrever nenhuma query SQL manual.
 */

@Repository // Registra a interface como um componente de persistência do Spring
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Categoria: Classe que representa a tabela mapeada
    // Long: Tipo da chave primária (ID) definida na model Categoria

    // Query Method: O Spring Data JPA interpreta o nome do método e gera a query correspondente
    // Equivale ao SQL: SELECT COUNT(*) > 0 FROM categoria WHERE nome = :nome
    boolean existsByNome(String nome);
    
    /* 
     * SE O PROFESSOR PERGUNTAR COMO FUNCIONAM OS 'QUERY METHODS':
     * Explique que o Spring Data JPA possui um analisador de sintaxe (parser) que lê o nome do método. 
     * A presença de palavras como 'findBy', 'existsBy', 'countBy', associada ao nome do atributo ('Nome', 'Descricao'), 
     * diz ao Spring exatamente qual consulta SQL ele deve montar em tempo de execução.
     * 
     * SE O PROFESSOR PEDIR PARA ADICIONAR UMA QUERY MANUAL PERSONALIZADA (JPQL):
     * Você pode usar a anotação @Query:
     *    @org.springframework.data.jpa.repository.Query("SELECT c FROM Categoria c WHERE c.nome LIKE %:termo%")
     *    java.util.List<Categoria> buscarPorParteDoNome(String termo);
     */
}

