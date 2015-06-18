package com.github.mdsimmo.legomill;

/**
 * All the actions that all mills should be able to perform.
 */
public interface Mill {

	/**
	 * Gets the depth of the drill. Depth is zero when tip is at rotational center.
	 * Positive away from the machine.
	 * @return the drills depth
	 */
	float getDrill();
	
	/**
	 * Gets the rotation of the spindle in radians. Positive in anti-clockwise direction.
	 * @return the spindles rotation
	 */
	float getSpindle();
	
	/**
	 * Gets the distance that the carriage is from the start.
	 * @return the carriage distance
	 */
	float getCarriage();

	/**
	 * Sets the distance the drill is from the center
	 * @param distance in mm
	 */
	void setDrill( float distance );
	
	/**
	 * Sets the rotation of the spindle
	 * the angle (in radians)
	 */
	void setSpindle( float angle );
	
	/**
	 * Sets where the carriage is
	 * @param distance in mm
	 */
	void setCarriage( float distance );
	
	/**
	 * Sets the drill, carriage and spindle all at once.
	 * @param drill the drill's depth (mm)
	 * @param carraige the carraige's depth (mm)
	 * @param spindle the spindle's depth (radians)
	 */
	void setLocation( float drill, float carraige, float spindle );
	
}
