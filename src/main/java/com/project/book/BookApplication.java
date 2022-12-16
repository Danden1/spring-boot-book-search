package com.project.book;

import com.project.book.api.KakaoMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
@EnableAsync
public class BookApplication {

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		return modelMapper;
	}

	@Bean
	public BlockingQueue<String> blockingQueue(){
		return new LinkedBlockingQueue<>();
	}

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

}
