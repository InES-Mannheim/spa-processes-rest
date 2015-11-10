package de.unimannheim.spa.process.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.unimannheim.spa.process.domain.Source;
import de.unimannheim.spa.process.persistence.ProjectRepository;

@Service
public class ProjectService {

	private Map<String, Map<String, Source>> projects;
	private final ProjectRepository repo;

	@Autowired
	public ProjectService(ProjectRepository repo) {
		this.repo = repo;
	}

	public boolean deleteById(String processId) {
		Preconditions.checkNotNull(processId, "Process Id must not be null.");
		Preconditions.checkArgument(!processId.isEmpty(), "Process Id must not be empty.");
		
		return repo.deleteById(processId);
//		for (Entry<String, Map<String, Source>> entryProject : projects.entrySet()) {
//			if (entryProject.getValue().containsKey(processId)) {
//				projects.remove(entryProject.getKey());
//				return true;
//			}
//		}
//		return false;
	}

	public List<String> listAll() {
		List<String> dummyProjectIDList = new ArrayList<String>();
		dummyProjectIDList.addAll(projects.keySet());
		return dummyProjectIDList;
	}

	public String create(ProjectType type) {
		Preconditions.checkNotNull(type, "Type must not be null.");
		final String generatedProjectID = this.generateProjectId();
		projects.put(generatedProjectID, Maps.newHashMap());
		return generatedProjectID;
	}

	private String generateProjectId() {
		return "Project-" + UUID.randomUUID();
	}

	public boolean add(String projectId, ProjectType type) {
		// TODO Auto-generated method stub
		return false;
	}

	public Optional<String> findById(String projectId) {
		return listAll().stream().filter(projectId::equals).findFirst();
	}
}
