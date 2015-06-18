package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {
	
	public static final boolean connected = true;
	ViewController view;
	MillControl control;
	boolean finished = false;

	@Override
	public void simpleInitApp() {
		view = new ViewController( this );
		control = new MillControl( this, designPart() );
		setPauseOnLostFocus( false );
	}

	public Spatial designPart() {
		return assetManager.loadModel( "Models/spiralCut.blend" ).scale( 8 );
	}

	@Override
	public void simpleUpdate( float tpf ) {
		view.handleCameraMove( tpf );
		if ( !control.isFinished() )
			control.update();
	}

	public static void main( String[] args ) throws Exception {
		/*int i = 0;
		Motor.A.resetTachoCount();
		while ( true ) {
			Motor.A.setSpeed( 100 );
			Motor.A.rotateTo( i++, true );
			Thread.sleep( 1 );
		}*/
		Main app = new Main();
		app.start();
	}

}