package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import renderEngine.Loader;

public class ParticleMaster {

	private static Map<ParticleTexture,List<Particle>> particles = new HashMap<>();
	private static ParticleRenderer renderer;
	
	public static void init(Loader loader , Matrix4f proj){
		renderer = new ParticleRenderer(loader, proj);
	}
	
	public static void update(Camera camera){
		Iterator<Entry<ParticleTexture, List<Particle>>> mapItr = particles.entrySet().iterator();
		
		while(mapItr.hasNext()){
			List<Particle> list = mapItr.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()){
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera);
				if(!stillAlive){
					iterator.remove();
					if(list.isEmpty()){
						mapItr.remove();
					}
				}
			}
			InsertionSort.sortHighToLow(list);
		}
		
		
	}
	
	public static void renderParticles(Camera camera){
		renderer.render(particles, camera);
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle p){
		List<Particle> list = particles.get(p.getTexture());
		if(list == null){
			list = new ArrayList<>();
			particles.put(p.getTexture(), list);
		}
		list.add(p);
		
	}
}
