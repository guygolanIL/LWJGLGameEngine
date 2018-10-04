package renderEngine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.TexturedModel;
import shaders.StaticShader;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel , List<Entity>> entities = new HashMap<>();
	
	public void render(Light sun , Camera camera){
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
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
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
