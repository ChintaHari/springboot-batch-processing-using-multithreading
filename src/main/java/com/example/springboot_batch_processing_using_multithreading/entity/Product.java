package com.example.springboot_batch_processing_using_multithreading.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Long id;
    private String name;
    private String category;
    private double price;
    @Column(name = "isOfferApplied")
    private boolean isOfferApplied;
    @Column(name = "discountPercentage")
    private double discountPercentage;
    @Column(name = "priceAfterDiscount")
    private double priceAfterDiscount;
    
}
