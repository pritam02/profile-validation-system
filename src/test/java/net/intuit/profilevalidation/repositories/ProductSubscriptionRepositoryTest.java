package net.intuit.profilevalidation.repositories;

import net.intuit.profilevalidation.models.Product;
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
public class ProductSubscriptionRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSubscriptionRepository productSubscriptionRepository;
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
    public void profileSubscriptionTest() {
        Profile businessProfile1 = new Profile();
        businessProfile1.setEmail("sourav.da@gmail.com");
        businessProfile1.setCompanyName("Dasgupta Shoe Shop");
        businessProfile1.setLegalName("Sourav Dasgupta");
        businessProfile1.setBusinessAddress("989, NSC bose Road, Garia, Kolkata - 700045");
        businessProfile1.setLegalAddress("993, NSC bose Road, Garia, Kolkata - 700045");
        businessProfile1.setTaxIdentifiers("BIFJUP16HH");
        businessProfile1.setWebsite("www.dasshoestore.in");

        boolean success1 = profileRepository.createProfile(businessProfile1);
        Assert.assertTrue(success1);
        List<Profile> profiles = profileRepository.getAllBusinessProfiles();
        Assert.assertEquals(profiles.size(), 1);
        Profile profile1 = profileRepository.getBusinessProfile("sourav.da@gmail.com");
        Assert.assertEquals(profile1.getLegalName(), "Sourav Dasgupta");

        boolean success11 = productRepository.insertProduct("TestProduct1");
        boolean success12 = productRepository.insertProduct("TestProduct2");
        boolean success13 = productRepository.insertProduct("TestProduct23");
        Assert.assertTrue(success11);
        Assert.assertTrue(success12);
        List<Product> products = productRepository.getAllProducts();
        Assert.assertEquals(products.size(), 3);
        Product product1 = productRepository.getProductById(1);
        Product product2 = productRepository.getProductById(2);
        Assert.assertEquals(product1.getName(), "TestProduct1");
        Assert.assertEquals(product2.getName(), "TestProduct2");

        boolean flag1 = productSubscriptionRepository.subscribeToProduct("sourav.da@gmail.com", 1);
        boolean flag3 = productSubscriptionRepository.subscribeToProduct("sourav.da@gmail.com", 3);
        Assert.assertTrue(flag1);
        Assert.assertTrue(flag3);
    }
}
