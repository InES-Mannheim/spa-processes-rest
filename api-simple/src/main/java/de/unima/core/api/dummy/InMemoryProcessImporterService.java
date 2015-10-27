package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.unima.core.api.ProcessImporterService;
import de.unima.core.api.Source;

public class InMemoryProcessImporterService implements ProcessImporterService{
  
  private Map<String, Map<String, Source>> projects;
  
  public InMemoryProcessImporterService(){
    projects = new HashMap<String, Map<String, Source>>();
    for(int i = 0; i < 5; i++){
      String tmpProjectID = "dummyProjectID"+i;
      
      Map<String, Source> dummyProcessMap = new HashMap<String, Source>();
      
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID1", new DummySource(new ByteArrayInputStream("dummyProcessID1".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID2", new DummySource(new ByteArrayInputStream("dummyProcessID2".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID3", new DummySource(new ByteArrayInputStream("dummyProcessID3".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID4", new DummySource(new ByteArrayInputStream("dummyProcessID4".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID5", new DummySource(new ByteArrayInputStream("dummyProcessID5".getBytes())));
      
      projects.put(tmpProjectID, dummyProcessMap);
    }
  }
  
  public String save(String projectId, Source source) {
		Preconditions.checkNotNull(source);
		Preconditions.checkNotNull(projectId);
		Preconditions.checkArgument(!projectId.isEmpty());
		
		final String processIdGenerated = generateProcessId(projectId);
		final Map<String, Source> auxProcessMap = getNewOrExistingProcessesMap(projectId);
		auxProcessMap.put(processIdGenerated, source);
		projects.put(projectId, auxProcessMap);
		return processIdGenerated;
  }

  private String generateProcessId(String projectId){
	  return projectId+"-dummyProcessID"+UUID.randomUUID();
  }
  
  private Map<String, Source> getNewOrExistingProcessesMap(String projectId) {
	  final Map<String, Source> projectProccesses = projects.get(projectId);
	  return projectProccesses == null? Maps.newHashMap():projectProccesses;
  }

  public Optional<Source> getById(final String processId) {
	Preconditions.checkNotNull(processId, "Process Id must not be null.");
	Preconditions.checkState(!processId.isEmpty(), "Process Id must not be empty.");
	
	return projects.entrySet().stream()
	.map(entries -> entries.getValue().entrySet())
	.flatMap(entries -> entries.stream())
	.filter(entry -> entry.getKey().equals(processId))
	.findFirst()
	.map(entry -> entry.getValue());
  }

  public List<String> getAll(String projectId) {
    List<String> listAllProcessId = new ArrayList<String>();
    if(projectId != null && !projectId.isEmpty()){
      if(projects.containsKey(projectId)){
        for(Entry<String, Source> entryProcess : projects.get(projectId).entrySet()){
          listAllProcessId.add(entryProcess.getKey());
        }
        return listAllProcessId;
      }
    }
    return null;
  }

  public boolean deleteById(String id) {
    if(id != null && !id.isEmpty()){
      for(Entry<String, Map<String, Source>> entryProject : projects.entrySet()){
        if(entryProject.getValue().containsKey(id)){
          entryProject.getValue().remove(id);
          return true;
        }
      }
    }
    return false;
  }

  public boolean updateById(String id, Source source) {
    // TODO Auto-generated method stub
    return false;
  }

}
