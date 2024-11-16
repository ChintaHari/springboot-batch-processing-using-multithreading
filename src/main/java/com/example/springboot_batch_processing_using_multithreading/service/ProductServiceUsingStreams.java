package com.example.springboot_batch_processing_using_multithreading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_batch_processing_using_multithreading.entity.Product;
import com.example.springboot_batch_processing_using_multithreading.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceUsingStreams {
    
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
    public void processUsingStreams(List<Long> productIds) {
        productIds.stream().forEach(this::fetchUpdateAndPublish);
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
        
}
