package com.templar.games.stormrunner.templarutil.audio;

import java.io.File;
import java.net.URL;

public abstract class AudioDevice
{
  public abstract Sound getSound(File paramFile);

  public abstract Sound getSound(URL paramURL);

  public abstract Sound getSound(String paramString);

  public abstract void loopMethod(SoundRecord paramSoundRecord);

  public abstract void playMethod(SoundRecord paramSoundRecord);

  public abstract void stopMethod(SoundRecord paramSoundRecord);
}