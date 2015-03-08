package mill;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.remote.RemoteMotor;

import com.jme3.math.FastMath;

/**
 * An object representing the physical mill. When this class is asked to do
 * something, they will be carried out in real life. All actions will block
 * until the real life mill has finished moving.
 */
public class PhysicalMill implements Mill {

	private static final TouchSensor STOP_SENSOR = Main.connected ? new TouchSensor(
			SensorPort.S1 ) : null;
	private static final float DRILL_RATIO = -9.2f; // deg/mm
	private static final float CARRAGE_RATIO = DRILL_RATIO * 12 / 16 * 24; // deg/mm
	private static final float SPINDLE_RATIO = -56f / 12; // degMotor/degSpindle

	RemoteMotor drill = Main.connected ? Motor.A : null;
	RemoteMotor carrage = Main.connected ? Motor.B : null;
	RemoteMotor spindle = Main.connected ? Motor.C : null;

	float drillDepth = START_DEPTH;
	float carrageDistance = START_CARRAGE;
	float spindleRotation = START_ROTATION;

	public PhysicalMill() {
		if ( !Main.connected )
			return;
		carrage.resetTachoCount();
		drill.resetTachoCount();
		spindle.resetTachoCount();
	}

	@Override
	public void tickDrill( boolean in ) {
		drillDepth += STEP_DRILL_IN * ( in ? -1 : 1 );
		setDrillDepth( drillDepth );
	}

	@Override
	public void setDrillDepth( float depth ) {
		drill.rotateTo( (int)( ( depth - START_DEPTH ) * DRILL_RATIO ), true );
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
		carrage.rotateTo(
				(int)( ( distance - START_CARRAGE ) * CARRAGE_RATIO ), true );
		carrageDistance = distance;
	}

	@Override
	public float getCarrage() {
		return carrageDistance;
	}

	@Override
	public void tickSpindle( boolean forwards ) {
		spindleRotation += STEP_SPINDLE_ROTATE * ( forwards ? 1 : -1 );
		setSpindle( spindleRotation );
	}

	@Override
	public void setSpindle( float radians ) {
		spindle.rotateTo(
				(int)( ( radians - START_ROTATION ) * SPINDLE_RATIO * FastMath.RAD_TO_DEG ),
				true );
		spindleRotation = radians;
	}

	@Override
	public float getSpindle() {
		return spindleRotation;
	}

	public boolean isMoving() {
		return drill.isMoving() || spindle.isMoving() || carrage.isMoving();
	}

	public boolean isAtEnd() {
		return STOP_SENSOR.isPressed();
	}

}
