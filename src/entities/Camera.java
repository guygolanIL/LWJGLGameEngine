package entities;
 
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
 
public class Camera {
     
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;
     
    public Camera(){}
    
    public Camera(Vector3f position){
    	this.position = position;
    }
     
    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z-=0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
        	position.z+=0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            position.y+=0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
        	if(position.y > 1){
        		position.y-=0.1f;        		
        	}
        }
        
        
        if(Mouse.isButtonDown(0)){
        	this.yaw += Mouse.getDX() / 1.5;
        	this.pitch += Mouse.getDY() / 1.5;
        }
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public float getPitch() {
        return pitch;
    }
 
    public float getYaw() {
        return yaw;
    }
 
    public float getRoll() {
        return roll;
    }
     
     
 
}