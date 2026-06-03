package loja.bytegames.controller;

import loja.bytegames.model.Categoria;
import loja.bytegames.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarTodas(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias/listar";
    }

    @GetMapping("/nova")
    public String exibirFormCriar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form-criar";
    }

    @PostMapping
    public String salvarNova(@Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        if (result.hasErrors()) {
            return "categorias/form-criar";
        }
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            result.rejectValue("nome", "duplicate", "Ja existe uma categoria com esse nome.");
            return "categorias/form-criar";
        }
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        model.addAttribute("categoria", categoria);
        return "categorias/detalhar";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        model.addAttribute("categoria", categoria);
        return "categorias/form-editar";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        if (result.hasErrors()) {
            return "categorias/form-editar";
        }
        categoria.setId(id);
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id));
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}
