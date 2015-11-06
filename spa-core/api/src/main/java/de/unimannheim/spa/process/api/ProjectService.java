package de.unimannheim.spa.process.api;

import java.util.List;
import java.util.Optional;


/**
 * Service for managing projects.
 * 
 * @author Gregor Trefs
 */
public interface ProjectService {

  /**
   * Creates new project and returns unique id.
   * 
   * @param type of the project which should be created 
   * @return unique project id
   * @see {@link ProjectType}
   */
  String create(ProjectType type);
  
  /**
   * Adds project.
   *  
   * @param projectId of the project
   * @param type of the project
   * @return true if project was added successfully; false otherwise
   */
  boolean add(String projectId, ProjectType type);
  
  /**
   * Deletes project.
   * 
   * @param projectId id of the project
   * @return true if successful, false otherwise
   */
  boolean deleteById(String projectId);
  
  /**
   * Finds project.
   * 
   * @param projectId of of the project
   * @return the found project or empty
   */
  Optional<String> findById(String projectId);
  
  /**
   * Retrieves all project ids.
   * 
   * @return ids of projects
   */
  List<String> listAll();
  
  /**
   * Searches for a project.
   * 
   * @param projectId of the project
   * @return true if project exists; false otherwise
   */
  default boolean exists(String projectId) {
	  return findById(projectId).isPresent();
  }
}
