package uk.bris.esserver.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ESSLogger {
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	static private FileHandler fileHTML;
	static private Formatter formatterHTML;

	static public void setup() throws IOException {

		// Get the global logger to configure it
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		logger.setLevel(Level.INFO);
		fileTxt = new FileHandler("ESSLogging.txt");
		fileHTML = new FileHandler("ESSLogging.html");

		// create txt Formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		// create HTML Formatter
		formatterHTML = new LoggerFormatter();
		fileHTML.setFormatter(formatterHTML);
		logger.addHandler(fileHTML);
	}
}