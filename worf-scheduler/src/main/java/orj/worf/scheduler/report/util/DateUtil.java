package orj.worf.scheduler.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import orj.worf.util.DateFormatUtils;
import orj.worf.util.DateUtils;

public class DateUtil {

    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat sdfNo = new SimpleDateFormat("yyyyMMddHHmmss");
    public final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat DATE_FORMAT_DOT = new SimpleDateFormat("yyyy.MM.dd");
    public final static SimpleDateFormat DATE_TIME_FORMAT_DOT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private final static SimpleDateFormat DATE_FORMAT_CHINESE = new SimpleDateFormat("yyyy年MM月dd日");
    private final static SimpleDateFormat DATE_TIME_FORMAT_CHINESE = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public final static SimpleDateFormat DATE_FORMAT_hhmmssSSS = new SimpleDateFormat("HH:mm:ss:SSS");
    public final static SimpleDateFormat DATE_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
    private final static int SECONDTIME = 1000;

    /** 
     * 当前时间（秒）
     * 
     * @return
     *
     * @author WangZhi
     */
    public static int currentTimeSecond() {
        return (int) (System.currentTimeMillis() / SECONDTIME);
    }

    /**
     * 格式化日期，yyyy年MM月dd日 HH:mm:ss
     */
    public static String formatDateTimeWithChinese(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_FORMAT_CHINESE.format(date);
    }

    /**
     * 格式化日期时间，yyyy.MM.dd HH:mm:ss
     */
    public static String formatDateTimeWithDot(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_FORMAT_DOT.format(date);
    }

    /**
     * 格式化日期，yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateWith(Date date) {
        if (date == null) {
            return "";
        }
        return sdfTime.format(date);
    }

    /**
     * 格式化日期，yyyy年MM月dd日
     */
    public static String formatDateWithChinese(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT_CHINESE.format(date);
    }

    /**
     * 格式化日期，用'.'分隔
     */
    public static String formatDateWithDot(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT_DOT.format(date);
    }

    /** 获取当前日期的秒数 */
    public static int getCurrentTime() {
        return getDateSeconds(sdf.format(new Date()));
    }

    /**
     * 根据时间生成流水
     * 
     * @return
     *
     * @author WangZhi 2015年10月11日
     */
    public static String getDateOrderNo() {
        return sdfNo.format(new Date()) + RandomUtils.nextInt(100000, 999999);
    }

    public static int getDateSeconds(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (Exception e) {
            cal.setTime(new Date());
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return (int) (cal.getTime().getTime() / 1000);
    }

    public static String getDateStr() {
        return sdfNo.format(new Date());
    }

    public static String getDateString() {
        return DATE_FORMAT_yyyyMMdd.format(new Date());
    }

    /**
     * 获取日期时间(yyyy-MM-dd HH:mm:ss)的秒数
     */
    public static Integer getDateTimeSeconds(String dateStr) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdfTime.parse(dateStr));
        } catch (Exception e) {
            return null;
        }
        return (int) (cal.getTime().getTime() / 1000);
    }

    /**计算两个日期相差的年数,月数，天数，小时，分钟，秒*/
    public static int[] getDifferBetween2Date(Integer start, Integer end) {

        Date startDate = transferIntegerToDate(start);
        Date endDate = transferIntegerToDate(end);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        int[] result = new int[6];
        // 两个日期之间相差的年数
        result[0] = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) < 0 ? 0 : c2.get(Calendar.YEAR)
                - c1.get(Calendar.YEAR);
        // 两个日期相差的月数
        result[1] = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        // 两个日期相差的天数
        result[2] = (c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH)) < 0 ? 0 : c2
                .get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);

        // 两个日期相差的小时
        result[3] = (c2.get(Calendar.HOUR_OF_DAY) - c1.get(Calendar.HOUR_OF_DAY)) < 0 ? 0 : c2
                .get(Calendar.HOUR_OF_DAY) - c1.get(Calendar.HOUR_OF_DAY);

        // 两个日期相差的分钟
        result[4] = (c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE)) < 0 ? 0 : c2.get(Calendar.MINUTE)
                - c1.get(Calendar.MINUTE);

        // 两个日期相差的秒数
        result[5] = (c2.get(Calendar.SECOND) - c1.get(Calendar.SECOND)) < 0 ? 0 : c2.get(Calendar.SECOND)
                - c1.get(Calendar.SECOND);

        return result;
    }

    public static String getSpecifiedday(SimpleDateFormat sdf, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, amount);
        return sdf.format(cal.getTime());
    }

    public static int getTimeDifference() throws ParseException {
        Date now = new Date();
        Date d = DateUtils.addDays(now, 1);
        String str = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, d);
        Date date = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, str);
        int exp = (int) ((date.getTime() - now.getTime()) / 1000);
        return exp;
    }

    /**
     * 获取时间毫秒值 精确到秒(yyyy-MM-dd HH:mm:ss)，兼容yyyy-MM-dd
     * @param date
     * @return
     */
    public static int getTimeSeconds(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdfTime.parse(date));
        } catch (Exception e) {
            try {
                cal.setTime(sdf.parse(date));
            } catch (Exception ex) {
                cal.setTime(new Date());
            }
        }
        return (int) (cal.getTime().getTime() / 1000);
    }

    public static String getYesterday_yyyyMMdd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return sdf.format(cal.getTime());
    }

    /**
     * 
    * @Title: secondsToDateStr 
    * @Description: seconds时间转换成 string
    * @param @param seconds
    * @param @return    
    * @return String   
    * @throws
     */
    public static String secondsToDateStr(Integer seconds) {
        if (seconds != null && seconds > 0) {
            return sdf.format(transferIntegerToDate(seconds));
        }
        return null;
    }

    public static String secondsToDateTimeStr(Integer seconds) {
        if (seconds != null && seconds > 0) {
            return sdfTime.format(transferIntegerToDate(seconds));
        }
        return null;
    }

    public static String secondsToString(Integer seconds) {
        if (seconds != null && seconds > 0) {
            return sdf.format(transferIntegerToDate(seconds));
        }
        return null;
    }

    public static String secondsToTimeStr(Integer seconds) {
        if (seconds != null && seconds > 0) {
            return sdfTime.format(transferIntegerToDate(seconds));
        }
        return null;
    }

    public static String secondsToTimeString(Integer seconds) {
        if (seconds != null && seconds > 0) {
            return DATE_TIME_FORMAT_DOT.format(transferIntegerToDate(seconds));
        }
        return null;
    }

    /**
     * 把秒转化成时间
     * @param seconds(秒数)
     * @return
     */
    public static Date transferIntegerToDate(Integer seconds) {
        long millSec = Long.valueOf(seconds) * 1000;
        return transferLongToDate(millSec);
    }

    /**
     * 把毫秒转化成时间
     * @param millSec(毫秒数)
     * @return
     */
    public static Date transferLongToDate(Long millSec) {
        Date date = new Date(millSec);
        return date;
    }

    /**
     * 校验日期
     */
    public static boolean validateDate(String dateString, SimpleDateFormat dateFormat) {
        if (StringUtils.isBlank(dateString)) {
            return false;
        }
        try {
            Date date = dateFormat.parse(dateString);
            String dateStr = dateFormat.format(date);
            if (!dateString.equals(dateStr)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
