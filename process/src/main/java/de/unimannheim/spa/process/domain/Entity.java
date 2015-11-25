package de.unimannheim.spa.process.domain;

/**
 * General purpose interface identifying each implementing type as entity.
 * <p>
 * An entity is defined as a "thing with identity". More general, this means
 * it can be uniquely identified with an identifier. 
 * 
 * @author Gregor Trefs
 */
public interface Entity {
	String getId();
}
