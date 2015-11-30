package de.unimannheim.spa.process.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Charsets;

import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.service.ProcessService;
import de.unimannheim.spa.process.service.ProjectService;
import de.unimannheim.spa.process.service.ProjectType;

@RestController
@RequestMapping("/projects")
public class ProjectRestController {
  
  private final static MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                    MediaType.APPLICATION_JSON.getSubtype(),
                                                    Charsets.UTF_8);
  
  private final static MediaType OCTET_STREAM_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType(), 
                                                           MediaType.APPLICATION_OCTET_STREAM.getSubtype(),
                                                           Charsets.UTF_8);
  
  private final ProjectService projectService;
  private final ProcessService processService;
  
  @Autowired
  public ProjectRestController(ProcessService processService, ProjectService projectService) {
      this.processService = processService;
      this.projectService = projectService;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Project>> getAllProjects(){
	  return ResponseEntity.ok()
	                       .contentType(JSON_CONTENT_TYPE)
	                       .body(projectService.listAll());
  }
  
  @RequestMapping(value="/{projectID}/processes", method = RequestMethod.GET)
  public ResponseEntity<List<Process>> getAllProcesses(@PathVariable String projectID){
      return projectService.findById(projectID)
    		  .map(project -> project.getProcesses())
    		  .map(processes -> ResponseEntity.ok()
                      .contentType(JSON_CONTENT_TYPE)
                      .body(processes))
    		  .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .contentType(JSON_CONTENT_TYPE)
                      .body(Collections.emptyList())); 
  }
  
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Project> createProject(){
	  return ResponseEntity.status(HttpStatus.CREATED)
	                       .contentType(JSON_CONTENT_TYPE)
	                       .body(new Project(projectService.create(ProjectType.BPMN)));
  }
  
  @RequestMapping(value="/{projectID}/processes", method = RequestMethod.POST)
  public ResponseEntity<Process> createProcessWithFile(@PathVariable("projectID") String projectID,
                                              @RequestParam("processID") String processID,
                                              @RequestParam("processLabel") String processLabel,
                                              @RequestPart("processFile") MultipartFile processFile){
      Process processCreated = processService.create(projectID, processID, processLabel, processFile);
      projectService.addProcess(projectID, processCreated, ProjectType.BPMN);
      return ResponseEntity.status(HttpStatus.CREATED)
                           .contentType(JSON_CONTENT_TYPE)
                           .body(processCreated);
  }
  
  @RequestMapping(value="/{projectID}/processes/{processID}", method = RequestMethod.GET)
  public ResponseEntity<InputStreamResource> downloadProcessFile(@PathVariable("projectID") String projectID,
                                                                 @PathVariable("processID") String processID) throws IOException{
      return ResponseEntity.ok()
                           .contentType(OCTET_STREAM_CONTENT_TYPE)
                           .body(new InputStreamResource(projectService.getProcessFile(projectID, processID).getContent()));
  }

}
