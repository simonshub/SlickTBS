/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import main.Consts;

/**
 *
 * @author XyRoN
 */
public abstract class Log {
    public static final boolean SILENT = false;
    public static final String LOG_FILE = getLogTimestamp() + Consts.LOG_EXTENSION;
    
    public static final String LOG_PREFIX = "LOG : ";
    public static final String ERR_PREFIX = "ERR : ";
    
    public static void log (String... lines) {
        for (String line : lines) {
            try {
                String log_line = LOG_PREFIX + getTimestamp() + "  ----  " + line + "\n";
                Writer log = new BufferedWriter (new FileWriter (LOG_FILE, true));
                log.append(log_line);
                if (!SILENT)
                    System.out.print(log_line);
                log.close();
            } catch (IOException ex) {
                String log_line = ERR_PREFIX + getTimestamp() + "  ----  " + "Can't write to log file!" + "\n";
                System.err.println(log_line);
            }
        }
    }
    
    public static void err (String... lines) {
        for (String line : lines) {
            try {
                String log_line = ERR_PREFIX + getTimestamp() + "  ----  " + line + "\n";
                Writer log = new BufferedWriter (new FileWriter (LOG_FILE, true));
                log.append(log_line);
                if (!SILENT)
                    System.err.print(log_line);
                log.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void err (Exception ex) {
        int stackTraceCount = 2;
        int causeStackTraceCount = 0;
        
        int callersLineNumber = Thread.currentThread().getStackTrace()[stackTraceCount].getLineNumber();
        String callersClassName = Thread.currentThread().getStackTrace()[stackTraceCount].getClassName();
        String callersMethodName = Thread.currentThread().getStackTrace()[stackTraceCount].getMethodName();
        String exceptionClassName = ex.getClass().getSimpleName();
        
        err("Exception "+exceptionClassName+" caught at "+callersClassName+" @ "+callersMethodName+" ln:"+callersLineNumber);
        err("Exception message: "+ex.getMessage());
        
        if (ex.getCause()!=null) {
            int causeLineNumber = ex.getCause().getStackTrace()[causeStackTraceCount].getLineNumber();
            String causeClassName = ex.getCause().getStackTrace()[causeStackTraceCount].getClassName();
            String causeMethodName = ex.getCause().getStackTrace()[causeStackTraceCount].getMethodName();
            String causeName = ex.getCause().getClass().getSimpleName();
            
            err("Cause: "+causeName+" - "+causeClassName+" @ "+causeMethodName+" ln:"+causeLineNumber);
        }
        
        ex.printStackTrace();
    }
    
    public static final String getTimestamp () {
        return Consts.LOG_TIMESTAMP_FORM.format(new Date());
    }
    
    public static final String getLogTimestamp () {
        return Consts.LOG_DATE_FORM.format(new Date());
    }
    
}
