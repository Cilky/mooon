package hchaoyidan.engine.ad;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveDifficulty {

	private final int MIN_ENEMIES = 3;
	private final int MIN_POINTS = 20;
	private long nanos;
	
	/**
	 * Received info from LevelManager to dictate how many more enemies and points should be produced at this time
	 * @param playerPercent
	 * @param enemies
	 * @param points
	 * @param level
	 * @return
	 */
	public List<Integer> onTick(float playerPercent, int enemies, int points, boolean levelChange) {
		System.out.println("playerPercent " + playerPercent);
		List<Integer> toReturn = new ArrayList<Integer>();
		int enemyNum = 0;
		int pointsNum = 0;
		
		if(points < MIN_POINTS) {
			pointsNum = MIN_POINTS - points;
		}
		
		if(playerPercent >= 1.5) {
			enemyNum += 2;
		} else if (playerPercent >= 0.8) {
			enemyNum++;
		} else if (playerPercent < 0.3 && playerPercent >= 0.1) {
			pointsNum += 7;
			enemyNum = -1;
		} else if(playerPercent <= 0.05){
			pointsNum += 10;
			enemyNum = -2;
		}
		
		if(levelChange) {
			enemyNum = MIN_ENEMIES;
			pointsNum = MIN_POINTS;
		}
		toReturn.add(enemyNum);
		toReturn.add(pointsNum);

		return toReturn;
	}
}
