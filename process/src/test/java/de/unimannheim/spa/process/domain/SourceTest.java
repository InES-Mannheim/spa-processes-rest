package de.unimannheim.spa.process.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


import com.google.common.base.Charsets;

import de.unimannheim.spa.process.api.Source;
import de.unimannheim.spa.process.domain.Source;

public class SourceTest {
  
    private Source source;
	
	@Test
	public void dummySourceShouldReturnGivenInputStream() throws IOException {
	  final String stringTest = "Test String";
	  final InputStream isTest = new ByteArrayInputStream(stringTest.getBytes(Charsets.UTF_8));
	  source = new Source(isTest);
	  final InputStream resultTest = source.getContent();
	  assertThat("The return value should be 'Test String'", convertStreamToString(resultTest), is(equalTo(stringTest)));
	}
	
	static String convertStreamToString(java.io.InputStream is) throws IOException {
	  String theString = IOUtils.toString(is, Charsets.UTF_8);
	  return theString;
	}

}
