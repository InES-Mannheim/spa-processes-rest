package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.unima.core.api.Source;

public class DummySource implements Source {

	public DummySource(InputStream content) {
	}

	public InputStream getContent() {
		return new ByteArrayInputStream("test".getBytes());
	}

}
