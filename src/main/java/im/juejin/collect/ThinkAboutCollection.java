package im.juejin.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThinkAboutCollection {
    public static void removeList(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(10);
        
//        list.remove(1);
//        System.out.println(list);
        
//        list.remove(10);
//        System.out.println(list);
        
//        list.remove(new Integer(10));
//        System.out.println(list);
        
//        list.forEach(i -> {
//            if(i == 1){
//                list.remove(new Integer(1));
//            }
//        });
//        
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 1) {
                list.remove(i);
            }
        }
        System.out.println(list);
    }
    
    public static void removeByIter(List<Integer> list) {
        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            Integer element = iter.next();
            if (element % 5 == 0) {
                iter.remove();
            }
        }
    }
    
    public static List<Integer> removeByCopy(List<Integer> list) {
        List<Integer> newList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 5 == 0) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }
    
    public static void main(String[] args) {
        removeList();
        
//        List<Integer> list = new ArrayList<>();
//        for(int i=0;i<1000;i++){
//            list.add(i);
//        }
//        long begin = System.currentTimeMillis();
//        for(int i=0;i<10000;i++){
//            removeByIter(list);
//        }
//        System.out.println("cost time1:"+(System.currentTimeMillis()-begin));
//        
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        
//        begin = System.currentTimeMillis();
//        for(int i=0;i<10000;i++){
//            removeByCopy(list);
//        }
//        System.out.println("cost time2:"+(System.currentTimeMillis()-begin));
    }
}
