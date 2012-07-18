package com.templar.games.stormrunner.templarutil.audio;

import java.io.File;
import java.net.URL;

public class NullAudioDevice extends AudioDevice
{
  public Sound getSound(File paramFile)
  {
    return null;
  }

  public Sound getSound(URL paramURL)
  {
    return null;
  }

  public Sound getSound(String paramString)
  {
    return null;
  }

  public void loopMethod(SoundRecord paramSoundRecord)
    throws IllegalArgumentException
  {
  }

  public void playMethod(SoundRecord paramSoundRecord)
    throws IllegalArgumentException
  {
  }

  public void stopMethod(SoundRecord paramSoundRecord)
    throws IllegalArgumentException
  {
  }
}