package com.example.springboot_batch_processing_using_multithreading.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_batch_processing_using_multithreading.entity.Product;
import com.example.springboot_batch_processing_using_multithreading.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceUsingMultithreadingAndCompletableFuture {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${product.discount.update.topic}")
    private String topicName;

     public String resetDB(){
        productRepository.findAll().forEach(product -> {
            product.setDiscountPercentage(0);
            product.setPriceAfterDiscount(product.getPrice());
            product.setOfferApplied(false);
            productRepository.save(product);
        });
        return "DB reset successfully";
    }

    @Transactional
    public void processUsingMultithreadingAndCompletableFuture(List<Long> productIds) {
        List<List<Long>> batches = splitIntoBatch(productIds, 50);
        List<CompletableFuture<Void>> futures = batches.stream()
                .map(batch -> CompletableFuture.runAsync(() -> processProductIds(batch)))
                .collect(Collectors.toList());
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }
       
    private void processProductIds(List<Long> batch) {
        System.out.println("Processing batch " + batch + " by thread " + Thread.currentThread().getName());
        batch.forEach(this::fetchUpdateAndPublish);
    }
          
        
    private void fetchUpdateAndPublish(Long productId) {
        Product product = productRepository.findById(productId)
                                            .orElseThrow(() -> new RuntimeException("Product not found"));

        updateDiscountedPrice(product);
        productRepository.save(product);
        publishToKafka(product);
    }

    private void updateDiscountedPrice(Product product) {
        double price = product.getPrice();
        int discountPercentage = (price >= 1000) ? 10 : (price > 500 ? 5 : 0);
        double priceAfterDiscount = price - (price * discountPercentage / 100);
        if (discountPercentage > 0) {
            product.setOfferApplied(true);
        }
        product.setDiscountPercentage(discountPercentage);
        product.setPriceAfterDiscount(priceAfterDiscount);
    }

    private void publishToKafka(Product product) {
        try {
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(product));
        } catch (Exception e) {
            throw new RuntimeException("Error while publishing to Kafka", e);
        }
    }

    private List<List<Long>> splitIntoBatch(List<Long> productIds, int batchSize) {
        List<List<Long>> batches = new ArrayList<>();
        int totalSize = productIds.size();
        int batchCount = totalSize / batchSize;
        for(int i = 0; i < batchCount; i++) {
            int fromIndex = i * batchSize;
            int toIndex = fromIndex + batchSize;
            List<Long> batch = productIds.subList(fromIndex, toIndex);
            batches.add(batch);
        }
        return batches;
    }
    
}
