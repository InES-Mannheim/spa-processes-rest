package de.unimannheim.spa.process.domain;

import java.util.Objects;

public class Process implements Entity {

	private final String id;
	
	private String label;
	
	private Source source;
	
	public Process(String id) {
		this.id = id;
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

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
	

}
