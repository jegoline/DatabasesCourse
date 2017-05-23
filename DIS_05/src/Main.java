
public class Main {
	public static Thread createThread(int minPageid, int maxPageid) {
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				Client client = new Client();

				try {
					client.execute(minPageid, maxPageid);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		return thread1;
	}

	public static int main() {
		Thread thread1 = createThread(1,10);
		Thread thread2 = createThread(11,20);
		thread1.run();
		thread2.run();
		return 0;
	}
}
