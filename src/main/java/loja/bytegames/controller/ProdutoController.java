package loja.bytegames.controller;

import loja.bytegames.model.Produto;
import loja.bytegames.repository.ProdutoRepository;
import loja.bytegames.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "produtos/listar";
    }

    @GetMapping("/novo")
    public String exibirFormCriar(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "produtos/form-criar";
    }

    @PostMapping
    public String salvarNovo(@Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/form-criar";
        }
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        model.addAttribute("produto", produto);
        return "produtos/detalhar";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "produtos/form-editar";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/form-editar";
        }
        produto.setId(id);
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        produtoRepository.deleteById(id);
        return "redirect:/produtos";
    }
}
