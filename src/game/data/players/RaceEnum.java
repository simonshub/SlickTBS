/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players;

/**
 *
 * @author emil.simon
 */
public enum RaceEnum {
    HUMAN   ("Human", "",
             10.00, 10.00, 10.00, 10.00),
    ELF     ("Elf", "",
             10.00, 10.00, 10.00, 10.00),
    DWARF   ("Dwarf", "",
             10.00, 10.00, 10.00, 10.00),
    ORC     ("Orc", "",
             10.00, 10.00, 10.00, 10.00),
    GOBLIN  ("Goblin", "",
             10.00, 10.00, 10.00, 10.00),
    ;
    
    public String description;
    public String display_name;
    
    public double attribute_strength_base;
    public double attribute_agility_base;
    public double attribute_intelligence_base;
    public double attribute_endurance_base;
    
    RaceEnum (String name, String descr, double stre, double agil, double inte, double endu) {
        display_name = name;
        description = descr;
        attribute_strength_base = stre;
        attribute_agility_base = agil;
        attribute_intelligence_base = inte;
        attribute_endurance_base = endu;
    }
}
