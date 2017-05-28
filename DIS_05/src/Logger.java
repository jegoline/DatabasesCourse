import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private int currentLSN = 0;
	private FileWriter logFile;

	public Logger() {
		File file = new File("dbms/log_file.txt");
		try {
			file.createNewFile();
			logFile = new FileWriter(file);
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

	public int logStartTA(int taid) {
		try {
			return writeToLog(taid + ",BOT");
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void logUpdate() {

	}

	public int logCommitTA(int taid) {
		try {
			return writeToLog(taid + ",EOT");
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void clean(int taid) {
		// TODO Auto-generated method stub
		
	}

	public boolean isCommitedTA(int taid) {
		return true;
	}
}
