/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.data.map;

/**
 *
 * @author emil.simon
 */
public enum ContinentTypeEnum {
    // NAME         FOREST ENUM                 WASTES ENUM             COV     MT      FR      WS
    FLAT_STEPPES    (TerrainTypeEnum.FOREST,    TerrainTypeEnum.ARID,   .33,    5.,     2.,     5.),
    MOUNTAINOUS     (TerrainTypeEnum.FOREST,    TerrainTypeEnum.ARID,   .8,     10.,    8.,     0.),
    DESERT          (TerrainTypeEnum.FOREST,    TerrainTypeEnum.DESERT, .8,     2.,     0.,     10.),
    WOODLANDS       (TerrainTypeEnum.FOREST,    TerrainTypeEnum.ARID,   .8,     3.,     10.,    0.),
    VALLEY          (TerrainTypeEnum.FOREST,    TerrainTypeEnum.ARID,   .5,     3.,     3.,     0.5),
    BARRENS         (TerrainTypeEnum.MARSHES,   TerrainTypeEnum.WASTES, .85,    10.,    2.,     20.),
    
    ;
    
    public TerrainTypeEnum forest;
    public TerrainTypeEnum wasteland;
    
    public double coverage;
    public double mt_factor;
    public double fr_factor;
    public double ws_factor;
    
    ContinentTypeEnum (TerrainTypeEnum forest, TerrainTypeEnum wasteland, double coverage, double mt, double fr, double ws) {
        this.forest = forest;
        this.wasteland = wasteland;
        this.coverage = coverage;
        this.mt_factor = mt;
        this.fr_factor = fr;
        this.ws_factor = ws;
    }
    
    public static ContinentTypeEnum getRandom () {
        int index = (int)(Math.random() * ContinentTypeEnum.values().length);
        return ContinentTypeEnum.values()[index];
    }
}
