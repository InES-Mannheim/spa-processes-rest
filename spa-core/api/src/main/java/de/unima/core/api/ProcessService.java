package de.unima.core.api;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing Processes
 * 
 * @author Gregor Trefs
 */
public interface ProcessService {

	/**
	 * Creates process in given project.
	 * 
	 * @param projectId
	 *            id of the project
	 * @return unique process Id or empty if project already exists
	 * @throws IllegalArgumentException if project does not exist
	 */
	String create(String projectId);

	/**
	 * Retrieves all processes found for the given project.
	 * 
	 * @param projectId
	 *            id of the project
	 * @return list of process ids or empty list if project does not exist
	 */
	List<String> getAll(String projectId);

	/**
	 * Finds process.
	 * 
	 * @param id
	 *           of the process
	 * @return the found process or empty
	 */
	Optional<String> findById(String id);

	/**
	 * Deletes process.
	 * 
	 * @param processId
	 *            of the process
	 * @return true if deletion was successful; false otherwise
	 */
	boolean deleteById(String processId);

	/**
	 * Updates given process identified by given id with the content contained
	 * in the source.
	 * 
	 * @param id
	 *            of the process
	 * @return true if successful; false otherwise
	 */
	boolean updateById(String id, Source source);

	/**
	 * Searches for a process.
	 * 
	 * @param id of the process
	 * @return true if present; false otherwise
	 */
	default boolean exists(String id) {
		return findById(id).isPresent();
	}

}