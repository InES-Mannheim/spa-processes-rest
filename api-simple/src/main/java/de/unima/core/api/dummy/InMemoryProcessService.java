package de.unima.core.api.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.base.Preconditions;

import de.unima.core.api.ProcessService;
import de.unima.core.api.ProjectService;
import de.unima.core.api.ProjectType;
import de.unima.core.api.Source;

public class InMemoryProcessService implements ProcessService {

	public InMemoryProcessService() {
	}

	  public List<String> getAll(String projectId) {
	    Preconditions.checkNotNull(projectId, "Project Id must not be null.");
	    Preconditions.checkArgument(!projectId.isEmpty(), "Project Id must not be empty");
	    List<String> listAllProcessId = new ArrayList<String>();
	    if(data.projects.containsKey(projectId)){
	      for(Entry<String, Source> entryProcess : data.projects.get(projectId).entrySet()){
	        listAllProcessId.add(entryProcess.getKey());
	      }
	      return listAllProcessId;
	    }
	    return null;
	  }

	  public boolean deleteById(String id) {
	    Preconditions.checkNotNull(id, "Process Id must not be null.");
	    Preconditions.checkArgument(!id.isEmpty(), "Process Id must not be empty.");
	    for(Entry<String, Map<String, Source>> entryProject : data.projects.entrySet()){
	      if(entryProject.getValue().containsKey(id)){
	        entryProject.getValue().remove(id);
	        return true;
	      }
	    }
	    return false;
	  }

	  @Override
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

	@Override
	public String create(ProjectType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(String projectId, ProjectType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<String> findById(String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listAll() {
		// TODO Auto-generated method stub
		return null;
	}
}