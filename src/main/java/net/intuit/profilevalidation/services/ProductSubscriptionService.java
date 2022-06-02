package net.intuit.profilevalidation.services;

import net.intuit.profilevalidation.repositories.ProductSubscriptionRepository;
import net.intuit.profilevalidation.repositories.ProductValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSubscriptionService {
    @Autowired
    private ProductSubscriptionRepository productSubscriptionRepository;
    @Autowired
    private ProductValidationRepository productValidationRepository;
    public boolean subscribeToProduct(String userEmail, int productId) {
        boolean isSubscriptionSuccessFull = productSubscriptionRepository.subscribeToProduct(userEmail, productId);
        if (isSubscriptionSuccessFull == false) {
            return false;
        }
        boolean isValidationInitialised = productValidationRepository.initialiseValidationForUserAndProduct(userEmail, productId);
        return isValidationInitialised;
    }
}
