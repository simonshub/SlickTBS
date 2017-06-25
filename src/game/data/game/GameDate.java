/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

/**
 *
 * @author XyRoN
 */
public class GameDate {
    
    public static final int START_YEAR = 1;
    public static final int START_MONTH = 1;
    public static final int START_DAY = 1;
    public static final int DAYS_IN_WEEK = 7;
    public static final int MAX_MONTH = 12;
    public static final int MAX_DAY = 30;
    
    public static final String[] MONTH_NAMES = { "January","February","March","April","May","June","July","August","September","October","November","December" };
    public static final String[] DAY_NAMES = { "Monday","Tuesday","Wensday","Thursday","Friday","Saturday","Sunday" };
    
    public int day;
    public int month;
    public int year;
    
    public GameDate (int d, int m, int y) {
        day = d;
        month = m;
        year = y;
    }
    
    /**
     * Returns a string object representation of this ingame date,
     * formatted according to the given 'form' parameter. The 
     * following substrings are found and replaced by their 
     * respectable values:
     * 
     * D = replaced with name of day in week
     * M = replaced with name of month
     * d = replaced with day (as a number, 1-30)
     * m = replaced with month (as a number, 1-12)
     * y = replaced with year
     * 
     * @param form
     * @return 
     */
    public String format (String form) {
        String result = form;
        
        result = result.replaceAll("D", DAY_NAMES [(this.day % DAYS_IN_WEEK) - START_DAY]);
        result = result.replaceAll("M", MONTH_NAMES [this.month - START_MONTH]);
        result = result.replaceAll("d", String.valueOf(day));
        result = result.replaceAll("m", String.valueOf(month));
        result = result.replaceAll("y", String.valueOf(year));
        
        return result;
    }
    
}
