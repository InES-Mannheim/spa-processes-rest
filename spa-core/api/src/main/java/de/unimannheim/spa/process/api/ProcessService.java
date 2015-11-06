package de.unimannheim.spa.process.api;

import java.util.List;

/**
 * Service for managing Processes
 * 
 * @author Gregor Trefs
 */
public interface ProcessService {

	/**
	 * Creates process in given project. The source of the process is initially
	 * empty and should be updated.
	 * 
	 * @param projectId
	 *            id of the project
	 * @return unique process Id or empty if project exists
	 * @throws IllegalArgumentException
	 *             if project does not exist
	 */
	default String create(String projectId) {
		return create(projectId, Source.empty());
	}
	
	/**
	 * Creates process in given project. The source of the process is set to
	 * the given source.
	 * 
	 * @param projectId id of the project
	 * @param source where the proccess is stored
	 * @return unique process Id or empty if project exists
	 * @throws IllegalArgumentException
	 *             if project does not exist
	 */
	String create(String projectId, Source source);

	/**
	 * Retrieves all processes found for the given project.
	 * 
	 * @param projectId
	 *            id of the project
	 * @return list of process ids or empty list if project does not exist
	 */
	List<String> getAll(String projectId);

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
	 * @param id
	 *            of the process
	 * @return true if present; false otherwise
	 */
	default boolean exists(String id) {
		return getAll(id).contains(id);
	}

}