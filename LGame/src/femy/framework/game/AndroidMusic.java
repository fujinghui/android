package femy.framework.game;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import femy.framework.base.Music;

public class AndroidMusic implements Music, OnCompletionListener{
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;
	public AndroidMusic(AssetFileDescriptor assetDescriptor){
		mediaPlayer = new MediaPlayer();
		try{
			mediaPlayer.setDataSource(
					assetDescriptor.getFileDescriptor(), 
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		}catch(Exception e){
			throw new RuntimeException("Couldn't load music");
		}
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		synchronized(this){
			isPrepared = false;
		}
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		mediaPlayer.stop();
		synchronized(this){
			isPrepared = false;
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if(mediaPlayer.isPlaying())
		{
			return ;
		}
		try{
			synchronized(this){
				if(!isPrepared){
					mediaPlayer.prepare();
				mediaPlayer.start();
				}
			}
		}catch(IllegalStateException e){
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setLooping(boolean looping) {
		// TODO Auto-generated method stub
		mediaPlayer.setLooping(looping);
	}
	public void setVolume(float volume){
		mediaPlayer.setVolume(volume, volume);
	}

}
