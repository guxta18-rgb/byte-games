package loja.bytegames.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/*
 * Este arquivo representa a classe Categoria.
 * Ela serve para definir como uma categoria de jogo (como RPG, Ação, etc.) 
 * será guardada dentro da nossa tabela do banco de dados MySQL.
 */

// A anotação @Entity diz para o Java que esta classe representa uma tabela no banco de dados.
@Entity
// A anotação @Table diz qual é o nome exato da tabela que vai ser criada no banco de dados.
@Table(name = "categoria")
public class Categoria {

    // O @Id diz que esse atributo é a chave primária da nossa tabela (o identificador único).
    // O @GeneratedValue diz que o banco de dados vai gerar esse número sozinho (1, 2, 3, etc.).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O @NotBlank garante que o nome da categoria não seja vazio ou cheio de espaços.
    // O @Size limita o tamanho do texto do nome.
    // O @Column diz que este campo é obrigatório (nullable = false) e não pode repetir no banco (unique = true).
    @NotBlank(message = "O nome da categoria é obrigatório!")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    @Column(nullable = false, unique = true)
    private String nome;

    // O @Size limita a descrição para ter no máximo 200 letras/caracteres.
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres.")
    private String descricao;

    // O @OneToMany serve para conectar as tabelas. 
    // Significa que "Uma categoria pode ter muitos produtos associados a ela".
    // mappedBy = "categoria" avisa que a variável 'categoria' está dentro da classe Produto.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    // Construtor vazio: é uma exigência do Hibernate/JPA para conseguir criar a classe a partir do banco.
    public Categoria() {
    }

    // --- Métodos de Acesso (Getters e Setters) ---
    // Servem para pegar (get) ou mudar (set) as variáveis que são privadas (private).

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

    // Métodos para a Lista de Produtos
    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}

