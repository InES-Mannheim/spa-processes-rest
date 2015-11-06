package de.unimannheim.spa.process.domain;

import java.util.Objects;

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
	
	default boolean equals(Object obj) {
		return Objects.equals(obj, this);
	}
	
	default	int hashCode() {
		return Objects.hash(getId());
	}
}
