package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class ViewController implements AnalogListener, ActionListener {

	final SimpleApplication application;
	final InputManager inputManager;
	final Camera cam;
	boolean goingLeft, goingRight, goingUp, goingDown, goingForwards,
			goingBack, running;
	float rotationSpeed = 2;

	public ViewController( SimpleApplication application ) {
		this.application = application;
		this.inputManager = application.getInputManager();
		this.cam = application.getCamera();

		setUpKeys();
	}

	public void setUpKeys() {
		application.getFlyByCamera().setEnabled( false );
		inputManager.addMapping( "left", new KeyTrigger( KeyInput.KEY_A ) );
		inputManager.addMapping( "right", new KeyTrigger( KeyInput.KEY_D ) );
		inputManager.addMapping( "forwards", new KeyTrigger( KeyInput.KEY_W ) );
		inputManager.addMapping( "back", new KeyTrigger( KeyInput.KEY_S ) );
		inputManager.addMapping( "up", new KeyTrigger( KeyInput.KEY_SPACE ) );
		inputManager.addMapping( "down", new KeyTrigger( KeyInput.KEY_LSHIFT ) );
		inputManager
				.addMapping( "run", new KeyTrigger( KeyInput.KEY_LCONTROL ) );
		inputManager.addMapping( "rotateLeft", new MouseAxisTrigger(
				MouseInput.AXIS_X, true ) );
		inputManager.addMapping( "rotateRight", new MouseAxisTrigger(
				MouseInput.AXIS_X, false ) );
		inputManager.addMapping( "rotateUp", new MouseAxisTrigger(
				MouseInput.AXIS_Y, true ) );
		inputManager.addMapping( "rotateDown", new MouseAxisTrigger(
				MouseInput.AXIS_Y, false ) );
		inputManager.addListener( this, "left", "right", "forwards", "back",
				"up", "down", "run", "rotateLeft", "rotateRight", "rotateUp",
				"rotateDown" );
	}

	@Override
	public void onAction( String name, boolean isPressed, float tpf ) {
		switch ( name ) {
		case "left":
			goingLeft = isPressed;
			break;
		case "right":
			goingRight = isPressed;
			break;
		case "up":
			goingUp = isPressed;
			break;
		case "down":
			goingDown = isPressed;
			break;
		case "forwards":
			goingForwards = isPressed;
			break;
		case "back":
			goingBack = isPressed;
			break;
		case "run":
			running = isPressed;
			break;
		}
	}

	@Override
	public void onAnalog( String name, float value, float tpf ) {
		if ( name.equals( "rotateLeft" ) )
			rotateCamera( value, new Vector3f( 0, 1, 0 ) );
		if ( name.equals( "rotateRight" ) )
			rotateCamera( -value, new Vector3f( 0, 1, 0 ) );
		if ( name.equals( "rotateUp" ) )
			rotateCamera( value, cam.getLeft() );
		if ( name.equals( "rotateDown" ) )
			rotateCamera( -value, cam.getLeft() );
	}

	public void handleCameraMove( float tpf ) {
		inputManager.setCursorVisible( false );
		float walkSpeed = running ? 6 : 3;
		Vector3f loc = cam.getLocation();
		Vector3f left = cam.getLeft().multLocal( walkSpeed * tpf );
		Vector3f forwards = cam.getDirection().multLocal( walkSpeed * tpf );
		Vector3f up = new Vector3f( 0, walkSpeed * tpf, 0 );

		if ( goingLeft )
			loc.addLocal( left );
		if ( goingRight )
			loc.subtractLocal( left );
		if ( goingForwards )
			loc.addLocal( forwards );
		if ( goingBack )
			loc.subtractLocal( forwards );
		if ( goingUp )
			loc.addLocal( up );
		if ( goingDown )
			loc.subtractLocal( up );

		cam.setLocation( loc );
	}

	protected void rotateCamera( float value, Vector3f axis ) {

		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis( rotationSpeed * value, axis );

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult( up, up );
		mat.mult( left, left );
		mat.mult( dir, dir );

		Quaternion q = new Quaternion();
		q.fromAxes( left, up, dir );
		q.normalizeLocal();

		cam.setAxes( q );
	}
}
