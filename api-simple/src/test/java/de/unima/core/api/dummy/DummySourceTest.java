package de.unima.core.api.dummy;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummySourceTest {
	
	@Test
	public void dummySourceShouldReturnGivenInputStream() {
		final InputStream content = new ByteArrayInputStream("test".getBytes());
		final DummySource source = new DummySource(content);
		
		final InputStream result = source.getContent();
		assertThat(convertStreamToString(result), is(equalTo("test")));
	}
	
	// https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	@SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

}
