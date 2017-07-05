/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.world;

import java.util.Random;
import main.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class NameGenerator {
    
    public static final String VOCAL_SPLIT_DELIMITER = "\\|";
    
    public static final String VOCAL_RACE_PARAM = "%race";
    public static final String VOCAL_NAME_PARAM = "%name";
    public static final String VOCAL_PLACE_PARAM = "%place";
    public static final String VOCAL_PLURAL_PARAM = "%plural";
    public static final String VOCAL_ADJECTIVE_PARAM = "%adjective";
    
    public static final String[] VOWEL_LIST = new String [] { "a", "e", "i", "o", "u", "io", "iu", "ao", "ua", "oa", "ia", "ae", "ie", "ei", "eo", "eu", "ou" };
    public static final String[] VOCAL_LIST = new String [] {
        "t", "v", "b" , "d" , "c", "k", "l", "m", "n", "g", "s", "f", "p", "w",
        "t", "v", "b" , "d" , "c", "k", "l", "m", "n", "g", "s", "f", "p", "w",
        "th", "br", "gh", "kh", "dr", "vr", "wr", "ph", "nt", "nh", "st", "sl", "ch", "sh"
    };
    
    public static String continent () {
        String result = "";
        Random rand = new Random ();
        boolean vowel = rand.nextBoolean();
        int len = SlickUtils.rand(3,4);
        
        for (int i=0;i<len;i++) {
            if (vowel)
                result += VOWEL_LIST[SlickUtils.randIndex(VOWEL_LIST.length)];
            else
                result += VOCAL_LIST[SlickUtils.randIndex(VOCAL_LIST.length)];
            
            vowel = !vowel;
        }
        
        if (vowel)
            result += "a";
        else
            result = result.substring(0, result.length()-1) + "a";
        
        return SlickUtils.capitalizeWords(result);
    }
    
    public static String character (RaceEnum race, boolean is_female) {
        String result = "";
        boolean to_add = false;
        
        if (!is_female) {
            for (String block : race.male_name_blocks) {
                String param = String.valueOf(block.charAt(0));
                
                switch (param) {
                    case "?" : // sometimes operator
                        to_add = Math.random() >= 0.5;
                        break;
                    case "*" : // always operator
                        to_add = true;
                        break;
                    case "/" : // chain operator
                        to_add = to_add;
                        break;
                    case "\\" : // possible chain operator
                        to_add = (Math.random() >= 0.5) ? to_add : false;
                        break;
                    case "!" : // inverted chain operator
                        to_add = !to_add;
                        break;
                    default :
                        break;
                }
                
                if (to_add) {
                    String[] vocals = block.substring(2).split(VOCAL_SPLIT_DELIMITER);
                    result += vocals[SlickUtils.randIndex(vocals.length)];
                }
            }
        } else {
            for (String block : race.female_name_blocks) {
                String param = String.valueOf(block.charAt(0));
                
                switch (param) {
                    case "?" : // sometimes operator
                        to_add = Math.random() >= 0.5;
                        break;
                    case "*" : // always operator
                        to_add = true;
                        break;
                    case "/" : // chain operator
                        to_add = to_add;
                        break;
                    case "\\" : // possible chain operator
                        to_add = (Math.random() >= 0.5) ? to_add : false;
                        break;
                }
                
                if (to_add) {
                    String[] vocals = block.substring(2).split(VOCAL_SPLIT_DELIMITER);
                    result += vocals[SlickUtils.randIndex(vocals.length)];
                }
            }
        }
        
        return result;
    }
    
    public static String place (RaceEnum race) {
        String result = "";
        boolean to_add = false;
        
        for (String block : race.place_name_blocks) {
            String param = String.valueOf(block.charAt(0));

            switch (param) {
                case "?" : // sometimes operator
                    to_add = Math.random() >= 0.5;
                    break;
                case "*" : // always operator
                    to_add = true;
                    break;
                case "/" : // chain operator
                    to_add = to_add;
                    break;
                case "\\" : // possible chain operator
                    to_add = (Math.random() >= 0.5) ? to_add : false;
                    break;
                case "!" : // inverted chain operator
                    to_add = !to_add;
                    break;
                default :
                    break;
            }

            if (to_add) {
                String[] vocals = block.substring(2).split(VOCAL_SPLIT_DELIMITER);
                result += vocals[SlickUtils.randIndex(vocals.length)].replaceAll(VOCAL_NAME_PARAM, NameGenerator.character(race, false))
                        .replaceAll(VOCAL_ADJECTIVE_PARAM, race.adjective).replaceAll(VOCAL_RACE_PARAM, race.name).replaceAll(VOCAL_PLURAL_PARAM, race.plural);
            }
        }
        
        return result;
    }
    
    public static String faction (String place_name, FactionTypeEnum type) {
        String result = "";
        
        if (SlickUtils.chanceRoll(50)) {
            result = type.getRuleName() + " of " + place_name;
        } else {
            result = place_name + type.getRuleName();
        }
        
        return result;
    }
}
