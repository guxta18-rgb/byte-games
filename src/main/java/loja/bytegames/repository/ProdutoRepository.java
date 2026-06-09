package loja.bytegames.repository;

// Importação do modelo Produto e utilitários
import loja.bytegames.model.Produto;
import java.util.List;

// Importações do Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * CONTEXTO DESTA INTERFACE:
 * Este é o Repositório de Produto (ProdutoRepository). Assim como a CategoriaRepository,
 * ela estende JpaRepository para prover todo o CRUD padrão de produtos. Além disso, expõe 
 * um método de busca customizado para fazer filtros por nome do jogo na barra de pesquisa da loja.
 */

@Repository // Registra a interface como repositório Spring Data JPA no contêiner IoC
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Query Method: Busca produtos cujo nome contenha o termo digitado.
    // Containing: Equivale ao operador SQL 'LIKE %termo%'
    // IgnoreCase: Ignora a diferença entre letras maiúsculas e minúsculas no banco
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    /* 
     * SE O PROFESSOR PEDIR PARA ADICIONAR FILTRAGEM POR CATEGORIA:
     * Você pode adicionar outro Query Method aproveitando o relacionamento:
     *    List<Produto> findByCategoriaId(Long categoriaId);
     * 
     * SE O PROFESSOR PEDIR PARA FILTRAR APENAS PRODUTOS COM ESTOQUE DISPONÍVEL:
     * Você pode fazer:
     *    List<Produto> findByEstoqueGreaterThan(Integer estoqueMinimo); // ex: passar 0 no parâmetro
     */
}