package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.unimannheim.spa.process.api.ProjectService;
import de.unimannheim.spa.process.api.ProjectType;
import de.unimannheim.spa.process.api.Source;

public class InMemoryProjectService implements ProjectService {

	private Map<String, Map<String, Source>> projects;

	public InMemoryProjectService() {
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

	public boolean deleteById(String processId) {
		Preconditions.checkNotNull(processId, "Process Id must not be null.");
		Preconditions.checkArgument(!processId.isEmpty(), "Process Id must not be empty.");
		for (Entry<String, Map<String, Source>> entryProject : projects.entrySet()) {
			if (entryProject.getValue().containsKey(processId)) {
				projects.remove(entryProject.getKey());
				return true;
			}
		}
		return false;
	}

	public List<String> listAll() {
		List<String> dummyProjectIDList = new ArrayList<String>();
		dummyProjectIDList.addAll(projects.keySet());
		return dummyProjectIDList;
	}

	public String create(ProjectType type) {
		Preconditions.checkNotNull("Type must not be null.", type);
		final String generatedProjectID = this.generateProjectId();
		projects.put(generatedProjectID, Maps.newHashMap());
		return generatedProjectID;
	}

	private String generateProjectId() {
		return "Project-" + UUID.randomUUID();
	}

	@Override
	public boolean add(String projectId, ProjectType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<String> findById(String projectId) {
		return listAll().stream().filter(projectId::equals).findFirst();
	}
}
