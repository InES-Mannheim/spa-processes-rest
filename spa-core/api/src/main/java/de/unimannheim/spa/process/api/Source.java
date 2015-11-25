package de.unimannheim.spa.process.api;

import java.io.IOException;
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
	
	public static Source empty() {
		return new Source() {
			@Override
			public InputStream getContent() {
				return new InputStream() {
					@Override
					public int read() throws IOException {
						return -1;
					}
				};
			}
		};
	}
}
