package net.intuit.profilevalidation.helpers;

import net.intuit.profilevalidation.models.Profile;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.stereotype.Component;

@Component
public class ProfileHelper {
    public boolean isValidBusinessProfile(Profile profile) {
        return Util.isValidString(profile.getEmail()) &&
                Util.isValidString(profile.getCompanyName()) &&
                Util.isValidString(profile.getLegalName()) &&
                Util.isValidString(profile.getBusinessAddress()) &&
                Util.isValidString(profile.getLegalAddress()) &&
                Util.isValidString(profile.getTaxIdentifiers());
    }
}
