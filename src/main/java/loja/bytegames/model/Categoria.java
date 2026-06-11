package loja.bytegames.model;

// Importação das anotações do JPA para persistência em banco de dados
import jakarta.persistence.*;

// Importação das anotações do Jakarta Validation para validação de campos
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Importação da classe de listas do Java
import java.util.List;

/*
 * CONTEXTO DESTA CLASSE:
 * Esta é a classe de Entidade Categoria (Categoria.java). Ela representa uma tabela do banco de dados 
 * relacional mapeada pelo framework ORM (Mapeamento Objeto-Relacional) chamado Hibernate/JPA.
 * Ela serve para organizar os produtos/jogos em divisões lógicas (ex: RPG, FPS, Ação).
 */

@Entity // Define para a JPA que esta classe é uma entidade mapeada para uma tabela no banco de dados
@Table(name = "categoria") // Especifica o nome físico da tabela no banco de dados
public class Categoria {

    // @Id: Define que este atributo é a Chave Primária (Primary Key) da tabela no banco
    // @GeneratedValue: Define a estratégia de geração do ID. IDENTITY indica auto-incremento (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank: Validação que impede textos nulos, vazios ou apenas com espaços em branco
    // @Size: Validação que limita o comprimento mínimo e máximo do texto inserido
    // @Column: Configura metadados da coluna no banco (obrigatória e valor único, impedindo duplicados)
    @NotBlank(message = "O nome da categoria é obrigatório!")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    @Column(nullable = false, unique = true)
    private String nome;

    // @Size: Validação que define o tamanho máximo da descrição
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres.")
    private String descricao;

    // @OneToMany: Mapeamento de relacionamento Um para Muitos. Uma categoria possui muitos produtos.
    // mappedBy: Indica qual é o atributo na classe 'Produto' que é o dono da relação (mapeamento bidirecional)
    // cascade = CascadeType.ALL: Operações de salvar, atualizar ou deletar nesta categoria se aplicam aos produtos dela
    // fetch = FetchType.LAZY: Carrega os produtos do banco sob demanda (preguiçoso), poupando memória ao listar apenas categorias
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    // Construtor padrão sem argumentos: Obrigatório para o framework Hibernate instanciar objetos vindo do banco
    public Categoria() {
    }

    // --- MÉTODOS GETTERS E SETTERS (Métodos de Acesso e Modificação) ---
    // Essenciais para o encapsulamento do Java (variáveis privadas expostas de forma controlada)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}

