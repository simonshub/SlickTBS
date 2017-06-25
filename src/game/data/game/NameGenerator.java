/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

import game.data.game.Culture.CultureTypeEnum;
import game.data.map.Continent;
import java.util.Random;
import main.Consts;
import main.utils.Log;
import main.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class NameGenerator {
    
    public static final String VOCAL_SPLIT_DELIMITER = "\\|";
    
    public static final String VOCAL_RULE_PARAM = "%rule";
    public static final String VOCAL_RACE_PARAM = "%race";
    public static final String VOCAL_NAME_PARAM = "%name";
    public static final String VOCAL_PLACE_PARAM = "%place";
    public static final String VOCAL_SOURCE_PARAM = "%source";
    public static final String VOCAL_PLURAL_PARAM = "%plural";
    public static final String VOCAL_CULTURE_PARAM = "%culture";
    public static final String VOCAL_CONTINENT_PARAM = "%continent";
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
    
    public static String culture (Race race, Continent source_continent, CultureTypeEnum type, String founder, String place, String culture_type) {
        String result = "";
        boolean to_add = false;
        
        for (String block : race.culture_name_blocks) {
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
                
                String add_vocal = vocals[SlickUtils.randIndex(vocals.length)];
                add_vocal = add_vocal.replace(VOCAL_NAME_PARAM, founder);
                add_vocal = add_vocal.replace(VOCAL_PLACE_PARAM, place);
                add_vocal = add_vocal.replace(VOCAL_CONTINENT_PARAM, source_continent.name);
                add_vocal = add_vocal.replace(VOCAL_CULTURE_PARAM, culture_type);
                add_vocal = add_vocal.replace(VOCAL_ADJECTIVE_PARAM, race.adjective);
                add_vocal = add_vocal.replace(VOCAL_PLURAL_PARAM, race.plural);
                add_vocal = add_vocal.replace(VOCAL_RACE_PARAM, race.name);
                
                result += add_vocal;
            }
        }
        
        return result;
    }
    
    public static String faction (Race race, Continent source_continent, CultureTypeEnum type, String founder, String place, String culture_type) {
        String result = "";
        boolean to_add = false;
        
        for (String block : race.faction_name_blocks) {
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
                
                String add_vocal = vocals[SlickUtils.randIndex(vocals.length)];
                add_vocal = add_vocal.replace(VOCAL_NAME_PARAM, founder);
                add_vocal = add_vocal.replace(VOCAL_PLACE_PARAM, place);
                add_vocal = add_vocal.replace(VOCAL_CONTINENT_PARAM, source_continent.name);
                add_vocal = add_vocal.replace(VOCAL_CULTURE_PARAM, culture_type);
                add_vocal = add_vocal.replace(VOCAL_ADJECTIVE_PARAM, race.adjective);
                add_vocal = add_vocal.replace(VOCAL_PLURAL_PARAM, race.plural);
                add_vocal = add_vocal.replace(VOCAL_RACE_PARAM, race.name);
                add_vocal = add_vocal.replace(VOCAL_RULE_PARAM, type.getRuleName());
                
                result += add_vocal;
            }
        }
        
        return result;
    }
    
    public static String character (Race race, boolean is_female) {
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
    
    public static String place (Race race) {
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
    
}
