package de.unima.core.project;

import de.unimannheim.spa.process.api.ProjectType;
import de.unimannheim.spa.process.api.Source;

public interface Project {

  ProjectType getProjectType();
  
  String importDataToProject(Source src);
  
  Source exportDataFromProject(String id);
  
}
