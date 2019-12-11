package ru.alfabank.platform.helpers;

import org.apache.logging.log4j.*;

public class LoggerHelper {

	private static final Logger parentLogger = LogManager.getLogger();

	private Logger logger = parentLogger;

	private void qwaasd() {
		logger.info("");
	}
}
