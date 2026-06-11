package loja.bytegames.config;

// Importação das entidades e repositories do projeto
import loja.bytegames.model.Categoria;
import loja.bytegames.model.Produto;
import loja.bytegames.repository.CategoriaRepository;
import loja.bytegames.repository.ProdutoRepository;

// Importações do Spring Boot para execução na inicialização
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/*
 * CONTEXTO DESTA CLASSE:
 * Esta classe é um semeador de banco de dados (Database Seeder). Ela implementa 'CommandLineRunner', 
 * o que significa que o método 'run' será disparado automaticamente logo após o Spring Boot carregar.
 * Ela serve para cadastrar categorias e produtos iniciais (de teste) se o banco de dados estiver vazio, 
 * facilitando a apresentação do projeto sem a necessidade de cadastrar tudo manualmente toda vez.
 */

@Component // Registra a classe como um componente gerenciado pelo Spring Boot (Bean)
public class DataInitializer implements CommandLineRunner {

    // Declaração dos repositórios para inserção de dados
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependência por construtor feita automaticamente pelo Spring
    public DataInitializer(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    // Método que é executado após a inicialização do servidor
    @Override
    public void run(String... args) throws Exception {
        // Verifica se a tabela de categorias está vazia (evita duplicar registros a cada reinicialização)
        if (categoriaRepository.count() == 0) {
            
            // --- 1. INSTANCIAÇÃO E PREENCHIMENTO DAS CATEGORIAS ---

            Categoria rpg = new Categoria(); 
            rpg.setNome("RPG"); 
            rpg.setDescricao("Jogos de interpretacao de personagens com narrativas epicas.");

            Categoria acao = new Categoria(); 
            acao.setNome("Acao e Aventura"); 
            acao.setDescricao("Combate intenso e exploracao de mundos abertos.");

            Categoria fps = new Categoria(); 
            fps.setNome("FPS / Tiro"); 
            fps.setDescricao("Jogos de tiro em primeira pessoa com acao intensa.");

            Categoria estrategia = new Categoria(); 
            estrategia.setNome("Estrategia"); 
            estrategia.setDescricao("Planejamento tatico e gerenciamento de recursos.");

            // Salva as categorias no banco de dados (o JPA gera os IDs automaticamente)
            categoriaRepository.save(rpg);
            categoriaRepository.save(acao);
            categoriaRepository.save(fps);
            categoriaRepository.save(estrategia);

            // --- 2. INSTANCIAÇÃO E PREENCHIMENTO DOS PRODUTOS (JOGOS) ---

            Produto p1 = new Produto();
            p1.setNome("The Witcher 3: Wild Hunt");
            p1.setDescricao("Um dos maiores RPGs ja criados. Explore o Continente como Geralt de Rivia, um cacador de monstros profissional.");
            p1.setPreco(new BigDecimal("59.90")); // BigDecimal evita erros de arredondamento inerentes ao float/double
            p1.setEstoque(15); 
            p1.setCategoria(rpg); // Cria a relação de chave estrangeira com a categoria RPG

            Produto p2 = new Produto();
            p2.setNome("God of War");
            p2.setDescricao("Kratos e seu filho Atreus embarcam em uma jornada epica pela mitologia nordica cheia de desafios.");
            p2.setPreco(new BigDecimal("149.90")); 
            p2.setEstoque(8); 
            p2.setCategoria(acao);

            Produto p3 = new Produto();
            p3.setNome("Counter-Strike 2");
            p3.setDescricao("O classico FPS competitivo renovado. Jogue com amigos em partidas online intensas e ranqueadas.");
            p3.setPreco(new BigDecimal("0.01")); 
            p3.setEstoque(999); 
            p3.setCategoria(fps);

            Produto p4 = new Produto();
            p4.setNome("Civilization VI");
            p4.setDescricao("Construa um imperio que resistira ao teste do tempo. Lide com diplomacia, ciencia, guerra e exploracao.");
            p4.setPreco(new BigDecimal("79.90")); 
            p4.setEstoque(20); 
            p4.setCategoria(estrategia);

            Produto p5 = new Produto();
            p5.setNome("Elden Ring");
            p5.setDescricao("Explore as Terras Intermedias em um RPG de acao desafiador criado por FromSoftware e George R.R. Martin.");
            p5.setPreco(new BigDecimal("199.90")); 
            p5.setEstoque(0); 
            p5.setCategoria(rpg);

            // Salva todos os produtos na tabela do banco
            produtoRepository.save(p1); 
            produtoRepository.save(p2); 
            produtoRepository.save(p3);
            produtoRepository.save(p4); 
            produtoRepository.save(p5);

            // Print de confirmação que aparece apenas no console/terminal
            System.out.println("Dados de teste inseridos com sucesso!");
        }
    }
}

