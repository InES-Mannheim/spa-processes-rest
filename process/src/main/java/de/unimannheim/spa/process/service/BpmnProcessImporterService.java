package de.unimannheim.spa.process.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.unimannheim.spa.process.domain.Source;

/**
 * Service for importing BPMN
 * 
 * @author Gregor Trefs
 */
//@Service
public class BpmnProcessImporterService {

	private final Map<String, Map<String, Source>> repo;

	public BpmnProcessImporterService(Map<String, Map<String, Source>> repo) {
		Preconditions.checkNotNull(repo, "Repostiroy must not be empty.");
		this.repo = repo;
	}

	public final String importProcess(String projectId, Source source) {
		Preconditions.checkNotNull(source, "source must not be null.");
		Preconditions.checkNotNull(projectId, "Project Id must not be null.");
		Preconditions.checkArgument(!projectId.isEmpty(), "Project Id must not be empty.");

		final String processIdGenerated = generateProcessId(projectId);
		final Map<String, Source> auxProcessMap = getNewOrExistingProcessesMap(projectId);
		auxProcessMap.put(processIdGenerated, source);
		repo.put(projectId, auxProcessMap);
		return processIdGenerated;
	}

	private String generateProcessId(String projectId) {
		return projectId + "-dummyProcessID" + UUID.randomUUID();
	}

	private Map<String, Source> getNewOrExistingProcessesMap(String projectId) {
		final Map<String, Source> projectProccesses = repo.get(projectId);
		return projectProccesses == null ? Maps.newHashMap() : projectProccesses;
	}

	public final Optional<Source> exportProcess(final String processId) {
		Preconditions.checkNotNull(processId, "Process Id must not be null.");
		Preconditions.checkState(!processId.isEmpty(), "Process Id must not be empty.");

		return repo.entrySet()
				.stream()
				.map(entries -> entries.getValue()
				.entrySet())
				.flatMap(entries -> entries.stream())
				.filter(entry -> entry.getKey().equals(processId))
				.findFirst()
				.map(entry -> entry.getValue());
	}

}
