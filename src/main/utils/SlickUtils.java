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
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author emil.simon
 */
public abstract class SlickUtils {
    
    public static final String COMMENT = "#";
    public static final String ARG_DELIMITER = "=";
    public static final String LIST_DELIMITER = ";;";
    
    public static String[] splitArgs (String line, String delimiter) {
        if (line.contains(COMMENT))
            line = line.substring(0, line.indexOf(COMMENT));
        
        String[] args = line.split(delimiter);
        
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
    
    public static String beautifyString (String ugly) {
        String result = ugly.toLowerCase().replaceAll("_", " ");
        return capitalizeWords(result);
    }
    
    
    
    public static void readObjectFromFile (File f, Object obj) throws FileNotFoundException, IOException {
        // TODO implement array reading
        BufferedReader br = new BufferedReader (new FileReader (f));
        String reportContent = "readObjectFromFile:[ ";
        String line;
        
        while ((line=br.readLine()) != null) {
            if (line.startsWith(COMMENT) || line.isEmpty()) continue;
            
            String[] args = SlickUtils.splitArgs(line, ARG_DELIMITER);
            try {
                Field aField = obj.getClass().getField(args[0]);
                Class fieldClass = aField.getType();
                
                if (!fieldClass.isArray()) {
                    if (fieldClass.equals(List.class)) {
                        // is a list
                        String[] elements = splitArgs(args[1], LIST_DELIMITER);
                        ParameterizedType listType = (ParameterizedType) aField.getGenericType();
                        Class<?> elementClass = (Class<?>) listType.getActualTypeArguments()[0];
                        
                        List<Object> list = new ArrayList<> ();
                        for (String element : elements) {
                            if (elementClass.equals(String.class)) {
                                list.add(element);
                            } else if (elementClass.equals(Integer.class)) {
                                list.add(Integer.parseInt(element));
                            } else if (elementClass.equals(Float.class)) {
                                list.add(Float.parseFloat(element));
                            } else if (elementClass.equals(Double.class)) {
                                list.add(Double.parseDouble(element));
                            } else if (elementClass.equals(Boolean.class)) {
                                list.add(Boolean.parseBoolean(element));
                            } else if (elementClass.equals(Character.class)) {
                                list.add(element.charAt(0));
                            } else if (elementClass.isEnum()) {
                                // is an enum
                                list.add(Enum.valueOf((Class<Enum>) elementClass, element.toUpperCase()));
                            }  else if (elementClass.isPrimitive()) {
                                // is a primitive
                                if (elementClass.equals(int.class)) {
                                    list.add(Integer.parseInt(element));
                                } else if (elementClass.equals(float.class)) {
                                    list.add(Float.parseFloat(element));
                                } else if (elementClass.equals(double.class)) {
                                    list.add(Double.parseDouble(element));
                                } else if (elementClass.equals(boolean.class)) {
                                    list.add(Boolean.parseBoolean(element));
                                } else if (elementClass.equals(char.class)) {
                                    list.add(element.charAt(0));
                                }
                            }
                        }
                        aField.set(obj, list);
                    }
                    // is not an array
                    else if (fieldClass.isEnum()) {
                        // is an enum
                        aField.set(obj, Enum.valueOf((Class<Enum>) aField.getType(), args[1].toUpperCase()));
                    } else if (fieldClass.equals(String.class)) {
                        aField.set(obj, args[1]);
                    } else if (fieldClass.equals(Integer.class)) {
                        aField.set(obj, Integer.parseInt(args[1]));
                    } else if (fieldClass.equals(Float.class)) {
                        aField.set(obj, Float.parseFloat(args[1]));
                    } else if (fieldClass.equals(Double.class)) {
                        aField.set(obj, Double.parseDouble(args[1]));
                    } else if (fieldClass.equals(Boolean.class)) {
                        aField.set(obj, Boolean.parseBoolean(args[1]));
                    } else if (fieldClass.equals(Character.class)) {
                        aField.set(obj, args[1].charAt(0));
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
                    }
                } else {
                    // is an array
                    String[] elements = splitArgs(args[1], LIST_DELIMITER);
                    Object[] array = new Object [elements.length];
                    for (int i=0;i<elements.length;i++) {
                        if (fieldClass.equals(String.class)) {
                            array[i] = elements[i];
                        } else if (fieldClass.equals(Integer.class)) {
                            array[i] = Integer.parseInt(elements[i]);
                        } else if (fieldClass.equals(Float.class)) {
                            array[i] = Float.parseFloat(elements[i]);
                        } else if (fieldClass.equals(Double.class)) {
                            array[i] = Double.parseDouble(elements[i]);
                        } else if (fieldClass.equals(Boolean.class)) {
                            array[i] = Boolean.parseBoolean(elements[i]);
                        } else if (fieldClass.equals(Character.class)) {
                            array[i] = elements[i].charAt(0);
                        } else if (fieldClass.isPrimitive()) {
                            if (fieldClass.equals(int.class)) {
                                array[i] = Integer.parseInt(elements[i]);
                            } else if (fieldClass.equals(float.class)) {
                                array[i] = Float.parseFloat(elements[i]);
                            } else if (fieldClass.equals(double.class)) {
                                array[i] = Double.parseDouble(elements[i]);
                            } else if (fieldClass.equals(boolean.class)) {
                                array[i] = Boolean.parseBoolean(elements[i]);
                            } else if (fieldClass.equals(char.class)) {
                                array[i] = elements[i].charAt(0);
                            }
                        } else if (fieldClass.isEnum()) {
                            // is an enum
                            array[i] =  Enum.valueOf((Class<Enum>) aField.getType(), args[1].toUpperCase());
                        }
                    }
                    aField.set(obj, array);
                }
                reportContent += fieldClass.getSimpleName()+" "+aField.getName()+"="+args[1]+"; ";
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) { Log.err(ex); }
        }
        br.close();
        reportContent += "]";
//        Log.log(reportContent);
    }
    
    public static void writeObjectToFile (File f, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException {
        if (!f.exists()) f.createNewFile();
        
        BufferedWriter bw = new BufferedWriter (new FileWriter (f));
        String result = "";
        for (Field field : obj.getClass().getFields()) {
            if (field.getType().isPrimitive() || field.getType().equals(String.class))
                result += field.getName() + ARG_DELIMITER + field.get(obj).toString() + "\n";
            else if (field.getClass().equals(List.class))
                result += getListAsStringList((List<?>) field.get(obj), LIST_DELIMITER);
            else if (field.getClass().isArray())
                result += getArrayAsStringList((Object[]) field.get(obj), LIST_DELIMITER);
        }
        bw.write(result);
        bw.flush();
        bw.close();
    }
    
    public static void writeClassToFile (File f, Class cls) throws IOException, IllegalArgumentException, IllegalAccessException {
        if (!f.exists()) f.createNewFile();
        
        BufferedWriter bw = new BufferedWriter (new FileWriter (f));
        String result = "";
        for (Field field : cls.getFields()) {
            if (field.getType().isPrimitive() || field.getType().equals(String.class))
                result += field.getName() + ARG_DELIMITER + field.get(null).toString() + "\n";
            else if (field.getClass().equals(List.class))
                result += getListAsStringList((List<?>) field.get(null), LIST_DELIMITER);
            else if (field.getClass().isArray())
                result += getArrayAsStringList((Object[]) field.get(null), LIST_DELIMITER);
        }
        bw.write(result);
        bw.flush();
        bw.close();
    }
    
    
    
    public static List<?> shuffleList(List<?> a) {
        Object[] array = a.toArray();
        shuffleArray(array);
        return Arrays.asList(array);
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
    
    
    public static String getArrayAsStringList (Object[] array, String delimiter) {
        String result = "";
        Object obj;
        for (int i=0;i<array.length;i++) {
            obj = array[i];
            result += obj.toString();
            if (i+1<array.length) result += delimiter;
        }
        return result;
    }
    
    public static String getListAsStringList (List<?> array, String delimiter) {
        String result = "";
        Object obj;
        for (int i=0;i<array.size();i++) {
            obj = array.get(i);
            result += obj.toString();
            if (i+1<array.size()) result += delimiter;
        }
        return result;
    }
    
    public static boolean equalsAnyInArray(Object[] array, Object obj) {
        for (Object arr_obj : array)
            if (arr_obj.equals(obj)) return true;
        return false;
    }
    
    public static int cyclicalIndex (Object[] array, int index) {
        if (index >= array.length)
            index -= array.length;
        if (index < 0)
            index += array.length;
        return index;
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
    
    public static Object randArrayObject (Object[] array) {
        return array[randIndex(array.length)];
    }
    
    public static Object randListObject (List<?> list) {
        return list.get(randIndex(list.size()));
    }
    
    public static boolean chanceRoll (double chance) {
        return (Math.random()<=chance);
    }
    
    public static int randPlusMinus (int num, int plus, int minus) {
        int offset = rand(minus,plus);
        return num+offset;
    }
    
    public static final String capitalizeFirstChar (String str) {
        if (str.isEmpty())
            return "";
        return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
    }
    
    
    
    public class Strings {
        
        public final String[] listToArray (List<String> list) {
            return Arrays.copyOf(list.toArray(), list.size(), String[].class);
        }

        public final String[] removeEmpty (String... list) {
            List<String> result = new ArrayList<> ();
            for (String s : list) {
                if (!s.isEmpty())
                    result.add(s);
            }
            return result.toArray(new String [result.size()]);
        }

        public final String[] trimAll (String... list) {
            String[] result = new String [list.length];
            for (int i=0;i<list.length;i++) {
                result[i] = list[i].trim();
            }
            return result;
        }

        public final String concatList (List<String> lines, String separator) {
            String res = "";
            for (String line : lines) {
                res += line + separator;
            }
            return res;
        }

        public final String concatArray (String[] lines, String separator) {
            String res = "";
            for (String line : lines) {
                res += line + separator;
            }
            return res;
        }
        
    }
    public static Strings Strings;
    
}
