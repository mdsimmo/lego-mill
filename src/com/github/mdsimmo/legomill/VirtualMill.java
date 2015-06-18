package com.github.mdsimmo.legomill;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * A mill that performs all actions in the virtual space
 */
public class VirtualMill implements Mill {

	private final Node drill;
	private float rotation;
	private final Spatial part;
	private final Node scene;
	
	public VirtualMill( SimpleApplication app, Spatial part ) {
		scene = new Node( "mill" );
		app.getRootNode().attachChild( scene );
		scene.scale( 0.01f ); // get it into more normal units

		Mesh drillMesh = new Box( 60.0f, 1f, 0.5f );
		Geometry drillGeo = new Geometry( "drill", drillMesh );
		Material drillMaterial = new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" );
		drillMaterial.setColor( "Color", ColorRGBA.Gray );
		drillGeo.setMaterial( drillMaterial );
		drillGeo.move( 60, 0, 0 );
		drill = new Node( "drill" );
		drill.attachChild( drillGeo );
		scene.attachChild( drill );

		this.part = part;
		scene.attachChild( part );

		Box box = new Box( 
				new Vector3f( MillControl.DRILL_MIN, 0,    MillControl.CARRIAGE_MIN ),
				new Vector3f( MillControl.DRILL_MAX, 0.1f, MillControl.CARRIAGE_MAX ) );
		Spatial floor = new Geometry( "floor", box );
		floor.setMaterial( new Material( app.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md" ) );
		floor.setLocalTranslation( 0, -Math.max( Math.abs( MillControl.DRILL_MAX), Math.abs( MillControl.DRILL_MIN) ), 0 );
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
		setDrill   ( MillControl.DRILL_START    );
		setCarriage( MillControl.CARRIAGE_START );
		setSpindle ( MillControl.SPINDLE_START  );
	}

	@Override
	public float getDrill() {
		return -drill.getLocalTranslation().x;
	}

	@Override
	public void setDrill( float depth ) {
		drill.setLocalTranslation( drill.getLocalTranslation().setX( -depth ) );
	}

	@Override
	public float getCarriage() {
		return drill.getLocalTranslation().z;
	}
	
	@Override
	public void setCarriage( float distance ) {
		drill.setLocalTranslation( drill.getLocalTranslation().setZ( distance ) );
	}

	public float getSpindle() {
		return rotation;
	}
	
	@Override
	public void setSpindle( float radians ) {
		Quaternion quaternion = new Quaternion();
		quaternion.fromAngleAxis( radians, new Vector3f( 0, 0, 1 ) );
		part.setLocalRotation( quaternion );
		this.rotation = radians;
	}

	public boolean isColision() {
		CollisionResults results = new CollisionResults();
		return part.collideWith( drill.getWorldBound(), results ) > 0;
	}

	@Override
	public void setLocation( float drill, float carraige, float spindle ) {
		setDrill( drill );
		setCarriage( carraige );
		setSpindle( spindle );
	}

	public void touchPart() {
		// iteratively find the touch point
		while ( !isColision() && getDrill() < MillControl.DRILL_MAX )
			setDrill( getDrill() + 0.1f );
		while ( isColision() )
			setDrill( getDrill() - 0.01f);
		while( !isColision() && getDrill() < MillControl.DRILL_MAX )
			setDrill( getDrill() + 0.001f );
	}
	
}