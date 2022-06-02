package net.intuit.profilevalidation.controllers;

import net.intuit.profilevalidation.constants.ResponseStatus;
import net.intuit.profilevalidation.models.ProductValidation;
import net.intuit.profilevalidation.models.request.ProductValidationRequest;
import net.intuit.profilevalidation.models.response.CommonHttpPostResponse;
import net.intuit.profilevalidation.models.response.ProductValidationResponse;
import net.intuit.profilevalidation.models.response.ProductValidationResponseData;
import net.intuit.profilevalidation.services.ProductValidationService;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductValidationController {
    @Autowired
    private ProductValidationService productValidationService;

    @RequestMapping(value = "/api/v1/product/validation/{email}", method = RequestMethod.GET)
    public ResponseEntity<ProductValidationResponse> getProfileByUser(@PathVariable String email) {
        ProductValidationResponse productValidationResponse = new ProductValidationResponse();
        productValidationResponse.setUserId(email);
        productValidationResponse.setStatusCode(HttpStatus.OK.value());
        productValidationResponse.setStatus(ResponseStatus.SUCCESS);
        List<ProductValidation> validationsForProducts = productValidationService.getValidationStatusForAllProducts(email);
        ProductValidationResponseData productValidationResponseData = new ProductValidationResponseData();
        productValidationResponseData.setValidationStatus(validationsForProducts);
        productValidationResponse.setData(productValidationResponseData);
        return new ResponseEntity<>(productValidationResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/product/validation", method = RequestMethod.PUT)
    public ResponseEntity<CommonHttpPostResponse> setValidationStatusForUserAndProduct(@RequestBody ProductValidationRequest productValidationRequest) {
        CommonHttpPostResponse commonHttpPostResponse = new CommonHttpPostResponse();
        commonHttpPostResponse.setStatusCode(HttpStatus.OK.value());
        boolean success = false;
        if (Util.isValidString(productValidationRequest.getUserId()) && Util.isValidString(productValidationRequest.getStatus()) && productValidationRequest.getProductId() > 0) {
            success = productValidationService.setValidationStatusForUserAndProduct(productValidationRequest.getUserId(), productValidationRequest.getProductId(), productValidationRequest.getStatus());
        }
        if (success == true) {
            commonHttpPostResponse.setStatus(ResponseStatus.SUCCESS);
        } else {
            commonHttpPostResponse.setStatus(ResponseStatus.FAILED);
        }
        return new ResponseEntity<>(commonHttpPostResponse, HttpStatus.OK);
    }
}
