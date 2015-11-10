package de.unimannheim.spa.process.persistence;

public interface Repository<I,T> {
	public boolean deleteById(I id);
	public boolean save(T entity);
}
