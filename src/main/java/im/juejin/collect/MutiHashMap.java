package im.juejin.collect;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class MutiHashMap<K, V> {
	private Map<K, TreeSet<V>> map = new HashMap<>();

	public TreeSet<V> get(K k) {
		return map.get(k);
	}

	public TreeSet<V> put(K k, V v) {
		TreeSet<V> set = map.getOrDefault(k, new TreeSet<V>());
		set.add(v);
		return map.put(k, set);
	}
	
	public TreeSet<V> remove(K k){
		return map.remove(k);
	}

	public static void main(String[] args) {
		MutiHashMap<String, Integer> mutiMap = new MutiHashMap<>();
		mutiMap.put("kv1", 1);
		mutiMap.put("kv1", 2);
		mutiMap.put("kv1", 3);
		mutiMap.put("kv1", 2);
		System.out.println("mutiMap kv1:" + mutiMap.get("kv1"));
	}
}
