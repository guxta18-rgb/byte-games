package loja.bytegames.controller;

// Importação das classes de modelo e repositórios
import loja.bytegames.model.Produto;
import loja.bytegames.repository.ProdutoRepository;

// Importações do Java Servlet e componentes do Spring Boot
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Coleções utilitárias do Java para manipulação de listas e mapas (carrinho de compras)
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * CONTEXTO DESTA CLASSE:
 * Este é o Controlador da Loja (LojaController). Ele cuida da experiência pública do cliente:
 * a página principal (vitrine), a funcionalidade de busca de produtos e toda a engrenagem do 
 * carrinho de compras que utiliza a sessão do usuário (HttpSession) para guardar os jogos selecionados.
 */

@Controller // Registra como um controlador MVC
public class LojaController {

    // Repositório de produtos usado para buscar os dados para a vitrine e carrinho
    private final ProdutoRepository produtoRepository;

    // Construtor com injeção automática de dependência
    public LojaController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // -------------------------------------------------------------------------
    // 1. PÁGINA PRINCIPAL / VITRINE DA LOJA
    // Rota GET para "/" (URL raiz do site)
    // -------------------------------------------------------------------------
    @GetMapping("/")
    public String vitrine(@RequestParam(value = "search", required = false) String search, Model model) {
        // @RequestParam(value = "search", required = false): Captura um parâmetro opcional na URL (ex: /?search=witcher)
        
        List<Produto> listaProdutos;

        // Se o usuário digitou algum texto na barra de busca:
        if (search != null && !search.trim().isEmpty()) {
            // Consulta o banco trazendo produtos cujo nome contenha o termo digitado (ignorando maiúsculas/minúsculas)
            listaProdutos = produtoRepository.findByNomeContainingIgnoreCase(search);
            // Envia o termo de busca de volta para o HTML (para mostrar "Resultados para: 'termo'")
            model.addAttribute("termoBuscado", search);
        } else {
            // Caso contrário (sem busca ativa), traz todos os produtos cadastrados no banco
            listaProdutos = produtoRepository.findAll();
        }

        // Manda a lista de produtos (filtrada ou cheia) para renderização no HTML vitrine.html
        model.addAttribute("produtos", listaProdutos);
        
        // Retorna a view em: src/main/resources/templates/loja/vitrine.html
        return "loja/vitrine";
    }

    // -------------------------------------------------------------------------
    // 2. ADICIONAR PRODUTO AO CARRINHO DE COMPRAS
    // Rota POST para "/carrinho/adicionar" (envio seguro via formulário)
    // -------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam("produtoId") Long produtoId, HttpSession session) {
        // @RequestParam("produtoId"): Captura o ID do produto enviado pelo botão da vitrine
        // HttpSession: Representa a sessão do navegador do cliente. Guarda dados temporários na memória do servidor

        // Recupera o mapa do carrinho de compras atualmente salvo na sessão
        // O mapa associa o ID do Produto (Long) com a quantidade adicionada (Integer)
        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        
        // Se a sessão for nova e o carrinho ainda não existir, criamos um mapa vazio
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        // Recupera a quantidade atual do produto no carrinho de forma simples para iniciantes
        int quantidadeAtual = 0;
        if (carrinho.containsKey(produtoId)) {
            quantidadeAtual = carrinho.get(produtoId);
        }
        
        // Incrementa em 1 a quantidade e insere/atualiza no mapa do carrinho
        int novaQuantidade = quantidadeAtual + 1;
        carrinho.put(produtoId, novaQuantidade);

        // Atualiza a variável na sessão com o carrinho modificado
        session.setAttribute("carrinho", carrinho);

        // Redireciona o navegador do usuário para abrir a rota GET "/carrinho"
        return "redirect:/carrinho";
    }

    // -------------------------------------------------------------------------
    // 3. EXIBIR A TELA DO CARRINHO DE COMPRAS
    // Rota GET para "/carrinho"
    // -------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @GetMapping("/carrinho")
    public String exibirCarrinho(HttpSession session, Model model) {
        // Recupera o carrinho (ID -> Quantidade) armazenado na sessão do cliente
        Map<Long, Integer> carrinhoSessao = (Map<Long, Integer>) session.getAttribute("carrinho");

        // Lista de mapas estruturados que guardará cada item do carrinho com todos os seus detalhes no HTML
        List<Map<String, Object>> itensCarrinho = new ArrayList<>();
        double totalGeral = 0.0;

        // Se a sessão possui um carrinho ativo:
        if (carrinhoSessao != null) {
            // Percorre cada entrada (par ID e quantidade)
            for (Map.Entry<Long, Integer> item : carrinhoSessao.entrySet()) {
                
                // Busca o produto correspondente no banco de dados
                Optional<Produto> produtoOptional = produtoRepository.findById(item.getKey());
                Produto prod = null;
                if (produtoOptional.isPresent()) {
                    prod = produtoOptional.get();
                }
                
                // Se o produto foi encontrado no banco de dados de fato:
                if (prod != null) {
                    Map<String, Object> linha = new HashMap<>();
                    
                    // Calcula o subtotal deste item: Preço Unitário * Quantidade
                    double subtotal = prod.getPreco().doubleValue() * item.getValue();
                    totalGeral += subtotal; // Soma ao total geral do carrinho

                    // Estrutura os dados para exibição simplificada no Thymeleaf
                    linha.put("produto", prod);
                    linha.put("quantidade", item.getValue());
                    linha.put("subtotal", subtotal);
                    
                    // Adiciona o item formatado na lista final
                    itensCarrinho.add(linha);
                }
            }
        }

        // Manda os dados estruturados e o total geral calculados para renderizar na view
        model.addAttribute("itens", itensCarrinho);
        model.addAttribute("totalGeral", totalGeral);
        
        // Retorna a view em: src/main/resources/templates/loja/carrinho.html
        return "loja/carrinho";
    }

    // -------------------------------------------------------------------------
    // 4. LIMPAR CARRINHO DE COMPRAS
    // Rota POST para "/carrinho/limpar"
    // -------------------------------------------------------------------------
    @PostMapping("/carrinho/limpar")
    public String limparCarrinho(HttpSession session) {
        // Remove completamente o atributo do carrinho da sessão
        session.removeAttribute("carrinho");
        
        // Redireciona para atualizar a tela do carrinho vazia
        return "redirect:/carrinho";
        
        /* 
         * SE O PROFESSOR PEDIR PARA REMOVER APENAS UM JOGO ESPECÍFICO DO CARRINHO (Botão Remover Item):
         * Crie uma nova rota POST:
         * 
         *    @PostMapping("/carrinho/remover")
         *    public String removerItem(@RequestParam("produtoId") Long produtoId, HttpSession session) {
         *        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
         *        if (carrinho != null) {
         *            carrinho.remove(produtoId); // Remove do mapa usando a chave do ID do produto
         *            session.setAttribute("carrinho", carrinho);
         *        }
         *        return "redirect:/carrinho";
         *    }
         * 
         * SE O PROFESSOR PEDIR PARA IMPLEMENTAR A COMPRA / CHECKOUT (com redução de estoque):
         * Crie uma rota POST que lê o carrinho, desconta o estoque de cada produto no banco de dados e limpa a sessão:
         * 
         *    @PostMapping("/carrinho/checkout")
         *    public String checkout(HttpSession session) {
         *        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
         *        if (carrinho != null) {
         *            for (Map.Entry<Long, Integer> item : carrinho.entrySet()) {
         *                Produto prod = produtoRepository.findById(item.getKey()).orElseThrow();
         *                // Reduz a quantidade comprada do estoque
         *                prod.setEstoque(prod.getEstoque() - item.getValue());
         *                produtoRepository.save(prod); // Atualiza o banco
         *            }
         *            session.removeAttribute("carrinho"); // Limpa o carrinho
         *        }
         *        return "redirect:/?sucesso=compra-realizada";
         *    }
         */
    }
}
