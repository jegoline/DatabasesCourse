import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class PersistenceManager {
	private static final PersistenceManager instance = new PersistenceManager();

	private static class Value {
		private int taid;
		private String data;

		public Value(int taid, String data) {
			this.taid = taid;
			this.data = data;
		}
	}

	private Map<Integer, Value> buffer = new HashMap<>();
	private Logger logger = new Logger();
	private AtomicInteger currentTAID = new AtomicInteger(0);

	private PersistenceManager() {
	}

	public int beginTransaction() {
		return logger.logStartTA(currentTAID.incrementAndGet());
	}

	public void commit(int taid) {
		logger.logCommitTA(taid);
	}

	public void write(int taid, int pageid, String data) {
		Value value = new Value(taid, data);
		buffer.put(pageid, value);
		persistPermanentlyIfRequired();
	}

	private synchronized int persistPermanentlyIfRequired() {
		try {
			for (Entry<Integer, Value> entry : buffer.entrySet()) {
				if (logger.isCommitedTA(entry.getValue().taid)) {
					FileWriter writer = new FileWriter("dbms/page_" + entry.getKey());
					writer.write(entry.getKey() + "," + entry.getValue().taid + ", " + entry.getValue().data);
					writer.close();

					buffer.remove(entry.getKey());

					// as soon as info is propagated to permanent DB => we do
					// not need this log entry for redo anymore
					logger.clean(entry.getValue().taid);
				}
			}
		} catch (IOException ex) {

		}
		return 0;
	}

	public static PersistenceManager getInstance() {
		return instance;
	}
}
