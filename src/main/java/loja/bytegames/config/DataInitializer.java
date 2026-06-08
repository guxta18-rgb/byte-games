package loja.bytegames.config;

import loja.bytegames.model.Categoria;
import loja.bytegames.model.Produto;
import loja.bytegames.repository.CategoriaRepository;
import loja.bytegames.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/*
 * Este arquivo serve para colocar dados iniciais (de teste) no nosso banco de dados.
 * Assim que o Spring Boot terminar de ligar, ele roda este código e cadastra
 * as primeiras categorias e jogos automaticamente caso o banco esteja vazio.
 */

// A anotação @Component avisa ao Spring Boot para gerenciar e executar esta classe.
@Component
public class DataInitializer implements CommandLineRunner {

    // Criamos variáveis para guardar nossos gerenciadores do banco de dados (repositories).
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    // Construtor: serve para o Spring Boot passar os repositories reais para nós usarmos.
    public DataInitializer(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    // O método run() é executado automaticamente assim que o programa começa a rodar.
    @Override
    public void run(String... args) throws Exception {
        // Se a quantidade de categorias no banco de dados for igual a zero, cadastramos os dados de teste.
        if (categoriaRepository.count() == 0) {
            
            // --- Criação das Categorias ---

            // Categoria 1: RPG
            Categoria rpg = new Categoria(); 
            rpg.setNome("RPG"); 
            rpg.setDescricao("Jogos de interpretacao de personagens com narrativas epicas.");

            // Categoria 2: Ação e Aventura
            Categoria acao = new Categoria(); 
            acao.setNome("Acao e Aventura"); 
            acao.setDescricao("Combate intenso e exploracao de mundos abertos.");

            // Categoria 3: FPS
            Categoria fps = new Categoria(); 
            fps.setNome("FPS / Tiro"); 
            fps.setDescricao("Jogos de tiro em primeira pessoa com acao intensa.");

            // Categoria 4: Estratégia
            Categoria estrategia = new Categoria(); 
            estrategia.setNome("Estrategia"); 
            estrategia.setDescricao("Planejamento tatico e gerenciamento de recursos.");

            // Salvando as categorias no banco de dados MySQL
            categoriaRepository.save(rpg);
            categoriaRepository.save(acao);
            categoriaRepository.save(fps);
            categoriaRepository.save(estrategia);

            // --- Criação dos Produtos (Jogos) ---

            // Produto 1
            Produto p1 = new Produto();
            p1.setNome("The Witcher 3: Wild Hunt");
            p1.setDescricao("Um dos maiores RPGs ja criados. Explore o Continente como Geralt de Rivia, um cacador de monstros profissional.");
            p1.setPreco(new BigDecimal("59.90")); 
            p1.setEstoque(15); 
            p1.setCategoria(rpg);

            // Produto 2
            Produto p2 = new Produto();
            p2.setNome("God of War");
            p2.setDescricao("Kratos e seu filho Atreus embarcam em uma jornada epica pela mitologia nordica cheia de desafios.");
            p2.setPreco(new BigDecimal("149.90")); 
            p2.setEstoque(8); 
            p2.setCategoria(acao);

            // Produto 3
            Produto p3 = new Produto();
            p3.setNome("Counter-Strike 2");
            p3.setDescricao("O classico FPS competitivo renovado. Jogue com amigos em partidas online intensas e ranqueadas.");
            p3.setPreco(new BigDecimal("0.01")); 
            p3.setEstoque(999); 
            p3.setCategoria(fps);

            // Produto 4
            Produto p4 = new Produto();
            p4.setNome("Civilization VI");
            p4.setDescricao("Construa um imperio que resistira ao teste do tempo. Lide com diplomacia, ciencia, guerra e exploracao.");
            p4.setPreco(new BigDecimal("79.90")); 
            p4.setEstoque(20); 
            p4.setCategoria(estrategia);

            // Produto 5
            Produto p5 = new Produto();
            p5.setNome("Elden Ring");
            p5.setDescricao("Explore as Terras Intermedias em um RPG de acao desafiador criado por FromSoftware e George R.R. Martin.");
            p5.setPreco(new BigDecimal("199.90")); 
            p5.setEstoque(0); 
            p5.setCategoria(rpg);

            // Salvando os produtos no banco de dados MySQL
            produtoRepository.save(p1); 
            produtoRepository.save(p2); 
            produtoRepository.save(p3);
            produtoRepository.save(p4); 
            produtoRepository.save(p5);

            System.out.println("Dados de teste inseridos com sucesso!");
        }
    }
}

