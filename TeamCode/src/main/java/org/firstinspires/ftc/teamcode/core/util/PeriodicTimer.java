package org.firstinspires.ftc.teamcode.core.util;

import Ori.Coval.Logging.Logger.KoalaLog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PeriodicTimer {

  private static final double OVERRUN_THRESHOLD_MS = 20.0;

  private static final Map<String, Long> starts = new ConcurrentHashMap<>();
  private static final Map<String, TimerStats> allStats = new ConcurrentHashMap<>();

  private PeriodicTimer() {}

  public static void start(String name) {
    starts.put(name, System.nanoTime());
  }

  public static void stop(String name) {
    Long startNs = starts.remove(name);
    if (startNs == null) return;

    double elapsedMs = (System.nanoTime() - startNs) / 1_000_000.0;
    updateStats(name, elapsedMs);
  }

  private static void updateStats(String name, double elapsedMs) {
    TimerStats stats = allStats.computeIfAbsent(name, k -> new TimerStats());
    stats.addSample(elapsedMs);

    KoalaLog.log("Timers/" + name + "/LastMs", stats.getLast(), true);
    KoalaLog.log("Timers/" + name + "/AvgMs", stats.getAverage(), true);
    KoalaLog.log("Timers/" + name + "/MaxMs", stats.getMax(), true);
    KoalaLog.log("Timers/" + name + "/Overruns", stats.getOverruns(), true);
  }

  public static String getSummary(String name) {
    TimerStats stats = allStats.get(name);
    if (stats == null) return "No data";
    return stats.toString();
  }

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
