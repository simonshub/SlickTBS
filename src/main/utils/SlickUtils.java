/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

/**
 *
 * @author emil.simon
 */
public abstract class SlickUtils {
    
    public static String[] splitArgs (String line) {
        String[] args = line.split("=");
        
        if (args.length != 2)
            return null;
        
        for (int i=0;i<args.length;i++) {
            args[i] = args[i].trim();
        }
        
        return args;
    }
    
    
    
    public static String getFileName (String path) {
        return path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
    }
    public static String capitalizeWords (String sentance) {
        String result = "";
        for (int i=0;i<sentance.length();i++) {
            if (i==0)
                result += Character.toUpperCase(sentance.charAt(i));
            else if (sentance.charAt(i-1)==' ')
                result += Character.toUpperCase(sentance.charAt(i));
            else
                result += sentance.charAt(i);
        }
        return result;
    }
    
    
    
    public static void readObjectFromFile (File f, Object obj) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader (new FileReader (f));
        String line;
        while ((line=br.readLine()) != null) {
            if (line.startsWith("#")) continue;
            
            String[] args = SlickUtils.splitArgs(line);
            try {
                Field aField = obj.getClass().getField(args[0]);
                Class fieldClass = aField.getType();
                
                if (fieldClass.equals(String.class)) {
                    aField.set(obj, args[1]);
                } else if (fieldClass.isPrimitive()) {
                    if (fieldClass.equals(int.class)) {
                        aField.set(obj, Integer.parseInt(args[1]));
                    } else if (fieldClass.equals(float.class)) {
                        aField.set(obj, Float.parseFloat(args[1]));
                    } else if (fieldClass.equals(double.class)) {
                        aField.set(obj, Double.parseDouble(args[1]));
                    } else if (fieldClass.equals(boolean.class)) {
                        aField.set(obj, Boolean.parseBoolean(args[1]));
                    } else if (fieldClass.equals(char.class)) {
                        aField.set(obj, args[1].charAt(0));
                    }
                } }
            catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {  }
        }
        br.close();
    }
    
    public static void writeObjectToFile (File f, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException {
        BufferedWriter bw = new BufferedWriter (new FileWriter (f));
        String result = "";
        for (Field field : obj.getClass().getFields()) {
            if (field.getType().isPrimitive() || field.getType().equals(String.class))
                result += field.getName() + "=" + field.get(obj).toString() + "\n";
        }
        bw.write(result);
        bw.flush();
        bw.close();
    }
    
    
    
    public static void shuffleArray(Object[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(Object[] a, int i, int change) {
        Object helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }
    
    
    public static boolean equalsAnyInArray(Object[] array, Object obj) {
        for (Object arr_obj : array)
            if (arr_obj.equals(obj)) return true;
        return false;
    }
    
    
    
    
    /**
     * 
     * @param min inclusive
     * @param max not inclusive
     * @return 
     */
    public static int rand (int min, int max) {
        if (min > max)
            throw new ArithmeticException ();
        if (min==max)
            return max;
        
        int result = (int)(Math.round(Math.random() * (max-min)) + min);
        return result;
    }
    
    public static double rand (double min, double max) {
        if (min > max)
            throw new ArithmeticException ();
        if (min==max)
            return max;
        
        double result = (Math.random() * (max-min)) + min;
        return result;
    }
    
    public static int randIndex (int size) {
        return (int)(Math.floor(Math.random()*size));
    }
    
    public static boolean chanceRoll (double chance) {
        return (Math.random()<=chance);
    }
    
    public static int numPlusMinus (int num, int plus, int minus) {
        int offset = rand(minus,plus);
        return num+offset;
    }
    
}
