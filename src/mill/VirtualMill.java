package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * A mill that performs all actions in the virtual space
 */
public class VirtualMill implements Mill {

	Spatial drill;
	Spatial part;
	Node scene;

	public VirtualMill( SimpleApplication app ) {
		scene = new Node( "mill" );
		app.getRootNode().attachChild( scene );
		scene.scale( 0.01f ); // get it into more normal units
		
		drill = app.getAssetManager().loadModel( "Models/drillSimple.blend" );
		drill.rotate( 0, 0, FastMath.HALF_PI );
		setDrill( drill );
		
		Box box = new Box( new Vector3f( Mill.START_DEPTH, 0,
				Mill.START_CARRAGE ), new Vector3f( -Mill.MAXIMUM_DEPTH, 0.1f,
				-Mill.MAXIMUM_CARRAGE ) );
		Spatial floor = new Geometry( "floor", box );
		floor.setMaterial( new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" ) );
		floor.setLocalTranslation( 0, -Mill.MAXIMUM_DEPTH, 0 );
		scene.attachChild( floor );

		Vector3f lightSource = new Vector3f( -0.1f, -0.7f, -0.1f );
		DirectionalLight light = new DirectionalLight();
		light.setDirection( lightSource );
		light.setColor( ColorRGBA.White.mult( 0.8f ) );
		scene.addLight( light );
		
		DirectionalLight backLight = new DirectionalLight();
		backLight.setDirection( lightSource.mult( -1 ) );
		backLight.setColor( ColorRGBA.White.mult( 0.3f ));
		scene.addLight( backLight );
	}

	/**
	 * Sets the drill spatial to use. This method should only be called before
	 * before any of the movement methods are called. A default drill is
	 * provided so it is not necercary to call this method.
	 * 
	 * @param drill
	 *            the drill
	 */
	public void setDrill( Spatial drill ) {
		if ( this.drill != null )
			scene.detachChild( this.drill );
		this.drill = drill;
		drill.scale( 1000 ); // because blender works in meters
		scene.attachChild( drill );
	}

	public void setDrillBounds( BoundingVolume bounds ) {
		this.drill.setModelBound( bounds );
	}

	/**
	 * Sets what part to use. This method should be called before any
	 * 
	 * @param part
	 */
	public void setPart( Spatial part ) {
		if ( this.part != null )
			scene.detachChild( this.part );
		this.part = part;
		part.scale( 1000 ); // because blender works in meters
		scene.attachChild( part );
		reset();
	}

	@Override
	public void tickDrill( boolean in ) {
		drill.move( ( in ? 1 : -1 ) * STEP_DRILL_IN, 0, 0 );
	}

	@Override
	public float getDrillDepth() {
		return -drill.getLocalTranslation().x;
	}

	@Override
	public void setDrillDepth( float depth ) {
		drill.setLocalTranslation( drill.getLocalTranslation().setX( -depth ) );
	}

	@Override
	public void tickCarrage( boolean forwards ) {
		drill.move( 0, 0, ( forwards ? -1 : 1 ) * STEP_CARRAGE_MOVE );
	}

	@Override
	public void setCarrage( float distance ) {
		drill.setLocalTranslation( drill.getLocalTranslation().setZ( -distance ) );
	}

	@Override
	public float getCarrage() {
		return -drill.getLocalTranslation().z;
	}

	@Override
	public void tickSpindle( boolean forwards ) {
		part.rotate( 0, 0, ( forwards ? 1 : -1 ) * STEP_CHUCK_ROTATE );
	}

	@Override
	public void setSpindle( float radians ) {
		Quaternion quaternion = new Quaternion();
		quaternion.fromAngleAxis( radians, new Vector3f( 0, 0, 1 ) );
		part.setLocalRotation( quaternion );
	}

	public float getSpindle() {
		return part.getLocalRotation().toAngleAxis( new Vector3f( 0, 0, 1 ) );
	}

	public boolean isColision() {
		CollisionResults results = new CollisionResults();
		return part.collideWith( drill.getWorldBound(), results ) > 0;
	}

}
