package mill;

import java.security.Permission;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {
	
	public static final boolean connected = false;
	ViewController viewController;
	MillControl mill;
	boolean finished = false;

	@Override
	public void simpleInitApp() {
		viewController = new ViewController( this );
		mill = new MillControl( this, designPart() );
		setPauseOnLostFocus( false );
	}

	public Spatial designPart() {
		return assetManager.loadModel( "Models/spiralCut.blend" ).scale( 8 );
	}

	@Override
	public void simpleUpdate( float tpf ) {
		viewController.handleCameraMove( tpf );
		if ( !finished && mill.update() ) {
			finished = true;
		}
	}

	public static void main( String[] args ) {
		// PhysicalMill mill = new PhysicalMill();
		// mill.setDrillDepth( Mill.MAXIMUM_DEPTH );
		Main app = new Main();
		app.start();
		forbidSystemExitCall();
	}

	private static void forbidSystemExitCall() {
		final SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPermission( Permission permission ) {
				if ( permission.getName().contains( "exitVM" ) ) {
					throw new SecurityException();
				}
			}
		};
		System.setSecurityManager( securityManager );
	}

}