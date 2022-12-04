package com.project.book;

import com.project.book.api.KakaoMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



import org.modelmapper.ModelMapper;

@SpringBootApplication
// @RequiredArgsConstructor
public class BookApplication {

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

}
