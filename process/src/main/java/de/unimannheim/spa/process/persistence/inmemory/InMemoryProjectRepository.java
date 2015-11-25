package de.unimannheim.spa.process.persistence.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.persistence.ProjectRepository;

@Component
@Profile("development")
public class InMemoryProjectRepository implements ProjectRepository {

	private List<Project> memory;

	public InMemoryProjectRepository() {
		this(InMemoryOps.create5ProjectsWith5ProcessesEach());
	}

	public InMemoryProjectRepository(List<Project> init) {
	    this.memory = Lists.newArrayList();
	    init.stream().forEach(project -> memory.add(project));
	}

	@Override
	public boolean deleteById(String id) {
	  return (memory.removeIf(project -> project.getId().equalsIgnoreCase(id)));
	}

	@Override
	public boolean save(Project entity) {
		return (memory.stream().noneMatch(project -> project.getId().equalsIgnoreCase(entity.getId()))) ? memory.add(entity) : false;
	}

    @Override
    public List<Project> getAll() {
        return memory;
    }

    @Override
    public boolean addProcess(String projectID, Process process) {
      if(memory.stream().anyMatch(project -> project.getId().equals(projectID))){
        memory.stream().filter(project -> project.getId().equals(projectID)).map(project -> {
          return project.getProcesses().add(process);
        }).collect(Collectors.toList());
        return true;
      }
      return false;
    }

    @Override
    public void deleteAll() {
      this.memory.clear();
      this.memory = InMemoryOps.create5ProjectsWith5ProcessesEach();
    }
    
}
