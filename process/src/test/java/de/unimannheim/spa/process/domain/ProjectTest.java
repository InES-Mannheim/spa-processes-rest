package de.unimannheim.spa.process.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ProjectTest{

    private final String PROJECT_ID_TEST = "ProjectIDTest";
  
    private Project projectToTest;
    
    @Before
    public void init(){
        projectToTest = new Project(PROJECT_ID_TEST, generate5Processes());        
    }
    
    @Test
    public void projectShouldReturnListOf5Processes(){
        final List<Process> expectedProcesses = generate5Processes();
        
    }
    
    private List<Process> generate5Processes(){
        List<Process> processes = Lists.newLinkedList();
        IntStream.range(0, 5).forEach(i -> {
            Process tmpProcess = new Process("ProcessID"+i);
            processes.add(tmpProcess);
        });
        processes.stream().collect(Collectors.counting());
        return processes;
    }
    
    
}
