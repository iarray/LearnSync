package synclearn;

import java.io.Console;

public class volatileDemo extends Thread{
	static boolean run = true;
	
	
	@Override
	public void run() {
		System.out.println("t1 Runing");

		while(run) {}
		
		System.out.print("t1 END");
	}
	
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new volatileDemo());
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = false;
		System.out.print("run is " + run);
	}
}
