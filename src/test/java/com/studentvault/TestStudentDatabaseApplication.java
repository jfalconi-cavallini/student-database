package com.studentvault;

import org.springframework.boot.SpringApplication;

public class TestStudentDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.from(StudentDatabaseApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
