package synclearn;

import java.util.ArrayList;
import java.util.List;

/**
 *  ���������� , �߳�1���б��������10��Ԫ��, �߳�2�жϵ��߳�1���б���������5��ʱ��, �߳�2��ֹ
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
					System.out.println("�����" + i);
					
					if(list.size() == 5) {
						lock.notify();
						try {
							lock.wait();//��Ҫ©�����, notify�����ͷ���,����ִ��wait�ͷ���,��t2���Ի�ȡ��
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
				
				lock.notifyAll(); //��Ҫ©��,�������߳�1����ִ��
				System.out.println("�Ѿ������5��Ԫ��, �߳�2��ֹ");
				throw new RuntimeException();
			}
		});
		
		t1.start();
		t2.start();
	}
}
