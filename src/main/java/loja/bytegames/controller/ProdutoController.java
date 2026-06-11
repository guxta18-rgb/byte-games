package loja.bytegames.controller;

import loja.bytegames.model.Produto; 
import loja.bytegames.repository.ProdutoRepository; 
import loja.bytegames.repository.CategoriaRepository; 

import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model; 
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.*; 
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Optional; 

@Controller 
@RequestMapping("/produtos") 
public class ProdutoController {

    private final ProdutoRepository produtoRepository; 
    private final CategoriaRepository categoriaRepository; 

    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("produtos", produtoRepository.buscarTodosProdutos());
        return "produtos/listar";
    }

    @GetMapping("/novo")
    public String exibirFormCriar(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
        return "produtos/form-criar";
    }

    @PostMapping
    public String salvarNovo(@Valid @ModelAttribute("produto") Produto produto,
            BindingResult result,
            @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
            return "produtos/form-criar";
        }
        
        try {
            String nomeImagem = salvarImagem(imagemFile);
            if (nomeImagem != null) {
                produto.setImagem(nomeImagem);
            }
        } catch (IOException e) {
            model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
            result.rejectValue("imagem", "erro.imagem", "Erro ao salvar imagem no servidor: " + e.getMessage());
            return "produtos/form-criar";
        }
        
        produtoRepository.inserirProduto(
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getCategoria().getId(),
            produto.getImagem()
        );
        
        return "redirect:/produtos";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable("id") Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.buscarProdutoPorId(id);
        
        if (produtoOptional.isPresent()) {
            model.addAttribute("produto", produtoOptional.get());
            return "produtos/detalhar";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    @GetMapping("/{id}/editar")
    public String exibirFormEditar(@PathVariable("id") Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.buscarProdutoPorId(id);
        
        if (produtoOptional.isPresent()) {
            model.addAttribute("produto", produtoOptional.get());
            model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
            return "produtos/form-editar";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @Valid @ModelAttribute("produto") Produto produto,
            BindingResult result,
            @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
            return "produtos/form-editar";
        }
        
        try {
            if (imagemFile != null && !imagemFile.isEmpty()) {
                String nomeImagem = salvarImagem(imagemFile);
                produto.setImagem(nomeImagem);
            }
        } catch (IOException e) {
            model.addAttribute("categorias", categoriaRepository.buscarTodasCategorias());
            result.rejectValue("imagem", "erro.imagem", "Erro ao atualizar imagem no servidor: " + e.getMessage());
            return "produtos/form-editar";
        }
        
        produtoRepository.atualizarProduto(
            id,
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getCategoria().getId(),
            produto.getImagem()
        );
        
        return "redirect:/produtos";
    }

    private String salvarImagem(MultipartFile imagemFile) throws IOException {
        if (imagemFile == null || imagemFile.isEmpty()) {
            return null;
        }
        
        String pastaDestino = "uploads/";
        Path uploadPath = Paths.get(pastaDestino);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String nomeOriginal = imagemFile.getOriginalFilename();
        String nomeFinal = UUID.randomUUID().toString() + "_" + 
                (nomeOriginal != null ? nomeOriginal.replaceAll("\\s+", "_") : "imagem.jpg");
        
        Path path = uploadPath.resolve(nomeFinal);
        
        Files.copy(imagemFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        
        return nomeFinal;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id) {
        Optional<Produto> produtoOptional = produtoRepository.buscarProdutoPorId(id);
        
        if (produtoOptional.isPresent()) {
            produtoRepository.deletarProdutoPorId(id);
            return "redirect:/produtos";
        } else {
            throw new IllegalArgumentException("ID invalido: " + id);
        }
    }
}
