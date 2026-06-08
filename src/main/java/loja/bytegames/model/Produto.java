package loja.bytegames.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/*
 * Este arquivo representa a classe Produto.
 * Ela serve para definir os detalhes de um jogo (como nome, descrição, preço e estoque) 
 * que vamos cadastrar na nossa loja e salvar no banco de dados MySQL.
 */

// A anotação @Entity avisa ao Spring Boot/Java que esta classe se tornará uma tabela no banco de dados.
@Entity
// A anotação @Table define o nome exato da tabela no banco de dados.
@Table(name = "produto")
public class Produto {

    // O @Id indica que o atributo 'id' é a chave primária.
    // O @GeneratedValue com a estratégia IDENTITY faz o banco de dados numerar os IDs de 1 em 1 automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank não aceita texto vazio ou apenas espaços em branco.
    // @Size restringe a quantidade de caracteres para o nome do jogo.
    // @Column configura a coluna no banco de dados (obrigatória e tamanho máximo de 100).
    @NotBlank(message = "O nome do jogo/produto é obrigatório!")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    // @NotBlank não permite descrição vazia.
    // @Size limita o tamanho da descrição.
    @NotBlank(message = "A descrição é obrigatória!")
    @Size(max = 500, message = "A descrição não pode passar de 500 caracteres.")
    @Column(nullable = false, length = 500)
    private String descricao;

    // @NotNull garante que o preço seja preenchido.
    // @DecimalMin garante que o preço não seja de graça (mínimo de R$ 0,01).
    // @Column define o tamanho e precisão decimal no banco de dados (ex: R$ 99999999.99).
    @NotNull(message = "O preço é obrigatório!")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    // @NotNull garante que o estoque seja preenchido.
    // @Min impede que o estoque tenha valores negativos (mínimo 0).
    @NotNull(message = "A quantidade em estoque é obrigatória!")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoque;

    // @ManyToOne indica que "Muitos produtos pertencem a uma categoria".
    // @JoinColumn indica qual é o nome da coluna que faz a ligação das duas tabelas (categoria_id).
    // @NotNull garante que todo produto seja associado a alguma categoria cadastrada.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "Selecione uma categoria válida!")
    private Categoria categoria;

    // Construtor vazio: obrigatório para o framework JPA criar instâncias do produto.
    public Produto() {
    }

    // --- Métodos de Acesso (Getters e Setters) ---
    // Usados para ler ou alterar as variáveis privadas.

    // Métodos para o ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Métodos para o Nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Métodos para a Descrição
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Métodos para o Preço
    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    // Métodos para o Estoque
    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    // Métodos para a Categoria
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}