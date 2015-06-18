package mill;

import lejos.nxt.Motor;
import lejos.nxt.remote.RemoteMotor;

import com.jme3.math.FastMath;

/**
 * An object representing the physical mill. When this class is asked to do
 * something, they will be carried out in real life. All methods will return immediately,
 * but the mill will take time to complete the requested action.
 */
public class PhysicalMill implements Mill {

	private static final float DRILL_RATIO = 9.2f; // deg/mm
	private static final float CARRIAGE_RATIO = -DRILL_RATIO * 12 / 16 * 24; // deg/mm
	private static final float SPINDLE_RATIO = -56f / 12 * FastMath.RAD_TO_DEG; // degMotor/radSpindle

	private RemoteMotor drill = Motor.A;
	private RemoteMotor carriage = Motor.B;
	private RemoteMotor spindle = Motor.C;

	public PhysicalMill() {
		carriage.resetTachoCount();
		drill.resetTachoCount();
		spindle.resetTachoCount();
		
		drill.setSpeed( 80 );
		spindle.setSpeed( 100 );
		carriage.setSpeed( 200 );
		
	}

	@Override
	public float getDrill() {
		return ( drill.getTachoCount() + MillControl.DRILL_START ) / DRILL_RATIO;
	}

	@Override
	public float getSpindle() {
		return ( spindle.getTachoCount() + MillControl.SPINDLE_START ) / SPINDLE_RATIO;
	}

	@Override
	public float getCarriage() {
		return ( carriage.getTachoCount() + MillControl.CARRIAGE_START) / CARRIAGE_RATIO;
	}

	@Override
	public void setDrill( float distance ) {
		float deg = ( distance - MillControl.DRILL_START ) * DRILL_RATIO;
		drill.rotateTo( (int)deg, true );
	}

	@Override
	public void setSpindle( float angle ) {
		float deg = ( angle - MillControl.SPINDLE_START ) * SPINDLE_RATIO;
		spindle.rotateTo( (int)deg, true );
	}

	@Override
	public void setCarriage( float distance ) {
		float deg = ( distance - MillControl.SPINDLE_START ) * CARRIAGE_RATIO;
		carriage.rotateTo( (int)deg, true );
	}

	@Override
	public void setLocation( float drill, float carriage, float spindle ) {
		/* float drillDist = Math.abs( ( drill - getDrill() ) * DRILL_RATIO );
		float carriageDist = Math.abs( ( carriage - getCarriage() ) * CARRIAGE_RATIO );
		float spindleDist = Math.abs( ( spindle - getSpindle() ) * SPINDLE_RATIO );
		
		this.drill.setSpeed( drillDist < 2 ? 0 : 100 );
		this.carriage.setSpeed( carriageDist < 2 ? 0 : 200 );
		this.spindle.setSpeed( spindleDist < 2 ? 0 : 200 ); */
		
		setDrill( drill );
		setCarriage( carriage );
		setSpindle( spindle );
		
	}

}