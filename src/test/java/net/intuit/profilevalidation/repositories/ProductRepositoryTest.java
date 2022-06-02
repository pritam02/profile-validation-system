package net.intuit.profilevalidation.repositories;

import net.intuit.profilevalidation.models.Product;
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
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
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
    public void productTest() {
        boolean success1 = productRepository.insertProduct("TestProduct1");
        boolean success2 = productRepository.insertProduct("TestProduct2");
        boolean success3 = productRepository.insertProduct("TestProduct3");
        Assert.assertTrue(success1);
        Assert.assertTrue(success2);
        List<Product> products = productRepository.getAllProducts();
        Assert.assertEquals(products.size(), 3);
        Product product1 = productRepository.getProductById(1);
        Product product2 = productRepository.getProductById(2);
        Assert.assertEquals(product1.getName(), "TestProduct1");
        Assert.assertEquals(product2.getName(), "TestProduct2");
    }
}
