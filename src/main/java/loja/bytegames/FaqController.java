package loja.bytegames;

import loja.bytegames.repository.FaqRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

// @Controller mapeia as requisições web para esta classe
// @RequestMapping("/faq") define o prefixo das rotas deste controlador
@Controller
@RequestMapping("/faq")
public class FaqController {

    private final FaqRepository faqRepository;

    public FaqController(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    // GET /faq - Exibe a lista pública e o painel de gerenciamento
    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("faqs", faqRepository.buscarTodosFaqs());
        return "faq/listar";
    }

    // GET /faq/novo - Abre a tela de cadastro
    @GetMapping("/novo")
    public String exibirFormCriar(Model model) {
        model.addAttribute("faq", new Faq());
        return "faq/form-criar";
    }

    // POST /faq - Salva o novo FAQ validando as informações no backend
    @PostMapping
    public String salvarNovo(@Valid @ModelAttribute("faq") Faq faq, BindingResult result) {
        // Validação no Backend: @Valid ativa as anotações do Faq.java e result guarda se houve erro
        if (result.hasErrors()) {
            return "faq/form-criar"; // Permanece na página exibindo os erros
        }

        faqRepository.inserirFaq(
            faq.getPergunta(),
            faq.getResposta(),
            faq.getEmailContato()
        );

        return "redirect:/faq";
    }

    // GET /faq/{id}/editar - Abre a tela de edição
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Optional<Faq> faqOptional = faqRepository.buscarFaqPorId(id);
        if (faqOptional.isPresent()) {
            model.addAttribute("faq", faqOptional.get());
            return "faq/form-editar";
        }
        return "redirect:/faq";
    }

    // POST /faq/{id} - Salva as alterações da edição validando os dados
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("faq") Faq faq, BindingResult result) {
        // Validação no Backend: @Valid e result validam os dados editados
        if (result.hasErrors()) {
            return "faq/form-editar";
        }

        faqRepository.atualizarFaq(
            id,
            faq.getPergunta(),
            faq.getResposta(),
            faq.getEmailContato()
        );

        return "redirect:/faq";
    }

    // POST /faq/{id}/excluir - Exclui o FAQ pelo ID
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        faqRepository.deletarFaqPorId(id);
        return "redirect:/faq";
    }
}
