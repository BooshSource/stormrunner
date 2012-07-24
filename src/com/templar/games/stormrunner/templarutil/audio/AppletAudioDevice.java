package com.templar.games.stormrunner.templarutil.audio;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;

public class AppletAudioDevice extends AudioDevice
{
  Applet applet;

  public AppletAudioDevice(Applet paramApplet)
  {
    this.applet = paramApplet;
  }

  public Sound getSound(File paramFile)
  {
    return null;
  }

  public Sound getSound(URL paramURL) {
    return new AppletSound(this.applet.getAudioClip(paramURL));
  }

  public Sound getSound(String paramString) {
    return new AppletSound(this.applet.getAudioClip(this.applet.getDocumentBase(), paramString));
  }

  public void loopMethod(SoundRecord paramSoundRecord) throws IllegalArgumentException
  {
    if ((paramSoundRecord.getSound() instanceof AppletSound))
    {
      ((AppletSound)paramSoundRecord.getSound()).getAudioClip().loop();

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type AppletSound to an AppletAudioDevice");
  }

  public void playMethod(SoundRecord paramSoundRecord) throws IllegalArgumentException {
    if ((paramSoundRecord.getSound() instanceof AppletSound))
    {
      ((AppletSound)paramSoundRecord.getSound()).getAudioClip().play();

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type AppletSound to an AppletAudioDevice");
  }

  public void stopMethod(SoundRecord paramSoundRecord) throws IllegalArgumentException {
    if ((paramSoundRecord.getSound() instanceof AppletSound))
    {
      ((AppletSound)paramSoundRecord.getSound()).getAudioClip().stop();

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type AppletSound to an AppletAudioDevice");
  }
}