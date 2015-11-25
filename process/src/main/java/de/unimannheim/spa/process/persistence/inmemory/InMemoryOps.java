package de.unimannheim.spa.process.persistence.inmemory;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Maps;

import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Source;

public class InMemoryOps {
	
	public static Map<String, Map<String, Source>> createRepoWith5ProjectsAnd5ProcessesEach() {
		final Map<String, Map<String, Source>> repo = Maps.newHashMap();
		IntStream.range(0, 5).forEach(i -> {
			final String projectId = "dummyProjectID" + i;
			final Map<String, Source> processToSource = new HashMap<String, Source>();
			IntStream.range(0, 5).forEach(j -> {
				processToSource.put(projectId + "-dummyProcessID" + j,
						new Source(new ByteArrayInputStream(("dummyProcessID" + j).getBytes())));
			});
			repo.put(projectId, processToSource);
		});
		return repo;
	}
	
	public static List<Project> create5ProjectsWith5ProcessesEach(){
		return createRepoWith5ProjectsAnd5ProcessesEach()
				.entrySet()
				.stream().map(projectToProcesses -> {
					final Project project = new Project(projectToProcesses.getKey());
					project.setLabel(String.join("-", projectToProcesses.getKey(), "label"));
					project.setProcesses(projectToProcesses.getValue()
							.entrySet()
							.stream()
							.map(processToResource -> {
								final Process process = new Process(processToResource.getKey());
								process.setSource(processToResource.getValue());
								process.setLabel(String.join("-", processToResource.getKey(), "label"));
								return process;
							}).collect(Collectors.toList()));
					return project;
				}).collect(Collectors.toList());
	}
}
