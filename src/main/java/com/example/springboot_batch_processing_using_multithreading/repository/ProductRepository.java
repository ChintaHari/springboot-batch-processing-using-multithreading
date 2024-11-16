package com.example.springboot_batch_processing_using_multithreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot_batch_processing_using_multithreading.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
