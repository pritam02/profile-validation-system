package net.intuit.profilevalidation.controllers;

import net.intuit.profilevalidation.constants.ResponseStatus;
import net.intuit.profilevalidation.models.Product;
import net.intuit.profilevalidation.models.request.ProductCreateRequest;
import net.intuit.profilevalidation.models.response.*;
import net.intuit.profilevalidation.services.ProductService;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @RequestMapping(value = "/api/v1/product", method = RequestMethod.POST)
    public ResponseEntity<CommonHttpPostResponse> addProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        CommonHttpPostResponse commonHttpPostResponse = new CommonHttpPostResponse();
        commonHttpPostResponse.setStatusCode(HttpStatus.OK.value());
        boolean success = false;
        if (Util.isValidString(productCreateRequest.getName())) {
            success = productService.insertProduct(productCreateRequest.getName());
        }
        if (success == true) {
            commonHttpPostResponse.setStatus(ResponseStatus.SUCCESS);
        } else {
            commonHttpPostResponse.setStatus(ResponseStatus.FAILED);
        }
        return new ResponseEntity<>(commonHttpPostResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int productId) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setStatusCode(HttpStatus.OK.value());
        Product product = productService.getProductById(productId);
        Map<String, Product> productMap = new HashMap<>();
        if (product == null) {
            productResponse.setStatus(ResponseStatus.FAILED);
        } else {
            productResponse.setStatus(ResponseStatus.SUCCESS);
            productMap.put(String.valueOf(product.getId()), product);
        }
        ProductResponseData productResponseData = new ProductResponseData();
        productResponseData.setProducts(productMap);
        productResponse.setData(productResponseData);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/product", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getAllProfiles() {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setStatusCode(HttpStatus.OK.value());
        productResponse.setStatus(ResponseStatus.SUCCESS);
        List<Product> products = productService.getAllProducts();
        Map<String, Product> productMap = new HashMap<>();
        for (Product product: products) {
            productMap.put(String.valueOf(product.getId()), product);
        }
        ProductResponseData productResponseData = new ProductResponseData();
        productResponseData.setProducts(productMap);
        productResponse.setData(productResponseData);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
