package textures;
 
public class ModelTexture {
     
    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;
    
    private int numberOfRows = 1;
    private boolean hasTransparancy = false;
    private boolean useFakeLighting = false;
    
    public ModelTexture(int texture){
        this.textureID = texture;
    }

    public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
    
    public int getNumberOfRows() {
		return numberOfRows;
	}
    
    public boolean isUseFakeLighting() {
		return useFakeLighting;
	}
    
    
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparancy() {
		return hasTransparancy;
	}

	public void setHasTransparancy(boolean hasTransparancy) {
		this.hasTransparancy = hasTransparancy;
	}

	public int getID(){
        return textureID;
    }

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
    
 
}