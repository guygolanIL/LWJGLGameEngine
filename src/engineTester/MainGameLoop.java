package engineTester;
 
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
 
public class MainGameLoop {
 
    public static void main(String[] args) {
 
        DisplayManager.createDisplay();
        Loader loader = new Loader();
         
        
        
        RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
        RawModel fernModel = OBJLoader.loadObjModel("fern", loader); 
        
        
        TexturedModel treeTexturedModel = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));
        ModelTexture fernAtlas = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernAtlas.setNumberOfRows(2);
        TexturedModel fernTexturedModel = new TexturedModel(fernModel,fernAtlas);
        fernTexturedModel.getTexture().setHasTransparancy(true);
        fernTexturedModel.getTexture().setUseFakeLighting(true);
        
        Terrain terrain1 = new Terrain(-1 , -1, loader, new ModelTexture(loader.loadTexture("grass")),"heightmap");
        Terrain terrain2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")), "heightmap");
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain1);
        terrains.add(terrain2);
        
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i = 0 ; i < 400 ; i++){
        	if(i % 2 == 0){
        		float x = random.nextFloat() * 800 - 400;
        		float z = random.nextFloat() * -600;
        		float y = terrain1.getHeightOfTerrain(x, z);
        		entities.add(new Entity(fernTexturedModel,random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360 , 0, 0.9f));
        	}
        	if(i % 5 == 0){
        		float x = random.nextFloat() * 800 - 400;
        		float z = random.nextFloat() * -600;
        		float y = terrain1.getHeightOfTerrain(x, z);
        		entities.add(new Entity(treeTexturedModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1+4));
        	}
        }
        
        

        TexturedModel personTexturedModel = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
        Player person = new Player(personTexturedModel, new Vector3f(0, 0, 0), 0, -180, 0, 1);
        entities.add(person);
        
       
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(20000,40000,20000), new Vector3f(1, 1, 1)));
        Camera camera = new Camera(person);
         
        MasterRenderer renderer = new MasterRenderer(loader);
        
        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture health = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.9f), new Vector2f(0.15f, 0.23f));
        
        guis.add(health);
        
        
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(),terrain1);
        
        // **********WATER RENDERER set up*************** 
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos );
        List<WaterTile> waters = new ArrayList<>();
        waters.add(new WaterTile(-75, -75, 0));
        
        
        
        // *****************GAME LOOP **********************
        while(!Display.isCloseRequested()){
        	camera.move();
        	person.move(terrain1);
        	picker.update();

        	GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
        	
        	//reflection frame texture render
        	fbos.bindReflectionFrameBuffer();
        	float distance = (camera.getPosition().y - waters.get(0).getHeight()) * 2;
        	camera.getPosition().y -= distance;
        	camera.invertPitch();
        	renderer.renderScene(entities , terrains , lights , camera , new Vector4f(0, 1, 0, -waters.get(0).getHeight()));
        	camera.getPosition().y += distance;
        	camera.invertPitch();
        	
        	//refraction frame texture render
        	fbos.bindRefractionFrameBuffer();
        	renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight()));
        	
        	
        	
        	//render to screen
        	GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
        	fbos.unbindCurrentFrameBuffer();
        	renderer.renderScene(entities , terrains , lights , camera, new Vector4f(0, -1, 0, 100000));
        	waterRenderer.render(waters, camera);
        	guiRenderer.render(guis);
        	
        	
            DisplayManager.updateDisplay();
        }
 
        
        // *****************CLEAN UP ***********************
        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
 
    }
 
}