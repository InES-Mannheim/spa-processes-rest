package de.unimannheim.spa.process.persistence.inmemory;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.unimannheim.spa.process.domain.Process;
import de.unimannheim.spa.process.domain.Source;
import de.unimannheim.spa.process.persistence.ProcessRepository;

@Component
@Profile("development")
public class InMemoryProcessRepository implements ProcessRepository {

  @Override
  public boolean deleteById(String id) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean save(Process entity) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<Process> getAll() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public boolean updateById(String projectID, String processID, Source source) {
    return false;
  }

}
