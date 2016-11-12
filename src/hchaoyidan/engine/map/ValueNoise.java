package hchaoyidan.engine.map;

import java.util.Random;

import starter.Vec2f;

/**
 * The procedural noise generator for map terrain and units
 * @author yidanzeng
 *
 */
public class ValueNoise {
	// BUG: Produces a procedurally generated graph, but doesn't change its values even when seeds change
	
	int baseSeed;
	int currentSeed;
	Random rand;
	
	/**
	 * Creates a value noise generator
	 * @param seed
	 */
	public ValueNoise(int seed) {
		this.currentSeed = seed;
		rand = new Random();
	}
	
	/**
	 * Creates randomly generated noise
	 * @param vec position 
	 * @return float noise
	 */
	private float noise(Vec2f vec) {
		rand.setSeed(currentSeed + vec.hashCode());
		return rand.nextFloat();
	}

	/**
	 * Smoothing function
	 * @param vec position
	 * @return float after smoothing
	 */
	private float smoothNoise(Vec2f vec) {

		float corners = (noise(new Vec2f(vec.x-1, vec.y-1)) + noise(new Vec2f(vec.x+1, vec.y-1)) + noise(new Vec2f(vec.x-1, vec.y+1)) + noise(new Vec2f(vec.x+1, vec.y+1))) / 16;
		float sides = (noise(new Vec2f(vec.x-1, vec.y)) + noise(new Vec2f(vec.x+1, vec.y)) + noise(new Vec2f(vec.x, vec.y-1)) + noise(new Vec2f(vec.x, vec.y+1))) / 8;
		float center  =  noise(new Vec2f(vec.x, vec.y)) / 4;

		return corners + sides + center;
	}
	
	/**
	 * Function for rounding out interpolation
	 * @param a 
	 * @param b
	 * @param x
	 * @return
	 */
	private float cosineInterpolate(float a, float b, float x) {

		double ft = x * 3.1415927;
		double f = (1 - Math.cos(ft)) * .5;
		float func = (float) f;
		
		return a*(1-func) + b*func;
	}
	
	/**
	 * Interpolation function
	 * @param vec
	 * @return
	 */
	private float interpolatedNoise(Vec2f vec) { 
		int intX  = (int) vec.x;
		float fractionalX = vec.x - intX;

		int intY  = (int) vec.y;
		float fractionalY = vec.y - intY;

		float v1 = smoothNoise(new Vec2f(intX, intY));
		float v2 = smoothNoise(new Vec2f(intX + 1, intY));
		float v3 = smoothNoise(new Vec2f(intX, intY + 1));
  		float v4 = smoothNoise(new Vec2f(intX + 1, intY + 1));

  		float i1 = cosineInterpolate(v1 , v2 , fractionalX);
  		float i2 = cosineInterpolate(v3 , v4 , fractionalX);

  		return cosineInterpolate(i1 , i2 , fractionalY);
	}
	
	/**
	 * Actual function called for generating interpolated values
	 * @param vec 
	 * @param freq
	 * @param persistance
	 * @param octaves
	 * @return
	 */
	public float valueNoise(Vec2f vec, float freq, float persistance, int octaves) {
		float total = 0;
		float amp = 0.5f;
		
		for(int i = 0; i < octaves; i++) {
			currentSeed = baseSeed + 1;
			total =  total + interpolatedNoise(new Vec2f(vec.x * freq, vec.y * freq)) * amp;
			amp = amp * persistance;
			freq = freq * 2;
		}
		
		return total;
	}
}
