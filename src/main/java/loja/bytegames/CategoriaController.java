package loja.bytegames;

import loja.bytegames.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

// @Controller diz que esta classe controla páginas HTML
// @RequestMapping("/categorias") define a rota principal
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // @GetMapping lista todas as categorias em /categorias
    @GetMapping
    public String listarTodas(Model model) {
        model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
        return "categorias/listar"; // Abre templates/categorias/listar.html
    }

    // @GetMapping("/nova") exibe o formulário de criação de categorias
    @GetMapping("/nova")
    public String exibirFormCriar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form-criar"; // Abre templates/categorias/form-criar.html
    }

    // @PostMapping salva uma nova categoria no banco
    @PostMapping
    public String salvarNova(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result) {
        // Se houver algum erro de validação (nome em branco, etc.)
        if (result.hasErrors()) {
            return "categorias/form-criar";
        }

        // Verifica se o nome digitado já existe para evitar categorias duplicadas
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            // Rejeita o valor e exibe um erro amigável na tela
            result.rejectValue("nome", "duplicate", "Já existe uma categoria com esse nome.");
            return "categorias/form-criar";
        }

        // Insere a categoria pelo método do repositório
        categoriaRepository.inserirCategoria(categoria.getNome(), categoria.getDescricao());
        return "redirect:/categorias";
    }

    // @GetMapping("/{id}") exibe informações detalhadas de uma categoria
    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        Optional<Categoria> categoriaOptional = categoriaRepository.buscarCategoriaPorId(id);
        if (categoriaOptional.isPresent()) {
            model.addAttribute("categoria", categoriaOptional.get());
            return "categorias/detalhar"; // Abre templates/categorias/detalhar.html
        }
        return "redirect:/categorias";
    }

    // @GetMapping("/{id}/editar") abre o formulário de edição de categorias
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Optional<Categoria> categoriaOptional = categoriaRepository.buscarCategoriaPorId(id);
        if (categoriaOptional.isPresent()) {
            model.addAttribute("categoria", categoriaOptional.get());
            return "categorias/form-editar"; // Abre templates/categorias/form-editar.html
        }
        return "redirect:/categorias";
    }

    // @PostMapping("/{id}") atualiza os dados da categoria
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        if (result.hasErrors()) {
            return "categorias/form-editar";
        }

        // Atualiza a categoria utilizando o repositório
        categoriaRepository.atualizarCategoria(id, categoria.getNome(), categoria.getDescricao());
        return "redirect:/categorias";
    }

    // @PostMapping("/{id}/excluir") deleta a categoria pelo ID
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        categoriaRepository.deletarCategoriaPorId(id);
        return "redirect:/categorias";
    }
}
