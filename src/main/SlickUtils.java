/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

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
    
    
    
    public static void readObjectFromFile (File f, Object obj) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader (new FileReader (f));
        String line;
        while ((line=br.readLine()) != null) {
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
    
}
