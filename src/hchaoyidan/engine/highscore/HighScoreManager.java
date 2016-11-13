package hchaoyidan.engine.highscore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author hilerychao
 *
 */

public class HighScoreManager {

	List<Integer> highScores = new ArrayList<>();
	
	public void setNewHighScores(int highScore) {
		highScores = readHighScore(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "highScores");
		highScores.add(highScore);
		Collections.sort(highScores);
		while (highScores.size() > 5) {
			highScores.remove(highScores.get(0));
		}
	}
	
	public void outputHighScores() {
		try {
			BufferedWriter writer = null;
		    writer = new BufferedWriter( new FileWriter(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "highScores"));
		    for(int i = 0; i < Math.min(highScores.size(), 5); i++) {
		    	  writer.write(Integer.toString(highScores.get(i)));
		    	  writer.write(System.getProperty( "line.separator" ));
		    	}
		    writer.close();
		    System.out.println("high scores written to file.");
		} catch ( IOException e) {
		}
	}
	
	public List<Integer> readHighScore(String fileName) {
		List<Integer> toReturn = new ArrayList<>();
		try {
			BufferedReader reader = null;

			reader = new BufferedReader(new FileReader(fileName));

			String sCurrentLine;
			
			while ((sCurrentLine = reader.readLine()) != null) {
				toReturn.add(Integer.parseInt(sCurrentLine));
			}

			reader.close();
			return toReturn;
		} catch (IOException e) {
			System.out.println("Unable to read.");
			return toReturn;
		} catch (NumberFormatException e) {
			System.out.println("Unable to read.");
			return toReturn;
		}
	}

	public List<Integer> getHighScores() {
		return highScores;
	}
}
