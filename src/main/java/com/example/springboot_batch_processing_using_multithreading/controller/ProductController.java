package com.example.springboot_batch_processing_using_multithreading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_batch_processing_using_multithreading.service.ProductServiceUsingMultithreadingAndCompletableFuture;
import com.example.springboot_batch_processing_using_multithreading.service.ProductServiceUsingParallelStreams;
import com.example.springboot_batch_processing_using_multithreading.service.ProductServiceUsingStreams;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductServiceUsingStreams productServiceUsingStreams;

    @Autowired
    private ProductServiceUsingParallelStreams productServiceUsingParallelStreams;

    @Autowired
    private ProductServiceUsingMultithreadingAndCompletableFuture productServiceUsingMultithreadingAndCompletableFuture;

    @PostMapping("/reset")
    public String resetDB() {
        return productServiceUsingStreams.resetDB();
    }

    @PostMapping("/process/using-streams")
    public String processUsingStreams(@RequestBody List<Long> productIds) {
        productServiceUsingStreams.processUsingStreams(productIds);
        return "Processing completed using streams";
    }

    @PostMapping("/process/using-parallel-streams")
    public String processUsingParallelStreams(@RequestBody List<Long> productIds) {
        productServiceUsingParallelStreams.processUsingParallelStreams(productIds);
        return "Processing completed using parallel streams";
    }

    @PostMapping("/process/using-multithreading-and-completable-future")
    public String processUsingMultithreadingAndCompletableFuture(@RequestBody List<Long> productIds) {
        productServiceUsingMultithreadingAndCompletableFuture.processUsingMultithreadingAndCompletableFuture(productIds);
        return "Processing completed using multithreading and completable future";
    }
}
