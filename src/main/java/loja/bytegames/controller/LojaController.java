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

@Controller
public class LojaController {

    private final ProdutoRepository produtoRepository;

    public LojaController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/")
    public String vitrine(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Produto> listaProdutos;

        if (search != null && !search.trim().isEmpty()) {
            listaProdutos = produtoRepository.findByNomeContainingIgnoreCase(search);
            model.addAttribute("termoBuscado", search);
        } else {
            listaProdutos = produtoRepository.findAll();
        }

        model.addAttribute("produtos", listaProdutos);
        return "loja/vitrine";
    }

    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam("produtoId") Long produtoId, HttpSession session) {

        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        carrinho.put(produtoId, carrinho.getOrDefault(produtoId, 0) + 1);

        session.setAttribute("carrinho", carrinho);

        return "redirect:/carrinho";
    }

    @GetMapping("/carrinho")
    public String exibirCarrinho(HttpSession session, Model model) {
        Map<Long, Integer> carrinhoSessao = (Map<Long, Integer>) session.getAttribute("carrinho");

        List<Map<String, Object>> itensCarrinho = new ArrayList<>();
        double totalGeral = 0.0;

        if (carrinhoSessao != null) {
            for (Map.Entry<Long, Integer> item : carrinhoSessao.entrySet()) {
                Produto prod = produtoRepository.findById(item.getKey()).orElse(null);
                if (prod != null) {
                    Map<String, Object> linha = new HashMap<>();
                    double subtotal = prod.getPreco().doubleValue() * item.getValue();
                    totalGeral += subtotal;

                    linha.put("produto", prod);
                    linha.put("quantidade", item.getValue());
                    linha.put("subtotal", subtotal);
                    itensCarrinho.add(linha);
                }
            }
        }

        model.addAttribute("itens", itensCarrinho);
        model.addAttribute("totalGeral", totalGeral);
        return "loja/carrinho";
    }

    @PostMapping("/carrinho/limpar")
    public String limparCarrinho(HttpSession session) {
        session.removeAttribute("carrinho");
        return "redirect:/carrinho";
    }
}