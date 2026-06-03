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

        // 0. Preparação: Garantir categoria para o produto
        System.out.println("\n⚙️ [PREPARAÇÃO] Criando categoria de teste...");
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria Temp Teste");
        categoria.setDescricao("Categoria temporária para testes automatizados");
        categoria = categoriaRepository.save(categoria);
        assertNotNull(categoria.getId(), "Categoria deveria ter um ID gerado pelo banco.");
        System.out.println("✅ Categoria criada com sucesso no MySQL! ID: " + categoria.getId());

        // 1. CREATE (Insert)
        System.out.println("\n➕ [1. CREATE] Cadastrando produto fictício...");
        Produto novoProduto = new Produto();
        novoProduto.setNome("Jogo Teste Automatizado");
        novoProduto.setDescricao("Um produto temporário inserido para validar a conexão do banco de dados Byteloja");
        novoProduto.setPreco(new BigDecimal("99.99"));
        novoProduto.setEstoque(5);
        novoProduto.setCategoria(categoria);

        novoProduto = produtoRepository.save(novoProduto);
        Long produtoId = novoProduto.getId();
        assertNotNull(produtoId, "O produto deveria ter um ID gerado pelo banco.");
        System.out.println("✅ Produto cadastrado com sucesso no MySQL! ID: " + produtoId);

        // 2. READ (Select)
        System.out.println("\n🔍 [2. READ] Buscando o produto recém-criado...");
        Optional<Produto> produtoBuscadoOpt = produtoRepository.findById(produtoId);
        assertTrue(produtoBuscadoOpt.isPresent(), "O produto deveria ter sido encontrado no banco.");
        
        Produto produtoBuscado = produtoBuscadoOpt.get();
        System.out.println("✅ Produto localizado no MySQL com sucesso!");
        System.out.println("   👉 Nome: " + produtoBuscado.getNome());
        System.out.println("   👉 Preço original: R$ " + produtoBuscado.getPreco());
        System.out.println("   👉 Estoque: " + produtoBuscado.getEstoque());

        // 3. UPDATE (Alterar preço)
        System.out.println("\n🔄 [3. UPDATE] Atualizando o preço do produto...");
        BigDecimal novoPreco = new BigDecimal("129.90");
        produtoBuscado.setPreco(novoPreco);
        produtoBuscado = produtoRepository.save(produtoBuscado);
        
        assertEquals(novoPreco, produtoBuscado.getPreco(), "O preço deveria ter sido atualizado para 129.90.");
        System.out.println("✅ Preço do produto atualizado com sucesso no MySQL!");
        System.out.println("   👉 Novo Preço: R$ " + produtoBuscado.getPreco());

        // 4. DELETE (Remover do banco)
        System.out.println("\n❌ [4. DELETE] Removendo o produto de teste...");
        produtoRepository.deleteById(produtoId);
        
        Optional<Produto> produtoDeletadoOpt = produtoRepository.findById(produtoId);
        assertFalse(produtoDeletadoOpt.isPresent(), "O produto não deveria mais existir no banco.");
        System.out.println("✅ Produto removido com sucesso do MySQL!");

        // Limpeza da categoria temporária
        categoriaRepository.deleteById(categoria.getId());
        System.out.println("🧹 Categoria temporária limpa com sucesso!");

        System.out.println("\n=======================================================");
        System.out.println("🎉 FIM DO TESTE - CONEXÃO COM O BANCO 'Byteloja' 100% OK!");
        System.out.println("=======================================================\n");
    }
}
