package game.data.world.map.hex;

public enum FogOfWar {

	VISIBLE		(1.0f),
	REVEALED	(0.5f),
	HIDDEN		(0.0f),
	
	;
	
	public final float LEVEL;
	
	private FogOfWar (float level) {
		LEVEL = level;
	}
	
}
