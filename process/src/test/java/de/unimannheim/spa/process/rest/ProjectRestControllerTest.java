/*******************************************************************************
 * Copyright 2016 University of Mannheim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *          http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.unimannheim.spa.process.rest;

import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import org.springframework.web.util.NestedServletException;

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
  
    @Rule
    public ExpectedException expected = ExpectedException.none();
  
    private final String NON_EXISTENT_PROJECT_ID_TO_TEST = "nonExistentProjectID";
    
    private final MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                    MediaType.APPLICATION_JSON.getSubtype(),
                                                    Charset.forName("utf8"));
    private final MediaType OCTET_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType(), 
                                                    MediaType.APPLICATION_OCTET_STREAM.getSubtype(),
                                                    Charset.forName("utf8"));
    
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void projectRestcontrollerShouldReturn0OrMoreProjects() throws Exception{
        mockMvc.perform(get("/projects"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(JSON_CONTENT_TYPE))
               .andExpect(jsonPath("$", hasSize((greaterThanOrEqualTo(0)))));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProject() throws Exception{
        mockMvc.perform(post("/projects").param("projectLabel", "ProjectLabel"))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(JSON_CONTENT_TYPE))
               .andExpect(jsonPath("$.id", isA(String.class)))
               .andExpect(jsonPath("$.label", containsString("ProjectLabel")));
    }
    
    @Test
    public void itShouldReturn0ProcessesOfProjectID() throws Exception{
        final String projectIDForTest = createProjectAndReturnID();
        mockMvc.perform(get("/projects/"+projectIDForTest+"/processes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(JSON_CONTENT_TYPE))
               .andExpect(jsonPath("$.buckets", hasSize(0)));
    }
    
    private String createProjectAndReturnID() throws Exception{
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
               .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }
    
    @Test
    public void itShouldCreateAndReturnNewProcessWithFile() throws Exception{
        final String projectIDForTest = createProjectAndReturnID();
        final String processLabelToTest = "newProcessLabelToTest";
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.MULTIPART_FORM_DATA_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        mockMvc.perform(fileUpload("/projects/"+projectIDForTest+"/processes").file(processFileToTest)
                                                                              .param("processLabel", processLabelToTest)
                                                                              .param("format", "BPMN2"))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(JSON_CONTENT_TYPE))
               .andExpect(jsonPath("$.id", containsString("http://www.uni-mannheim.de/spa/DataBucket/")))
               .andExpect(jsonPath("$.label", equalTo(processLabelToTest)));
    }
    
    @Test
    public void itShouldReturnNotFoundForCreatingAProcessInNonExistentProjectID() throws Exception{
        final String processLabelToTest = "newProcessLabelToTest";
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.MULTIPART_FORM_DATA_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        mockMvc.perform(fileUpload("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST+"/processes").file(processFileToTest)
                                                                              .param("processLabel", processLabelToTest)
                                                                              .param("format", "BPMN2"))
                .andExpect(status().isNotFound());
    }
    
    private Path getFilePath(final String fileName) {
      try {
          return Paths.get(Resources.getResource(fileName).toURI());
      } catch (URISyntaxException e) {
          throw Throwables.propagate(e);
      }
    }
    
    @Test
    public void itShouldReturnTheSupportedInputFormats() throws Exception{
        mockMvc.perform(get("/projects/importformats"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(JSON_CONTENT_TYPE))
               .andExpect(jsonPath("$.[0]", equalTo("BPMN2")))
               .andExpect(jsonPath("$.[1]", equalTo("RDF")))
               .andExpect(jsonPath("$.[2]", equalTo("XES")))
               .andExpect(jsonPath("$.[3]", equalTo("XSD")));
    }
    
    @Test
    public void itShouldReturn500ForCreatingAProcessWithUnsopportedFormat() throws Exception{
        expected.expect(NestedServletException.class);
        final String projectIDForTest = createProjectAndReturnID();
        final String processLabelToTest = "newProcessLabelToTest";
        final String unsupportedFormat = "TXT";
        MockMultipartFile processFileToTest = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.MULTIPART_FORM_DATA_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        mockMvc.perform(fileUpload("/projects/"+projectIDForTest+"/processes").file(processFileToTest)
                                                                              .param("processLabel", processLabelToTest)
                                                                              .param("format", unsupportedFormat))
               .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void itShouldReturnOkForRemovingAnExistentDataBucket() throws Exception{
        final String projectIDForTest = createProjectAndReturnID();
        final String processIDForTest = createProcessAndReturnID(projectIDForTest, "BPMN2");
        mockMvc.perform(delete("/projects/"+projectIDForTest+"/processes/"+processIDForTest))
               .andExpect(status().isOk());
    }
    
    private String createProcessAndReturnID(String projectID, String format) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String processCreatedID = "";
        final String processLabel = "newProcessLabelToTest";
        MockMultipartFile processFile = new MockMultipartFile("processFile", "example-spa.bpmn", MediaType.APPLICATION_OCTET_STREAM_VALUE, Files.toByteArray(getFilePath("example-spa.bpmn").toFile()));
        String JSONFromServer = mockMvc.perform(fileUpload("/projects/"+projectID+"/processes").file(processFile)
                                                                              .param("processLabel", processLabel)
                                                                              .param("format", format))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();
        try{
          Map<String, Object> map = mapper.readValue(JSONFromServer, new TypeReference<Map<String, Object>>(){});
          processCreatedID = ((String) map.get("id")).substring(42);
        }catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processCreatedID;
    }
    
    @Test
    public void itShouldReturnNotFoundForRemovingANonExistentProjectID() throws Exception{
      final String processIDForTest = "NonExistentProcessID";
      mockMvc.perform(delete("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST+"/processes/"+processIDForTest))
             .andExpect(status().isNotFound());
    }
    
    @Test
    public void itShouldReturnProcessFileWithSpecificFormat() throws Exception{
      final String formatForTest = "BPMN2";
      final String projectIDForTest = createProjectAndReturnID();
      final String processIDForTest = createProcessAndReturnID(projectIDForTest, formatForTest);
      mockMvc.perform(get("/projects/"+projectIDForTest+"/processes/"+processIDForTest))
             .andExpect(status().isOk())
             .andExpect(content().contentType(OCTET_CONTENT_TYPE));
    }
    
    @Test
    public void itShouldRemoveAnExistentProject() throws Exception{
      final String projectIDToRemove = createProjectAndReturnID();
      mockMvc.perform(delete("/projects/"+projectIDToRemove))
             .andExpect(status().isOk());
    }
    
    @Test
    public void itShouldReturnNOTFOUNDForRemoveANonxistentProject() throws Exception{
      mockMvc.perform(delete("/projects/"+NON_EXISTENT_PROJECT_ID_TO_TEST))
             .andExpect(status().isNotFound());
    }
}
