package im.juejin.basic;

public class IntegerTest {
	
	public static void compare(){
		int i1 = 1;
        Integer i2 = 1;
        Integer i3 = new Integer(1);
        Integer i4 = i1;
        Integer i5 = new Integer(1);

        // 直接和int比较会自动拆箱，也就是比较基础数据类型的值
        System.out.println("i1 == i2:" + (i1 == i2));
        System.out.println("i1 == t3:" + (i1 == i3));
        System.out.println("i1 == t4:" + (i1 == i4));
        System.out.println("i1 == t5:" + (i1 == i5));
        
        // Integer i2 = 1  <=> Integer i2 = Integer.valueOf(1);
        // 对象之间的 == 就是比较对象是否引用相同的存储区域
        System.out.println("i2 == i3:" + (i2 == i3));
        // i2 和 i3 直接获取Integer常量池中的值
        System.out.println("i2 == i4:" + (i2 == i4));
        System.out.println("i2 == i5:" + (i2 == i5));
        
	}
	
	public static void autoBox(){
		/**
		 * Integer为了减少大量的封装类，Integer会在类的常量池中创建IntegerCache，提前创建Integer对象，
		 * 当范围在 [-128,127] 的值，会从Integer的缓冲池中直接获取
		 * public static Integer valueOf(int i) {
		        if (i >= IntegerCache.low && i <= IntegerCache.high)
		            return IntegerCache.cache[i + (-IntegerCache.low)];
		        return new Integer(i);
		    }
		 */
		
		Integer i1 = 10;  
        Integer i2 = 10;
        Integer i3 = Integer.valueOf(10);
        System.out.println("i1 == i2:" + (i1 == i2));
        System.out.println("i1 == i3:" + (i1 == i3));
 
        Integer i4 = 1000;
        Integer i5 = 1000;
        Integer i6 = Integer.valueOf(1000);
        System.out.println("i4 == i5:" + (i4 == i5));
        System.out.println("i4 == i6:" + (i4 == i6));
        
		// Integer i1 = 10 <==> Integer i1 = Integer.valueOf(10);
	}
	
	
	
	public static void main(String[] args) {
		autoBox();
//		compare();
	}
}
