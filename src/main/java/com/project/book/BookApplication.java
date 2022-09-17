package com.project.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



import org.modelmapper.ModelMapper;

@SpringBootApplication
// @RequiredArgsConstructor
public class BookApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

}
