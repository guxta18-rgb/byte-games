package loja.bytegames.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * CONTEXTO DESTA CLASSE:
 * Esta classe de configuração (WebConfig.java) estende WebMvcConfigurer para registrar manipuladores 
 * de recursos dinâmicos. Ela permite que arquivos salvos em uma pasta física externa (neste caso, 
 * a pasta 'uploads/' na raiz do projeto) possam ser acessados diretamente pelo navegador via URL, 
 * de forma similar aos arquivos estáticos que ficam dentro de resources/static/.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Define o caminho absoluto para a pasta uploads/ na raiz do projeto
        Path uploadDir = Paths.get("./uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        
        // Mapeia todas as requisições que começam com "/uploads/**" para a pasta física
        // Nota: A barra final em 'file:/...' é importante para que o Spring resolva os subdiretórios corretamente
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}
