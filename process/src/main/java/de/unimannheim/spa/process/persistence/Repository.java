package de.unimannheim.spa.process.persistence;

import java.util.List;

public interface Repository<I,T> {
	public boolean deleteById(I id);
	public boolean save(T entity);
	public List<T> getAll();
}
