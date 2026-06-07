package org.firstinspires.ftc.teamcode.core.util;

import Ori.Coval.Logging.Logger.KoalaLog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for measuring and tracking execution times of periodic tasks.
 *
 * <p>Supports scoped timing through {@link #start(String)} and {@link #stop(String)}. Collected
 * statistics are automatically logged to KoalaLog.
 */
public final class PeriodicTimer {

  private static final double OVERRUN_THRESHOLD_MS = 20.0;

  private static final Map<String, Long> starts = new ConcurrentHashMap<>();
  private static final Map<String, TimerStats> allStats = new ConcurrentHashMap<>();

  private PeriodicTimer() {}

  /**
   * Starts a timing measurement for the given name.
   *
   * @param name timer identifier
   */
  public static void start(String name) {
    starts.put(name, System.nanoTime());
  }

  /**
   * Stops a timing measurement, updates statistics, and logs the current values.
   *
   * <p>If the timer was not previously started, the call is ignored.
   *
   * @param name timer identifier
   */
  public static void stop(String name) {
    Long startNs = starts.remove(name);
    if (startNs == null) return;

    double elapsedMs = (System.nanoTime() - startNs) / 1_000_000.0;
    updateStats(name, elapsedMs);
  }

  /**
   * Updates the statistics associated with a timer and logs them to KoalaLog.
   *
   * @param name timer identifier
   * @param elapsedMs measured elapsed time in milliseconds
   */
  private static void updateStats(String name, double elapsedMs) {
    TimerStats stats = allStats.computeIfAbsent(name, k -> new TimerStats());
    stats.addSample(elapsedMs);

    KoalaLog.log("Timers/" + name + "/LastMs", stats.getLast(), true);
    KoalaLog.log("Timers/" + name + "/AvgMs", stats.getAverage(), true);
    KoalaLog.log("Timers/" + name + "/MaxMs", stats.getMax(), true);
    KoalaLog.log("Timers/" + name + "/Overruns", stats.getOverruns(), true);
  }

  /**
   * Returns a formatted summary of the collected statistics.
   *
   * @param name timer identifier
   * @return formatted statistics string, or {@code "No data"} if no samples exist
   */
  public static String getSummary(String name) {
    TimerStats stats = allStats.get(name);
    if (stats == null) return "No data";
    return stats.toString();
  }

  /**
   * Clears all stored data associated with the given timer.
   *
   * @param name timer identifier
   */
  public static void reset(String name) {
    starts.remove(name);
    allStats.remove(name);
  }

  private static final class TimerStats {
    private double last = 0;
    private double min = 999;
    private double max = 0;
    private double sum = 0;
    private long count = 0;
    private long overruns = 0;

    /**
     * Adds a new timing sample.
     *
     * @param ms elapsed time in milliseconds
     */
    synchronized void addSample(double ms) {
      last = ms;
      if (ms < min) min = ms;
      if (ms > max) max = ms;
      sum += ms;
      count++;
      if (ms > OVERRUN_THRESHOLD_MS) overruns++;
    }

    synchronized double getLast() {
      return last;
    }

    synchronized double getAverage() {
      return count == 0 ? 0.0 : sum / count;
    }

    synchronized double getMax() {
      return max;
    }

    synchronized long getOverruns() {
      return overruns;
    }
  }
}
