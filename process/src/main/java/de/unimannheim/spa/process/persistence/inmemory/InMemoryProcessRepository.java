package de.unimannheim.spa.process.persistence.inmemory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.unimannheim.spa.process.persistence.ProcessRepository;

@Component
@Profile("development")
public class InMemoryProcessRepository implements ProcessRepository {

	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(Process entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
