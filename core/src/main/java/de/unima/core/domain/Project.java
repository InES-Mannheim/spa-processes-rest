package de.unima.core.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.jena.ext.com.google.common.collect.Maps;

import com.google.common.collect.ImmutableList;

import de.unima.core.persistence.AbstractEntity;

/**
 * A project combines {@link Schema}s and {@link DataPool}s.
 */
public class Project extends AbstractEntity<String>{
	
	private final Repository repository;
	private final Map<String, DataPool> datapools;
	private final Map<String, Schema> schemas;
	
	public Project(String id, Repository repository, String label) {
		super(id, label);
		this.repository = repository;
		this.datapools = Maps.newHashMap();
		this.schemas = Maps.newHashMap();
	}

	/**
	 * Returns the repository this project belongs to.
	 * 
	 * @return {@link Repository} of this project
	 */
	public Repository getRepository(){
		return repository;
	}
	
	/**
	 * Adds a new {@link DataPool}.
	 * 
	 * @param dataPool which should be added
	 * @return true if successful; false otherwise
	 */
	public boolean addDataPool(DataPool dataPool){
		datapools.put(dataPool.getId(), dataPool);
		return true;
	}
	
	/**
	 * Returns {@code DataPool}s which belong to this project.
	 * 
	 * @return All {@code DataPool}s
	 */
	public List<DataPool> getDataPools(){
		return ImmutableList.<DataPool>builder().addAll(datapools.values()).build();
	}
	
	/**
	 * Finds the {@code DataPool} with given id.
	 * 
	 * @param id of the pool
	 * @return the pool if found; empty otherwise
	 */
	public Optional<DataPool> findDataPoolById(String id){
		return Optional.ofNullable(datapools.get(id));
	}
	
	/**
	 * Removes and returns the {@code DataPool} with given id.
	 * 
	 * @param id of the pool
	 * @return the pool if found; empty otherwise
	 */
	public Optional<DataPool> removeDataPoolById(String id){
		return Optional.ofNullable(datapools.remove(id));
	}
	
	/**
	 * Returns all schemas which are linked to 
	 * 
	 * @return all linked schemas
	 */
	public List<Schema> getLinkedSchemas(){
		return ImmutableList.<Schema>builder().addAll(schemas.values()).build();
	}
	
	/**
	 * Is given schema with given id linked?
	 * 
	 * @param id of the schema
	 */
	public boolean isSchemaLinked(String id){
		return findLinkedSchemaById(id).isPresent();
	}
	
	/**
	 * Finds the schema with given id.
	 * 
	 * @param id of the schema
	 * @return the schema; empty otherwise
	 */
	public Optional<Schema> findLinkedSchemaById(String id){
		return Optional.of(schemas.get(id));
	}
	
	/**
	 * Adds given schema to this project.
	 * 
	 * @param schema the new schema
	 * @return true if successful; false otherwise 
	 */
	public boolean linkSchema(Schema schema){
		schemas.put(schema.getId(), schema);
		return true;
	}
	
	/**
	 * Unlinks given schema from this project.
	 * 
	 * @param id of the schema which should be unlinked
	 * @return unlinked Schema if successful; empty otherwise
	 */
	public Optional<Schema> unlinkSchema(String id){
		return Optional.ofNullable(schemas.remove(id));
	}
	
	/**
	 * Unlinks all schemas.
	 * 
	 * @return all previously linked schemas
	 */
	public List<Schema> unlinkAllSchemas(){
		return schemas.keySet().stream().map(schemas::remove).collect(Collectors.toList());
	}
}
