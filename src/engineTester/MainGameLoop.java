package engineTester;
 
import models.RawModel;
import models.TexturedModel;
 




import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
 




import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
 
public class MainGameLoop {
 
    public static void main(String[] args) {
 
        DisplayManager.createDisplay();
        Loader loader = new Loader();
         
        RawModel model = OBJLoader.loadObjModel("stall", loader);
         
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        
        Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
        Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1, 1, 1)); 
        
        Camera camera = new Camera();
         
        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
        	camera.move();
        	
        	if(Mouse.isButtonDown(0)){
        		entity.increaseRotation(Mouse.getDY(), Mouse.getDX(), 0);        		
        	}
        	
        	renderer.processEntity(entity);
        	renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
 
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
 
    }
 
}