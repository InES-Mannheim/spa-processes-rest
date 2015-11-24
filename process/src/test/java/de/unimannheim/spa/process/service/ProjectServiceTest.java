package de.unimannheim.spa.process.service;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.unimannheim.spa.process.config.InMemoryConfig;
import de.unimannheim.spa.process.domain.Project;
import de.unimannheim.spa.process.domain.Process;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@ContextConfiguration(classes={InMemoryConfig.class})
public class ProjectServiceTest {
  
  @Rule
  public ExpectedException expected = ExpectedException.none();
  
  @Autowired
  private ProjectService service;
  
  @Test
  public void dummyProjectServiceShouldReturnTrueForDeletingAnExistentProjectID(){
    final String dummyProjectID = "dummyProjectID1";
    final boolean resultTest = service.deleteById(dummyProjectID);
    assertThat("The return value should be True.", resultTest, is(equalTo(true)));
  }
  
  @Test
  public void itShouldReturnFalseForDeletingANonExistentProjectID(){
    final String dummyProjectID = "nonExistentProcessID";
    final boolean resultTest = service.deleteById(dummyProjectID);
    assertThat("The return value should be False.", resultTest, is(equalTo(false)));
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForDeletingANullProjectID(){
    expected.expect(NullPointerException.class);
    final String dummyProcessID = null;
    service.deleteById(dummyProcessID);
  }
  
  @Test
  public void itShouldThrowIllegalArgumentExceptionForDeletingAnEmptyProjectID(){
    expected.expect(IllegalArgumentException.class);
    final String dummyProcessID = "";
    service.deleteById(dummyProcessID);
  }
  
  @Test
  public void itShouldCreateAndReturn5Projects(){
    final List<Project> toTest = service.listAll();
    assertThat("The size of the list should be 5.", toTest, hasSize(5));
  }
  
  @Test
  public void itShouldCreateAProjectReceivingAProjectTypeAndReturnTheID(){
    final String generateProjectIDTest = service.create(ProjectType.BPMN);
    assertThat("The value returned should be an String.", generateProjectIDTest, isA(String.class));
  }

  @Test
  public void itShouldReturn6ProjectsAfterCreatingANewProject(){
    service.create(ProjectType.BPMN);
    final List<Project> toTest = service.listAll();
    assertThat("The size of the list should be 6.", toTest, hasSize(6));
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForCreateANullProjectType(){
    expected.expect(NullPointerException.class);
    service.create(null);
  }
  
  @Test
  public void itShouldReturnTrueForAddingANewProcessToProjectID(){
    final String dummyProjectID = "dummyProjectID1";
    final Process mockedProcessToAdd = Mockito.mock(Process.class);
    final boolean returnToTest = service.addProcess(dummyProjectID, mockedProcessToAdd, ProjectType.BPMN);
    assertThat("The return should be true.", returnToTest, is(equalTo(true)));
  }
  
  @Test
  public void itShouldReturnFalseForAddingANewProcessToNonExistentProjectID(){
    final String dummyProjectID = "nonExistentProjectID";
    final Process mockedProcessToAdd = Mockito.mock(Process.class);
    final boolean returnToTest = service.addProcess(dummyProjectID, mockedProcessToAdd, ProjectType.BPMN);
    assertThat("The return should be false.", returnToTest, is(equalTo(false)));
  }
  
  @Test
  public void itShouldReturnOptionalProjectForProjectID(){
    final String dummyProjectID = "dummyProjectID4";
    final Optional<Project> projectToTest = service.findById(dummyProjectID);
    assertThat("It should return an Optional<Project>", projectToTest, isA(Optional.class));
  }

}
