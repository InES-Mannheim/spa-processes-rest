package de.unimannheim.spa.process.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class Project implements Entity {

	private final String id;
	
	private String label;
	
	private List<Process> processes;
	
	public Project(String id) {
		this.id = id;
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

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}
	
	public boolean addProcess(Process process) {
		if(process == null) processes = Lists.newArrayList();
		return processes.add(process);
	}

}
