
public class Main {
	public static Thread createThread(int clientId, int minPageid, int maxPageid) {
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				Client client = new Client();
				try {
					client.execute(clientId, minPageid, maxPageid);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "client " + clientId);
		return thread1;
	}

	public static void main(String... args) {
		int numOfClients = 10;
		for (int cl = 0; cl < numOfClients; cl++) {
			Thread thread = createThread(cl, cl * 20, cl * 20 + 19);
			thread.run();
		}
	}
}
