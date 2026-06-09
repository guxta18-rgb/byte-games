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
        
        /* 
         * SE O PROFESSOR PEDIR PARA MUDAR A PORTA DO SERVIDOR (ex: de 8080 para 8081):
         * Explique que isso é feito no arquivo 'src/main/resources/application.properties' 
         * adicionando a linha: server.port=8081
         * 
         * SE O PROFESSOR PEDIR PARA MUDAR O IDIOMA/FUSO HORÁRIO PADRÃO DO APP NA INICIALIZAÇÃO:
         * Você pode configurar no início do método main antes do run:
         *    java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("America/Sao_Paulo"));
         *    java.util.Locale.setDefault(new java.util.Locale("pt", "BR"));
         */
    }
}

