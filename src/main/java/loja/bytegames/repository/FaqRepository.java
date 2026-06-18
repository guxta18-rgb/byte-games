package loja.bytegames.repository;

import loja.bytegames.Faq;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

    // Método padrão para buscar todos os FAQs cadastrados
    default List<Faq> buscarTodosFaqs() {
        return findAll();
    }

    // Método padrão para buscar um FAQ pelo ID
    default Optional<Faq> buscarFaqPorId(Long id) {
        return findById(id);
    }

    // Método personalizado para criar um novo FAQ
    default void inserirFaq(String pergunta, String resposta, String emailContato) {
        Faq faq = new Faq();
        faq.setPergunta(pergunta);
        faq.setResposta(resposta);
        faq.setEmailContato(emailContato);
        save(faq);
    }

    // Método personalizado para atualizar um FAQ existente
    default void atualizarFaq(Long id, String pergunta, String resposta, String emailContato) {
        findById(id).ifPresent(faq -> {
            faq.setPergunta(pergunta);
            faq.setResposta(resposta);
            faq.setEmailContato(emailContato);
            save(faq);
        });
    }

    // Método padrão para deletar um FAQ pelo ID
    default void deletarFaqPorId(Long id) {
        deleteById(id);
    }
}
