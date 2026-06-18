package loja.bytegames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Essa anotação diz ao Java que este é um projeto Spring Boot
@SpringBootApplication 
public class BytegamesApplication {

    // Método main: é o ponto de partida onde o sistema começa a rodar
    public static void main(String[] args) {
        // Inicia o servidor do Spring Boot
        SpringApplication.run(BytegamesApplication.class, args);
    }
}
