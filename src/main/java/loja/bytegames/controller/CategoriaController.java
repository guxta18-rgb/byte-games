package loja.bytegames.controller;

// Importação da entidade Categoria e de seu repositório de banco de dados
import loja.bytegames.model.Categoria;
import loja.bytegames.repository.CategoriaRepository;

// Importações do jakarta validation e componentes do Spring MVC
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// Utilidade Java para manipular buscas opcionais que podem ser nulas
import java.util.Optional;

/*
 * CONTEXTO DESTA CLASSE:
 * Este é o Controlador de Categorias (CategoriaController). Ele gerencia as requisições web
 * voltadas para a organização dos jogos (RPG, Ação, FPS, etc.). Permite listar, criar, editar
 * e excluir categorias do sistema.
 */

@Controller // Registra a classe como um controlador Spring MVC
@RequestMapping("/categorias") // Define que as URLs mapeadas começam com "/categorias"
public class CategoriaController {

    // Repositório que fornece os métodos de manipulação da tabela no banco de dados
    private final CategoriaRepository categoriaRepository;

    // Construtor: injeção de dependência automática das classes de dados pelo Spring
    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // -------------------------------------------------------------------------
    // 1. ROTA DE LISTAGEM DE CATEGORIAS
    // Rota GET para "/categorias"
    // -------------------------------------------------------------------------
    @GetMapping
    public String listarTodas(Model model) {
        // Busca todas as categorias no banco de dados e adiciona no modelo para visualização do Thymeleaf
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // Retorna a view em: src/main/resources/templates/categorias/listar.html
        return "categorias/listar";
    }

    // -------------------------------------------------------------------------
    // 2. ROTA PARA EXIBIR FORMULÁRIO DE NOVA CATEGORIA
    // Rota GET para "/categorias/nova"
    // -------------------------------------------------------------------------
    @GetMapping("/nova")
    public String exibirFormCriar(Model model) {
        // Envia uma nova instância vazia de Categoria para ser populada pelo formulário th:object
        model.addAttribute("categoria", new Categoria());
        
        // Retorna o HTML em: src/main/resources/templates/categorias/form-criar.html
        return "categorias/form-criar";
    }

    // -------------------------------------------------------------------------
    // 3. ROTA PARA SALVAR A NOVA CATEGORIA
    // Rota POST para "/categorias"
    // -------------------------------------------------------------------------
    @PostMapping
    public String salvarNova(@Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        // @Valid: valida o objeto com base nas anotações da model (ex: @NotBlank)
        // BindingResult: armazena os erros encontrados na validação dos campos do formulário
        
        // Se houver algum erro de validação (ex: nome menor que o mínimo de caracteres):
        if (result.hasErrors()) {
            return "categorias/form-criar";
        }
        
        // Regra de negócio: verifica se já existe uma categoria no banco de dados com este nome
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            // Rejeita o valor do campo 'nome' e adiciona a mensagem de duplicidade
            result.rejectValue("nome", "duplicate", "Já existe uma categoria cadastrada com esse nome.");
            return "categorias/form-criar";
        }
        
        // Salva a categoria preenchida na tabela do banco
        categoriaRepository.save(categoria);
        
        // Redireciona para atualizar a listagem (/categorias) por GET
        return "redirect:/categorias";
    }

    // -------------------------------------------------------------------------
    // 4. ROTA PARA VER DETALHES DE UMA CATEGORIA
    // Rota GET para "/categorias/{id}"
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        // Busca a categoria pelo ID usando Optional para evitar retornos nulos
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        // Se a categoria foi localizada:
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            // Passa o objeto localizado para o Thymeleaf
            model.addAttribute("categoria", categoria);
            
            // Retorna o HTML em: src/main/resources/templates/categorias/detalhar.html
            return "categorias/detalhar";
        } else {
            // Lança uma exceção informando que o ID fornecido não é válido
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // -------------------------------------------------------------------------
    // 5. ROTA PARA EXIBIR FORMULÁRIO DE EDIÇÃO DE CATEGORIA
    // Rota GET para "/categorias/{id}/editar"
    // -------------------------------------------------------------------------
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        // Carrega os dados existentes do banco pelo ID
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            // Mapeia os dados no formulário de edição
            model.addAttribute("categoria", categoria);
            
            // Retorna a view em: src/main/resources/templates/categorias/form-editar.html
            return "categorias/form-editar";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // -------------------------------------------------------------------------
    // 6. ROTA PARA SALVAR A ATUALIZAÇÃO DA CATEGORIA
    // Rota POST para "/categorias/{id}"
    // -------------------------------------------------------------------------
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult result) {
        
        // Se as edições do usuário gerarem erros de validação:
        if (result.hasErrors()) {
            return "categorias/form-editar";
        }
        
        // Garante que o Spring atualize o registro com o ID correto e não crie uma nova categoria
        categoria.setId(id);
        
        // Executa o salvamento (UPDATE) do registro
        categoriaRepository.save(categoria);
        
        // Redireciona para a listagem atualizada de categorias
        return "redirect:/categorias";
    }

    // -------------------------------------------------------------------------
    // 7. ROTA PARA EXCLUIR UMA CATEGORIA
    // Rota POST para "/categorias/{id}/excluir"
    // -------------------------------------------------------------------------
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        // Busca a categoria antes da exclusão
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        if (categoriaOptional.isPresent()) {
            // Deleta o registro pelo ID
            categoriaRepository.deleteById(id);
            // Retorna para a listagem
            return "redirect:/categorias";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
        
        /* 
         * SE O PROFESSOR PEDIR PARA IMPEDIR A EXCLUSÃO CASO HAJA PRODUTOS NELA (Restrição de Integridade):
         * Atualmente, se a categoria tiver jogos e você deletá-la, por cascade, os produtos podem sumir 
         * ou gerar erro de chave estrangeira. Você pode impedir a exclusão manual assim:
         * 
         *    Categoria categoria = categoriaOptional.get();
         *    if (categoria.getProdutos() != null && !categoria.getProdutos().isEmpty()) {
         *        // Redireciona impedindo a exclusão e mostrando um parâmetro de erro
         *        return "redirect:/categorias?erro=categoria-possui-produtos";
         *    }
         */
    }
}

