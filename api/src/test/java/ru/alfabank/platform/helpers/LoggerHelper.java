package ru.alfabank.platform.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerHelper {

	private static final Logger parentLogger = LogManager.getLogger();

	private Logger logger = parentLogger;

	private void qwaasd() {
		logger.info("");
	}
}
