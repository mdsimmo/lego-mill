package mill;


/**
 * Same as a PhysicalMill but doesn't do actually do anything. Useful for testing
 * when the nxt isn't attached.
 */
public class DummyPhysicalMill extends PhysicalMill {

	@Override
	public void setDrillDepth( float depth ) {
		drillDepth = depth;
	}

	@Override
	public void setCarrage( float distance ) {
		carrageDistance = distance;
	}

	@Override
	public void setSpindle( float radians ) {
		spindleRotation = radians;
	}
	
	@Override
	public boolean isMoving() {
		return false;
	}
	
	@Override
	public boolean isAtEnd() {
		return drillDepth >= MAXIMUM_CARRAGE;
	}
}
