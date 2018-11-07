package game.data.world.map;

public enum FogOfWarEnum {

    VISIBLE	(1.0f),
    REVEALED	(0.5f),
    HIDDEN	(0.0f),

    ;

    public final float LEVEL;
	
    private FogOfWarEnum (float level) {
        LEVEL = level;
    }
	
}
