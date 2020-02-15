package torach.java_conf.gr.jp.carmaintenance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodayDate {

    final Date today_date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy'/'MM'/'dd");
    final String today_Date = df.format(today_date);
}
