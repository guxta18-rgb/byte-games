package loja.bytegames.controller;

import loja.bytegames.model.Produto;
import loja.bytegames.repository.ProdutoRepository;
import loja.bytegames.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/*
 * Este arquivo representa o Controlador de Produtos.
 * Ele cuida de todas as ações relacionadas aos jogos cadastrados na loja,
 * como listar todos, ver os detalhes, cadastrar um novo jogo, editar as 
 * informações de um jogo existente e deletá-lo.
 */

// A anotação @Controller avisa que esta classe gerencia as telas e rotas de produtos.
// Todas as rotas dessa classe começam com "/produtos".
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    // Criamos as variáveis para guardar nossos repositories necessários (produtos e categorias).
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    // Construtor: serve para o Spring Boot passar os repositories para nós usarmos.
    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Rota GET /produtos -> Lista todos os produtos (jogos) cadastrados no banco.
    @GetMapping
    public String listarTodos(Model model) {
        // Busca todos os produtos e joga na variável "produtos" que o HTML consegue ler.
        model.addAttribute("produtos", produtoRepository.findAll());
        
        // Abre o arquivo de listagem em templates/produtos/listar.html
        return "produtos/listar";
    }

    // Rota GET /produtos/novo -> Exibe o formulário de cadastro de novo jogo.
    @GetMapping("/novo")
    public String exibirFormCriar(Model model) {
        // Envia um produto vazio para o formulário preencher os campos.
        model.addAttribute("produto", new Produto());
        // Envia a lista de todas as categorias cadastradas para o usuário selecionar uma no formulário.
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // Abre o formulário em templates/produtos/form-criar.html
        return "produtos/form-criar";
    }

    // Rota POST /produtos -> Salva o novo produto digitado no formulário.
    @PostMapping
    public String salvarNovo(@Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        // Se houver algum erro de validação (ex: preço negativo, nome em branco), volta para o formulário.
        if (result.hasErrors()) {
            // Recarrega as categorias para o formulário não ficar com o campo de seleção vazio.
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/form-criar";
        }
        
        // Salva o produto no banco de dados.
        produtoRepository.save(produto);
        
        // Redireciona o navegador para a lista de produtos.
        return "redirect:/produtos";
    }

    // Rota GET /produtos/{id} -> Mostra os detalhes de um produto específico.
    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        // Usamos Optional para buscar pelo ID no banco de dados.
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se encontramos o produto
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            // Mandamos o produto encontrado para o HTML.
            model.addAttribute("produto", produto);
            
            // Abre o HTML em templates/produtos/detalhar.html
            return "produtos/detalhar";
        } else {
            // Se não encontrar o ID, dispara um erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // Rota GET /produtos/{id}/editar -> Abre a tela de edição preenchida com os dados do jogo.
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        // Busca o produto pelo ID.
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se o produto existir no banco
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            // Mandamos o produto para os campos da tela de edição.
            model.addAttribute("produto", produto);
            // Mandamos a lista de categorias para a seleção.
            model.addAttribute("categorias", categoriaRepository.findAll());
            
            // Abre o formulário em templates/produtos/form-editar.html
            return "produtos/form-editar";
        } else {
            // Se o ID for inválido, dá erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // Rota POST /produtos/{id} -> Recebe os dados alterados na tela de edição e salva no banco.
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        // Se tiver erro de digitação/validação, volta para a tela de edição.
        if (result.hasErrors()) {
            // Recarrega as categorias.
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "produtos/form-editar";
        }
        
        // Definimos o ID para indicar ao Spring Boot qual produto cadastrado no banco deve ser atualizado.
        produto.setId(id);
        produtoRepository.save(produto);
        
        // Redireciona o usuário para a lista de produtos.
        return "redirect:/produtos";
    }

    // Rota POST /produtos/{id}/excluir -> Remove um produto do banco pelo ID.
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        // Busca o produto antes para certificar que ele existe.
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se ele existir no banco, fazemos a remoção.
        if (produtoOptional.isPresent()) {
            produtoRepository.deleteById(id);
            // Redireciona de volta para a lista.
            return "redirect:/produtos";
        } else {
            // Se o ID for inválido, dá erro.
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }
}

