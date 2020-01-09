package ru.alfabank.platform.helpers;

import java.util.Calendar;

public class DateHelper {

  /**
   * Adding to current time pus {@code i} {@code timeUnit}.
   * @param i how much
   * @param timeUnit time unit
   * @return resulting date and time as a string formatted with the appropriate way
   */
  public static Calendar getNowPlus(int i, int timeUnit) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(timeUnit, i);
    return calendar;
  }
}
