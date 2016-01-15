package de.unimannheim.spa.process.rekord.builders;

import java.util.List;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.*;

import de.unima.core.domain.model.DataPool;
import de.unima.core.domain.model.Project;
import de.unima.core.domain.model.Schema;

public interface ProjectBuilder {
  Key<Project, String> id = SimpleKey.named("id");
  Key<Project, String> label = SimpleKey.named("label");
  Key<Project, List<DataPool>> dataPools = SimpleKey.named("dataPools");
  Key<Project, List<Schema>> linkedSchemas = SimpleKey.named("linkedSchemas");
  Rekord<Project> rekord = Rekords.of(Project.class).accepting(id, label, dataPools, linkedSchemas);
}
