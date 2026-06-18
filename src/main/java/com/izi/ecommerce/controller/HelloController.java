package com.izi.ecommerce.controller;

import com.izi.ecommerce.common.errors.BadRequestException;
import com.izi.ecommerce.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	// localhost:8080/hello
	@GetMapping("/hello")
	public String hello() {
		return "Hello world";
	}

	// localhost:8080/bad-request
	@GetMapping("/bad-request")
	public ResponseEntity<String> badRequestError() {
		throw new BadRequestException("ada error bad request");
	}

	// localhost:8080/generic-error
	@GetMapping("/generic-error")
	public ResponseEntity<String> genericError() {
		throw new RuntimeException("generic error");
	}

	// localhost:8080/not-found
	@GetMapping("/not-found")
	public ResponseEntity<String> notFoundError() {
		throw new ResourceNotFoundException("data tidak ditemukan");
	}
}