package net.intuit.profilevalidation.services;

import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.models.Product;
import net.intuit.profilevalidation.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public boolean insertProduct(String productName) {
        return productRepository.insertProduct(productName);
    }
    public Product getProductById(int productId) {
        return productRepository.getProductById(productId);
    }
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}
