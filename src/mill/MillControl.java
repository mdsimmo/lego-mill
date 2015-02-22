package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 * Handles all the logic needed to cut a part
 */
public class MillControl {

	SimpleApplication app;
	VirtualMill virtual;
	PhysicalMill physical;
	boolean hasColided;
	boolean forwards = true;

	public MillControl( SimpleApplication app, Spatial part ) {
		this.app = app;
		this.virtual = new VirtualMill( app );
		this.virtual.setPart( part );
		this.physical = new PhysicalMill();
	}

	public void update() {
		if ( !hasColided ) {
			// move down until touching the part
			virtual.tickDrillIn();
			if ( virtual.isColision()
					|| virtual.getDrillDepth() < Mill.MIN_DEPTH ) {
				hasColided = true;
				virtual.tickDrillOut();
				float position = virtual.getCarrage();
				if ( Mill.START_CARRAGE > position
						|| position > Mill.MAXIMUM_CARRAGE ) {
					if ( virtual.getSpindle() > FastMath.PI ) {
						app.stop();
						return;
					}
					virtual.tickSpindleForwards();
					physical.tickSpindleForwards();
					forwards = !forwards;
				}
				virtual.tickCarrage( forwards );
			} else {
				physical.tickDrillIn();
			}
		}
		if ( hasColided ) {
			// move drill out then across
			if ( virtual.isColision()
					&& virtual.getDrillDepth() < Mill.MAXIMUM_DEPTH ) {
				// keep going out
				virtual.tickDrillOut();
				physical.tickDrillOut();
			} else {
				// safe to go across
				physical.tickCarrage( forwards );
				hasColided = false;
			}
		}
	}
}
