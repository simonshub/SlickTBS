package game.data.player;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import game.data.player.armies.Army;
import game.data.player.settlements.Settlement;
import game.data.player.technology.Technology;

public class Player {
	
	public String player_name;
	public Color primary_color;
	public Color secondary_color;

	public List<Army> armies;
	public List<Settlement> settlements;
	public List<Technology> researched_technologies;
	
	public Player (String name, float pr, float pg, float pb, float sr, float sg, float sb) {
		player_name = name;
		primary_color = new Color (pr,pg,pb,1f);
		secondary_color = new Color (sr,sg,sb,1f);
		
		armies = new ArrayList<> ();
		settlements = new ArrayList<> ();
		researched_technologies = new ArrayList<> ();
	}
	
}
