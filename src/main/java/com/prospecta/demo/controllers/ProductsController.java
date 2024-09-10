package com.prospecta.demo.controllers;

import com.prospecta.demo.dtos.Product;
import com.prospecta.demo.exceptions.ProductException;
import com.prospecta.demo.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService service;

    public ProductsController(ProductsService service) {
        this.service = service;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        try {
            return new ResponseEntity<>(service.getProductsByCategory(category), HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getProductCategories() {
        return new ResponseEntity<>(service.getProductCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct) {
        try {
            return new ResponseEntity<>(service.addProduct(newProduct), HttpStatus.ACCEPTED);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
