package com.github.mdsimmo.legomill;


/**
 * Same as a PhysicalMill but doesn't do actually do anything. Useful for testing
 * when the nxt isn't attached.
 */
public class DummyMill implements Mill {

	private float drill = MillControl.DRILL_START;
	private float carriage = MillControl.CARRIAGE_START;
	private float spindle = MillControl.SPINDLE_START;
	
	
	@Override
	public float getDrill() {
		return drill;
	}

	@Override
	public float getSpindle() {
		return spindle;
	}

	@Override
	public float getCarriage() {
		return carriage;
	}

	@Override
	public void setDrill( float distance ) {
		this.drill = distance;
	}

	@Override
	public void setSpindle( float angle ) {
		this.spindle = angle;
	}

	@Override
	public void setCarriage( float distance ) {
		this.carriage = distance;
	}

	@Override
	public void setLocation( float drill, float carraige, float spindle ) {
		setDrill( drill );
		setCarriage( carraige );
		setSpindle( spindle );
	}

	
	
}
