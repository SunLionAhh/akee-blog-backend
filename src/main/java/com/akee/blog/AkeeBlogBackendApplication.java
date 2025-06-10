package com.akee.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.akee.blog.mapper")
public class AkeeBlogBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkeeBlogBackendApplication.class, args);
	}

}
