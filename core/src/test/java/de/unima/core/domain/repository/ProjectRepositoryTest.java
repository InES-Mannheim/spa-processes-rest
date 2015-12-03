package de.unima.core.domain.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Before;
import org.junit.Test;

import de.unima.core.domain.DataPool;
import de.unima.core.domain.Project;
import de.unima.core.domain.Repository;
import de.unima.core.domain.Schema;
import de.unima.core.domain.Vocabulary;
import de.unima.core.storage.jena.JenaTDBStore;

public class ProjectRepositoryTest {

	private ProjectRepository projectRepository;
	private final Property containsDatapool = ResourceFactory.createProperty(Vocabulary.containsDataPool);
	private final Property belongsToRepository = ResourceFactory.createProperty(Vocabulary.belongsToRepository);
	private final Property linksSchema = ResourceFactory.createProperty(Vocabulary.linksSchema);
	private final Resource project = ResourceFactory.createResource("http://www.test.de/Project/1");
	private final Resource datapool = ResourceFactory.createResource("http://www.test.de/DataPool/1");
	private final Resource schema = ResourceFactory.createResource("http://www.test.de/Schema/1");
	private final Resource type = ResourceFactory.createResource(Vocabulary.Project);
	private final Resource repository = ResourceFactory.createResource("http://www.test.de/Repository/1");
	

	@Before
	public void setup() {
		projectRepository = new ProjectRepository(JenaTDBStore.withUniqueMemoryLocation());
	}
	
	@Test
	public void whenRepositoryIsSavedItShouldContainLinkedSchemasAndContainingDataBuckets(){
		projectRepository.save(createProject());
		
		final boolean containsSchemasAndDataPools = projectRepository.getStore().readWithConnection(connection -> connection.as(Dataset.class).map(dataset -> {
			final Model namedModel = dataset.getNamedModel("http://www.test.de/Project/1/graph");
			return namedModel.contains(ResourceFactory.createStatement(project, RDF.type, type)) &&
			namedModel.contains(ResourceFactory.createStatement(project, linksSchema, schema)) &&
			namedModel.contains(project, RDFS.label, ResourceFactory.createTypedLiteral("first")) &&
			namedModel.contains(ResourceFactory.createStatement(project, containsDatapool, datapool)) &&
			namedModel.contains(this.project, belongsToRepository, repository);
		})).get().get();
		
		assertThat(containsSchemasAndDataPools, is(true));
	}
	
	@Test
	public void whenSavedRepositoryIsDeletedTheCorrespondingNamedModelShouldBeEmpty(){
		final Project project = createProject();
		
		projectRepository.save(project);
		projectRepository.delete(project);
		
		final boolean modelIsEmpty = projectRepository.getStore().readWithConnection(connection -> connection.as(Dataset.class).map(dataset -> {
			return dataset.getNamedModel("http://www.test.de/Project/1/graph").isEmpty();
		})).get().get();
		
		assertThat(modelIsEmpty, is(true));
	}
	
	@Test
	public void savedProjectShouldBeSameAsLoadedProject(){
		final Project project = createProject();
		projectRepository.save(project);
		final Optional<Project> loadedProject = projectRepository.findById(project.getId());
		assertThat(loadedProject.isPresent(),is(true));
		assertThat(loadedProject.get(), is(equalTo(project)));
		assertThat(loadedProject.get().getLinkedSchemas().get(0), is(equalTo(project.getLinkedSchemas().get(0))));
		assertThat(loadedProject.get().getDataPools().get(0), is(equalTo(project.getDataPools().get(0))));
	}
	
	private Project createProject() {
		final Repository repo = new Repository("http://www.test.de/Repository/1");
		final Project project = new Project("http://www.test.de/Project/1", "first" , repo);
		project.addDataPool(new DataPool("http://www.test.de/DataPool/1", project));
		project.linkSchema(new Schema("http://www.test.de/Schema/1"));
		return project;
	}
}
