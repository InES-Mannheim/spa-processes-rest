package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.unima.core.api.ProcessImporterService;
import de.unima.core.api.Source;

public class DummyProcessImporterService implements ProcessImporterService{
  
  private Map<String, Map<String, Source>> projects;
  
  public DummyProcessImporterService(){
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
    if(projectId != null && !projectId.isEmpty() && source != null){
      String processIdGenerated = "";
      if(projects.containsKey(projectId)){
        Map<String, Source> auxProcessMap = projects.get(projectId);
        processIdGenerated = projectId+"-dummyProcessID"+Integer.toString(auxProcessMap.size()+1);
        auxProcessMap.put(processIdGenerated, source);
        projects.put(projectId, auxProcessMap);
        return processIdGenerated;
      } else {
        // What happen when the project does not exist?
      }
    }
    return null;
  }

  public Source getById(String processId) {
    if(processId != null && !processId.isEmpty()){
      for(Entry<String, Map<String, Source>> entryProject : projects.entrySet()){
        for(Entry<String, Source> entryProcess : entryProject.getValue().entrySet()){
          if(entryProcess.getKey().equalsIgnoreCase(processId)){
            return entryProcess.getValue();
          }
        }
      }
    }
    return null;
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
