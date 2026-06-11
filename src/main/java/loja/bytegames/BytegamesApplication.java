package loja.bytegames;

// Importação das classes do Spring Boot que iniciam a aplicação
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * CONTEXTO DESTA CLASSE:
 * Este é o ponto de partida (Entry Point) de toda a aplicação Spring Boot.
 * É a classe que contém o método 'main', que é executado primeiro quando iniciamos o projeto.
 * A partir daqui, o Spring Boot inicializa o servidor web embutido (Tomcat), faz o escaneamento 
 * dos componentes (controllers, repositories, etc.) e configura o acesso ao banco de dados.
 */

@SpringBootApplication // Configura automaticamente o Spring, habilita o escaneamento de pacotes e a autoconfiguração
public class BytegamesApplication {

    // Método main: O ponto de partida oficial de execução do Java
    public static void main(String[] args) {
        // Inicializa a aplicação Spring Boot e liga o servidor interno na porta padrão (8080)
        SpringApplication.run(BytegamesApplication.class, args);
    }
}

