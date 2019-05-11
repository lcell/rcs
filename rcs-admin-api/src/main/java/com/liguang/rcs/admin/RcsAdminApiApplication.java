package com.liguang.rcs.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages= { "com.liguang.rcs.db", "com.liguang.rcs.admin"})
@MapperScan("com.liguang.rcs.db.dao")
@EnableScheduling
@EnableTransactionManagement
public class RcsAdminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RcsAdminApiApplication.class, args);
	}

}
