package loja.bytegames;

// Importações dos repositórios, de sessão http e coleções do java
import loja.bytegames.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

// @Controller diz que esta classe controla páginas HTML públicas
@Controller
public class LojaController {

    private final ProdutoRepository produtoRepository;

    public LojaController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Rota raiz (página inicial / vitrine pública)
    @GetMapping("/")
    public String vitrine(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Produto> listaProdutos;
        
        // Se houver algum termo de busca digitado pelo usuário na vitrine
        if (search != null && !search.trim().isEmpty()) {
            // Busca apenas os produtos correspondentes ao nome digitado
            listaProdutos = produtoRepository.findByNomeContainingIgnoreCase(search);
            model.addAttribute("termoBuscado", search);
        } else {
            // Caso contrário, lista todos os produtos
            listaProdutos = produtoRepository.buscarTodosProdutos();
        }
        
        model.addAttribute("produtos", listaProdutos);
        return "loja/vitrine"; // Abre templates/loja/vitrine.html
    }

    // Adiciona um produto ao carrinho de compras utilizando a Sessão HTTP
    @SuppressWarnings("unchecked")
    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam("produtoId") Long produtoId, HttpSession session) {
        // Recupera o carrinho (um mapa contendo o ID do produto e a quantidade) da sessão do usuário
        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }
        
        // Incrementa em 1 a quantidade do produto selecionado
        carrinho.put(produtoId, carrinho.getOrDefault(produtoId, 0) + 1);
        
        // Salva novamente o carrinho atualizado na sessão
        session.setAttribute("carrinho", carrinho);
        
        // Redireciona o usuário direto para ver o carrinho
        return "redirect:/carrinho";
    }

    // Exibe a página do carrinho de compras com a tabela de itens e valores
    @SuppressWarnings("unchecked")
    @GetMapping("/carrinho")
    public String exibirCarrinho(HttpSession session, Model model) {
        // Busca o carrinho salvo na sessão
        Map<Long, Integer> carrinhoSessao = (Map<Long, Integer>) session.getAttribute("carrinho");
        
        // Lista contendo estruturas de chaves/valores de cada item (produto, quantidade e subtotal)
        List<Map<String, Object>> itensCarrinho = new ArrayList<>();
        double totalGeral = 0.0;

        if (carrinhoSessao != null) {
            // Percorre cada item no carrinho da sessão
            for (Map.Entry<Long, Integer> item : carrinhoSessao.entrySet()) {
                Optional<Produto> produtoOptional = produtoRepository.buscarProdutoPorId(item.getKey());
                
                // Se o produto realmente existir no banco
                if (produtoOptional.isPresent()) {
                    Produto prod = produtoOptional.get();
                    Map<String, Object> linha = new HashMap<>();
                    
                    // Calcula o subtotal (preço do produto multiplicado pela quantidade selecionada)
                    double subtotal = prod.getPreco().doubleValue() * item.getValue();
                    totalGeral += subtotal;

                    // Mapeia os dados do item para ler no HTML do Thymeleaf
                    linha.put("produto", prod);
                    linha.put("quantidade", item.getValue());
                    linha.put("subtotal", subtotal);
                    itensCarrinho.add(linha);
                }
            }
        }

        // Envia a lista de itens e o total geral para a página HTML do carrinho
        model.addAttribute("itens", itensCarrinho);
        model.addAttribute("totalGeral", totalGeral);
        return "loja/carrinho"; // Abre templates/loja/carrinho.html
    }

    // Limpa todos os itens do carrinho de compras da sessão do usuário
    @PostMapping("/carrinho/limpar")
    public String limparCarrinho(HttpSession session) {
        session.removeAttribute("carrinho");
        return "redirect:/carrinho";
    }

    // Exibe a tela pública de detalhes de um jogo selecionado na vitrine
    @GetMapping("/produto/{id}")
    public String detalharProduto(@PathVariable("id") Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.buscarProdutoPorId(id);
        if (produtoOptional.isPresent()) {
            model.addAttribute("produto", produtoOptional.get());
            return "loja/detalhar"; // Abre templates/loja/detalhar.html
        }
        return "redirect:/";
    }
}
