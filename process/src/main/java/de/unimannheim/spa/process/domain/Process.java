package de.unimannheim.spa.process.domain;

public class Process implements Entity {

	private String id;

	public Process(String id) {
		this.id = id;
	
	}
	
	@Override
	public String getId() {
		return id;
	}

}
