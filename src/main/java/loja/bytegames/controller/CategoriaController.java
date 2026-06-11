package loja.bytegames.controller;

import loja.bytegames.model.Categoria;
import loja.bytegames.repository.CategoriaRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarTodas(Model model) {
        model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
        return "categorias/listar";
    }

    @GetMapping("/nova")
    public String exibirFormCriar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form-criar";
    }

    @PostMapping
    public String salvarNova(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result) {
        if (result.hasErrors()) {
            return "categorias/form-criar";
        }
        
        // Evita duplicidade de nome da categoria
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            result.rejectValue("nome", "duplicate", "Já existe uma categoria cadastrada com esse nome.");
            return "categorias/form-criar";
        }
        
        categoriaRepository.inserirCategoria(categoria.getNome(), categoria.getDescricao());
        return "redirect:/categorias";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        Optional<Categoria> categoriaOptional = categoriaRepository.buscarCategoriaPorId(id);
        
        if (categoriaOptional.isPresent()) {
            model.addAttribute("categoria", categoriaOptional.get());
            return "categorias/detalhar";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Optional<Categoria> categoriaOptional = categoriaRepository.buscarCategoriaPorId(id);
        
        if (categoriaOptional.isPresent()) {
            model.addAttribute("categoria", categoriaOptional.get());
            return "categorias/form-editar";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        
        if (result.hasErrors()) {
            return "categorias/form-editar";
        }
        
        categoriaRepository.atualizarCategoria(id, categoria.getNome(), categoria.getDescricao());
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.buscarCategoriaPorId(id);
        
        if (categoriaOptional.isPresent()) {
            categoriaRepository.deletarCategoriaPorId(id);
            return "redirect:/categorias";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }
}
