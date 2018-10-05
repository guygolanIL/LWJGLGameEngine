package engineTester;
 
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
 
public class MainGameLoop {
 
    public static void main(String[] args) {
 
        DisplayManager.createDisplay();
        Loader loader = new Loader();
         
        RawModel model = OBJLoader.loadObjModel("stall", loader);
         
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        
        Entity entity1 = new Entity(staticModel, new Vector3f(5,0,-25),0,90,0,1);
        Entity entity2 = new Entity(staticModel, new Vector3f(5,0,-35),0,90,0,1);
        Entity entity3 = new Entity(staticModel, new Vector3f(-5,0,-25) ,0,-90,0,1);
        Entity entity4 = new Entity(staticModel, new Vector3f(-5,0,-35) ,0,-90,0,1);
        Terrain terrain1 = new Terrain(-1 , -1, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(0 , -1, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain3 = new Terrain(1 ,-1, loader, new ModelTexture(loader.loadTexture("grass")));
        Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1, 1, 1)); 
        
        Camera camera = new Camera(new Vector3f(0, 10, 0));
         
        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
        	camera.move();
        	
        	
        	
        	renderer.processEntity(entity1);
        	renderer.processEntity(entity2);
        	renderer.processEntity(entity3);
        	renderer.processEntity(entity4);
        	renderer.processTerrain(terrain1);
        	renderer.processTerrain(terrain2);
        	renderer.processTerrain(terrain3);
        	renderer.render(light.setPosition(camera.getPosition()), camera);
            DisplayManager.updateDisplay();
        }
 
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
 
    }
 
}