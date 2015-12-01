package de.unimannheim.spa.process.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Source;
import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.persistence.ProjectRepository;

@Service
public class ProjectService {
  
    private final String FOLDER_PATH = "processes/";
    private final String FILE_EXTENSION = ".bpmn";

	private final ProjectRepository repo;

	@Autowired
	public ProjectService(ProjectRepository repo) {
		this.repo = repo;
	}

	public boolean deleteById(String processId) {
		Preconditions.checkNotNull(processId, "Process Id must not be null.");
		Preconditions.checkArgument(!processId.isEmpty(), "Process Id must not be empty.");
		return repo.deleteById(processId);
	}

	public List<Project> listAll() {
		return repo.getAll();
	}
	
	public String create(String projectId, String projectLabel, ProjectType type){
        Preconditions.checkNotNull(projectId, "projectID must not be null.");
        Preconditions.checkNotNull(projectLabel, "processLabel must not be null.");
        Preconditions.checkNotNull(type, "Type must not be null.");
        if(Strings.isNullOrEmpty(projectId))
          projectId = generateProjectId();
        if(Strings.isNullOrEmpty(projectLabel))
          projectLabel = String.join("-", projectId, "label");
        Project newProject = new Project(projectId, projectLabel);
        return repo.save(newProject)?newProject.getId():new String();
    }

	private String generateProjectId() {
		return "Project-" + UUID.randomUUID();
	}

	public boolean addProcess(String projectId, Process process, ProjectType type) {
		return repo.addProcess(projectId, process);
	}

	public Optional<Project> findById(String projectId) {
		return listAll().stream().filter(project -> project.getId().equals(projectId)).findFirst();
	}
	
	public Source getProcessFile(String projectID, String processID) throws IOException{
	    FileSystemResource processFile = new FileSystemResource(new File(String.join("", FOLDER_PATH, processID, FILE_EXTENSION)));
	    return new Source(processFile.getInputStream());
	}
	
	public void deleteAll(){
	  repo.deleteAll();
	}
}
