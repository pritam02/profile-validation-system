package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;
import net.intuit.profilevalidation.models.ProductValidation;

import java.util.List;

@Getter
@Setter
public class ProductValidationResponseData {
    private List<ProductValidation> validationStatus;
}
