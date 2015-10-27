package de.unima.core.api.dummy;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;

import de.unima.core.api.ProjectType;


public class DummyProjectServiceTest {
  
  @Test
  public void dummyProjectServiceShouldDeleteTheProcessWithID(){
    final String dummyProcessID = "anyID";
    final DummyProjectService dummyProjectService = new DummyProjectService();
    boolean result = dummyProjectService.delete(dummyProcessID);
    assertThat("The process "+dummyProcessID+" was not deleted!", result, is(equalTo(true)));
  }
  
  @Test
  public void dummyProjectServiceShouldReturnFalseIfProcessIDIsEmptyOrNull(){
    final DummyProjectService dummyProjectService = new DummyProjectService();
    boolean resultEmptyTest = dummyProjectService.delete("");
    boolean resultNullTest = dummyProjectService.delete(null);
    assertThat("The processID is empty, it should return false!", resultEmptyTest, is(equalTo(false)));
    assertThat("The processID is null, it should return false!", resultNullTest, is(equalTo(false)));
  }

  @Test
  public void dummyProjectServiceShouldReturnAListWithAllProjectsIDInTheDB(){
    final DummyProjectService dummyProjectService = new DummyProjectService();
    List<String> dummyProjectIDListTest = dummyProjectService.listAll();
    assertThat("The list should not be null!", dummyProjectIDListTest, is(not(equalTo(null))));
    assertThat("The list should not be empty!", dummyProjectIDListTest.size(), is(not(equalTo(0))));
    assertThat("The list should have 5 elements!", dummyProjectIDListTest, IsCollectionWithSize.hasSize(5));
    assertThat("The first element should be 'DummyProjectID1'!", dummyProjectIDListTest.get(0), is("DummyProjectID1"));
    assertThat("The last element should be 'DummyProjectID5'!", dummyProjectIDListTest.get(4), is("DummyProjectID5"));
  }
  
  @Test
  public void dummyProjectServiceShouldCreateAProjectReceivingAProjectTypeAndReturnTheID(){
    final DummyProjectService dummyProjectService = new DummyProjectService();
    String projectIDTest = dummyProjectService.create(ProjectType.BPMN);
    assertThat("The Project ID should not be null", projectIDTest, is(notNullValue()));
    assertThat("The Project ID should not be empty", projectIDTest, is(not("")));
    assertThat("The Project ID should be 'DummyProjectID'", projectIDTest, is("DummyProjectID"));
  }

}
