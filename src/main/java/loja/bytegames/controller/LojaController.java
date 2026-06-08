package loja.bytegames.controller;

import loja.bytegames.model.Produto;
import loja.bytegames.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * Este arquivo representa o Controlador da Loja (Vitrine e Carrinho).
 * Ele gerencia o que o cliente vê na página principal (vitrine), a busca de jogos,
 * e todo o funcionamento do carrinho de compras usando sessões do navegador.
 */

// A anotação @Controller indica que esta classe gerencia as telas da loja.
@Controller
public class LojaController {

    // Gerenciador da tabela de produtos no banco.
    private final ProdutoRepository produtoRepository;

    // Construtor: recebe o repository do Spring Boot.
    public LojaController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Rota GET / -> É a página principal (vitrine) da loja.
    // @RequestParam(search) serve para capturar o que o usuário digitou na barra de pesquisa (opcional).
    @GetMapping("/")
    public String vitrine(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Produto> listaProdutos;

        // Se o usuário digitou algo na busca e não é apenas espaços em branco
        if (search != null && !search.trim().isEmpty()) {
            // Buscamos no banco os produtos correspondentes
            listaProdutos = produtoRepository.findByNomeContainingIgnoreCase(search);
            // Mandamos o termo buscado de volta para a tela para mostrar "Resultados para: ..."
            model.addAttribute("termoBuscado", search);
        } else {
            // Se não buscou nada, trazemos todos os produtos cadastrados no banco.
            listaProdutos = produtoRepository.findAll();
        }

        // Mandamos a lista de produtos para o HTML vitrine.html
        model.addAttribute("produtos", listaProdutos);
        return "loja/vitrine";
    }

    // Rota POST /carrinho/adicionar -> Adiciona um jogo na sacola de compras.
    // HttpSession serve para guardar os produtos que o cliente escolheu enquanto navega no site.
    @SuppressWarnings("unchecked")
    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam("produtoId") Long produtoId, HttpSession session) {

        // Pegamos o carrinho atual da sessão do usuário.
        // O carrinho guarda o ID do produto (Long) e a quantidade escolhida (Integer).
        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        
        // Se ainda não existir um carrinho na sessão do usuário, criamos um novo mapa vazio.
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        // --- Simplificação iniciante do getOrDefault ---
        // Verificamos se o produto já estava no carrinho. 
        // Se sim, pegamos a quantidade antiga. Se não, a quantidade começa em zero.
        int quantidadeAtual = 0;
        if (carrinho.containsKey(produtoId)) {
            quantidadeAtual = carrinho.get(produtoId);
        }
        
        // Adicionamos mais 1 na quantidade e salvamos de volta no mapa do carrinho.
        int novaQuantidade = quantidadeAtual + 1;
        carrinho.put(produtoId, novaQuantidade);

        // Guardamos o carrinho atualizado de volta na sessão do usuário.
        session.setAttribute("carrinho", carrinho);

        // Redireciona o usuário para a tela do carrinho para exibir o resultado.
        return "redirect:/carrinho";
    }

    // Rota GET /carrinho -> Exibe os produtos adicionados na sacola de compras.
    @SuppressWarnings("unchecked")
    @GetMapping("/carrinho")
    public String exibirCarrinho(HttpSession session, Model model) {
        // Buscamos o carrinho na sessão do usuário.
        Map<Long, Integer> carrinhoSessao = (Map<Long, Integer>) session.getAttribute("carrinho");

        // Criamos uma lista de mapas para montar as linhas da tabela no HTML.
        // Cada item terá: o produto completo, a quantidade e o subtotal (preco * quantidade).
        List<Map<String, Object>> itensCarrinho = new ArrayList<>();
        double totalGeral = 0.0;

        // Se o carrinho existir e tiver itens
        if (carrinhoSessao != null) {
            // Percorremos cada item que está guardado no carrinho
            for (Map.Entry<Long, Integer> item : carrinhoSessao.entrySet()) {
                
                // --- Simplificação iniciante do orElse ---
                // Buscamos o produto no banco de dados.
                Optional<Produto> produtoOptional = produtoRepository.findById(item.getKey());
                Produto prod = null;
                if (produtoOptional.isPresent()) {
                    prod = produtoOptional.get();
                }
                
                // Se o produto existir de verdade no banco
                if (prod != null) {
                    Map<String, Object> linha = new HashMap<>();
                    
                    // Calculamos o subtotal: preço unitário multiplicado pela quantidade de itens.
                    double subtotal = prod.getPreco().doubleValue() * item.getValue();
                    totalGeral += subtotal;

                    // Colocamos as informações estruturadas na linha
                    linha.put("produto", prod);
                    linha.put("quantidade", item.getValue());
                    linha.put("subtotal", subtotal);
                    
                    // Adicionamos a linha à lista de itens do carrinho
                    itensCarrinho.add(linha);
                }
            }
        }

        // Enviamos a lista de itens estruturados e o total geral para a tela do carrinho.
        model.addAttribute("itens", itensCarrinho);
        model.addAttribute("totalGeral", totalGeral);
        return "loja/carrinho";
    }

    // Rota POST /carrinho/limpar -> Esvazia a sacola de compras.
    @PostMapping("/carrinho/limpar")
    public String limparCarrinho(HttpSession session) {
        // Remove o carrinho inteiro da sessão do usuário.
        session.removeAttribute("carrinho");
        return "redirect:/carrinho";
    }
}