package planner.com.d_planner.ui.util;

/**
 * Created by soongu on 2016-11-02.
 */

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @(#)DateUtil.java v1.1 19-Nov-2002
 *
 *                   날짜 계산에 유용한 여러가지 메소드.
 *
 *
 *                   public static int[] getMonthDaysArray(int yr) 지정한 년도의 각 달에
 *                   포함된 날수의 배열을 구하는 메소드
 *
 *                   public static int getDaysInYear(int y) 지정한 년도에 포함된 날수 구하는
 *                   메소드
 *
 *                   public static int getDaysInMonth(int m, int y) 지정한 년도의 지정한
 *                   달에 포함된 날수 구하는 메소드
 *
 *                   public static int getDaysFromMonthFirst(int d, int m, int
 *                   y) public static int getDaysFromMonthFirst(String s) 지정한
 *                   년도의 1월 1일 이후에 경과한 날수 구하는 메소드 만약 지정한 날짜가 유효한 범위를 벗어나면 예외상황을
 *                   던진다.
 *
 *                   public static int getDaysFromYearFirst(int d, int m, int y)
 *                   public static int getDaysFromYearFirst(String s) 지정한 년도의 1월
 *                   1일 이후에 경과한 날수 구하는 메소드
 *
 *                   public static int getDaysFrom21Century(int d, int m, int y)
 *                   public static int getDaysFrom21Century(String s) 2000년 1월
 *                   1일 이후에 경과한 날수 구하는 메소드
 *
 *                   public static int getDaysBetween(String s1, String s2) 지정한
 *                   두 날짜의 (양 끝 제외) 날짜 차이 구하는 메소드
 *
 *                   public static int getDaysDiff(String s1, String s2) 지정한 두
 *                   날짜의 날짜 차이 구하는 메소드
 *
 *                   public static int getDaysFromTo(String s1, String s2) 지정한 두
 *                   날짜의 (양 끝 포함) 날짜 차이 구하는 메소드
 *
 *                   public static int getWeekdaysInMonth(int weekDay, int m,
 *                   int y) public static int getWeekdaysInMonth(String
 *                   weekName, int m, int y) 지정한 년도의 지정한 달에 포함된 지정한 요일의 개수 구하는
 *                   메소드
 *
 *                   public static String getDateStringFrom21Century(int
 *                   elapsed) 2000년 1월 이후 elapsed일 경과한 날짜를 String 타입으로는 메소드
 *
 *                   public static String addDate(int offset, int d, int m, int
 *                   y) public static String addDate(int offset, String date)
 *                   지정한 날짜 이후 지정한 offset일 경과한 날짜를 String 타입으로는 메소드
 *
 *
 *                   [참고 1] 그레고리식 달력은 1582 년도 10월 달력이 비정상적이다. 즉, 10월 4일 다음 날이
 *                   10월 15일이었다. 그러므로, 그 달은 열흘이 부족한 21일이 한달이었다.
 *
 *                   [참고 2] 영국식 달력은 1752 년도 9월 달력이 비정상적이다. 즉, 9월 2일 다음 날이 9월
 *                   14일이었다. 그러므로, 그 달은 열하루가 부족한 19일이 한달이었다.
 *
 *
 * @date 10-Sep-2002 (ver 1.0) DateDiffTest.java
 * @date 19-Nov-2002 (ver 1.1)
 * @author Pilho Kim (phkim at cluecom.co.kr)
 */

public class DateUtil {

    // //////////////////////////////////////////////////////////////////////////
    // 일년 간 달력의 월별 날짜수 배열을 구한다.
    //

    public static String getDate(int year, int month, int week, int dayOfWeek)

    {

		/*
		 *
		 * Calendar.SUNDAY = 1
		 *
		 * Calendar.MONDAY = 2
		 *
		 * Calendar.TUESDAY = 3
		 *
		 * Calendar.WEDNESDAY = 4
		 *
		 * Calendar.THURSDAY = 5
		 *
		 * Calendar.FRIDAY = 6
		 *
		 * Calendar.SATURDAY = 7
		 */

        DecimalFormat df = new DecimalFormat("00");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.MONTH, month - 1);

        calendar.set(Calendar.WEEK_OF_MONTH, week);

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        String strMonth = df.format(calendar.get(Calendar.MONTH) + 1);

        String strDay = df.format(calendar.get(Calendar.DAY_OF_MONTH));

        String date = strMonth + "월" + strDay + "일";

        return strDay;

    }

    public static int[] getMonthDaysArray(int yr) {
        int[] a1 = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int[] a2 = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (yr <= 1582) {
            if ((yr % 4) == 0) {
                if (yr == 4) {
                    return a1;
                }
                return a2;
            }
        } else {
            if (((yr % 4) == 0) && ((yr % 100) != 0)) {
                return a2;
            } else if ((yr % 400) == 0) {
                return a2;
            }
        }

        return a1;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정된 년도와 달에 따른 요일 편차를 구한다.
    //
    public static int addWeekDays(int y, int m) {
        int[] b1 = { 3, 0, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3 };
        int[] b2 = { 3, 1, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3 };
        int i = 0;
        int rval = 0;

        if (y <= 1582) {
            if ((y % 4) == 0) {
                if (y == 4) {
                    for (i = 0; i < m - 1; i++)
                        rval += b1[i];
                } else {
                    for (i = 0; i < m - 1; i++)
                        rval += b2[i];
                }
            } else {
                for (i = 0; i < m - 1; i++)
                    rval += b1[i];
            }
        } else {
            if (((y % 4) == 0) && ((y % 100) != 0)) {
                for (i = 0; i < m - 1; i++)
                    rval += b2[i];
            } else if ((y % 400) == 0) {
                for (i = 0; i < m - 1; i++)
                    rval += b2[i];
            } else {
                for (i = 0; i < m - 1; i++)
                    rval += b1[i];
            }
        }

        return rval;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 총 날짜 수를 구한다.
    //
    public static int getDaysInYear(int y) {
        if (y > 1582) {
            if (y % 400 == 0)
                return 366;
            else if (y % 100 == 0)
                return 365;
            else if (y % 4 == 0)
                return 366;
            else
                return 365;
        } else if (y == 1582)
            return 355;
        else if (y > 4) {
            if (y % 4 == 0)
                return 366;
            else
                return 365;
        } else if (y > 0)
            return 365;
        else
            return 0;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도, 지정한 월의 총 날짜 수를 구한다.
    //
    public static int getDaysInMonth(int m, int y) {
        if (m < 1 || m > 12)
            throw new RuntimeException("Invalid month: " + m);

        int[] b = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (m != 2 && m >= 1 && m <= 12 && y != 1582)
            return b[m - 1];
        if (m != 2 && m >= 1 && m <= 12 && y == 1582)
            if (m != 10)
                return b[m - 1];
            else
                return b[m - 1] - 10;

        if (m != 2)
            return 0;

        // m == 2 (즉 2월)
        if (y > 1582) {
            if (y % 400 == 0)
                return 29;
            else if (y % 100 == 0)
                return 28;
            else if (y % 4 == 0)
                return 29;
            else
                return 28;
        } else if (y == 1582)
            return 28;
        else if (y > 4) {
            if (y % 4 == 0)
                return 29;
            else
                return 28;
        } else if (y > 0)
            return 28;
        else
            throw new RuntimeException("Invalid year: " + y);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 지정한 월의 첫날 부터 지정한 날 까지의 날짜 수를 구한다.
    //
    public static int getDaysFromMonthFirst(int d, int m, int y) {
        if (m < 1 || m > 12)
            throw new RuntimeException("Invalid month " + m + " in " + d + "/"
                    + m + "/" + y);

        int max = getDaysInMonth(m, y);
        if (d >= 1 && d <= max)
            return d;
        else
            throw new RuntimeException("Invalid date " + d + " in " + d + "/"
                    + m + "/" + y);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 첫날 부터 지정한 월의 지정한 날 까지의 날짜 수를 구한다.
    //
    public static int getDaysFromYearFirst(int d, int m, int y) {
        if (m < 1 || m > 12)
            throw new RuntimeException("Invalid month " + m + " in " + d + "/"
                    + m + "/" + y);

        int max = getDaysInMonth(m, y);
        if (d >= 1 && d <= max) {
            int sum = d;
            for (int j = 1; j < m; j++)
                sum += getDaysInMonth(j, y);
            return sum;
        } else
            throw new RuntimeException("Invalid date " + d + " in " + d + "/"
                    + m + "/" + y);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다.
    // 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
    //
    public static int getDaysFrom21Century(int d, int m, int y) {
        if (y >= 2000) {
            int sum = getDaysFromYearFirst(d, m, y);
            for (int j = y - 1; j >= 2000; j--)
                sum += getDaysInYear(j);
            return sum - 1;
        } else if (y > 0 && y < 2000) {
            int sum = getDaysFromYearFirst(d, m, y);
            for (int j = 1999; j >= y; j--)
                sum -= getDaysInYear(y);
            return sum - 1;
        } else
            throw new RuntimeException("Invalid year " + y + " in " + d + "/"
                    + m + "/" + y);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 지정한 월의 첫날 부터 지정한 날 까지의 날짜 수를 구한다.
    //
    public static int getDaysFromMonthFirst(String s) {
        int d, m, y;
        if (s.length() == 8) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(4, 6));
            d = Integer.parseInt(s.substring(6));
            return getDaysFromMonthFirst(d, m, y);
        } else if (s.length() == 10) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(5, 7));
            d = Integer.parseInt(s.substring(8));
            return getDaysFromMonthFirst(d, m, y);
        } else if (s.length() == 11) {
            d = Integer.parseInt(s.substring(0, 2));
            String strM = s.substring(3, 6).toUpperCase();
            String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
            m = 0;
            for (int j = 1; j <= 12; j++) {
                if (strM.equals(monthNames[j - 1])) {
                    m = j;
                    break;
                }
            }
            if (m < 1 || m > 12)
                throw new RuntimeException("Invalid month name: " + strM
                        + " in " + s);
            y = Integer.parseInt(s.substring(7));
            return getDaysFromMonthFirst(d, m, y);
        } else
            throw new RuntimeException("Invalid date format: " + s);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 첫날 부터 지정한 월의 지정한 날 까지의 날짜 수를 구한다.
    //
    public static int getDaysFromYearFirst(String s) {
        int d, m, y;
        if (s.length() == 8) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(4, 6));
            d = Integer.parseInt(s.substring(6));
            return getDaysFromYearFirst(d, m, y);
        } else if (s.length() == 10) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(5, 7));
            d = Integer.parseInt(s.substring(8));
            return getDaysFromYearFirst(d, m, y);
        } else if (s.length() == 11) {
            d = Integer.parseInt(s.substring(0, 2));
            String strM = s.substring(3, 6).toUpperCase();
            String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
            m = 0;
            for (int j = 1; j <= 12; j++) {
                if (strM.equals(monthNames[j - 1])) {
                    m = j;
                    break;
                }
            }
            if (m < 1 || m > 12)
                throw new RuntimeException("Invalid month name: " + strM
                        + " in " + s);
            y = Integer.parseInt(s.substring(7));
            return getDaysFromYearFirst(d, m, y);
        } else
            throw new RuntimeException("Invalid date format: " + s);
    }

    // ////////////////////////////////////////////////////////////////////
    // 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다.
    // 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
    //
    public static int getDaysFrom21Century(String s) {
        int d, m, y;
        if (s.length() == 8) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(4, 6));
            d = Integer.parseInt(s.substring(6));
            return getDaysFrom21Century(d, m, y);
        } else if (s.length() == 10) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(5, 7));
            d = Integer.parseInt(s.substring(8));
            return getDaysFrom21Century(d, m, y);
        } else if (s.length() == 11) {
            d = Integer.parseInt(s.substring(0, 2));
            String strM = s.substring(3, 6).toUpperCase();
            String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
            m = 0;
            for (int j = 1; j <= 12; j++) {
                if (strM.equals(monthNames[j - 1])) {
                    m = j;
                    break;
                }
            }
            if (m < 1 || m > 12)
                throw new RuntimeException("Invalid month name: " + strM
                        + " in " + s);
            y = Integer.parseInt(s.substring(7));
            return getDaysFrom21Century(d, m, y);
        } else
            throw new RuntimeException("Invalid date format: " + s);
    }

    // ///////////////////////////////////////////
    // (양 끝 제외) 날짜 차이 구하기
    //
    public static int getDaysBetween(String s1, String s2) {
        int y1 = getDaysFrom21Century(s1);
        int y2 = getDaysFrom21Century(s2);
        return y1 - y2 - 1;
    }

    // ///////////////////////////////////////////
    // 날짜 차이 구하기
    //
    public static int getDaysDiff(String s1, String s2) {
        int y1 = getDaysFrom21Century(s1);
        int y2 = getDaysFrom21Century(s2);
        return y1 - y2;
    }

    // ///////////////////////////////////////////
    // (양 끝 포함) 날짜 차이 구하기
    //
    public static int getDaysFromTo(String s1, String s2) {
        int y1 = getDaysFrom21Century(s1);
        int y2 = getDaysFrom21Century(s2);
        return y1 - y2 + 1;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도, 지정한 월에 지정한 요일이 들어 있는 회수를 구한다.
    //
    public static int getWeekdaysInMonth(int weekDay, int m, int y) {
        int week = ((weekDay - 1) % 7);
        int days = getDaysInMonth(m, y);
        int sum = 6; // 2000년 1월 1일은 토요일
        if (y >= 2000) {
            for (int i = 2000; i < y; i++)
                sum += getDaysInYear(i);
        } else {
            for (int i = y; i < 2000; i++)
                sum -= getDaysInYear(i);
        }
        for (int i = 1; i < m; i++)
            sum += getDaysInMonth(i, y);

        // if (sum < 0)
        // sum += 350*3000;

        int firstWeekDay = sum % 7;
        if (firstWeekDay < 0) {
            firstWeekDay += 7;
        }

        // firstWeekDay += 1;
        int n = firstWeekDay + days;
        int counter = (n / 7) + (((n % 7) > week) ? 1 : 0);
        if (firstWeekDay > week)
            counter--;

        return counter;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 년도의 지정한 달에 지정한 요일이 들어 있는 회수를 구한다.
    //
    public static int getWeekdaysInMonth(String weekName, int m, int y) {
        StringBuffer weekNames1 = new StringBuffer("일월화수목금토");
        StringBuffer weekNames2 = new StringBuffer("日月火水木金土");
        String[] weekNames3 = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };

        int n = weekNames1.indexOf(weekName);
        if (n < 0)
            n = weekNames2.indexOf(weekName);
        if (n < 0) {
            String str = weekName.toLowerCase();
            for (int i = 0; i < weekNames3.length; i++) {
                if (str.equals(weekNames3[i])) {
                    n = i;
                    break;
                }
            }
        }
        // System.out.println("n = " + n);

        if (n < 0)
            throw new RuntimeException("Invalid week name: " + weekName);

        return getWeekdaysInMonth(n + 1, m, y);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 2000년 1월 1일 기준을 n일 경과한 날짜 구하기
    //
    // @return yyyy-mm-dd 양식의 String 타입
    public static String getDateStringFrom21Century(int elapsed) {
        int y = 2000;
        int m = 1;
        int d = 1;

        int n = elapsed + 1;
        int j = 2000;
        if (n > 0) {
            while (n > getDaysInYear(j)) {
                n -= getDaysInYear(j);
                j++;
            }
            y = j;

            int i = 1;
            while (n > getDaysInMonth(i, y)) {
                n -= getDaysInMonth(i, y);
                i++;
            }
            m = i;
            d = n;
        } else if (n < 0) {
            while (n < 0) {
                n += getDaysInYear(j - 1);
                j--;
            }
            y = j;

            int i = 1;
            while (n > getDaysInMonth(i, y)) {
                n -= getDaysInMonth(i, y);
                i++;
            }
            m = i;
            d = n;
        }

        String strY = "" + y;
        String strM = "";
        String strD = "";

        if (m < 10)
            strM = "0" + m;
        else
            strM = "" + m;

        if (d < 10)
            strD = "0" + d;
        else
            strD = "" + d;

        return strY + "/" + strM + "/" + strD;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 날짜를 offset일 경과한 날짜 구하기
    //
    // @return yyyy-mm-dd 양식의 String 타입
    public static String addDate(int offset, int d, int m, int y) {
        int z = getDaysFrom21Century(d, m, y);
        int n = z + offset;
        return getDateStringFrom21Century(n);
    }

    // //////////////////////////////////////////////////////////////////////////
    // 지정한 날짜를 offset일 경과한 날짜 구하기
    //
    // @return yyyy-mm-dd 양식의 String 타입
    public static String addDate(int offset, String date) {
        int z = getDaysFrom21Century(date);
        int n = z + offset;
        return getDateStringFrom21Century(n);
    }

    public static void main(String[] args) {
        System.out.println("----------------------------------------------");
        System.out.println("지정한 년도에 포함된 날수 구하기:");
        int x = getDaysInYear(2002);
        System.out.println(x);
        System.out.println("지정한 년도의 지정한 달에 포함된 날수 구하기:");
        int y = getDaysInMonth(1, 2002);
        System.out.println(y);
        System.out.println("2000년 1월 1일 이후 경과한 날수 구하기:");
        int z = getDaysFromYearFirst(11, 9, 2002);
        System.out.println(z);
        int z1 = getDaysFromYearFirst("20020911");
        System.out.println(z1);
        int z2 = getDaysFromYearFirst("2002/09/11");
        System.out.println(z2);
        int z3 = getDaysFromYearFirst("2002-09-11");
        System.out.println(z3);
        int z4 = getDaysFromYearFirst("11-Sep-2002");
        System.out.println(z4);
        int z5 = getDaysFromMonthFirst("11-Sep-2002");
        System.out.println(z5);
        int z6 = getDaysFrom21Century(11, 9, 2002);
        System.out.println(z6);
        int z7 = getDaysFrom21Century("2002-09-11");
        System.out.println(z7);
        int z8 = getDaysFrom21Century("11-Sep-2002");
        System.out.println(z8);
        int u = getDaysDiff("11-Sep-2002", "01-Jan-2002");
        System.out.println(u);
        int u2 = getDaysDiff("01-Jan-2002", "11-Sep-2002");
        System.out.println(u2);
        int u3 = getDaysDiff("11-Sep-2002", "31-Dec-2000");
        System.out.println(u3);
        int u4 = getDaysDiff("2002-09-10", "2002-09-01");
        System.out.println(u4);
        int u5 = getDaysFrom21Century("31-Dec-2000");
        System.out.println(u5);
        int u6 = getDaysFrom21Century("2000-12-31");
        System.out.println(u6);
        int u7 = getDaysFrom21Century("1999-12-31");
        System.out.println(u7);
        int u8 = getDaysFrom21Century("2000-01-01");
        System.out.println(u8);

        System.out
                .println("-------------------------------------------------------------");
        System.out.println("지정한 년도의 지정한 달에 포함된 지정한 요일의 개수 구하기:");
        System.out.println("2002년 11월에 금요일 개수는 "
                + getWeekdaysInMonth(6, 11, 2002));
        System.out.println("2002년 11월에 목요일 개수는 "
                + getWeekdaysInMonth("목", 11, 2002));
        System.out.println("2002년 12월에 금요일 개수는 "
                + getWeekdaysInMonth(6, 12, 2002));
        System.out.println("2002년 12월에 金요일 개수는 "
                + getWeekdaysInMonth("金", 12, 2002));
        System.out.println("2002년 12월에 Tuesday 개수는 "
                + getWeekdaysInMonth("tue", 12, 2002));
        System.out.println("2002년 12월에 Saturday 개수는 "
                + getWeekdaysInMonth("sat", 12, 2001));
        System.out.println("1998년 12월에 Saturday 개수는 "
                + getWeekdaysInMonth("sat", 12, 1998));

        System.out
                .println("-------------------------------------------------------------");
        System.out.println("1990년 12월에   Sunday 개수는 "
                + getWeekdaysInMonth("sun", 12, 1990));
        System.out.println("                Monday 개수는 "
                + getWeekdaysInMonth("mon", 12, 1990));
        System.out.println("               Tuesday 개수는 "
                + getWeekdaysInMonth("tue", 12, 1990));
        System.out.println("              Wednsday 개수는 "
                + getWeekdaysInMonth("wed", 12, 1990));
        System.out.println("              Thursday 개수는 "
                + getWeekdaysInMonth("thu", 12, 1990));
        System.out.println("                Friday 개수는 "
                + getWeekdaysInMonth("fri", 12, 1990));
        System.out.println("              Saturday 개수는 "
                + getWeekdaysInMonth("sat", 12, 1990));

        System.out
                .println("-------------------------------------------------------------");
        System.out.println("2002년 1월 1일 기준으로 지정한 날수 만큼 경과한 날짜 구하기:");
        System.out.println(getDateStringFrom21Century(366));
        System.out.println(getDateStringFrom21Century(-365));
        System.out.println(addDate(364, 1, 1, 2002));
        System.out.println(addDate(365, "2002/01/01"));
    }
}

