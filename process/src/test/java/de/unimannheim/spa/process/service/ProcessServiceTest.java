package de.unimannheim.spa.process.service;

import static de.unimannheim.spa.process.service.InMemoryOps.creatRepoWith5ProjectsAnd5ProcessesEach;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.unimannheim.spa.process.api.Source;

public class ProcessServiceTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private ProcessService service;
	
	private Map<String, Map<String, Source>> repo;
	
	@Before
	public void setUp() {
		service = new ProcessService(creatRepoWith5ProjectsAnd5ProcessesEach());
	}

	@Test
	public void inMemoryProcessServiceShouldReturnFiveProcessIDForAProjectID() {
		final String dummyProjectID = "dummyProjectID1";
		List<String> listProcessIDTest = service.getAll(dummyProjectID);
		assertThat("The list should have 5 elements", listProcessIDTest, hasSize(5));
	}

	@Test
	public void itShouldThrowNullPointerExceptionForNullProjectID() {
		expected.expect(NullPointerException.class);
		final String dummyNullProjectID = null;
		service.getAll(dummyNullProjectID);
	}

	@Test
	public void itShouldThrowIllegalArgumentExceptionForEmptyProjectID() {
		expected.expect(IllegalArgumentException.class);
		final String dummyEmptyProjectID = "";
		service.getAll(dummyEmptyProjectID);
	}

	@Test
	public void itShouldReturnTrueForDeleteProcessWithID() {
		final String dummyProcessId = "dummyProjectID1-dummyProcessID2";
		boolean deleteTest = service.deleteById(dummyProcessId);
		assertThat("The return value should be True", deleteTest, is(true));
	}

	@Test
	public void itShouldReturnFalseForDeleteProcessWithNonExistentID() {
		final String dummyProcessId = "nonExistentProcessID";
		boolean deleteTest = service.deleteById(dummyProcessId);
		assertThat("The return value should be True", deleteTest, is(false));
	}

	@Test
	public void itShouldThrowNullPointerExceptionForNullProcessID() {
		expected.expect(NullPointerException.class);
		final String dummyProcessId = null;
		final Source dummySource = mock(Source.class);
		service.updateById(dummyProcessId, dummySource);
	}

	@Test
	public void itShouldThrowNullPointerExceptionForNullSource() {
		expected.expect(NullPointerException.class);
		final String dummyProcessId = "notNullProcessID";
		final Source dummySource = null;
		service.updateById(dummyProcessId, dummySource);
	}

	@Test
	public void itShouldThrowIllegalArgumentExceptionForEmptyProcessID() {
		expected.expect(IllegalArgumentException.class);
		final String dummyProcessId = "";
		final Source dummySource = mock(Source.class);
		service.updateById(dummyProcessId, dummySource);
	}

	@Test
	public void itShouldFalseForNonExistentProcessID() {
		final String dummyProcessId = "nonExistentProcessID";
		final Source dummySource = mock(Source.class);
		boolean returnTest = service.updateById(dummyProcessId, dummySource);
		assertThat("The return should be false.", returnTest, is(equalTo(false)));
	}

	@Test
	public void itShouldReturnTrueForNonExistentProcessID() {
		final String dummyProcessId = "dummyProjectID1-dummyProcessID1";
		final Source dummySource = mock(Source.class);
		boolean returnTest = service.updateById(dummyProcessId, dummySource);
		assertThat("The return should be true.", returnTest, is(equalTo(true)));
	}
	
	@Test
	public void itShouldAddTheNewProcessToGivenProject() {
		final String processId = service.create("dummyProjectID1");
		assertThat(repo.get("dummyProjectID1").keySet(), hasItem(processId));
	}
	
	@Test
	public void newProcessShouldHaveEmptySourceAsSource() throws IOException {
		final String processId = service.create("dummyProjectID1");
		assertThat(repo.get("dummyProjectID1").get(processId).getContent().read(), is(-1));
	}
}
