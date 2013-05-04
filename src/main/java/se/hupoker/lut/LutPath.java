package se.hupoker.lut;

import java.io.InputStream;

/**
 * 
 * @author Alexander Nyberg
 *
 */
final public class LutPath {
	private static InputStream path(String post) {
		return LutPath.class.getResourceAsStream(post);
	}

    public static String getFlopHs() { return "flop_hs.dat"; }
    public static String getFlopPpot() { return "flop_ppot.dat"; }
    public static String getFlopNpot() { return "flop_npot.dat"; }

	public static String getTurnHs() { return "turn_hs.dat"; }
	public static String getTurnNpot() { return "turn_ppot.dat"; }
	public static String getTurnPpot() { return "turn_npot.dat"; }

	public static String getRiverHs() { return "river_hs.dat"; }
}