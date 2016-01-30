package de.unimannheim.spa.process.rekord.builders;

import java.util.List;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;

import de.unima.core.domain.model.DataBucket;
import de.unima.core.domain.model.DataPool;

public interface DataPoolBuilder {
  Key<DataPool, List<DataBucket>> buckets = SimpleKey.named("buckets");
  Rekord<DataPool> rekord = Rekords.of(DataPool.class).accepting(buckets);
}
