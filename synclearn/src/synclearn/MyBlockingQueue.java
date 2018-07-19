package synclearn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * ʹ��wait/notify ʵ����������
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
			/* ���Ƕ��̵߳������,������������������������߳� */
			while(count >= maxSize) { //ע������Ϊʲô����while����if
				try {
					lock.wait();
					/* Ϊʲ��while������if, ���������������߳�a��b���е��������ȴ�״̬
					 * ��ʱtake����ȡ��Ԫ�ص���notifyAll ������a��b
					 * a��ȡ������,�����������Ԫ��, ��ʱ�պö���������
					 * ��b��ȡ��, �����if�Ļ�,bֱ������ִ����������Ԫ��,
					 * ����ʱ�����������������Ԫ��,�������̰߳�ȫ����
					 */
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			list.add(obj);
			count++;
			lock.notifyAll(); //�����������߳�ȡ����, ע�����ﲻ����notify, ��Ϊnotify���ѵ��̲߳�ȷ��,���ܻ��ѵ���������
		}
	}
	
	public T take() {
		T ret = null;
		synchronized(lock) {
			while(count<=0) { //����Ҳ����while,������if, һ��wait���Ǵ���whileʹ��
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			ret = list.remove(0);
			count--;
			lock.notifyAll(); //�����������߳�����
		}
		return ret;
	}
	 
	
	public static void main(String[] args) {
		MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(10);
		Random random = new Random();
		
		//����������, �����������Ƿ������
		for (int i = 0; i < 10; i++) {
			queue.put(i);
		}
		
	
		//�����������߳�
		new Thread(()->{
			while(true) {
				int p = random.nextInt(100);
				queue.put(p);
				System.out.println("�Ž���Ԫ��:" + p);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	
		//�ȵ�2��������������
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		new Thread(()->{
			//����10���������߳�
			while(true) {
				int p = queue.take();
				System.out.println("ȡ��Ԫ��:" + p);
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
