package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
      
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID1", new SimpleSource(new ByteArrayInputStream("dummyProcessID1".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID2", new SimpleSource(new ByteArrayInputStream("dummyProcessID2".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID3", new SimpleSource(new ByteArrayInputStream("dummyProcessID3".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID4", new SimpleSource(new ByteArrayInputStream("dummyProcessID4".getBytes())));
      dummyProcessMap.put(tmpProjectID+"-dummyProcessID5", new SimpleSource(new ByteArrayInputStream("dummyProcessID5".getBytes())));
      
      projects.put(tmpProjectID, dummyProcessMap);
    }
  }
  
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
    Preconditions.checkNotNull(projectId, "Project Id must not be null.");
    Preconditions.checkArgument(!projectId.isEmpty(), "Project Id must not be empty");
    List<String> listAllProcessId = new ArrayList<String>();
    if(projects.containsKey(projectId)){
      for(Entry<String, Source> entryProcess : projects.get(projectId).entrySet()){
        listAllProcessId.add(entryProcess.getKey());
      }
      return listAllProcessId;
    }
    return null;
  }

  public boolean deleteById(String id) {
    Preconditions.checkNotNull(id, "Process Id must not be null.");
    Preconditions.checkArgument(!id.isEmpty(), "Process Id must not be empty.");
    for(Entry<String, Map<String, Source>> entryProject : projects.entrySet()){
      if(entryProject.getValue().containsKey(id)){
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
    if(projects.entrySet().stream()
        .map(entries -> entries.getValue().entrySet())
        .flatMap(entries -> entries.stream())
        .filter(entry -> entry.getKey().equals(id))
        .findFirst()
        .map(entry -> entry).isPresent()){
      return true;
    }
    return false;
  }

}
