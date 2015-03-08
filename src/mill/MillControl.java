package mill;

import lejos.nxt.Sound;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 * Handles all the logic needed to cut a part
 */
public class MillControl {

	VirtualMill virtual;
	PhysicalMill physical;
	boolean hasColided;
	boolean tickAcross;
	int loops = 0;

	public MillControl( SimpleApplication app, Spatial part ) {
		this.virtual = new VirtualMill( app, part );
		this.physical = Main.connected ? new PhysicalMill() : new DummyPhysicalMill();
	}
	

	public boolean update() {
		if ( physical.isMoving())
			return false;
		if ( !hasColided ) {
			// move down until touching the part
			virtual.tickDrillIn();
			if ( virtual.isColision()
					|| virtual.getDrillDepth() < Mill.MIN_DEPTH ) {
				hasColided = true;
				virtual.tickDrillOut();
				float position = physical.getSpindle();
				if ( position > loops * FastMath.TWO_PI ) {
					if ( physical.isAtEnd() ) {
						end();
						return true;
					}
					tickAcross = true;
					loops++;
					virtual.tickCarrageForwards();
					hasColided = true;
				}
				virtual.tickSpindleForwards();
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
				physical.tickSpindleForwards();
				hasColided = false;
				if ( tickAcross ) {
					physical.tickCarrageForwards();
					tickAcross = false;
				}
			}
		}
		return false;
	}
	
	public void end() {
		physical.setDrillDepth( Mill.MAXIMUM_DEPTH );
		virtual.setDrillDepth( Mill.MAXIMUM_DEPTH );
		Sound.beepSequenceUp();
	}
}
