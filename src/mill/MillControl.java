package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 * Handles all the logic needed to cut a part
 */
public class MillControl {

	// movement constraints on the drill
	public static final float DRILL_START    = -25;  // mm
	public static final float DRILL_MIN      = -40;  // mm
	public static final float DRILL_MAX      =  20;  // mm
	public static final float CARRIAGE_START =  0;   // mm
	public static final float CARRIAGE_MAX   =  100; // mm
	public static final float CARRIAGE_MIN   =  0;   // mm
	public static final float SPINDLE_START  =  0;   // deg
	
	// mm across per rotation
	private static final float CARRIAGE_STEP = 3f;  // mm per rotation
	
	// accuracy control (an speed control when nxt not attached)
	private static final float SPINDLE_STEP  = FastMath.TWO_PI / 500;
	
	private final VirtualMill virtual;
	
	private final Mill physical;

	public MillControl( SimpleApplication app, Spatial part ) {
		this.virtual = new VirtualMill( app, part );
		this.physical = Main.connected ? new PhysicalMill() : new DummyMill();
	}

	public void update() {
		// set virtual to current physical
		/*virtual.setDrill( physical.getDrill() );
		virtual.setSpindle( physical.getSpindle() );
		virtual.setCarriage( physical.getCarriage() );*/
		
		// move spindle slightly
		virtual.setSpindle( virtual.getSpindle() + SPINDLE_STEP );
		float loops = virtual.getSpindle() / FastMath.TWO_PI;
		virtual.setCarriage( loops * CARRIAGE_STEP );
		virtual.touchPart();
		
		// tell physical to go somewhere
		physical.setLocation( 
				virtual.getDrill(),
				virtual.getCarriage(),
				virtual.getSpindle() );
	}
	
	public boolean isFinished() {
		return false;
	}
}
