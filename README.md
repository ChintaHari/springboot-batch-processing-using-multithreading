# Spring Boot Batch Processing Using Multithreading

## Introduction

This Java-based Spring Boot application is designed to streamline batch processing tasks within a retail environment. It allows for the efficient application of discounts to a list of product IDs based on specific criteria, updates the product details in a database, and subsequently publishes these changes to a Kafka topic for real-time event handling.

## Project Description

### What's This Project About?
In a typical e-commerce or retail setting, adjusting prices based on promotions, seasonal discounts, or stock levels is a common but resource-intensive task, especially when dealing with large volumes of products. This application tackles this challenge by automating the discount application process, ensuring that product pricing is dynamically adjusted and consistent across all platforms.

### Real-Time Example
Imagine an online store preparing for a Black Friday sale. They plan to offer a 10% discount on all electronics that cost more than $1000 and a 5% discount on those priced above $500 but below $1000. Manually adjusting the prices of thousands of products could lead to errors and inconsistencies. This application automates this process: it receives a list of product IDs, checks each product’s price, applies the appropriate discount, saves the updated prices back to the database, and pushes these changes to a Kafka topic. This setup ensures that all downstream systems get updated in real-time about the price changes, enabling a consistent shopping experience during high-traffic sales events.

### How Are We Doing It?
The application uses three different approaches to process the batch updates:
1. **Using Streams**: Processes the list of product IDs sequentially.
2. **Using Parallel Streams**: Leverages Java’s parallel streams to take advantage of multicore processors for faster processing.
3. **Using CompletableFuture**: Implements multithreading by creating batches of product IDs and processing each batch in parallel, which can be more efficient in systems where lots of I/O operations are involved.

Each method has its own endpoint, allowing the application to be tested under different processing scenarios to understand which method performs best under various conditions.

### Purpose of This Project
The primary goal of this project is to demonstrate how different Java concurrency models can be applied to real-world batch processing tasks in a Spring Boot application. It provides a practical example of how modern Java features can be used to improve the performance and scalability of backend systems in retail and e-commerce operations.
