package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

/**
 * A mill that performs all actions in the virtual space
 */
public class VirtualMill implements Mill {

	private final Node drill;
	private final Spatial part;
	private final Node scene;

	public VirtualMill( SimpleApplication app, Spatial part ) {
		scene = new Node( "mill" );
		app.getRootNode().attachChild( scene );
		scene.scale( 0.01f ); // get it into more normal units

		Mesh drillMesh = new Cylinder( 10, 10, 2, 60, true );
		Geometry drillGeo = new Geometry( "drill", drillMesh );
		drillGeo.rotate( 0, FastMath.HALF_PI, 0 );
		Material drillMaterial = new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" );
		drillMaterial.setColor( "Color", ColorRGBA.Gray );
		drillGeo.setMaterial( drillMaterial );
		drillGeo.move( -60/2, 0, 0 );
		drill = new Node( "drill" );
		drill.attachChild( drillGeo );
		scene.attachChild( drill );

		this.part = part;
		scene.attachChild( part );
		
		Box box = new Box( 
				new Vector3f( Mill.START_DEPTH, 0, Mill.START_CARRAGE ),
				new Vector3f( -Mill.MAXIMUM_DEPTH, 0.1f, -Mill.MAXIMUM_CARRAGE ) );
		Spatial floor = new Geometry( "floor", box );
		floor.setMaterial( new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" ) );
		floor.setLocalTranslation( 0, -Mill.MAXIMUM_DEPTH, 0 );
		scene.attachChild( floor );

		/*Vector3f size = new Vector3f( 26, 26, 0 );
		Box foamSize = new Box( size.mult( -0.5f ), size.mult( 0.5f ));
		Geometry geo = new Geometry( "foamSize", foamSize );
		geo.setMaterial( new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" ) );
		scene.attachChild( geo );*/
		
		Vector3f lightSource = new Vector3f( -0.1f, -0.7f, -0.1f );
		DirectionalLight light = new DirectionalLight();
		light.setDirection( lightSource );
		light.setColor( ColorRGBA.White.mult( 0.8f ) );
		scene.addLight( light );

		DirectionalLight backLight = new DirectionalLight();
		backLight.setDirection( lightSource.mult( -1 ) );
		backLight.setColor( ColorRGBA.White.mult( 0.3f ) );
		scene.addLight( backLight );
		
		// go to start position
		setCarrage( START_CARRAGE );
		setSpindle( START_ROTATION );
		setDrillDepth( START_DEPTH );
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
		part.rotate( 0, 0, ( forwards ? 1 : -1 ) * STEP_SPINDLE_ROTATE );
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
