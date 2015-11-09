package de.unimannheim.spa.process.service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.google.common.collect.Maps;

import de.unimannheim.spa.process.domain.Source;

public class InMemoryOps {
	
	public static Map<String, Map<String, Source>> creatRepoWith5ProjectsAnd5ProcessesEach() {
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
}
