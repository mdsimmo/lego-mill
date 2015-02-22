package mill;

import com.jme3.math.FastMath;

import lejos.nxt.Motor;
import lejos.nxt.remote.RemoteMotor;

/**
 * An object representing the physical mill. When this class is asked to do
 * something, they will be carried out in real life. All actions will block
 * until the real life mill has finished moving.
 */
public class PhysicalMill implements Mill {

	private static final float DRILL_RATIO = 9.2f; // deg/mm
	private static final float CARRAGE_RATIO = DRILL_RATIO * 12 / 16; // deg/mm
	private static final float SPINDLE_RATIO = -56f / 12; // degMotor/degSpindle

	RemoteMotor drill = Motor.A;
	RemoteMotor carrage = Motor.B;
	RemoteMotor spindle = Motor.C;

	float drillDepth = 0;
	float carrageDistance = 0;
	float spindleRotation = 0;

	public PhysicalMill() {
		reset();
	}

	@Override
	public void tickDrill( boolean in ) {
		drillDepth += STEP_DRILL_IN * ( in ? 1 : -1 );
		setDrillDepth( drillDepth );
	}

	@Override
	public void setDrillDepth( float depth ) {
		drill.rotateTo( (int)( depth * DRILL_RATIO ), true );
		drillDepth = depth;
	}

	@Override
	public float getDrillDepth() {
		return drillDepth;
	}

	@Override
	public void tickCarrage( boolean forwards ) {
		carrageDistance += STEP_CARRAGE_MOVE * ( forwards ? 1 : -1 );
		setCarrage( carrageDistance );
	}

	@Override
	public void setCarrage( float distance ) {
		carrage.rotateTo( (int)( distance * CARRAGE_RATIO ), true );
		carrageDistance = distance;
	}

	@Override
	public float getCarrage() {
		return carrageDistance;
	}

	@Override
	public void tickSpindle( boolean forwards ) {
		spindleRotation += STEP_CHUCK_ROTATE * ( forwards ? 1 : -1 );
		setSpindle( spindleRotation );
	}

	@Override
	public void setSpindle( float radians ) {
		spindle.rotateTo( (int)( radians * SPINDLE_RATIO * FastMath.RAD_TO_DEG), true );
		spindleRotation = radians;
	}

	@Override
	public float getSpindle() {
		return spindleRotation;
	}
	
	public boolean isMoving() {
		return drill.isMoving() || spindle.isMoving() || carrage.isMoving();
	}

}
