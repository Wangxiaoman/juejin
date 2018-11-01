package im.juejin.collect;

import java.util.ArrayList;
import org.springframework.util.CollectionUtils;
import lombok.Data;

public class IpList {
	private static ArrayList<Ip> ipList = new ArrayList<>();

	public static void load() {
		ipList.add(new Ip("0.0.0.0", "0.255.255.255", "保留地址", "无"));
		ipList.add(new Ip("1.0.0.0", "1.0.0.255", "美国", "亚太互联网络信息中心"));
		ipList.add(new Ip("1.0.1.0", "1.0.3.154", "中国福建省福州市","中国电信"));
		ipList.add(new Ip("1.0.3.155", "1.0.3.255", "中国福建省泉州市","中国电信"));
		ipList.add(new Ip("1.0.4.0", "1.0.7.255", "澳大利亚墨尔本","Goldenit有限公司"));
		ipList.add(new Ip("1.0.9.0", "255.255.255.255", "其他","其他"));
	}

	public static Ip search(String ip) {
		long longIp = Ip.ip2Long(ip);
		if(CollectionUtils.isEmpty(ipList)){
			return null;
		}
		int index = binarySearch(ipList, 0, ipList.size(), longIp);
		if(index >= 0){
			return ipList.get(index);
		}
		return null;
	}
	
	private static int binarySearch(ArrayList<Ip> ips, int fromIndex, int toIndex, long ip) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Ip midIp =  ips.get(mid);
			// 该ip如果大于当前节点的结束ip，那么向后搜索
			if (midIp.getEndIp() < ip)
				low = mid + 1;
			// 该ip如果小于当前节点的开始ip，那么向前搜索
			else if (midIp.getBeginIp() > ip)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	public static void main(String[] args) {
		load();
		System.out.println(search("243.12.1.1"));
		System.out.println(search("0.0.0.1"));
		System.out.println(search("1.0.1.0"));
		System.out.println(search("1.0.2.255"));
		System.out.println(search("1.0.8.0"));
	}
}

@Data
class Ip {
	public Ip(String beginIpStr, String endIpStr, String address, String operator) {
		this.beginIp = ip2Long(beginIpStr);
		this.endIp = ip2Long(endIpStr);
		this.address = address;
		this.operator = operator;
	}

	private long beginIp;
	private long endIp;
	private String address;
	private String operator;

	// ip 转换
	public static Long ip2Long(final String ip) {
		Long iplong = null;
		try {
			final String[] ipNums = ip.split("\\.");
			iplong = (Long.parseLong(ipNums[0]) << 24) + (Long.parseLong(ipNums[1]) << 16)
					+ (Long.parseLong(ipNums[2]) << 8) + (Long.parseLong(ipNums[3]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iplong;
	}
}
