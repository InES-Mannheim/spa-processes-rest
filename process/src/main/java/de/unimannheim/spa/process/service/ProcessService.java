package de.unimannheim.spa.process.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.unimannheim.spa.process.domain.Source;
import de.unimannheim.spa.process.domain.Process;

/**
 * Service for managing Processes
 * 
 * @author Gregor Trefs
 */
@Service
public class ProcessService {

	private Map<String, Map<String, Source>> repo;
	
	public ProcessService(){
	}

	public ProcessService(Map<String, Map<String, Source>> repo) {
		this.repo = repo;
	}

	public List<String> getAll(String projectId) {
		Preconditions.checkNotNull(projectId, "Project Id must not be null.");
		Preconditions.checkArgument(!projectId.isEmpty(), "Project Id must not be empty");
		List<String> listAllProcessId = new ArrayList<String>();
		if (repo.containsKey(projectId)) {
			for (Entry<String, Source> entryProcess : repo.get(projectId).entrySet()) {
				listAllProcessId.add(entryProcess.getKey());
			}
			return listAllProcessId;
		}
		return Collections.emptyList();
	}

	public boolean deleteById(String id) {
		Preconditions.checkNotNull(id, "Process Id must not be null.");
		Preconditions.checkArgument(!id.isEmpty(), "Process Id must not be empty.");
		for (Entry<String, Map<String, Source>> entryProject : repo.entrySet()) {
			if (entryProject.getValue().containsKey(id)) {
				entryProject.getValue().remove(id);
				return true;
			}
		}
		return false;
	}

	public boolean updateById(String id, Source source) {
		Preconditions.checkNotNull(id, "Process Id must not be null.");
		Preconditions.checkArgument(!id.isEmpty(), "Process Id must not be empty.");
		Preconditions.checkNotNull(source, "Source must not be null.");
		
		return repo.entrySet()
				.stream()
				.flatMap(project -> project.getValue().entrySet().stream())
				.filter(processToSource -> processToSource.getKey().equals(id))
				.findFirst()
				.map(processToSource -> processToSource.setValue(source))
				.isPresent();
	}
	
	public Process create(String projectId, MultipartFile processFile) {
        return create(projectId, null, null, processFile);
    } 
	
	public Process create(String projectId, String processId, MultipartFile processFile) {
	    return create(projectId, processId, null, processFile);
	}
	
	public Process create(String projectId, String processId, String processLabel, MultipartFile processFile) {
	    Preconditions.checkNotNull(projectId, "Project ID should not be null.");
		Preconditions.checkArgument(!projectId.isEmpty(), "Project ID should not be empty.");
		Preconditions.checkArgument(!processFile.isEmpty(), "Process file should not be empty.");
		
		if(Strings.isNullOrEmpty(processId)) 
		  processId = generateProcessId(projectId);
		if(Strings.isNullOrEmpty(processLabel))
		  processLabel = generateProcessLabel(processId);
	    
		storeProcessFile(processId, processFile);
	    
		return new Process(processId, processLabel, generateURI(projectId, processId));
	}
	
	private String generateURI(String projectID, String processID){
	    return new String("/projects/"+projectID+"/processes/"+processID);
	}

    private boolean storeProcessFile(String processId, MultipartFile processFile){
        try {
            byte[] bytes = processFile.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("processes/"+String.join(".", processId, "bpmn"))));
            stream.write(bytes);
            stream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	private String generateProcessId(String projectId) {
		return new String(projectId+"-Process-"+UUID.randomUUID());
	}
	
	private String generateProcessLabel(String processId){
	    return String.join("-", processId, "label");
	}
}