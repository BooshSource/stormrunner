package com.templar.games.stormrunner.templarutil.audio;

public interface SoundListener
{
  public static final int STOPPED = 0;
  public static final int ENDED = 1;
  public static final int MUTED = 2;

  public abstract void soundStopped(String paramString, int paramInt);
}