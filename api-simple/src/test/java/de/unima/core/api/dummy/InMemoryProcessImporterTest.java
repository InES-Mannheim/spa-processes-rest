package de.unima.core.api.dummy;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.unima.core.api.Source;

public class InMemoryProcessImporterTest {
	
  @Rule
  public ExpectedException expected = ExpectedException.none();
  
  private InMemoryProcessImporterService service;
  
  @Before
  public void setUp(){
	  service = new InMemoryProcessImporterService();
  }
  
  @Test
  public void dummyProcessImporterShouldSaveTheProcessRetrievedFromSourceAndReturnsGeneratedID(){
    final String dummyProjectID = "dummyProjectID1";
    final Source dummySource = new SimpleSource(new ByteArrayInputStream("dummyProcessID6".getBytes()));
    String processIDTest = service.save(dummyProjectID, dummySource);
    assertThat("The process ID should not be null", processIDTest, is(notNullValue()));
    assertThat("The process ID should not be empty", processIDTest, is(not("")));
    assertThat("It should return a String with the process id generated", processIDTest, isA(String.class));
  }
  
  @Test
  public void itShouldReturnNullForANullSource(){
    expected.expect(NullPointerException.class);
    final String dummyProjectID = "ProjectID";
    final Source dummySource = null;
    service.save(dummyProjectID, dummySource);
  }
  
  @Test
  public void itShouldReturnNullPointerExceptionForANullProjectID(){
    expected.expect(NullPointerException.class);
    final String dummyNullProjectID = null;
    final Source dummySource = mock(Source.class);
    service.save(dummyNullProjectID, dummySource);
  }
  
  @Test
  public void itShouldReturnNullPointerExceptionForAnEmptyProjectID(){
    expected.expect(IllegalArgumentException.class);
    final String dummyEmptyProjectID = "";
    final Source dummySource = mock(Source.class);
    service.save(dummyEmptyProjectID, dummySource);
  }
  
  @Test
  public void itShouldReturnTheSourceIdentifiedByProcessID() throws IOException{
    final String dummyProcessID = "dummyProjectID1-dummyProcessID1";
    Optional<Source> sourceTest = service.getById(dummyProcessID);
    assertThat("The source should be a Source!", sourceTest.get(), isA(Source.class));
  }
  
  @Test
  public void itShouldThrowIllegalStateExceptionForEmptyProcessID(){
	expected.expect(IllegalStateException.class);
    final String dummyEmptyProcessID = "";
    service.getById(dummyEmptyProcessID);
  }
  
  @Test
  public void itShouldThrowExceptionForNullProcessId(){
	expected.expect(NullPointerException.class);
    final String dummyNullProcessID = null;
    service.getById(dummyNullProcessID);
  }
  
  @Test
  public void itShouldReturnFiveProcessIDForAProjectID(){
    final String dummyProjectID = "dummyProjectID1";
    List<String> listProcessIDTest = service.getAll(dummyProjectID);
    assertThat("The list should have 5 elements", listProcessIDTest, hasSize(5));
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForNullProjectID(){
    expected.expect(NullPointerException.class);
    final String dummyNullProjectID = null;
    service.getAll(dummyNullProjectID);
  }
  
  @Test
  public void itShouldThrowIllegalArgumentExceptionForEmptyProjectID(){
    expected.expect(IllegalArgumentException.class);
    final String dummyEmptyProjectID = "";
    service.getAll(dummyEmptyProjectID);
  }

  @Test
  public void itShouldReturnTrueForDeleteProcessWithID(){
    final String dummyProcessId = "dummyProjectID1-dummyProcessID2";
    boolean deleteTest = service.deleteById(dummyProcessId);
    assertThat("The return value should be True", deleteTest, is(true));
  }
  
  @Test
  public void itShouldReturnFalseForDeleteProcessWithNonExistentID(){
    final String dummyProcessId = "nonExistentProcessID";
    boolean deleteTest = service.deleteById(dummyProcessId);
    assertThat("The return value should be True", deleteTest, is(false));
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForNullProcessID(){
    expected.expect(NullPointerException.class);
    final String dummyProcessId = null;
    final Source dummySource = mock(Source.class);
    service.updateById(dummyProcessId, dummySource);
  }
  
  @Test
  public void itShouldThrowNullPointerExceptionForNullSource(){
    expected.expect(NullPointerException.class);
    final String dummyProcessId = "notNullProcessID";
    final Source dummySource = null;
    service.updateById(dummyProcessId, dummySource);
  }
  
  @Test
  public void itShouldThrowIllegalArgumentExceptionForEmptyProcessID(){
    expected.expect(IllegalArgumentException.class);
    final String dummyProcessId = "";
    final Source dummySource = mock(Source.class);
    service.updateById(dummyProcessId, dummySource);
  }
  
  @Test
  public void itShouldFalseForNonExistentProcessID(){
    final String dummyProcessId = "nonExistentProcessID";
    final Source dummySource = mock(Source.class);
    boolean returnTest = service.updateById(dummyProcessId, dummySource);
    assertThat("The return should be false.", returnTest, is(equalTo(false)));
  }
  
  @Test
  public void itShouldReturnTrueForNonExistentProcessID(){
    final String dummyProcessId = "dummyProjectID1-dummyProcessID1";
    final Source dummySource = mock(Source.class);
    boolean returnTest = service.updateById(dummyProcessId, dummySource);
    assertThat("The return should be false.", returnTest, is(equalTo(true)));
  }
}
