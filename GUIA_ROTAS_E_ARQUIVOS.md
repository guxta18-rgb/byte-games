# Guia de Rotas e Manipulação de Arquivos no Spring Boot

Este guia prático explica como criar rotas de navegação (routing), fazer upload de arquivos (enviar arquivos) e download de arquivos (retornar arquivos) no Spring Boot utilizando Thymeleaf.

---

## 1. Criando Rotas (Routing) no Spring Boot

No Spring Boot, as rotas são mapeadas dentro de classes anotadas com `@Controller` (quando retornam páginas HTML) ou `@RestController` (quando retornam JSON ou dados puros).

As anotações principais para mapear URLs são:
*   `@GetMapping`: Usada para buscar dados ou carregar páginas HTML.
*   `@PostMapping`: Usada para submeter formulários ou enviar novos dados.

### Rota Simples (Retornando Página HTML)
Se você tem um arquivo HTML chamado `teste.html` em `src/main/resources/templates/`, a rota para exibi-lo é declarada assim:

```java
package loja.bytegames.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExemploController {

    @GetMapping("/teste")
    public String testarRota(Model model) {
        // Envia uma variável dinâmica para ser exibida no HTML
        model.addAttribute("nomeDoProfessor", "Danilo");
        
        // Retorna o nome do arquivo HTML (sem a extensão .html)
        return "teste"; 
    }
}
```

### Rota com Parâmetro na URL (Caminho Dinâmico)
Para capturar valores variáveis diretamente da URL (como `/produtos/5` ou `/detalhes/10`), utilize `@PathVariable`:

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@GetMapping("/detalhes/{id}")
public String verDetalhes(@PathVariable("id") Long id, Model model) {
    // Exemplo: O 'id' recebido pode ser usado para pesquisar no banco
    System.out.println("ID recebido na rota: " + id);
    return "detalhes";
}
```

---

## 2. Mandando Arquivo (Upload de Arquivo)

Para enviar um arquivo do navegador do usuário para o servidor Java, são necessárias duas partes: um formulário HTML estruturado e um endpoint configurado com `MultipartFile`.

### A. O Formulário no HTML (Thymeleaf)
O atributo mais importante no formulário HTML é o **`enctype="multipart/form-data"`**. Sem ele, o arquivo não será enviado corretamente.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Upload de Arquivos</title>
</head>
<body>
    <h2>Enviar Arquivo para o Servidor</h2>
    
    <form action="/upload" method="POST" enctype="multipart/form-data">
        <div>
            <label for="arquivo">Selecione o arquivo:</label>
            <input type="file" name="arquivo" id="arquivo" required />
        </div>
        <br/>
        <button type="submit">Enviar Arquivo</button>
    </form>
</body>
</html>
```

### B. O Controller no Java (Spring Boot)
No Java, usamos a classe `MultipartFile` para receber e gerenciar o arquivo recebido na requisição.

```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @PostMapping("/upload")
    public String receberArquivo(@RequestParam("arquivo") MultipartFile arquivo, Model model) {
        // Valida se um arquivo foi realmente selecionado
        if (arquivo.isEmpty()) {
            model.addAttribute("mensagem", "Por favor, selecione um arquivo.");
            return "resultado";
        }

        try {
            // Define o diretório na raiz do projeto onde os arquivos serão armazenados
            String pastaDestino = "uploads/";
            Path path = Paths.get(pastaDestino + arquivo.getOriginalFilename());
            
            // Cria a pasta 'uploads/' caso ela não exista no sistema
            Files.createDirectories(path.getParent());
            
            // Grava os bytes do arquivo no caminho de destino
            Files.write(path, arquivo.getBytes());

            model.addAttribute("mensagem", "Arquivo '" + arquivo.getOriginalFilename() + "' enviado com sucesso!");
        } catch (IOException e) {
            model.addAttribute("mensagem", "Erro ao salvar arquivo no servidor: " + e.getMessage());
        }

        return "resultado"; // Carrega a página templates/resultado.html
    }
}
```

---

## 3. Voltar Arquivo (Download de Arquivo)

Se o professor pedir para o servidor retornar um arquivo para download quando o usuário clicar em um botão ou link, você deve construir uma resposta do tipo `ResponseEntity<Resource>` definindo o cabeçalho `CONTENT_DISPOSITION`.

### O Controller para Download no Java

```java
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DownloadController {

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<Resource> baixarArquivo(@PathVariable String nomeArquivo) {
        try {
            // Caminho para o arquivo salvo no disco
            Path path = Paths.get("uploads/").resolve(nomeArquivo);
            Resource resource = new UrlResource(path.toUri());

            // Verifica se o arquivo existe e pode ser lido
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    // Define o header de cabeçalho indicando anexo para download ("attachment")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                // Retorna erro 404 caso o arquivo não exista
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

### Como Chamar o Download no HTML
No seu HTML Thymeleaf, crie uma tag `<a>` referenciando o endpoint com o nome do arquivo dinâmico ou estático:

```html
<!-- Exemplo estático -->
<a href="/download/manual.pdf">Baixar Manual em PDF</a>

<!-- Exemplo dinâmico com Thymeleaf pegando o nome do arquivo de um objeto -->
<a th:href="@{/download/{nome}(nome=${produto.nomeArquivo})}">Baixar Arquivo</a>
```
