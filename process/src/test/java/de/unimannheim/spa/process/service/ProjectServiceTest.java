package de.unimannheim.spa.process.service;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static de.unimannheim.spa.process.service.InMemoryOps.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.unimannheim.spa.process.config.InMemoryConfig;
import de.unimannheim.spa.process.persistence.ProjectRepository;
import de.unimannheim.spa.process.persistence.inmemory.InMemoryProjectRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@ContextConfiguration(classes={InMemoryConfig.class})
public class ProjectServiceTest {
  
  @Rule
  public ExpectedException expected = ExpectedException.none();
  
  private ProjectService service;
  
  private ProjectRepository repo;
  
  @Before
  public void setUp(){
	  this.repo = new InMemoryProjectRepository(create5ProjectsWith5ProcessesEach());
      service = new ProjectService(repo);
  }
  
  @Test
  public void dummyProjectServiceShouldReturnTrueForDeletingAnExistentProjectID(){
    final String dummyProcessID = "dummyProjectID1-dummyProcessID1";
    final boolean resultTest = service.deleteById(dummyProcessID);
    assertThat("The return value should be True.", resultTest, is(equalTo(true)));
  }
  
  @Test
  public void itShouldReturnFalseForDeletingANonExistentProjectID(){
    final String dummyProcessID = "nonExistentProcessID";
    final boolean resultTest = service.deleteById(dummyProcessID);
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
  public void itShouldCreateAProjectReceivingAProjectTypeAndReturnTheID(){
    final String generateProjectIDTest = service.create(ProjectType.BPMN);
    assertThat("The value returned should be an String.", generateProjectIDTest, isA(String.class));
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForCreateANullProjectType(){
    expected.expect(NullPointerException.class);
    service.create(null);
  }

}
