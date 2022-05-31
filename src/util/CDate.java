package util;

import java.time.YearMonth;
import java.util.Calendar;

public class CDate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4559473596601906179L;

	private static final int offset = 100000000;

	private Calendar cal;

	public CDate() {
		cal = Calendar.getInstance();
	}

	public void set(int year, int month, int day) {
		cal.set(year + offset, month, day);
	}

	public void addDay() {
		cal.add(Calendar.DAY_OF_MONTH, 1);
	}

	public int getYear() {
		return cal.get(Calendar.YEAR) - offset;
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getNumberOfDaysInMonth() {
		return YearMonth.of(getYear() + offset, getMonth()+1).lengthOfMonth();
	}

}
