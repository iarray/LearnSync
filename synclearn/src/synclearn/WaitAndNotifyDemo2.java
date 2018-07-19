package synclearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *  ���������� , �߳�1���б��������10��Ԫ��, �߳�2�жϵ��߳�1���б���������5��ʱ��, �߳�2��ֹ
 *  ʹ��CountDownLatch�Ż����
 * @author IArray
 *
 */
public class WaitAndNotifyDemo2 {

	public static void main(String[] args) {
		final List<Integer> list = new ArrayList<Integer>();
		final CountDownLatch countDownLatch = new CountDownLatch(1);  //��ʼֵ1,ÿ����һ��countDownֵ�ͼ�һ
		
		Thread t1 = new Thread(()->{
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
					//lock.notify();
					countDownLatch.countDown(); //��countDownֵΪ0ʱ,����
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
			System.out.println("�Ѿ������5��Ԫ��, �߳�2��ֹ");
			throw new RuntimeException();
		});
		
		t1.start();
		t2.start();
	}
}
