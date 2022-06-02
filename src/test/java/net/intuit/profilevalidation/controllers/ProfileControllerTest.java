package net.intuit.profilevalidation.controllers;

import net.intuit.profilevalidation.models.Profile;
import net.intuit.profilevalidation.utils.Util;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import redis.embedded.RedisServer;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProfileControllerTest {
    @Autowired
    private MockMvc mvc;
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
    public void profileControllerTest() {
        Profile businessProfile = new Profile();
        businessProfile.setEmail("pritamairtel@gmail.com");
        businessProfile.setCompanyName("Pal Pan shop");
        businessProfile.setLegalName("Pritam Pal");
        businessProfile.setBusinessAddress("98, Ashutosh Mukherjee Road, Bhowanipore, Kolkata - 700025");
        businessProfile.setLegalAddress("13, Girish Mukherjee Road, Kolkata - 700025");
        businessProfile.setTaxIdentifiers("BIFPP09HH");
        businessProfile.setWebsite("www.pritampanshop.in");
        try {
            mvc.perform(MockMvcRequestBuilders
                            .post("/api/v1/profile")
                            .content(Util.getStringFromJson(businessProfile))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").exists());
        } catch (Exception e) {

        }


        try {
            mvc.perform( MockMvcRequestBuilders
                            .get("/api/v1/profile/{email}", "pritamairtel@gmail.com"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.email").value(businessProfile.getEmail()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.companyName").value(businessProfile.getCompanyName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.legalName").value(businessProfile.getLegalName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.businessAddress").value(businessProfile.getBusinessAddress()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.legalAddress").value(businessProfile.getLegalAddress()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.profiles.*.taxIdentifiers").value(businessProfile.getTaxIdentifiers()));
        } catch (Exception e) {

        }
    }
}
