package de.unimannheim.spa.process.service;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.unimannheim.spa.process.api.ProjectType;
import de.unimannheim.spa.process.service.ProjectService;


public class ProjectServiceTest {
  
  @Rule
  public ExpectedException expected = ExpectedException.none();
  
  private ProjectService service;
  
  @Before
  public void setUp(){
      service = new ProjectService();
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
  public void itShouldReturnAListWithFiveProjectsID(){
    final ProjectService dummyProjectService = new ProjectService();
    final List<String> projectIDListTest = dummyProjectService.listAll();
    assertThat("The list should contain 5 elements.", projectIDListTest, hasSize(5));
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
