package de.unima.core.api.dummy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.unimannheim.spa.process.api.Source;

public class SimpleSource implements Source {
  
  private InputStream content;
  
  public SimpleSource(){
    this.content = new ByteArrayInputStream("test".getBytes());
  }
  
  public SimpleSource(InputStream content) {
    this.content = content;
  }

  public InputStream getContent() {
    return this.content;
  }

}
