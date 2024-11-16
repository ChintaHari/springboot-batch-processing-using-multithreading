package com.example.springboot_batch_processing_using_multithreading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_batch_processing_using_multithreading.service.ProductServiceUsingStreams;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductServiceUsingStreams productServiceUsingStreams;

    @GetMapping("/reset")
    public String resetDB() {
        return productServiceUsingStreams.resetDB();
    }

    @GetMapping("/process/using-streams")
    public String processUsingStreams(@RequestBody List<Long> productIds) {
        productServiceUsingStreams.processUsingStreams(productIds);
        return "Processing completed";
    }
}
