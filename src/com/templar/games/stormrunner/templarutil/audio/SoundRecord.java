package com.templar.games.stormrunner.templarutil.audio;

public class SoundRecord
{
  String Name;
  Sound Snd;
  AudioManager Manager;
  SoundListener Listener;
  boolean Looping;

  public SoundRecord(String paramString, Sound paramSound, AudioManager paramAudioManager, SoundListener paramSoundListener, boolean paramBoolean)
  {
    this.Name = paramString;
    this.Snd = paramSound;
    this.Manager = paramAudioManager;
    this.Listener = paramSoundListener;
    this.Looping = paramBoolean;
  }

  public void notify(String paramString, int paramInt) {
    if (this.Listener != null)
      this.Listener.soundStopped(paramString, paramInt);  }

  public boolean isLooping() {
    return this.Looping; } 
  public String getName() { return this.Name; } 
  public Sound getSound() { return this.Snd; } 
  public AudioManager getAudioManager() { return this.Manager; } 
  public SoundListener getSoundListener() { return this.Listener; }

  public String toString() {
    String str = "SoundRecord: " + this.Name + ",loop=" + this.Looping;
    return str;
  }
}