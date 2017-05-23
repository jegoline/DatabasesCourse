import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class PersistenceManager {

	public class Value {
		private int taid;
		private String data;

		public Value(int taid, String data) {
			this.taid = taid;
			this.data = data;
		}

	}

	HashMap<Integer, Value> buffer = new HashMap<>();
	private static final PersistenceManager instance = new PersistenceManager();
	HashMap<Integer, Boolean> commitedTr = new HashMap<>();

	private PersistenceManager() {
	}

	public int beginTransaction() {
		return 0;

	}

	public void commit(int taid) {
		commitedTr.put(taid, Boolean.TRUE);
	}

	public void write(int taid, int pageid, String data) {

		Value value = new Value(taid, data);
		buffer.put(pageid, value);

		if (buffer.size() > 5) {
			writeToFile();
		}
	}

	private int writeToFile() {
		try {
			for (Entry<Integer, Value> entry : buffer.entrySet()) {
				if (commitedTr.containsKey(entry.getValue().taid)) {
					FileWriter writer = new FileWriter("pages/page_" + entry.getKey());
					writer.write(entry.getKey() + "," + entry.getValue().taid + ", " + entry.getValue().data);
					writer.close();

					buffer.remove(entry.getKey());
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
