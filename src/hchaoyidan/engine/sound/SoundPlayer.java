package hchaoyidan.engine.sound;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer implements Runnable, LineListener, Serializable {

	private AudioInputStream sound;
	private Clip clip;
	private boolean playCompleted = false;
	private boolean loop = false;
			
	public SoundPlayer(File filePath, boolean loop) {
		Thread thread = new Thread(this);
		thread.start();

		this.loop = loop;
		try {
//			sound = AudioSystem.getAudioInputStream(filePath);
//			clip = AudioSystem.getClip();
//			clip.open(sound);
			sound = AudioSystem.getAudioInputStream(filePath);
            AudioFormat format = sound.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(sound);
            
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		if(clip != null) {
			
			if(loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.setFramePosition(0);
		        clip.start();
		        clip.drain();	
			}
			
		} 
	}

	public void close() {
		clip.close();
	}
	
	public void stop() {
		if (clip.isRunning()) clip.stop();
	}
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }

    }
}
