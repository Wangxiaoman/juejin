package im.juejin.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LocalCache {
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private static AtomicReference<Map<String, AtomicLong>> counter = new AtomicReference<>(new ConcurrentHashMap<>());

	private String getRedisKey(String domain) {
		String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
		return domain + ":" + dateStr;
	}

	// 域名每次请求调用该服务，写入本地Map中
	public void counter(String domain) {
		counter.get().getOrDefault(domain, new AtomicLong()).incrementAndGet();
	}

	// 从redis中获取该域名的访问计数
	public Long getCounter(String domain) {
		String counter = stringRedisTemplate.opsForValue().get(getRedisKey(domain));
		return StringUtils.isBlank(counter) ? 0 : Long.valueOf(counter);
	}

	// 每1s写入redis中
	@Scheduled(cron = "0/1 * * * * ?")
	public void updateCounterToRedis() {
		Map<String, AtomicLong> domainCounter = counter.getAndSet(new ConcurrentHashMap<>());
		for (Entry<String, AtomicLong> entry : domainCounter.entrySet()) {
			stringRedisTemplate.opsForValue().increment(getRedisKey(entry.getKey()), entry.getValue().get());
		}
	}
}
