package de.unimannheim.spa.process.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.service.ProcessService;
import de.unimannheim.spa.process.service.ProjectService;
import de.unimannheim.spa.process.service.ProjectType;

@RestController
@RequestMapping("/projects")
public class ProjectRestController {
  
  private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
                                                    MediaType.APPLICATION_JSON.getSubtype(),
                                                    Charset.forName("utf8"));
  
  private MediaType octetStreamContentType = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType(), 
                                                           MediaType.APPLICATION_OCTET_STREAM.getSubtype(),
                                                           Charset.forName("utf8"));
  
  @Autowired
  private ProjectService projectService;
  
  @Autowired
  private ProcessService processService;
  
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(method = RequestMethod.GET)
  List<Project> getAll(){
      return projectService.listAll();
  }
  
  @RequestMapping(value="/{projectID}", method = RequestMethod.GET)
  ResponseEntity<List<Process>> getAllProcesses(@PathVariable String projectID){
      ResponseEntity<List<Process>> response;
      List<Process> processes = Lists.newArrayList();
      Optional<Project> projectFetched = projectService.findById(projectID); 
      if(projectFetched.isPresent()){
          processes = projectFetched.get().getProcesses();
          response = ResponseEntity.ok()
                                   .contentType(jsonContentType)
                                   .body(processes);
      } else {
          response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .contentType(jsonContentType)
                                   .body(processes);
      }
      return response;
  }
  
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @RequestMapping(value="/{projectID}", method = RequestMethod.PUT)
  Project modifyProject(@PathVariable String projectID){
      // TODO: Implement the feature of modifying an existent project
      throw new UnsupportedOperationException();
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody Project createProject(){
      return new Project(projectService.create(ProjectType.BPMN));
  }
  
  @RequestMapping(value="/{projectID}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody ResponseEntity<Process> createProcessWithFile(@PathVariable("projectID") String projectID,
                                              @RequestParam("processID") String processID,
                                              @RequestParam("processLabel") String processLabel,
                                              @RequestPart("processFile") MultipartFile processFile){
      Process processCreated = processService.create(projectID, processID, processLabel, processFile);
      projectService.addProcess(projectID, processCreated, ProjectType.BPMN);
      return ResponseEntity.status(HttpStatus.CREATED)
                           .contentType(jsonContentType)
                           .body(processCreated);
  }
  
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value="/{projectID}/processes/{processID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  @ResponseBody ResponseEntity<InputStreamResource> downloadProcessFile(@PathVariable("projectID") String projectID,
                                                                 @PathVariable("processID") String processID) throws IOException{
      return ResponseEntity.ok()
                           .contentType(octetStreamContentType)
                           .body(new InputStreamResource(projectService.getProcessFile(projectID, processID).getContent()));
  }

}
