package mill;

import com.jme3.math.FastMath;

/**
 * All the actions that all mills should be able to perform.
 */
public interface Mill {

	// all units are in mm or radians
	public static final float STEP_DRILL_IN = 0.5f; // mm
	public static final float STEP_CARRAGE_MOVE = 0.5f; // mm
	public static final float STEP_CHUCK_ROTATE = FastMath.TWO_PI / 360; // radians
	public static final float MIN_DEPTH = -15;
	public static final float MAXIMUM_DEPTH = 40; // mm
	public static final float START_DEPTH = 0; // mm
	public static final float START_CARRAGE = 0; // mm
	public static final float MAXIMUM_CARRAGE = 200f; // mm
	public static final float START_ROTATION = 0; // radians
	
	public default void tickDrillOut() {
		tickDrill(false);
	}
	
	public default void tickDrillIn() {
		tickDrill(true);
	}
	
	public void tickDrill( boolean in );
	
	public void setDrillDepth( float depth );
	
	public float getDrillDepth();
	
	public default void tickCarrageForwards() {
		tickCarrage(true);
	}
	
	public default void tickCarrageBack() {
		tickCarrage(false);
	}
	
	public void tickCarrage( boolean forwards );
	
	public void setCarrage( float distance );
	
	public float getCarrage();
	
	public default void tickSpindleForwards() {
		tickSpindle(true);
	}
	
	public default void tickSpindleBack() {
		tickSpindle(false);
	}
	
	public void tickSpindle( boolean forwards );
	
	public void setSpindle( float radians );
	
	public float getSpindle();
	
	public default void reset() {
		setDrillDepth( START_DEPTH );
		setCarrage( START_CARRAGE );
		setSpindle( START_ROTATION );
	}
	
}
