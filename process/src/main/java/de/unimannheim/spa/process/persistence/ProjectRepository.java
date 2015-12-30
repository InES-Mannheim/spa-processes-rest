package de.unimannheim.spa.process.persistence;

import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Process;

public interface ProjectRepository extends Repository<String, Project> {
  public boolean addProcess(String projectID, Process process);
  public boolean deleteProcess(String projectID, String processID);
  public void deleteAll();
}