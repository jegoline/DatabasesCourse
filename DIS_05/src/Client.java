
public class Client {

	public void execute(int clientID, int minPageid, int maxPageid) throws InterruptedException {
		PersistenceManager persistenceManager = PersistenceManager.getInstance();
		for (int j = 0; j < 100; j++) {
			int taid = persistenceManager.beginTransaction();
			for (int i = minPageid; i < maxPageid; i++) {
				String data = "data of client " + clientID + " iteration " + j;
				persistenceManager.write(taid, i, data);
				
				System.out.println("taid " + taid + " " +  data);
			}
			persistenceManager.commit(taid);
			Thread.sleep(2000);
		}

	}
}
