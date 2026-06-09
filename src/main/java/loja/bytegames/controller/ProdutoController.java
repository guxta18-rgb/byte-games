package loja.bytegames.controller;

// Importação do modelo Produto e dos repositórios que acessam o banco de dados
import loja.bytegames.model.Produto; 
import loja.bytegames.repository.ProdutoRepository; 
import loja.bytegames.repository.CategoriaRepository; 

// Importação das validações e componentes do framework Spring Boot
import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model; 
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.*; 

// Importação da biblioteca utilitária do Java para lidar com valores que podem ser nulos
import java.util.Optional; 

/*
 * CONTEXTO DESTA CLASSE:
 * Este é o Controlador de Produtos (ProdutoController). No padrão de arquitetura MVC (Model-View-Controller),
 * ele atua como um intermediário. Ele recebe as requisições HTTP do navegador do usuário, solicita ou salva 
 * dados usando os repositories (banco de dados) e indica qual página HTML (View) deve ser renderizada ou 
 * para onde redirecionar o navegador.
 */

@Controller // Define esta classe como um controlador Spring MVC que gerencia páginas web
@RequestMapping("/produtos") // Define que todas as URLs tratadas aqui começam com "/produtos"
public class ProdutoController {

    // Declaração dos repositórios para comunicação com o banco de dados
    private final ProdutoRepository produtoRepository; 
    private final CategoriaRepository categoriaRepository; 

    // Construtor da classe: permite que o Spring Boot injete as dependências necessárias automaticamente
    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // -------------------------------------------------------------------------
    // 1. ROTA DE LISTAGEM DE PRODUTOS
    // Rota GET para "/produtos" (ex: http://localhost:8080/produtos)
    // -------------------------------------------------------------------------
    @GetMapping
    public String listarTodos(Model model) {
        // Busca todos os produtos do banco e os coloca em um atributo chamado "produtos" para o HTML ler
        model.addAttribute("produtos", produtoRepository.findAll());
        
        // Retorna o arquivo HTML localizado em: src/main/resources/templates/produtos/listar.html
        return "produtos/listar";
        
        /* 
         * SE O PROFESSOR PEDIR PARA ADICIONAR FILTRO OU BUSCA NESTA TELA:
         * 1. Modifique a assinatura do método para aceitar um parâmetro de busca opcional:
         *    public String listarTodos(@RequestParam(value = "nome", required = false) String nome, Model model)
         * 2. No corpo do método, substitua a chamada por:
         *    if (nome != null && !nome.isEmpty()) {
         *        model.addAttribute("produtos", produtoRepository.findByNomeContainingIgnoreCase(nome));
         *    } else {
         *        model.addAttribute("produtos", produtoRepository.findAll());
         *    }
         */
    }

    // -------------------------------------------------------------------------
    // 2. ROTA PARA EXIBIR FORMULÁRIO DE CADASTRO
    // Rota GET para "/produtos/novo"
    // -------------------------------------------------------------------------
    @GetMapping("/novo")
    public String exibirFormCriar(Model model) {
        // Passa um objeto Produto vazio para que o formulário HTML possa vincular seus campos
        model.addAttribute("produto", new Produto());
        
        // Busca todas as categorias no banco e passa para a tela para popular o campo "select"
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // Retorna o HTML em: src/main/resources/templates/produtos/form-criar.html
        return "produtos/form-criar";
        
        /* 
         * SE O PROFESSOR PEDIR PARA DEFINIR UM VALOR PADRÃO NA TELA DE CADASTRO:
         * Você pode preencher dados no objeto vazio antes de adicioná-lo ao model:
         *    Produto prodPadrao = new Produto();
         *    prodPadrao.setPreco(java.math.BigDecimal.ZERO); // Exemplo: inicia com preço 0
         *    model.addAttribute("produto", prodPadrao);
         */
    }

    // -------------------------------------------------------------------------
    // 3. ROTA PARA SALVAR UM NOVO PRODUTO
    // Rota POST para "/produtos" (processa o envio do formulário de cadastro)
    // -------------------------------------------------------------------------
    @PostMapping
    public String salvarNovo(@Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        // @Valid: Habilita a validação das anotações no modelo (ex: @NotBlank, @Min)
        // @ModelAttribute("produto"): Liga os campos enviados do HTML com o objeto Java Produto
        // BindingResult: Guarda as mensagens e informações sobre eventuais erros de validação
        
        // Se houver algum erro de validação (ex: preço negativo, nome do jogo vazio):
        if (result.hasErrors()) {
            // Recarrega as categorias no banco para que a seleção no HTML não quebre ou fique em branco
            model.addAttribute("categorias", categoriaRepository.findAll());
            // Mantém o usuário no formulário de criação exibindo os erros apontados
            return "produtos/form-criar";
        }
        
        // Salva os dados do produto como um novo registro na tabela do banco de dados
        produtoRepository.save(produto);
        
        // Redireciona o navegador para a página de listagem (/produtos) para evitar reenvio de formulário ao atualizar a tela
        return "redirect:/produtos";
        
        /* 
         * SE O PROFESSOR PEDIR UMA VALIDAÇÃO PERSONALIZADA (ex: impedir nome de jogo duplicado):
         * Adicione uma consulta customizada no repositório e verifique antes de salvar:
         *    if (produtoRepository.existsByNome(produto.getNome())) {
         *        // Vincula o erro diretamente ao campo 'nome' do objeto produto
         *        result.rejectValue("nome", "erro.produto", "Já existe um jogo cadastrado com este nome!");
         *        model.addAttribute("categorias", categoriaRepository.findAll());
         *        return "produtos/form-criar";
         *    }
         */
    }

    // -------------------------------------------------------------------------
    // 4. ROTA PARA VER DETALHES DE UM PRODUTO
    // Rota GET para "/produtos/{id}" (ex: http://localhost:8080/produtos/3)
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        // @PathVariable("id"): Captura o ID da URL e o injeta como variável Java
        
        // Faz a busca no banco pelo ID. O Optional evita erros se o ID não existir
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se o produto foi encontrado com sucesso no banco:
        if (produtoOptional.isPresent()) {
            // Extrai o objeto Produto de dentro do wrapper Optional
            Produto produto = produtoOptional.get();
            // Envia os dados do produto encontrado para a tela de visualização
            model.addAttribute("produto", produto);
            
            // Retorna o HTML em: src/main/resources/templates/produtos/detalhar.html
            return "produtos/detalhar";
        } else {
            // Caso o ID não exista no banco, lança um erro indicando ID inválido
            throw new IllegalArgumentException("ID invalido: " + id);
        }
        
        /* 
         * SE O PROFESSOR PEDIR PARA MOSTRAR UMA TELA DE ERRO AMIGÁVEL EM VEZ DE TRAVAR O APP:
         * Você pode capturar a ausência do ID e redirecionar com uma mensagem de erro:
         *    if (!produtoOptional.isPresent()) {
         *        model.addAttribute("erroMensagem", "Jogo não localizado no sistema.");
         *        return "error/404"; // Direciona para um HTML de erro amigável
         *    }
         */
    }

    // -------------------------------------------------------------------------
    // 5. ROTA PARA EXIBIR FORMULÁRIO DE EDIÇÃO
    // Rota GET para "/produtos/{id}/editar"
    // -------------------------------------------------------------------------
    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        // Busca o produto no banco pelo ID para preencher os campos do formulário com as informações atuais
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se o produto existir no banco:
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            // Envia o produto encontrado para o formulário de edição
            model.addAttribute("produto", produto);
            // Busca e envia a lista de categorias para popular o select de categorias
            model.addAttribute("categorias", categoriaRepository.findAll());
            
            // Retorna o HTML em: src/main/resources/templates/produtos/form-editar.html
            return "produtos/form-editar";
        } else {
            // Lança uma exceção se tentar editar um ID que não existe
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    // -------------------------------------------------------------------------
    // 6. ROTA PARA SALVAR A ATUALIZAÇÃO DO PRODUTO
    // Rota POST para "/produtos/{id}" (processa o envio do formulário de edição)
    // -------------------------------------------------------------------------
    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("produto") Produto produto,
            BindingResult result, Model model) {
        
        // Se houver algum erro de validação (ex: preço nulo ou negativo):
        if (result.hasErrors()) {
            // Recarrega as categorias para o campo select da tela não falhar
            model.addAttribute("categorias", categoriaRepository.findAll());
            // Mantém o usuário na tela de edição exibindo as correções necessárias
            return "produtos/form-editar";
        }
        
        // IMPORTANTE: Definir manualmente o ID no objeto recebido garante que o Spring Data JPA 
        // faça um UPDATE (atualização) no banco em vez de um INSERT (inserção de um novo jogo)
        produto.setId(id);
        
        // Salva as alterações feitas no produto no banco de dados
        produtoRepository.save(produto);
        
        // Redireciona o navegador do usuário para a listagem atualizada de produtos
        return "redirect:/produtos";
        
        /* 
         * SE O PROFESSOR PEDIR PARA IMPLEMENTAR LOG DE ATUALIZAÇÃO:
         * Você pode injetar um Logger ou imprimir no terminal os valores que mudaram:
         *    Produto original = produtoRepository.findById(id).orElseThrow();
         *    System.out.println("Alterando preço de " + original.getPreco() + " para " + produto.getPreco());
         */
    }

    // -------------------------------------------------------------------------
    // 7. ROTA PARA EXCLUIR UM PRODUTO
    // Rota POST para "/produtos/{id}/excluir"
    // -------------------------------------------------------------------------
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        // Busca o produto no banco de dados antes para garantir sua existência
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        // Se o produto de fato existir:
        if (produtoOptional.isPresent()) {
            // Executa a remoção do registro correspondente ao ID no banco de dados
            produtoRepository.deleteById(id);
            // Redireciona o navegador de volta para a lista atualizada
            return "redirect:/produtos";
        } else {
            // Lança uma exceção se tentar excluir um ID que não existe
            throw new IllegalArgumentException("ID invalido: " + id);
        }
        
        /* 
         * SE O PROFESSOR PERGUNTAR POR QUE USAMOS POST PARA EXCLUSÃO E NÃO GET:
         * Requisições GET devem ser seguras e idempotentes (não devem causar alteração de estado no banco).
         * Se usássemos GET para excluir (ex: clicando em um link como /produtos/3/excluir), um robô de busca 
         * (como o Googlebot) ou um pré-carregador de links do navegador poderia acessar a URL e apagar o registro sem querer.
         * O POST exige uma submissão de formulário ou uma chamada JavaScript ativa, tornando o processo muito mais seguro.
         */
    }
}

