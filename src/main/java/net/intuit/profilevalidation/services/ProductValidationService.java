package net.intuit.profilevalidation.services;

import net.intuit.profilevalidation.models.ProductValidation;
import net.intuit.profilevalidation.repositories.ProductValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductValidationService {
    @Autowired
    private ProductValidationRepository productValidationRepository;
    public boolean setValidationStatusForUserAndProduct(String userEmail, int product_id, String validationStatus) {
        return productValidationRepository.setValidationStatusForUserAndProduct(userEmail, product_id, validationStatus);
    }
    public List<ProductValidation> getValidationStatusForAllProducts(String userEmail) {
        return productValidationRepository.getValidationStatusForAllProducts(userEmail);
    }
}
