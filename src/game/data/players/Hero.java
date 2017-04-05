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
public class Hero {
    public RaceEnum hero_race;
    public HeroClassEnum hero_class;
    
    public double loyalty;
    public double health_regen;
    public double health_current;
    public double health_maximum;
    
    public double attribute_strength;
    public double attribute_agility;
    public double attribute_intelligence;
    public double attribute_endurance;
}
