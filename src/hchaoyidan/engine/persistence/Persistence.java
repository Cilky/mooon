package hchaoyidan.engine.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;

import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.entity.PhysicEntity;

public class Persistence<T extends PhysicEntity> implements Serializable {

	public void saveGame(PhysicsWorld world, String filename) {
		try {
			FileOutputStream out = new FileOutputStream(filename);

			ObjectOutputStream objectOut = new ObjectOutputStream(out);

			objectOut.writeObject(world);

			objectOut.close();

			out.close();

			System.out.println("Saved game");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to save game.");
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
			System.out.println("Unable to load");
		}
		return world;
	}

	public void saveConfig(String filename, Properties props) {
		try {
			File f = new File(filename);
			OutputStream out = new FileOutputStream(f);
			props.store(out, "");
		} catch (Exception e) {
			System.out.println("Unable to save");
		}
	}

	public Properties loadConfig(String filename) {
		Properties props = new Properties();
		try {
			File f = new File(filename);
			InputStream in = new FileInputStream(f);
			props.load(in);
			return props;
		} catch (Exception e) {
			System.out.println("Unable to load.");
			return props;
		}
	}
}
