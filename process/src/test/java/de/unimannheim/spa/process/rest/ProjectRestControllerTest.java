package de.unimannheim.spa.process.rest;

import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.unimannheim.spa.process.config.InMemoryConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@SpringApplicationConfiguration(classes = InMemoryConfig.class)
@WebAppConfiguration
public class ProjectRestControllerTest {
  
    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void itShouldCreateAndReturnNewProject() throws Exception{
        mockMvc.perform(post("/projects").param("projectLabel", "ProjectLabel"))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.id", isA(String.class)))
               .andExpect(jsonPath("$.label", containsString("ProjectLabel")));
    }
    
}
