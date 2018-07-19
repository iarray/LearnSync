package synclearn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 使用wait/notify 实现阻塞队列
 * @author 28642
 *
 */
public class MyBlockingQueue<T> {

	int count = 0;
	final int minSize = 0;
	final int maxSize;
	final List<T> list = new LinkedList<>();
	final Object lock = new Object();
	
	public MyBlockingQueue(int size) {
		this.maxSize = size;
	}
	 
	
	public int size() {
		return list.size();
	}
	
	public void put(T obj) {
		synchronized(lock) {
			/* 考虑多线程的情况下,如果队列已满则休眠生产者线程 */
			while(count >= maxSize) { //注意这里为什么是用while不是if
				try {
					lock.wait();
					/* 为什用while而不是if, 假设现在有两条线程a和b运行到这里进入等待状态
					 * 此时take方法取出元素调用notifyAll 唤醒了a和b
					 * a获取到了锁,往里面添加了元素, 此时刚好队列又满了
					 * 到b获取锁, 如果是if的话,b直接往下执行向队列添加元素,
					 * 但此时队列已满不能再添加元素,会引发线程安全问题
					 */
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			list.add(obj);
			count++;
			lock.notifyAll(); //唤醒消费者线程取数据, 注意这里不能用notify, 因为notify唤醒的线程不确定,可能唤醒的是生产者
		}
	}
	
	public T take() {
		T ret = null;
		synchronized(lock) {
			while(count<=0) { //这里也是用while,不能用if, 一般wait都是搭配while使用
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			ret = list.remove(0);
			count--;
			lock.notifyAll(); //唤醒生产者线程生产
		}
		return ret;
	}
	 
	
	public static void main(String[] args) {
		MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(10);
		Random random = new Random();
		
		//先填满队列, 看看生产者是否会生产
		for (int i = 0; i < 10; i++) {
			queue.put(i);
		}
		
	
		//创建生产者线程
		new Thread(()->{
			while(true) {
				int p = random.nextInt(100);
				queue.put(p);
				System.out.println("放进了元素:" + p);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	
		//先等2秒再启动消费者
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		new Thread(()->{
			//创建10条消费者线程
			while(true) {
				int p = queue.take();
				System.out.println("取到元素:" + p);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start(); 
	}
}
