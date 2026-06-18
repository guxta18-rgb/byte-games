package loja.bytegames;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "faq")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Validação 1: Pergunta obrigatória com tamanho mínimo e máximo
    @NotBlank(message = "A pergunta é obrigatória")
    @Size(min = 10, max = 150, message = "A pergunta deve ter entre 10 e 150 caracteres")
    private String pergunta;

    // Validação 2: Resposta obrigatória com tamanho mínimo e máximo
    @NotBlank(message = "A resposta é obrigatória")
    @Size(min = 10, max = 500, message = "A resposta deve ter entre 10 e 500 caracteres")
    private String resposta;

    // Validação 3: E-mail obrigatório com formato de e-mail correto
    @NotBlank(message = "O e-mail de contato é obrigatório")
    @Email(message = "Insira um endereço de e-mail de contato válido")
    private String emailContato;

    public Faq() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
