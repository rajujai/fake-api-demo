package com.prospecta.demo.services;

import com.prospecta.demo.dtos.Product;
import com.prospecta.demo.exceptions.ProductException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductsService {

    private final String BASE_URL = "https://fakestoreapi.com/products";

    private final RestTemplate REST_TEMPLATE;

    public ProductsService(RestTemplate restTemplate) {
        REST_TEMPLATE = restTemplate;
    }

    private <T> HttpEntity<T> getHeaders(T obj) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(obj, headers);
    }

    public List<Product> getProductsByCategory(final String category) throws ProductException {
        final ResponseEntity<List<Product>> res = REST_TEMPLATE.exchange(BASE_URL + "/category/" + category, HttpMethod.GET, getHeaders(null), new ParameterizedTypeReference<List<Product>>() {
        });
        if (HttpStatus.OK.isSameCodeAs(res.getStatusCode())) return res.getBody();
        else throw new ProductException("Error while fetching products by category : " + category);
    }

    public Product addProduct(final Product newProduct) throws ProductException {
        final ResponseEntity<Product> res = REST_TEMPLATE.exchange(BASE_URL, HttpMethod.POST, getHeaders(newProduct), new ParameterizedTypeReference<Product>() {
        });
        if (HttpStatus.OK.isSameCodeAs(res.getStatusCode())) return res.getBody();
        else throw new ProductException("Error while adding product : " + newProduct.getTitle());
    }

    public List<String> getProductCategories() {
        final ResponseEntity<List<String>> res = REST_TEMPLATE.exchange(BASE_URL + "/categories", HttpMethod.GET, getHeaders(null), new ParameterizedTypeReference<List<String>>() {
        });
        return res.getBody();
    }
}
