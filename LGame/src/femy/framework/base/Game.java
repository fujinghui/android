package femy.framework.base;

public interface Game {
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public Audio getAudio();
	public void setScreen(Screen Screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
}
