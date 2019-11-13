package com.mytool.mytoolbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyToolBackendApplication implements CommandLineRunner {

	@Autowired
	DexiRepository repository;

	@Autowired
	DiagramRepository repository2;

	@Autowired
	ImageRepository repository3;

	public static void main(String[] args) {
		SpringApplication.run(MyToolBackendApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Error, booted ok");
		System.out.println("HELLO WORLD");
		deleteAll();
	}

	public void deleteAll() {
		System.out.println("Deleting all records..");
		repository.deleteAll();
		repository2.deleteAll();
		repository3.deleteAll();
	}

	public void addSampleData() {
		System.out.println("Adding sample data");
		repository.save(new Dexi("1", "Jack Bauer", "New York"));
		repository.save(new Dexi("2", "Harvey Spectre", "London"));
		repository.save(new Dexi("3", "Mike Ross", "New Jersey"));
		repository.save(new Dexi("4", "Louise Litt", "Kathmandu"));
	}
}
