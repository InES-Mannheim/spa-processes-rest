package de.unima.core.api.dummy;

import java.util.ArrayList;
import java.util.List;

import de.unima.core.api.ProjectService;
import de.unima.core.api.ProjectType;

public class DummyProjectService implements ProjectService{

  public boolean delete(String processId){
    if(processId == null || processId.isEmpty()){
      return false;
    }
    return true;
  }

  public List<String> listAll(){
    List<String> dummyProjectIDList = new ArrayList<String>();
    dummyProjectIDList.add("DummyProjectID1");
    dummyProjectIDList.add("DummyProjectID2");
    dummyProjectIDList.add("DummyProjectID3");
    dummyProjectIDList.add("DummyProjectID4");
    dummyProjectIDList.add("DummyProjectID5");
    return dummyProjectIDList;
  }

  public String create(ProjectType type) {
    return new String("DummyProjectID");
  }
}
