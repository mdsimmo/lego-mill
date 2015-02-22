package mill;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {

	ViewController viewController;
	MillControl mill;

	@Override
	public void simpleInitApp() {
		viewController = new ViewController( this );
		mill = new MillControl( this, designPart() );
	}

	public Spatial designPart() {
		return assetManager.loadModel( "Models/person.blend" );
	}

	@Override
	public void simpleUpdate( float tpf ) {
		viewController.handleCameraMove( tpf );
		mill.update();
	}

	public static void main( String[] args ) {
		Main app = new Main();
		app.start();
	}

}