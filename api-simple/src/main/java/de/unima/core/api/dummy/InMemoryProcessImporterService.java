package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.unima.core.api.BpmnProcessImporterService;
import de.unima.core.api.Source;

public class InMemoryProcessImporterService implements BpmnProcessImporterService {

	private final Map<String, Map<String, Source>> projects;

	public InMemoryProcessImporterService(Map<String, Map<String, Source>> repo) {
		projects = new HashMap<String, Map<String, Source>>();
		for (int i = 0; i < 5; i++) {
			String tmpProjectID = "dummyProjectID" + i;

			Map<String, Source> dummyProcessMap = new HashMap<String, Source>();

			dummyProcessMap.put(tmpProjectID + "-dummyProcessID1",
					new SimpleSource(new ByteArrayInputStream("dummyProcessID1".getBytes())));
			dummyProcessMap.put(tmpProjectID + "-dummyProcessID2",
					new SimpleSource(new ByteArrayInputStream("dummyProcessID2".getBytes())));
			dummyProcessMap.put(tmpProjectID + "-dummyProcessID3",
					new SimpleSource(new ByteArrayInputStream("dummyProcessID3".getBytes())));
			dummyProcessMap.put(tmpProjectID + "-dummyProcessID4",
					new SimpleSource(new ByteArrayInputStream("dummyProcessID4".getBytes())));
			dummyProcessMap.put(tmpProjectID + "-dummyProcessID5",
					new SimpleSource(new ByteArrayInputStream("dummyProcessID5".getBytes())));
			
			projects.put(tmpProjectID, dummyProcessMap);
		}
	}

	@Override
	public String save(String projectId, Source source) {
		Preconditions.checkNotNull(source, "source must not be null.");
		Preconditions.checkNotNull(projectId, "Project Id must not be null.");
		Preconditions.checkArgument(!projectId.isEmpty(), "Project Id must not be empty.");

		final String processIdGenerated = generateProcessId(projectId);
		final Map<String, Source> auxProcessMap = getNewOrExistingProcessesMap(projectId);
		auxProcessMap.put(processIdGenerated, source);
		projects.put(projectId, auxProcessMap);
		return processIdGenerated;
	}

	private String generateProcessId(String projectId) {
		return projectId + "-dummyProcessID" + UUID.randomUUID();
	}

	private Map<String, Source> getNewOrExistingProcessesMap(String projectId) {
		final Map<String, Source> projectProccesses = projects.get(projectId);
		return projectProccesses == null ? Maps.newHashMap() : projectProccesses;
	}

	@Override
	public Optional<Source> load(final String processId) {
		Preconditions.checkNotNull(processId, "Process Id must not be null.");
		Preconditions.checkState(!processId.isEmpty(), "Process Id must not be empty.");

		return projects.entrySet().stream().map(entries -> entries.getValue().entrySet())
				.flatMap(entries -> entries.stream()).filter(entry -> entry.getKey().equals(processId)).findFirst()
				.map(entry -> entry.getValue());
	}

}
