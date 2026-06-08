package loja.bytegames.repository;

import loja.bytegames.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/*
 * Este arquivo representa o repositório de Produto.
 * Ele serve para gerenciar todas as leituras, salvamentos, exclusões 
 * e alterações na tabela de produtos no banco de dados.
 */

// A anotação @Repository avisa ao Spring Boot que esta interface gerencia a conexão da tabela de produtos.
@Repository
// Herdamos JpaRepository para ganhar comandos básicos como findAll(), save(), findById() e deleteById() de graça.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Este método serve para buscar produtos pelo nome ou parte dele.
    // Containing: significa buscar por partes do nome (como se usasse o comando LIKE %termo% no SQL).
    // IgnoreCase: significa que não importa se a busca for por letras maiúsculas ou minúsculas.
    // O Spring Data JPA monta essa consulta no banco de dados sozinho apenas lendo o nome do método!
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}