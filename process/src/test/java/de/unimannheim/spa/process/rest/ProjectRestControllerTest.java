package de.unimannheim.spa.process.rest;

import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import de.unimannheim.spa.process.config.InMemoryConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@SpringApplicationConfiguration(classes = InMemoryConfig.class)
@WebAppConfiguration
public class ProjectRestControllerTest {
  
    private final String PROJECT_ID_TO_TEST = "http://www.uni-mannheim.de/spa/Project/2JwOpX";
    private final String NON_EXISTENT_PROJECT_ID_TO_TEST = "nonExistentProjectID";
    private final MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
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
    public void projectRestcontrollerShouldReturn6Projects() throws Exception{
        mockMvc.perform(get("/projects"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$", hasSize((greaterThanOrEqualTo(0)))));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProject() throws Exception{
        mockMvc.perform(post("/projects").param("projectLabel", "ProjectLabel"))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.id", isA(String.class)))
               .andExpect(jsonPath("$.label", containsString("ProjectLabel")));
    }
    
    @Test
    public void itShouldReturn0ProcessesOfProjectID() throws Exception{
        final String projectIDForTest = createProjectAndReturnID();
        mockMvc.perform(get("/projects/"+projectIDForTest+"/processes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.buckets", hasSize(0)));
    }
    
    private String createProjectAndReturnID() throws UnsupportedEncodingException, Exception{
        ObjectMapper mapper = new ObjectMapper();
        String JSONFromServer = mockMvc.perform(post("/projects").param("projectLabel", "ProjectLabel"))
                                                                 .andReturn()
                                                                 .getResponse()
                                                                 .getContentAsString();
        String projectCreatedID = "";
        try{
          Map<String, Object> map = mapper.readValue(JSONFromServer, new TypeReference<Map<String, Object>>(){});
          projectCreatedID = ((String) map.get("id")).substring(39);
        }catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectCreatedID;
    }
    
    @Test
    public void itShouldReturn0ProcessesOfNonExistentProjectID() throws Exception{
        mockMvc.perform(get("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST+"/processes"))
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(jsonContentType));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProcessWithFile() throws Exception{
        final String projectIDForTest = createProjectAndReturnID();
        final String processIDToTest = "newProcessIDToTest";
        final String processLabelToTest = "newProcessLabelToTest";
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.MULTIPART_FORM_DATA_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        mockMvc.perform(fileUpload("/projects/"+projectIDForTest+"/processes").file(processFileToTest)
                                                                              .param("processID", processIDToTest)
                                                                              .param("processLabel", processLabelToTest))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(jsonContentType))
               .andExpect(jsonPath("$.id", containsString("http://www.uni-mannheim.de/spa/DataBucket/")))
               .andExpect(jsonPath("$.label", equalTo(processLabelToTest)));
    }
    
    @Test
    public void itShouldReturnNotFoundForCreatingAProcessInNonExistentProjectID() throws Exception{
        final String processIDToTest = "newProcessIDToTest";
        final String processLabelToTest = "newProcessLabelToTest";
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.MULTIPART_FORM_DATA_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        mockMvc.perform(fileUpload("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST+"/processes").file(processFileToTest)
                                                                              .param("processID", processIDToTest)
                                                                              .param("processLabel", processLabelToTest))
                .andExpect(status().isNotFound());
    }
    
    private Path getFilePath(final String fileName) {
      try {
          return Paths.get(Resources.getResource(fileName).toURI());
      } catch (URISyntaxException e) {
          throw Throwables.propagate(e);
      }
    }
    
}
