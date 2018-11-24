package decisiontree.entropy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CounterMap <K> implements Iterable<Map.Entry<K, Integer>>, Cloneable {
	  private final Map<K, Integer> map = new HashMap<>();

	  public int incrementAndGet(K key) {
	    return incrementAndGet(key, 1);
	  }

	  public int incrementAndGet(K key, int count) {
	    Integer value = map.get(key);
	    if (value == null) {
	      value = 0;
	    }
	    int newValue = count + value;
	    map.put(key, newValue);
	    return newValue;
	  }

	  public int get(K key) {
	    if (!map.containsKey(key)) {
	      return 0;
	    }

	    return map.get(key);
	  }

	  public void set(K key, int newValue) {
	    map.put(key, newValue);
	  }

	  public void reset(K key) {
	    map.remove(key);
	  }

	  public int size() {
	    return map.size();
	  }

	  public Iterator<Map.Entry<K, Integer>> iterator() {
	    return map.entrySet().iterator();
	  }

	  public Collection<Integer> values() {
	    return map.values();
	  }

	  public Set<K> keySet() {
	    return map.keySet();
	  }

	  public String toString() {
	    StringBuilder strVal = new StringBuilder();
	    for (Map.Entry<K, Integer> entry : this) {
	      strVal.append(entry.getKey().toString()).append(": ").append(entry.getValue()).append('\n');
	    }
	    return strVal.toString();
	  }

	  public Map<K, Integer> toMap() {
	    return map;
	  }

	  @Override
	  public CounterMap<K> clone() {
	    CounterMap<K> newInstance = new CounterMap<K>();
	    newInstance.map.putAll(map);
	    return newInstance;
	  }
	}