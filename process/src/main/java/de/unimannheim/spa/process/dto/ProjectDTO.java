package de.unimannheim.spa.process.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.jena.ext.com.google.common.collect.Maps;

import com.google.common.collect.ImmutableList;

import de.unima.core.domain.model.DataPool;
import de.unima.core.domain.model.Schema;

public class ProjectDTO{

  private String id;
  private String label;
  private final Map<String, DataPool> dataPools;
  private final Map<String, Schema> schemas;
  
  public ProjectDTO(String id, String label){
    this(id, label, Collections.emptyList(), Collections.emptyList());
  }
  
  public ProjectDTO(String id, String label, List<DataPool> dataPools){
     this(id, label, dataPools, Collections.emptyList());
  }
  
  public ProjectDTO(String id, String label, List<DataPool> dataPools, List<Schema> schemas) {
    super();
    this.id = id;
    this.label = label;
    this.dataPools = Maps.newHashMap();
    for(DataPool pool:dataPools){
        this.dataPools.put(pool.getId(), pool);
    }
    this.schemas = Maps.newHashMap();
    for(Schema schema:schemas){
        this.schemas.put(schema.getId(), schema);
    }
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getLabel() {
    return label;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public List<DataPool> getDataPools(){
    return ImmutableList.<DataPool>builder().addAll(dataPools.values()).build();
  }
  
  public List<Schema> getLinkedSchemas(){
    return ImmutableList.<Schema>builder().addAll(schemas.values()).build();
  }

}
