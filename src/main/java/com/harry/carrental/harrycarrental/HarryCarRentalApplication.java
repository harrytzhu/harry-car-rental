package com.harry.carrental.harrycarrental;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by harryzhu on 2022/7/24
 */
@SpringBootApplication
@MapperScan(value = "com.harry.carrental.harrycarrental.mapper")
public class HarryCarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarryCarRentalApplication.class, args);
	}

}
