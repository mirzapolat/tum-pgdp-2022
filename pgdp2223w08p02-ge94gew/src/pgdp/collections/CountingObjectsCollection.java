package pgdp.collections;

import java.util.HashMap;
import java.util.List;

public class CountingObjectsCollection<K> {
	private HashMap<K, Integer> map;

	public CountingObjectsCollection() {
		map = new HashMap<>();
	}

	public CountingObjectsCollection(CountingObjectsCollection<K> old) {
		map = new HashMap<>(old.map);
	}

	public void insert(K obj) {
		map.put(obj, map.getOrDefault(obj, 0) + 1);
	}

	public int getObjectCount(K obj) {
		return map.getOrDefault(obj, 0);
	}

	public int getTotalObjectCount() {
		int sum = 0;
		for(K k: map.keySet()) sum += map.get(k);
		return sum;
	}

	public List<K> getKeyList() {
		return map.keySet().stream().toList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CountingObjectsCollection<K> other = (CountingObjectsCollection<K>) obj;
		// Assure that both maps are equal without using map.equals
		// TODO 6
		return false;
	}
}
