package de.unimannheim.spa.process.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.base.Preconditions;

import de.unimannheim.spa.process.domain.Source;

/**
 * Service for managing Processes
 * 
 * @author Gregor Trefs
 */
public class ProcessService {

	private Map<String, Map<String, Source>> repo;

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

	public String create(String projectId) {
		return create(projectId, Source.empty());
	}
	
	public String create(String projectId, Source source) {
		Preconditions.checkArgument(repo.containsKey(projectId), "Project does not exist.");
		final String processId = generateProcessId(projectId);
		repo.get(projectId).put(processId, source);
		return processId;
	}
	
	private String generateProcessId(String projectId) {
		return projectId+"-Process-"+UUID.randomUUID();
	}
}