package synclearn;

import java.util.ArrayList;
import java.util.List;

/**
 *  阿里面试题 , 线程1往列表里面添加10个元素, 线程2判断当线程1的列表数量等于5的时候, 线程2终止
 * @author IArray
 *
 */
public class WaitAndNotifyDemo {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		final Object lock = new Object();
		
		Thread t1 = new Thread(()->{
			synchronized(lock) {
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
						lock.notify();
						try {
							lock.wait();//不要漏了这句, notify不会释放锁,这里执行wait释放锁,让t2可以获取锁
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				}
			}
		});
		
		Thread t2 = new Thread(()-> {
			synchronized(lock) {
				while(list.size() != 5) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				lock.notifyAll(); //不要漏了,这里让线程1继续执行
				System.out.println("已经添加了5个元素, 线程2终止");
				throw new RuntimeException();
			}
		});
		
		t1.start();
		t2.start();
	}
}
