package de.unimannheim.spa.process.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Source{
  
  private InputStream content;
  
  public Source(){
    this.content = new ByteArrayInputStream("test".getBytes());
  }
  
  public Source(InputStream content) {
    this.content = content;
  }

  public InputStream getContent() {
    return this.content;
  }

}
