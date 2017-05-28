
public class Client {

	public void execute(int clientID, int minPageid, int maxPageid) throws InterruptedException {
		PersistenceManager persistenceManager = PersistenceManager.getInstance();
		for (int j = 0; j < 1000; j++) {
			int taid = persistenceManager.beginTransaction();
			for (int i = minPageid; i < maxPageid; i++) {
				String data = "data of client " + clientID + " page iteration " + i;
				persistenceManager.write(taid, i, data);
			}
			
			Thread.sleep(2000);
		}

	}
}
