package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;
import net.intuit.profilevalidation.models.Profile;

import java.util.Map;

@Getter
@Setter
public class ProfileResponseData {
    Map<String, Profile> profiles;
}
