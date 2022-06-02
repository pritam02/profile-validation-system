package net.intuit.profilevalidation.repositories;

import net.intuit.profilevalidation.models.Profile;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;
    private static RedisServer redisServer;
    @BeforeClass
    public static void setup() {
        try {
            redisServer = new RedisServer(6370);
            redisServer.start();
        } catch (Exception e) {
            redisServer = null;
        }
    }

    @AfterClass
    public static void tearDown() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void profileTest() {
        Profile businessProfile1 = new Profile();
        businessProfile1.setEmail("sourav.da@gmail.com");
        businessProfile1.setCompanyName("Dasgupta Shoe Shop");
        businessProfile1.setLegalName("Sourav Dasgupta");
        businessProfile1.setBusinessAddress("989, NSC bose Road, Garia, Kolkata - 700045");
        businessProfile1.setLegalAddress("993, NSC bose Road, Garia, Kolkata - 700045");
        businessProfile1.setTaxIdentifiers("BIFJUP16HH");
        businessProfile1.setWebsite("www.dasshoestore.in");

        Profile businessProfile2 = new Profile();
        businessProfile2.setEmail("pritamairtel@gmail.com");
        businessProfile2.setCompanyName("Pal Pan shop");
        businessProfile2.setLegalName("Pritam Pal");
        businessProfile2.setBusinessAddress("98, Ashutosh Mukherjee Road, Bhowanipore, Kolkata - 700025");
        businessProfile2.setLegalAddress("13, Girish Mukherjee Road, Kolkata - 700025");
        businessProfile2.setTaxIdentifiers("BIFPP09HH");
        businessProfile2.setWebsite("www.pritampanshop.in");

        boolean success1 = profileRepository.createProfile(businessProfile1);
        boolean success2 = profileRepository.createProfile(businessProfile2);
        Assert.assertTrue(success1);
        Assert.assertTrue(success2);
        List<Profile> profiles = profileRepository.getAllBusinessProfiles();
        Assert.assertEquals(profiles.size(), 2);
        Profile profile1 = profileRepository.getBusinessProfile("sourav.da@gmail.com");
        Profile profile2 = profileRepository.getBusinessProfile("pritamairtel@gmail.com");
        Assert.assertEquals(profile1.getLegalName(), "Sourav Dasgupta");
        Assert.assertEquals(profile2.getLegalName(), "Pritam Pal");
    }
}
