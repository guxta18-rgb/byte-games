package loja.bytegames.repository;

import loja.bytegames.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Este arquivo representa o repositório de Categoria.
 * Em programação de banco de dados, o repositório é como um "gerente" que cuida 
 * das operações de salvar, ler, alterar e deletar informações na tabela correspondente.
 */

// A anotação @Repository avisa ao Spring Boot que esta interface cuida das conexões com o banco de dados.
@Repository
// Ao herdar (extends) JpaRepository, o Spring Boot cria automaticamente todos os comandos SQL básicos para nós.
// Passamos Categoria (a classe da tabela) e Long (o tipo de dados do ID).
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Este método verifica se já existe alguma categoria cadastrada com o nome passado no parâmetro.
    // O Spring Data é inteligente e cria a busca no banco apenas lendo o nome do método!
    boolean existsByNome(String nome);
}

