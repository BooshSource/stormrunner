package com.templar.games.stormrunner.templarutil.audio;

import java.applet.AudioClip;

public class AppletSound
  implements Sound
{
  AudioClip clip;

  public AppletSound(AudioClip paramAudioClip)
  {
    this.clip = paramAudioClip;
  }
  AudioClip getAudioClip() { return this.clip; }

  public boolean load()
  {
    this.clip.play();
    this.clip.stop();
    return true;
  }

  public Sound copy() {
    return new AppletSound(this.clip);
  }
}