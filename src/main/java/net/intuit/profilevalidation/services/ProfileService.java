package net.intuit.profilevalidation.services;

import net.intuit.profilevalidation.models.Profile;
import net.intuit.profilevalidation.repositories.ProductValidationRepository;
import net.intuit.profilevalidation.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProductValidationRepository productValidationRepository;
    public boolean createProfile(Profile businessProfile) {
        return profileRepository.createProfile(businessProfile);
    }
    public boolean updateProfile(Profile businessProfile) {
        boolean isUpdateSuccessFull = profileRepository.updateProfile(businessProfile);
        if (isUpdateSuccessFull == false) {
            return false;
        }
        return productValidationRepository.resetValidationOfAllProductsForUser(businessProfile.getEmail());
    }
    public Profile getBusinessProfile(String email) {
        return profileRepository.getBusinessProfile(email);
    }
    public List<Profile> getAllBusinessProfiles() {
        return profileRepository.getAllBusinessProfiles();
    }
}
