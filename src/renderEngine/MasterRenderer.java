package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class MasterRenderer {


    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
	
    private Matrix4f projectionMatrix;
    
    
	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel , List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	
	public MasterRenderer(){
		GL11.glEnable(GL11.GL_CULL_FACE);
    	GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		renderer = new EntityRenderer(shader , projectionMatrix);
	}
	public void render(Light sun , Camera camera){
		prepare();
		
		//entities rendering
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processEntity(Entity entity){
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch!= null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new LinkedList<>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		
		terrains.add(terrain);
	}

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.5f, 0, 0, 1);
    }
    
    
    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
    
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
}
