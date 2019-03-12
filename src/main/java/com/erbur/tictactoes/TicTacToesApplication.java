package com.erbur.tictactoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TicTacToesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicTacToesApplication.class, args);
	}

}
