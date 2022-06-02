package net.intuit.profilevalidation.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductValidation {
    private int productId;
    private String productName;
    private String validationStatus;
}
