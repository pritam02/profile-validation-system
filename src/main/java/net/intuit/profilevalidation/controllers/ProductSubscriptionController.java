package net.intuit.profilevalidation.controllers;

import net.intuit.profilevalidation.constants.ResponseStatus;
import net.intuit.profilevalidation.models.request.ProductSubscribeRequest;
import net.intuit.profilevalidation.models.response.CommonHttpPostResponse;
import net.intuit.profilevalidation.services.ProductSubscriptionService;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSubscriptionController {
    @Autowired
    private ProductSubscriptionService productSubscriptionService;
    @RequestMapping(value = "/api/v1/product/subscribe", method = RequestMethod.POST)
    public ResponseEntity<CommonHttpPostResponse> subscribeToProduct(@RequestBody ProductSubscribeRequest productSubscribeRequest) {
        CommonHttpPostResponse commonHttpPostResponse = new CommonHttpPostResponse();
        commonHttpPostResponse.setStatusCode(HttpStatus.OK.value());
        boolean success = false;
        if (Util.isValidString(productSubscribeRequest.getUserId()) && productSubscribeRequest.getProductId() > 0) {
            success = productSubscriptionService.subscribeToProduct(productSubscribeRequest.getUserId(), productSubscribeRequest.getProductId());
        }
        if (success == true) {
            commonHttpPostResponse.setStatus(ResponseStatus.SUCCESS);
        } else {
            commonHttpPostResponse.setStatus(ResponseStatus.FAILED);
        }
        return new ResponseEntity<>(commonHttpPostResponse, HttpStatus.OK);
    }
}
