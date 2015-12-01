package de.unimannheim.spa.process.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class Project implements Entity {

	private final String id;
	
	private String label;
	
	private List<Process> processes;
	
	@SuppressWarnings("unused")
    private int processNumber;
	
	public Project(String id) {
		this.id = id;
		this.label = String.join("-", id, "label");
		this.processes = Lists.newArrayList();
	}
	
	public Project(String id, String label){
	  this.id = id;
	  this.label = label;
	  this.processes = Lists.newArrayList();
	}
	
	public Project(String id, List<Process> processes){
	    this(id);
	    if(processes != null){
	      this.processes.addAll(processes); 
	    } else {
	      this.processes = Lists.newArrayList();
	      this.processes.addAll(processes);
	    }
	}
	
	public Project(String id, String label, List<Process> processes){
	    this(id, processes);
	    this.label = label;
    }

	@Override
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@JsonIgnore
	public List<Process> getProcesses() {
		return processes;
	}

	@JsonProperty
	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}
	
	public boolean addProcess(Process process) {
		if(process == null) processes = Lists.newArrayList();
		return processes.add(process);
	}

    public int getProcessNumber() {
      return processes.size();
    }

}
