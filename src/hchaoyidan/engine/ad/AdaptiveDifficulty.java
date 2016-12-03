package hchaoyidan.engine.ad;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveDifficulty {

	private final int MIN_ENEMIES = 3;
	private final int MIN_POINTS = 20;
	private int countdown = 1000;
	
	/**
	 * Received info from LevelManager to dictate how many more enemies and points should be produced at this time
	 * @param playerPercent
	 * @param enemies
	 * @param points
	 * @param level
	 * @return
	 */
	public List<Integer> onTick(int playerPercent, int enemies, int points, int level) {
		countdown--;
		
		List<Integer> toReturn = new ArrayList<Integer>();
		int enemyNum = 0;
		int pointsNum = 0;
		
		// baseline replenishing
		if(enemies < MIN_ENEMIES + level) { 
			enemyNum = (MIN_ENEMIES + level) - enemies; 
		}
		
		if(points < MIN_POINTS) {
			pointsNum = MIN_POINTS - points;
		}
		
		if(playerPercent >= 90) {
			enemyNum += 2;
		} else if (playerPercent >= 70) {
			enemyNum++;
		} else if (playerPercent < 50 && playerPercent >= 30 && countdown <= 0) {
			pointsNum += 5;
			if(enemyNum != 0) {
				enemyNum--;
			}
		} else if(playerPercent <= 10 && countdown <= 0){
			pointsNum += 10;
			if(enemyNum != 0) {
				enemyNum -= 2;
			}
		}

		if(countdown <= 0) {
			countdown = 1000;
		}
		
		toReturn.add(enemyNum);
		toReturn.add(pointsNum);

		return toReturn;
	}
}
