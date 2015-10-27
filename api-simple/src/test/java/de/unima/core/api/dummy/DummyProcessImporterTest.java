package de.unima.core.api.dummy;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import org.junit.Test;

import de.unima.core.api.Source;

public class DummyProcessImporterTest {
  
  @Test
  public void dummyProcessImporterShouldSaveTheProcessRetrievedFromSourceAndReturnsGeneratedID(){
    final String dummyProjectID = "dummyProjectID1";
    final Source dummySource = new DummySource(new ByteArrayInputStream("dummyProcessID6".getBytes()));
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    String processIDTest = dummyProcessImporterService.save(dummyProjectID, dummySource);
    assertThat("The process ID should not be null", processIDTest, is(notNullValue()));
    assertThat("The process ID should not be empty", processIDTest, is(not("")));
    assertThat("The process ID should be equal to 'dummyProjectID1-dummyProcessID6'", processIDTest, is(equalTo("dummyProjectID1-dummyProcessID6")));
  }
  
  @Test
  public void dummyProcessImporterShouldReturnNullForANullSource(){
    final String dummyProjectID = "ProjectID";
    final Source dummySource = null;
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    String processIDTest = dummyProcessImporterService.save(dummyProjectID, dummySource);
    assertThat("The process ID should be null", processIDTest, is(nullValue()));
  }
  
  @Test
  public void dummyProcessImporterShouldReturnNullForANullOrEmptyProjectID(){
    final String dummyEmptyProjectID = "";
    final String dummyNullProjectID = null;
    final Source dummySource = mock(Source.class);
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    String processIDEmptyTest = dummyProcessImporterService.save(dummyEmptyProjectID, dummySource);
    String processIDNullTest = dummyProcessImporterService.save(dummyNullProjectID, dummySource);
    assertThat("The process ID should be null for an empty project ID", processIDEmptyTest, is(nullValue()));
    assertThat("The process ID should be null for a null project ID", processIDNullTest, is(nullValue()));
  }
  
  @Test
  public void dummyProcessImporterShouldReturnTheSourceIdentifiedByProcessID() throws IOException{
    final String dummyProcessID = "dummyProjectID1-dummyProcessID1";
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    Source sourceTest = dummyProcessImporterService.getById(dummyProcessID);
    assertThat("The source should not be null!", sourceTest, is(notNullValue()));
    assertThat("The source should be a Source!", sourceTest, isA(Source.class));
  }
  
  @Test
  public void dummyProcessImporterShouldReturnNullForEmptyOrNullProcessID(){
    final String dummyEmptyProcessID = "";
    final String dummyNullProcessID = null;
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    Source sourceWithEmptyProcessIDTest = dummyProcessImporterService.getById(dummyEmptyProcessID);
    Source sourceWithNullProcessIDTest = dummyProcessImporterService.getById(dummyNullProcessID);
    assertThat("The source should be null!", sourceWithEmptyProcessIDTest, is(nullValue()));
    assertThat("The source should be null!", sourceWithNullProcessIDTest, is(nullValue()));
  }
  
  @Test
  public void dummyProcessImporterShouldReturnFiveProcessIDForAProjectID(){
    final String dummyProjectID = "dummyProjectID1";
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    List<String> listProcessIDTest = dummyProcessImporterService.getAll(dummyProjectID);
    assertThat("The list should not be null!", listProcessIDTest, is(notNullValue()));
    assertThat("The list should not be empty!", listProcessIDTest, is(not(empty())));
    assertThat("The list should have 5 elements!", listProcessIDTest, hasSize(5));
    assertThat("The list should have the elements 'dummyProjectID1-dummyProcessID1' to 'dummyProjectID1-dummyProcessID5'!", listProcessIDTest, hasItems("dummyProjectID1-dummyProcessID1", "dummyProjectID1-dummyProcessID2", "dummyProjectID1-dummyProcessID3", "dummyProjectID1-dummyProcessID4", "dummyProjectID1-dummyProcessID5"));
  }
  
  public void dummyProcessImporterShouldReturnNullForEmptyOrNullProjectID(){
    final String dummyEmptyProjectID = "";
    final String dummyNullProjectID = null;
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    List<String> listForEmptyProjectID = dummyProcessImporterService.getAll(dummyEmptyProjectID);
    List<String> listForNullProjectID = dummyProcessImporterService.getAll(dummyNullProjectID);
    assertThat("The list should be null!", listForEmptyProjectID, is(nullValue()));
    assertThat("The list should be null!", listForNullProjectID, is(nullValue()));
  }

  public void dummyProcessImporterShouldDeleteProcessWithID(){
    final String dummyProcessId = "dummyProjectID1-dummyProcessID2";
    final DummyProcessImporterService dummyProcessImporterService = new DummyProcessImporterService();
    boolean deleteTest = dummyProcessImporterService.deleteById(dummyProcessId);
    
  }
}
