package im.juejin.bloomfilter.bitset;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Implement bloom filter on redis bitset.
 */
public class RedisBitSet implements BaseBitSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private RedisTemplate<String, Boolean> redisTemplate;

	private String name;

	public RedisBitSet(String name) {
		this.name = name;
	}

	public void set(int bitIndex) {
		redisTemplate.opsForValue().setBit(this.name, bitIndex, true);
	}

	public void set(int bitIndex, boolean value) {
		redisTemplate.opsForValue().setBit(this.name, bitIndex, value);
	}

	public boolean get(int bitIndex) {
		return redisTemplate.opsForValue().getBit(this.name, bitIndex);
	}

	public void clear(int bitIndex) {
		redisTemplate.opsForValue().setBit(this.name, bitIndex, false);
	}

	public void clear() {
		redisTemplate.delete(this.name);
	}

	public long size() {
		return redisTemplate.getConnectionFactory().getConnection().bitCount(this.name.getBytes());
	}

	public boolean isEmpty() {
		return size() <= 0;
	}

}
