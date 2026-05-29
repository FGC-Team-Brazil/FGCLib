package org.firstinspires.ftc.teamcode.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PeriodicTimer {

  private static final double OVERRUN_THRESHOLD_MS = 20.0;

  private static final Map<String, Long> starts = new ConcurrentHashMap<>();
  private static final Map<String, Long> lastTicks = new ConcurrentHashMap<>();
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

  public static void tick(String name) {
    long now = System.nanoTime();
    Long last = lastTicks.put(name, now);

    if (last != null) {
      double elapsedMs = (now - last) / 1_000_000.0;
      updateStats(name, elapsedMs);
    }
  }

  private static void updateStats(String name, double elapsedMs) {
    TimerStats s = allStats.computeIfAbsent(name, k -> new TimerStats());
    s.addSample(elapsedMs);
  }

  public static String getSummary(String name) {
    TimerStats s = allStats.get(name);
    if (s == null) return "No data";
    return s.toString();
  }

  public static void reset(String name) {
    starts.remove(name);
    lastTicks.remove(name);
    allStats.remove(name);
  }

  private static class TimerStats {
    double last = 0, min = 999, max = 0, sum = 0;
    long count = 0, overruns = 0;

    synchronized void addSample(double ms) {
      last = ms;
      if (ms < min) min = ms;
      if (ms > max) max = ms;
      sum += ms;
      count++;
      if (ms > OVERRUN_THRESHOLD_MS) overruns++;
    }

    @Override
    public synchronized String toString() {
      return String.format(
          "%.1fms (Avg: %.1f | Max: %.1f) [Over: %d]", last, (sum / count), max, overruns);
    }
  }
}
