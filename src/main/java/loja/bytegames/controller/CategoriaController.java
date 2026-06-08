package loja.bytegames.controller;

import loja.bytegames.model.Categoria;
import loja.bytegames.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/*
 * Este arquivo representa o Controlador de Categorias.
 * O controlador é responsável por escutar o que o usuário clica ou digita na página HTML 
 * (as requisições), conversar com o banco de dados e devolver a página HTML correta para o navegador.
 */

// A anotação @Controller avisa que esta classe gerencia as telas e caminhos (rotas) do site.
// @RequestMapping("/categorias") faz todas as rotas dessa classe começarem com "/categorias".
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    // Criamos a variável do repositório para conseguirmos mexer na tabela de categorias.
    private final CategoriaRepository categoriaRepository;

    // Construtor: o Spring Boot injeta o repository automaticamente aqui.
    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Rota GET /categorias -> Lista todas as categorias cadastradas.
    @GetMapping
    public String listarTodas(Model model) {
        // Buscamos todas as categorias no banco e guardamos na variável "categorias" que o HTML consegue ler.
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // Retorna a página que está na pasta src/main/resources/templates/categorias/listar.html
        return "categorias/listar";
    }

    // Rota GET /categorias/nova -> Exibe a página com o formulário para criar uma categoria.
    @GetMapping("/nova")
    public String exibirFormCriar(Model model) {
        // Mandamos um objeto Categoria vazio para o HTML preencher com os dados digitados pelo usuário.
        model.addAttribute("categoria", new Categoria());
        
        // Abre o arquivo de formulário em templates/categorias/form-criar.html
        return "categorias/form-criar";
    }

    // Rota POST /categorias -> Recebe os dados do formulário e tenta salvar a nova categoria no banco.
    @PostMapping
    public String salvarNova(@Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        // Se houver algum erro de validação (ex: nome em branco), volta para a tela de criar categoria.
        if (result.hasErrors()) {
            return "categorias/form-criar";
        }
        
        // Se já existir uma categoria com o mesmo nome, adiciona um erro personalizado na tela.
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            result.rejectValue("nome", "duplicate", "Ja existe uma categoria com esse nome.");
            return "categorias/form-criar";
        }
        
        // Salva a nova categoria no banco de dados.
        categoriaRepository.save(categoria);
        
        // Redireciona o usuário para a página de listagem.
        return "redirect:/categorias";
    }

    // Rota GET /categorias/{id} -> Exibe os detalhes de uma categoria específica.
    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        // Buscamos a categoria usando o Optional, que serve para lidar com buscas que podem não achar nada.
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        // Se a categoria foi encontrada
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            // Mandamos a categoria encontrada para a tela HTML.
            model.addAttribute("categoria", categoria);
            
            // Abre a tela em templates/categorias/detalhar.html
            return "categorias/detalhar";
        } else {
            // Se o ID não existe no banco, gera um erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // Rota GET /categorias/{id}/editar -> Exibe o formulário de edição pré-preenchido.
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        // Buscamos no banco usando o Optional.
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        // Se a categoria existe
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            // Mandamos a categoria para a tela de edição preencher os campos.
            model.addAttribute("categoria", categoria);
            
            // Abre a tela em templates/categorias/form-editar.html
            return "categorias/form-editar";
        } else {
            // Se o ID for inválido, dá erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // Rota POST /categorias/{id} -> Recebe os dados alterados do formulário e atualiza a categoria.
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        // Se tiver erro de digitação/validação, volta para a tela de edição.
        if (result.hasErrors()) {
            return "categorias/form-editar";
        }
        
        // Definimos o ID recebido no objeto para o Spring atualizar o registro certo no banco em vez de criar outro.
        categoria.setId(id);
        categoriaRepository.save(categoria);
        
        // Volta para a listagem principal de categorias.
        return "redirect:/categorias";
    }

    // Rota POST /categorias/{id}/excluir -> Exclui uma categoria do banco.
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        // Buscamos no banco antes para garantir que a categoria existe.
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        // Se existir no banco, deletamos pelo ID.
        if (categoriaOptional.isPresent()) {
            categoriaRepository.deleteById(id);
            // Volta para a lista de categorias.
            return "redirect:/categorias";
        } else {
            // Se o ID não existir, dá erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }
}

