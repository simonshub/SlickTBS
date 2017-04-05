/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.players;

import java.util.List;

/**
 *
 * @author emil.simon
 */
public class Party {
    public Player owner;
    public List<Hero> heroes;
    
    public Integer[] combat_roster_indexes = new Integer [5];
}
