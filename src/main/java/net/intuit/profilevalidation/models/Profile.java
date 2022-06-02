package net.intuit.profilevalidation.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private String email;
    private String companyName;
    private String legalName;
    private String businessAddress;
    private String legalAddress;
    private String taxIdentifiers;
    private String website;
}
