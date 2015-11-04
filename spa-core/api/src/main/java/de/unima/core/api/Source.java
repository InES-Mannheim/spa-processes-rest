package de.unima.core.api;

import java.io.InputStream;

/**
 * Abstraction of the source of a process
 * 
 * @author Gregor Trefs
 */
public interface Source {

	/**
	 * Returns content as {@code InputStream}
	 * 
	 * @return content as {@code InputStream}
	 */
	InputStream getContent();
}
