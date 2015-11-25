package de.unimannheim.spa.process.domain;

import java.io.IOException;
import java.io.InputStream;

public class Source{
  
  private static final Source EMPTY = new Source(new InputStream() {
	@Override
	public final int read() throws IOException {
		return -1;
	}
  });
	
  private InputStream content;
  
  public Source(InputStream content) {
    this.content = content;
  }

  public InputStream getContent() {
    return this.content;
  }
  
  public static Source empty() {
	  return EMPTY; 
  }

}
