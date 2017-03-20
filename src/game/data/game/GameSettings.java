/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.game;

/**
 *
 * @author XyRoN
 */
public class GameSettings {
    public enum MapSize {
        TINY(16,2), SMALL(24,4), NORMAL(32,4), LARGE(48,6), HUGE(64,8);
        public int size;
        public int default_players;
        MapSize (int size, int default_players) { this.size=size; this.default_players=default_players; }
    };
    
    public MapSize map_size;
    public int number_of_players;
    
    
    public GameSettings () {
        map_size = MapSize.NORMAL;
        number_of_players = 4;
    }
}
