package synclearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *  阿里面试题 , 线程1往列表里面添加10个元素, 线程2判断当线程1的列表数量等于5的时候, 线程2终止
 *  使用CountDownLatch优化解决
 * @author IArray
 *
 */
public class WaitAndNotifyDemo2 {

	public static void main(String[] args) {
		final List<Integer> list = new ArrayList<Integer>();
		final CountDownLatch countDownLatch = new CountDownLatch(1);  //初始值1,每调用一次countDown值就减一
		
		Thread t1 = new Thread(()->{
			for(int i=1;i<=10;++i) {
				list.add(i); 
				try {
					Thread.sleep(500);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				System.out.println("添加了" + i);
				
				if(list.size() == 5) {
					//lock.notify();
					countDownLatch.countDown(); //当countDown值为0时,唤醒
				}
			}
		});
		
		Thread t2 = new Thread(()-> {
			if(list.size() != 5) {
				try {
					//lock.wait();
					countDownLatch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("已经添加了5个元素, 线程2终止");
			throw new RuntimeException();
		});
		
		t1.start();
		t2.start();
	}
}
