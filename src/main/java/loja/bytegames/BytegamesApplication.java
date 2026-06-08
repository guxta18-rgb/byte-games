package loja.bytegames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Este arquivo representa a classe principal do sistema.
 * É por aqui que o nosso programa começa a rodar. O Java procura esta classe 
 * e o método "main" para dar a partida em todo o servidor da nossa loja.
 */

// A anotação @SpringBootApplication indica para o Java que este é um projeto Spring Boot.
// Ela configura automaticamente o servidor local, conexões de banco e as outras classes do projeto.
@SpringBootApplication
public class BytegamesApplication {

    // O método main é o ponto de entrada oficial de qualquer sistema Java.
    public static void main(String[] args) {
        // Esta linha inicia o servidor da loja e faz o Spring Boot gerenciar as rotas e o banco de dados.
        SpringApplication.run(BytegamesApplication.class, args);
    }
}

