package loja.bytegames.model;

// Importação das anotações de persistência da JPA
import jakarta.persistence.*;

// Importações para validações de formulário do Jakarta Validation
import jakarta.validation.constraints.*;

// Importação para manipulação exata de valores monetários
import java.math.BigDecimal;

/*
 * CONTEXTO DESTA CLASSE:
 * Esta é a classe de Entidade Produto (Produto.java). Ela representa a tabela 'produto' no banco de dados.
 * Ela armazena os atributos essenciais de cada jogo (nome, descrição, preço e quantidade em estoque) 
 * e faz o relacionamento com a entidade Categoria.
 */

@Entity // Define a classe como uma entidade mapeada no banco de dados
@Table(name = "produto") // Especifica o nome da tabela física no banco de dados
public class Produto {

    // @Id: Define a chave primária
    // @GeneratedValue: Indica que o banco de dados auto-incrementa o ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank: Garante que o texto não seja nulo nem vazio
    // @Size: Limita a quantidade de caracteres permitidos
    // @Column: Define restrições físicas no banco de dados (não nulo e tamanho máximo de 100 caracteres)
    @NotBlank(message = "O nome do jogo/produto é obrigatório!")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    // @NotBlank: Torna a descrição obrigatória no formulário
    // @Size: Limita o comprimento do texto da descrição
    @NotBlank(message = "A descrição é obrigatória!")
    @Size(max = 500, message = "A descrição não pode passar de 500 caracteres.")
    @Column(nullable = false, length = 500)
    private String descricao;

    // @NotNull: Preço não pode ficar em branco
    // @DecimalMin: Impede preços iguais ou menores que zero (exige valor positivo de pelo menos 0.01)
    // @Column: Define precisão de 10 dígitos com até 2 casas decimais no banco (ex: 99999999.99)
    @NotNull(message = "O preço é obrigatório!")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    // @NotNull: Impede que a quantidade de estoque seja nula
    // @Min: Impede estoque negativo
    @NotNull(message = "A quantidade em estoque é obrigatória!")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoque;

    // @ManyToOne: Relacionamento Muitos para Um. Vários produtos podem pertencer a uma única Categoria.
    // fetch = FetchType.LAZY: Carrega os dados da categoria sob demanda para otimizar as consultas
    // @JoinColumn: Especifica o nome da coluna física de chave estrangeira (categoria_id)
    // @NotNull: Garante que todo produto cadastrado deve obrigatoriamente estar vinculado a uma categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "Selecione uma categoria válida!")
    private Categoria categoria;

    // Caminho ou nome do arquivo da imagem do produto salva no MySQL
    @Column(nullable = true, length = 255)
    private String imagem;

    // Construtor vazio: Requisito fundamental do Hibernate/JPA para converter registros do banco em instâncias Java
    public Produto() {
    }

    // --- MÉTODOS GETTERS E SETTERS ---
    // Métodos para encapsulamento das propriedades privadas da classe

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}