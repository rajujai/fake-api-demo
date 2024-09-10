package com.prospecta.demo.dtos;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;
}
