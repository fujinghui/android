package femy.framework.game;
import android.media.SoundPool;
import femy.framework.base.Sound;

public class AndroidSound implements Sound{
	int soundId;
	SoundPool soundPool;
	public AndroidSound(SoundPool soundPool, int soundId){
		this.soundId = soundId;
		this.soundPool = soundPool;
	}
	@Override
	public void play(float volume) {
		// TODO Auto-generated method stub
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		soundPool.unload(soundId);
	}
}
