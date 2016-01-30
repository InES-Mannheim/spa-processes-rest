package de.unimannheim.spa.process.rest;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.serialization.MapSerializer;

import de.unima.core.application.SPA;
import de.unima.core.domain.model.DataBucket;
import de.unima.core.domain.model.DataPool;
import de.unima.core.domain.model.Project;
import de.unimannheim.spa.process.rekord.builders.DataPoolBuilder;
import de.unimannheim.spa.process.rekord.builders.ProjectBuilder;
import de.unimannheim.spa.process.util.FileUtils;

@RestController
@RequestMapping("/projects")
public class ProjectRestController {
  
  private final static String BASE_URL = "http://www.uni-mannheim.de/spa/Project/";
  
  private final static MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                    MediaType.APPLICATION_JSON.getSubtype(),
                                                    Charsets.UTF_8);
  
  private final SPA spaService;
  
  @Autowired
  public ProjectRestController(SPA spaService) {
      this.spaService = spaService;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Map<String, Object>>> getAllProjects(){
      List<Map<String, Object>> allProjects = Lists.newArrayList();
      for(Project project : spaService.findAllProjects()){
        Rekord<Project> projectTmp = ProjectBuilder.rekord.with(ProjectBuilder.id, project.getId())
                                                   .with(ProjectBuilder.label, project.getLabel())
                                                   .with(ProjectBuilder.dataPools, DataPoolBuilder.rekord.with(DataPoolBuilder.buckets, project.getDataPools().get(0).getDataBuckets()))
                                                   .with(ProjectBuilder.linkedSchemas, project.getLinkedSchemas());
        allProjects.add(projectTmp.serialize(new MapSerializer()));
      }
      return ResponseEntity.ok()
                           .contentType(JSON_CONTENT_TYPE)
                           .body(allProjects);
  }
  
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> createProject(@RequestParam String projectLabel){
      Project projectCreated = spaService.createProject(projectLabel);
      spaService.saveProject(projectCreated);
      spaService.createDataPool(projectCreated, "Default DataPool"); 
      Rekord<Project> projectTmp = ProjectBuilder.rekord.with(ProjectBuilder.id, projectCreated.getId())
                                                        .with(ProjectBuilder.label, projectCreated.getLabel())
                                                        .with(ProjectBuilder.dataPools, DataPoolBuilder.rekord.with(DataPoolBuilder.buckets, projectCreated.getDataPools().get(0).getDataBuckets()))
                                                        .with(ProjectBuilder.linkedSchemas, projectCreated.getLinkedSchemas());
      return ResponseEntity.status(HttpStatus.CREATED)
                           .contentType(JSON_CONTENT_TYPE)
                           .body(projectTmp.serialize(new MapSerializer()));
  }
  
  @RequestMapping(value="/{projectID}/processes", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> getAllProcesses(@PathVariable String projectID){
      return spaService.findProjectById(BASE_URL+projectID)
                       .map(project -> project.getDataPools())
                       .map(processes -> {
                         Rekord<DataPool> dataPoolTmp = DataPoolBuilder.rekord.with(DataPoolBuilder.buckets, processes.get(0).getDataBuckets());
                         return ResponseEntity.ok()
                               .contentType(JSON_CONTENT_TYPE)
                               .body(dataPoolTmp.serialize(new MapSerializer()));
                         })
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                               .contentType(JSON_CONTENT_TYPE)
                               .body(Collections.emptyMap())); 
  }
  
  @RequestMapping(value="/{projectID}/processes", method = RequestMethod.POST)
  public ResponseEntity<? extends DataBucket> createProcessWithFile(@PathVariable("projectID") String projectID,
                                                                    @RequestParam("processID") String processID,
                                                                    @RequestParam("processLabel") String processLabel,
                                                                    @RequestPart("processFile") MultipartFile processFile) throws IllegalStateException, IOException{
      return spaService.findProjectById(BASE_URL+projectID)
                       .map(project -> {
                                final DataPool dataPool = project.getDataPools().get(0);
                                DataBucket process = null;
                                try {
                                  File fileToImport = FileUtils.convertMultipartToFile(processFile);
                                  process = spaService.importData(fileToImport, "BPMN2", processLabel, dataPool);
                                  fileToImport.delete();
                                } catch (Exception e) {
                                  e.printStackTrace();
                                }
                                return ResponseEntity.status(HttpStatus.CREATED)
                                                     .contentType(JSON_CONTENT_TYPE)
                                                     .body(process);
                              })
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                       .contentType(JSON_CONTENT_TYPE)
                                       .body(null));
      
  }
  
}
