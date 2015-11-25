package de.unimannheim.spa.process.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Process implements Entity {

	private final String id;
	
	private String label;
	
	private Source source;
	
	private String uri;
	
	public Process(String id) {
		this.id = id;
	}
	
	public Process(String id, String label){
	    this(id);
	    this.label = label;
	}
	
	public Process(String id, String label, String uri){
	    this(id, label);
	    this.uri = uri;
	}
	
	public Process(String id, String label, String uri, Source source){
	    this(id, label, uri);
	    this.source = source;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Process)) return false;
		return Objects.equals(this.id, ((Process) obj).id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@JsonIgnore
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getUri() {
	    return uri;
    }
  
    public void setUri(String uri) {
        this.uri = uri;
    }
	

}
