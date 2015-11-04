package de.unima.core.api;

import java.util.Optional;

/**
 * Service for importing BPMN
 * 
 * @author Gregor Trefs
 */
public interface BpmnProcessImporterService {

	/**
	 * Saves process retrieved from Source and returns a unique process id.
	 * 
	 * @param projectId
	 *            the project in which context this process should be generated
	 * @param source
	 *            which contains the process in BPMN format
	 * @return unique process ID or empty if process could not be imported
	 * @throws IllegalArgumentException
	 *             if source could not be read or project does not exist
	 */
	String save(String projectId, Source source);

	/**
	 * Loads process.
	 * 
	 * @param processId
	 *            id of the process
	 * @return Source with Process in BPMN format or {@code Optional#empty()} if
	 *         process is unknown
	 */
	Optional<Source> load(String processId);

}
