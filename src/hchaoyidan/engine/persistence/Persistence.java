package hchaoyidan.engine.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.entity.PhysicEntity;


public class Persistence<T extends PhysicEntity> implements Serializable {

	public void saveGame(PhysicsWorld world, String filename) throws IOException {
		try {
			FileOutputStream out = new FileOutputStream (filename);
			
			ObjectOutputStream objectOut = new ObjectOutputStream (out);

			objectOut.writeObject (world);

			objectOut.close();
			
			out.close();

			System.out.println("Saved game");
			// Code to write instance of GamingWorld will go here
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println ("Unable to create game data");
		}
	}
	
	public PhysicsWorld loadGame(String filename) {
		PhysicsWorld world = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(filename));
		world = (PhysicsWorld) in.readObject();
		
		in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to load");
		}
		return world;
	}
}
