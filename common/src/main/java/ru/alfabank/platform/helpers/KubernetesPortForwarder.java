package ru.alfabank.platform.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class KubernetesPortForwarder implements AutoCloseable {

  private static final Logger LOGGER = LogManager.getLogger(KubernetesPortForwarder.class);
  private static final ExecutorService FORWARDER = Executors.newSingleThreadExecutor();
  private static final Object FORWARD_MONITOR = new Object();
  private static final String forwardCommand =
      "kubectl -n alfabankru-develop port-forward mysql-mysql-master-0 3306:3306";

  private volatile boolean isForwarded = false;

  /**
   * Ensure is forwarded.
   */
  public void ensureForwarded() {
    while (!isForwarded) {
      forward();
    }
  }

  private void forward() {
    FORWARDER.execute(() -> {
      if (isForwarded) {
        return;
      }
      synchronized (FORWARD_MONITOR) {
        if (isForwarded) {
          return;
        }
        try {
          Process forwardProcess = Runtime.getRuntime().exec(forwardCommand);
          BufferedReader stdInput = new BufferedReader(new
              InputStreamReader(forwardProcess.getInputStream()));
          BufferedReader stdError = new BufferedReader(new
              InputStreamReader(forwardProcess.getErrorStream()));
          String s;
          while (!Thread.currentThread().isInterrupted()
              && (s = stdInput.readLine()) != null) {
            isForwarded = true;
            LOGGER.debug(s);
          }
          while (!Thread.currentThread().isInterrupted()
              && (s = stdError.readLine()) != null) {
            isForwarded = false;
            LOGGER.error(s);
          }
          if (Thread.currentThread().isInterrupted()) {
            forwardProcess.destroy();
          }
        } catch (IOException e) {
          LOGGER.error(String.format("Failed to perform port forward command.\n"
              + "Command: %s. Exception: %s", forwardCommand, e));
        } finally {
          isForwarded = false;
        }
      }
    });
  }

  @Override
  public void close() {
    FORWARDER.shutdownNow();
  }
}