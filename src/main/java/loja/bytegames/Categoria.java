package loja.bytegames;

// Importa os recursos de banco de dados e validações
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

// @Entity diz que essa classe representa uma tabela de Categorias no banco de dados
@Entity
@Table(name = "categoria")
public class Categoria {

    // @Id define a chave primária
    // @GeneratedValue define a geração automática do ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank garante que o nome da categoria não esteja vazio
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    // Descrição da categoria
    private String descricao;

    // @OneToMany define que uma Categoria pode ter vários produtos
    // mappedBy indica qual atributo na classe Produto faz o relacionamento
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    // Construtor vazio padrão exigido pelo banco
    public Categoria() {
    }

    // Métodos Getters e Setters para acessar e modificar as variáveis
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
