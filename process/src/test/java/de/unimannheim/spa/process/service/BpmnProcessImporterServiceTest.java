package de.unimannheim.spa.process.service;

import static de.unimannheim.spa.process.service.InMemoryOps.creatRepoWith5ProjectsAnd5ProcessesEach;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.unimannheim.spa.process.domain.Source;

public class BpmnProcessImporterServiceTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();

	private BpmnProcessImporterService service;

	@Before
	public void setUp() {
		service = new BpmnProcessImporterService(creatRepoWith5ProjectsAnd5ProcessesEach());
	}

	@Test
	public void dummyProcessImporterShouldSaveTheProcessRetrievedFromSourceAndReturnsGeneratedID() {
		final String dummyProjectID = "dummyProjectID1";
		final Source dummySource = new Source(new ByteArrayInputStream("dummyProcessID6".getBytes()));
		String processIDTest = service.importProcess(dummyProjectID, dummySource);
		assertThat("The process ID should not be null", processIDTest, is(notNullValue()));
		assertThat("The process ID should not be empty", processIDTest, is(not("")));
		assertThat("It should return a String with the process id generated", processIDTest, isA(String.class));
	}

	@Test
	public void itShouldReturnNullForANullSource() {
		expected.expect(NullPointerException.class);
		final String dummyProjectID = "ProjectID";
		final Source dummySource = null;
		service.importProcess(dummyProjectID, dummySource);
	}

	@Test
	public void itShouldReturnNullPointerExceptionForANullProjectID() {
		expected.expect(NullPointerException.class);
		final String dummyNullProjectID = null;
		final Source dummySource = mock(Source.class);
		service.importProcess(dummyNullProjectID, dummySource);
	}

	@Test
	public void itShouldReturnNullPointerExceptionForAnEmptyProjectID() {
		expected.expect(IllegalArgumentException.class);
		final String dummyEmptyProjectID = "";
		final Source dummySource = mock(Source.class);
		service.importProcess(dummyEmptyProjectID, dummySource);
	}

	@Test
	public void itShouldReturnTheSourceIdentifiedByProcessID() throws IOException {
		final String dummyProcessID = "dummyProjectID1-dummyProcessID1";
		Optional<Source> sourceTest = service.exportProcess(dummyProcessID);
		assertThat("The source should be a Source!", sourceTest.get(), isA(Source.class));
	}

	@Test
	public void itShouldThrowIllegalStateExceptionForEmptyProcessID() {
		expected.expect(IllegalStateException.class);
		final String dummyEmptyProcessID = "";
		service.exportProcess(dummyEmptyProcessID);
	}

	@Test
	public void itShouldThrowExceptionForNullProcessId() {
		expected.expect(NullPointerException.class);
		final String dummyNullProcessID = null;
		service.exportProcess(dummyNullProcessID);
	}
}
