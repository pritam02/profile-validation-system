package net.intuit.profilevalidation.controllers;

import net.intuit.profilevalidation.constants.ResponseStatus;
import net.intuit.profilevalidation.helpers.ProfileHelper;
import net.intuit.profilevalidation.models.Profile;
import net.intuit.profilevalidation.models.response.ProfileResponse;
import net.intuit.profilevalidation.models.response.ProfileResponseData;
import net.intuit.profilevalidation.models.response.ProfileUpdateResponse;
import net.intuit.profilevalidation.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileHelper profileHelper;

    @RequestMapping(value = "/api/v1/profile", method = RequestMethod.POST)
    public ResponseEntity<ProfileUpdateResponse> createProfile(@RequestBody Profile businessProfile) {
        ProfileUpdateResponse profileUpdateResponse = new ProfileUpdateResponse();
        profileUpdateResponse.setStatusCode(HttpStatus.OK.value());
        boolean success = false;
        if (profileHelper.isValidBusinessProfile(businessProfile)) {
            success = profileService.createProfile(businessProfile);
        }
        if (success == true) {
            profileUpdateResponse.setStatus(ResponseStatus.SUCCESS);
        } else {
            profileUpdateResponse.setStatus(ResponseStatus.FAILED);
        }
        return new ResponseEntity<>(profileUpdateResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/profile", method = RequestMethod.PUT)
    public ResponseEntity<ProfileUpdateResponse> updateProfile(@RequestBody Profile businessProfile) {
        ProfileUpdateResponse profileUpdateResponse = new ProfileUpdateResponse();
        profileUpdateResponse.setStatusCode(HttpStatus.OK.value());
        boolean success = false;
        if (profileHelper.isValidBusinessProfile(businessProfile)) {
            success = profileService.updateProfile(businessProfile);
        }
        if (success == true) {
            profileUpdateResponse.setStatus(ResponseStatus.SUCCESS);
        } else {
            profileUpdateResponse.setStatus(ResponseStatus.FAILED);
        }
        return new ResponseEntity<>(profileUpdateResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/profile/{email}", method = RequestMethod.GET)
    public ResponseEntity<ProfileResponse> getProfileByUser(@PathVariable String email) {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setStatusCode(HttpStatus.OK.value());
        Profile businessProfile = profileService.getBusinessProfile(email);
        Map<String, Profile> profileMap = new HashMap<>();
        if (businessProfile == null) {
            profileResponse.setStatus(ResponseStatus.FAILED);
        } else {
            profileResponse.setStatus(ResponseStatus.SUCCESS);
            profileMap.put(businessProfile.getEmail(), businessProfile);
        }
        ProfileResponseData profileResponseData = new ProfileResponseData();
        profileResponseData.setProfiles(profileMap);
        profileResponse.setData(profileResponseData);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/profile", method = RequestMethod.GET)
    public ResponseEntity<ProfileResponse> getAllProfiles() {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setStatusCode(HttpStatus.OK.value());
        profileResponse.setStatus(ResponseStatus.SUCCESS);
        List<Profile> businessProfiles = profileService.getAllBusinessProfiles();
        Map<String, Profile> profileMap = new HashMap<>();
        for (Profile businessProfile: businessProfiles) {
            profileMap.put(businessProfile.getEmail(), businessProfile);
        }
        ProfileResponseData profileResponseData = new ProfileResponseData();
        profileResponseData.setProfiles(profileMap);
        profileResponse.setData(profileResponseData);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
}
