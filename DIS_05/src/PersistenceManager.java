import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PersistenceManager {
	private static final PersistenceManager instance = new PersistenceManager();

	private static class Value {
		private int lsn;
		private String data;
		private long taid;

		public Value(int lsn, long taid, String data) {
			this.lsn = lsn;
			this.taid = taid;
			this.data = data;
		}
	}

	private Map<Integer, Value> buffer = new ConcurrentHashMap<>();
	private Logger logger = new Logger();
	private AtomicLong currentTAID = new AtomicLong(System.currentTimeMillis());

	private PersistenceManager() {
		try {
			System.out.println("Performing recovery..");
			recovery();
			//System.exit(0);
		} catch (IOException e) {
			System.err.println("Ooops! Something goes wrong!");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public long beginTransaction() {
		long taid = currentTAID.incrementAndGet();
		logger.logStartTA(taid);
		return taid;
	}

	public void commit(long taid) {
		logger.logCommitTA(taid);
	}

	public void write(long taid, int pageid, String data) {
		int lsn = logger.logUpdate(taid, pageid, data);
		Value value = new Value(lsn, taid, data);
		buffer.put(pageid, value);
		persistPermanentlyIfRequired();
	}

	public void recovery() throws IOException {
		List<Long> redoTAs = logger.analyse();
		for (Long taid : redoTAs) {
			List<String> logs = logger.getUpdateLogsForTA(taid);
			for (String logEntry : logs) {
				// parse
				String[] data = logEntry.split(",");
				int lsn = Integer.parseInt(data[0]);
				int pageid = Integer.parseInt(data[2]);

				int page_lsn = getPageLSN(pageid);
				if (lsn > page_lsn) {
					writePage(pageid, lsn, data[3]);
					System.out.println("Recovered page " + pageid + " prev LSN " + page_lsn + " new LSN " + lsn);
				}
			}
		}
	}

	private int getPageLSN(int pageid) throws IOException {
		File file = new File("dbms/page_" + pageid);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		br.close();

		int lsn = -1;
		if (line != null) {
			String[] data = line.split(",");
			lsn = Integer.parseInt(data[1]);
		}
		return lsn;
	}

	private void writePage(int pageid, int lsn, String userData) throws IOException {
		FileWriter writer = new FileWriter("dbms/page_" + pageid);
		writer.write(pageid + "," + lsn + ", " + userData);
		writer.close();
	}

	// propagate data to the permanent DB
	private synchronized void persistPermanentlyIfRequired() {
		if (buffer.size() < 5) {
			return;
		}

		try {
			for (Entry<Integer, Value> entry : buffer.entrySet()) {
				if (logger.isCommitedTA(entry.getValue().taid)) {
					writePage(entry.getKey(), entry.getValue().lsn, entry.getValue().data);
					buffer.remove(entry.getKey());

					// as soon as info is propagated to permanent DB => we do
					// not need this log entry anymore for redo
					logger.clean(entry.getValue().lsn,entry.getValue().taid);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static PersistenceManager getInstance() {
		return instance;
	}
}
