/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game.units;

import game.data.game.Spell;
import java.util.List;

/**
 *
 * @author XyRoN
 */
public class Apprentice extends Unit {
    public enum Rank {
        INITIATE(1), JOURNEYMAN(2), ADEPT(3), MAGE(4), ARCHMAGE(5);
        public int level;
        Rank(int level) {
            this.level=level;
        }
    };
    
    public int power;
    public int loyalty;
    public int current_exp;
    public int next_rank_exp;
    public int loyalty_float_target;
    public List<Spell> spells;
    
    public Rank rank;
    
    public Apprentice (int owner_id) {
        super(owner_id);
        
        power = 0;
        loyalty = 100;
        current_exp = 0;
        loyalty_float_target = 100;
        setRank(Rank.INITIATE);
    }
    
    public void addExp (int exp) {
        current_exp += exp;
        if (current_exp > next_rank_exp && rank.compareTo(Rank.ARCHMAGE)<0) {
            for (Rank r : Rank.values()) {
                if (rank.compareTo(r)<0) {
                    rank = r;
                    break;
                }
            }
        }
    }
    
    public String getRankName () {
        return rank.name().toLowerCase().replaceFirst(Character.toString(rank.name().charAt(0)), Character.toString(Character.toUpperCase(rank.name().charAt(0))));
    }
    
    private void setRank (Rank rank) {
        this.rank = rank;
        this.next_rank_exp = 0;
        for (int i=rank.level;i>0;i--)
            this.next_rank_exp += i*100;
    }
}
