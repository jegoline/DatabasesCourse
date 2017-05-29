import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Logger {
	private int currentLSN = 0;
	private FileWriter logFile;

	public Logger() {
		File file = new File("dbms/log_file.txt");
		try {
			file.createNewFile();
			logFile = new FileWriter(file, true); // append mode
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			currentLSN = (int) reader.lines().count();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int writeToLog(String data) throws IOException {
		synchronized (logFile) {
			currentLSN++;
			logFile.write(currentLSN + "," + data + "\n");
			logFile.flush();
			return currentLSN;
		}
	}

	public int logStartTA(long taid) {
		try {
			return writeToLog(taid + ",BOT");
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int logUpdate(long taid, int pageId, String redo) {
		try {
			return writeToLog(taid + "," + pageId + "," + redo);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int logCommitTA(long taid) {
		try {
			return writeToLog(taid + ",EOT");
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public List<Long> analyse() {
		List<String> logs = loadByPredicate(p -> p.contains("EOT"));
		return logs.stream().map(s -> Long.parseLong(s.split(",")[1])).collect(Collectors.toList());
	}

	private List<String> loadByPredicate(Predicate<String> predicate) {
		File file = new File("dbms/log_file.txt");
		List<String> logs = new ArrayList<>();
		try {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (predicate.test(line)) {
						logs.add(line);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logs;
	}

	public void clean(long taid) {
		// TODO Auto-generated method stub

	}

	public boolean isCommitedTA(long taid) {
		return !loadByPredicate(p -> p.contains(Long.toString(taid)) && p.contains("EOT")).isEmpty();
	}

	public List<String> getUpdateLogsForTA(long taid) {
		return loadByPredicate(p -> p.contains(Long.toString(taid)) && !p.contains("EOT") && !p.contains("BOT"));
	}
}
