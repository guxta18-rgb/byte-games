package loja.bytegames;

import loja.bytegames.model.Categoria;
import loja.bytegames.model.Produto;
import loja.bytegames.repository.CategoriaRepository;
import loja.bytegames.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BytegamesDatabaseTests {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testCrudFlowOnMySql() {
        System.out.println("\n=======================================================");
        System.out.println("🚀 INICIANDO FLUXO DE VALIDAÇÃO DE CONEXÃO E CRUD MYSQL");
        System.out.println("=======================================================");

        // 0. Preparação: Garantir categoria para o produto usando query explícita
        System.out.println("\n⚙️ [PREPARAÇÃO] Criando categoria de teste...");
        categoriaRepository.inserirCategoria("Categoria Temp Teste", "Categoria temporária para testes automatizados");
        
        Categoria categoria = categoriaRepository.buscarCategoriaPorNome("Categoria Temp Teste")
                .orElseThrow(() -> new AssertionError("Categoria de teste deveria ter sido inserida e encontrada."));
        assertNotNull(categoria.getId(), "Categoria deveria ter um ID gerado pelo banco.");
        System.out.println("✅ Categoria criada com sucesso no MySQL! ID: " + categoria.getId());

        // 1. CREATE (Insert usando query explícita)
        System.out.println("\n➕ [1. CREATE] Cadastrando produto fictício...");
        produtoRepository.inserirProduto(
                "Jogo Teste Automatizado",
                "Um produto temporário inserido para validar a conexão do banco de dados Byteloja",
                new BigDecimal("99.99"),
                5,
                categoria.getId(),
                null
        );

        Produto novoProduto = produtoRepository.findByNomeContainingIgnoreCase("Jogo Teste Automatizado")
                .stream().findFirst()
                .orElseThrow(() -> new AssertionError("O produto deveria ter sido inserido e encontrado."));
        Long produtoId = novoProduto.getId();
        assertNotNull(produtoId, "O produto deveria ter um ID gerado pelo banco.");
        System.out.println("✅ Produto cadastrado com sucesso no MySQL! ID: " + produtoId);

        // 2. READ (Select usando query explícita)
        System.out.println("\n🔍 [2. READ] Buscando o produto recém-criado...");
        Optional<Produto> produtoBuscadoOpt = produtoRepository.buscarProdutoPorId(produtoId);
        assertTrue(produtoBuscadoOpt.isPresent(), "O produto deveria ter sido encontrado no banco.");
        
        Produto produtoBuscado = produtoBuscadoOpt.get();
        System.out.println("✅ Produto localizado no MySQL com sucesso!");
        System.out.println("   👉 Nome: " + produtoBuscado.getNome());
        System.out.println("   👉 Preço original: R$ " + produtoBuscado.getPreco());
        System.out.println("   👉 Estoque: " + produtoBuscado.getEstoque());

        // 3. UPDATE (Alterar preço usando query explícita)
        System.out.println("\n🔄 [3. UPDATE] Atualizando o preço do produto...");
        BigDecimal novoPreco = new BigDecimal("129.90");
        
        produtoRepository.atualizarProduto(
                produtoId,
                produtoBuscado.getNome(),
                produtoBuscado.getDescricao(),
                novoPreco,
                produtoBuscado.getEstoque(),
                categoria.getId(),
                produtoBuscado.getImagem()
        );
        
        Produto produtoAtualizado = produtoRepository.buscarProdutoPorId(produtoId)
                .orElseThrow(() -> new AssertionError("O produto deveria existir após atualização."));
        
        assertEquals(novoPreco, produtoAtualizado.getPreco(), "O preço deveria ter sido atualizado para 129.90.");
        System.out.println("✅ Preço do produto atualizado com sucesso no MySQL!");
        System.out.println("   👉 Novo Preço: R$ " + produtoAtualizado.getPreco());

        // 4. DELETE (Remover do banco usando query explícita)
        System.out.println("\n❌ [4. DELETE] Removendo o produto de teste...");
        produtoRepository.deletarProdutoPorId(produtoId);
        
        Optional<Produto> produtoDeletadoOpt = produtoRepository.buscarProdutoPorId(produtoId);
        assertFalse(produtoDeletadoOpt.isPresent(), "O produto não deveria mais existir no banco.");
        System.out.println("✅ Produto removido com sucesso do MySQL!");

        // Limpeza da categoria temporária usando query explícita
        categoriaRepository.deletarCategoriaPorId(categoria.getId());
        
        Optional<Categoria> categoriaDeletadaOpt = categoriaRepository.buscarCategoriaPorId(categoria.getId());
        assertFalse(categoriaDeletadaOpt.isPresent(), "A categoria não deveria mais existir no banco.");
        System.out.println("🧹 Categoria temporária limpa com sucesso!");

        System.out.println("\n=======================================================");
        System.out.println("🎉 FIM DO TESTE - CONEXÃO COM O BANCO 'Byteloja' 100% OK!");
        System.out.println("=======================================================\n");
    }
}
