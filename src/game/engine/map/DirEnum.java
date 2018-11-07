package game.engine.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.simon.utils.SlickUtils;

public enum DirEnum {
//    CLOCKWISE
//    UPPER_RIGHT(1,-1 , 0,-1), RIGHT(1,0 , 1,0), LOWER_RIGHT(1,1 , 0,1), LOWER_LEFT(0,1 , -1,1), LEFT(-1,0 , -1,0), UPPER_LEFT(0,-1 , -1,-1),
	
//    GRID :
    UPPER_LEFT( 5 , 0,-1 , -1,-1),        UPPER_RIGHT( 0 , 1,-1 , 0,-1), 
    LEFT      ( 4 ,-1, 0 , -1, 0),        RIGHT      ( 1 , 1, 0 , 1, 0), 
    LOWER_LEFT( 3 , 0, 1 , -1, 1),        LOWER_RIGHT( 2 , 1, 1 , 0, 1)
    ;

	private final int clockwise_ordinal;
    private final int even_x_offset, even_y_offset;
    private final int odd_x_offset, odd_y_offset;

    DirEnum (int ord, int even_x, int even_y, int odd_x, int odd_y) { clockwise_ordinal=ord; even_x_offset=even_x; even_y_offset=even_y; odd_x_offset=odd_x; odd_y_offset=odd_y; }

    public static int getIndex (DirEnum dir) {
        for (int i=0;i<DirEnum.values().length;i++)
            if (dir.equals(DirEnum.values()[i])) return i;

        return 0;
    }

    public static DirEnum getRandom() {
        int index = (int)(Math.random() * DirEnum.values().length);
        return DirEnum.values()[index];
    }

    public static DirEnum getAdjacentClockwise (DirEnum dir, int offset) {
        int index = -1;
        for (int i=0;i<DirEnum.values().length;i++) {
            if (dir == DirEnum.values()[i]) {
                index = i;
                break;
            }
        }

        int adj_index = index + offset;
        while (adj_index>=DirEnum.values().length) adj_index -= DirEnum.values().length;
        while (adj_index<0) adj_index += DirEnum.values().length;
        return DirEnum.values()[adj_index];
    }

    public static DirEnum getAdjacentCounterClockwise (DirEnum dir, int offset) {
        return getAdjacentClockwise(dir,-offset);
    }

    public static DirEnum[] getAdjacents (DirEnum dir) {
        DirEnum[] result = new DirEnum[3];
        int index = DirEnum.getIndex(dir);
        
        result[0] = DirEnum.values() [ SlickUtils.cyclicalIndex(DirEnum.values(), index-1) ];
        result[1] = DirEnum.values() [ SlickUtils.cyclicalIndex(DirEnum.values(), index) ];
        result[2] = DirEnum.values() [ SlickUtils.cyclicalIndex(DirEnum.values(), index+1) ];
        
        return result;
    }
    
    public DirEnum opposite () {
        int index = DirEnum.getIndex(this);
        return DirEnum.values()[SlickUtils.cyclicalIndex(DirEnum.values(), index+3)];
    }
    
    public int getEvenX () {
    	return even_x_offset;
    }
    
    public int getEvenY () {
    	return even_y_offset;
    }
    
    public int getOddX () {
    	return odd_x_offset;
    }
    
    public int getOddY () {
    	return odd_y_offset;
    }
    
    public int getOrdinal () {
    	return clockwise_ordinal;
    }
    
    public DirEnum[] clockwiseValues () {
    	List<DirEnum> values = Arrays.asList(values());
    	values = values.stream().sorted( (v1,v2) -> v1.clockwise_ordinal - v2.clockwise_ordinal ).collect(Collectors.toList());
    	DirEnum[] sorted = new DirEnum[values.size()];
    	values.toArray(sorted);
    	return sorted;
    }
    
};