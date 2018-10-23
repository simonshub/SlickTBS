package game.data.player.technology;

import java.util.ArrayList;
import java.util.List;

public class Technology {

	public final String id;
	
	public final String display_name;
	
	public final int advancement_time;
	
	public final List<Technology> requirements;
	
	
	
	public Technology (String id, String display_name, int advancement_time) {
		this.id = id;
		this.display_name = display_name;
		this.advancement_time = advancement_time;
		
		this.requirements = new ArrayList<> ();
	}
	
}
