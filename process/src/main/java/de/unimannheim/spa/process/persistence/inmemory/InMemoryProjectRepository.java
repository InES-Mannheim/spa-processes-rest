package de.unimannheim.spa.process.persistence.inmemory;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.persistence.ProjectRepository;

@Component
@Profile("development")
public class InMemoryProjectRepository implements ProjectRepository {

	private final List<Project> memory;

	public InMemoryProjectRepository() {
		this(Lists.newArrayList());
	}

	public InMemoryProjectRepository(List<Project> init) {
		this.memory = init;
	}

	@Override
	public boolean deleteById(String id) {

	}

	@Override
	public boolean save(Project entity) {
		return false;
	}

}
