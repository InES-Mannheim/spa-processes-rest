package de.unimannheim.spa.process.rest;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.unimannheim.spa.process.config.InMemoryConfig;
import de.unimannheim.spa.process.persistence.ProjectRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@SpringApplicationConfiguration(classes = InMemoryConfig.class)
@WebAppConfiguration
public class ProjectRestControllerTest {
  
    private final String PROJECT_ID_TO_TEST = "dummyProjectID1";
    private final String NON_EXISTENT_PROJECT_ID_TO_TEST = "nonExistentProjectID";
  
    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));
    
    private MediaType octetStreamContentType = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType(), 
                                                             MediaType.APPLICATION_OCTET_STREAM.getSubtype(),
                                                             Charset.forName("utf8"));
    
    private MockMvc mockMvc;
    
    @Autowired
    private ProjectRepository projectRepository;
  
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void projectRestcontrollerShouldReturn6Projects() throws Exception{
        mockMvc.perform(get("/projects"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$", hasSize(5)));
    }
    
    @Test
    public void itShouldReturn6ProcessesOfProjectID() throws Exception{
        mockMvc.perform(get("/projects/"+PROJECT_ID_TO_TEST))
               .andExpect(status().isOk())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$", hasSize(5)));
    }
    
    @Test
    public void itShouldReturn0ProcessesOfNonExistentProjectID() throws Exception{
        mockMvc.perform(get("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST))
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProject() throws Exception{
        mockMvc.perform(post("/projects"))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.id", containsString("Project")))
               .andExpect(jsonPath("$.label", containsString("-label")))
               .andExpect(jsonPath("$.processNumber", equalTo(0)));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProcessWithFile() throws Exception{
        final String processIDToTest = "newProcessIDToTest";
        final String processLabelToTest = "newProcessLabelToTest";
        final String processURIToTest = "/projects/"+PROJECT_ID_TO_TEST+"/processes/"+processIDToTest;
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", new String("Test").getBytes());
        mockMvc.perform(fileUpload("/projects/"+PROJECT_ID_TO_TEST).file(processFileToTest)
                                                                   .param("processID", processIDToTest)
                                                                   .param("processLabel", processLabelToTest))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.id", equalTo(processIDToTest)))
               .andExpect(jsonPath("$.label", equalTo(processLabelToTest)))
               .andExpect(jsonPath("$.uri", equalTo(processURIToTest)));
    }
    
    @Test
    public void itShouldReturnTheFileOfTheProcess() throws Exception{
        final String processIDToTest = "ProcessIDTest";
        mockMvc.perform(get("/projects/"+PROJECT_ID_TO_TEST+"/processes/"+processIDToTest))
               .andExpect(status().isOk())
               .andExpect(content().contentType(octetStreamContentType));
    }
    
    @After
    public void reInitializeRepository(){
        projectRepository.deleteAll();
    }
  
}
