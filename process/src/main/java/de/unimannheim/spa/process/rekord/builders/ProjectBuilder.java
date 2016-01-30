/*******************************************************************************
 * Copyright 2016 University of Mannheim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *          http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
  BuildableKey<Project, Rekord<DataPool>> dataPools = RekordKey.named("dataPools").builtFrom(DataPoolBuilder.rekord);
  Key<Project, List<Schema>> linkedSchemas = SimpleKey.named("linkedSchemas");
  Rekord<Project> rekord = Rekords.of(Project.class).accepting(id, label, dataPools, linkedSchemas);
}
