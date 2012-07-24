package com.templar.games.stormrunner.templarutil.audio;

import com.templar.games.stormrunner.templarutil.Debug;
import java.io.PrintStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class AudioManager
{
  AudioDevice device;
  Hashtable audioCache = new Hashtable();
  Vector soundRecords = new Vector();
  boolean muted = false;

  public AudioManager(AudioDevice paramAudioDevice, Hashtable paramHashtable)
  {
    this.device = paramAudioDevice;
    if (paramHashtable != null)
    {
      Enumeration localEnumeration = paramHashtable.keys();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        getSound(str, (String)paramHashtable.get(str));
      }
    }
  }

  public Sound getSound(String paramString, URL paramURL)
  {
    Sound localSound = this.device.getSound(paramURL);
    if (!localSound.load())
      Debug.println("Sound.load() failed on " + localSound);
    else
      this.audioCache.put(paramString, localSound);
    return localSound;
  }

  public Sound getSound(String paramString1, String paramString2) {
    Sound localSound = this.device.getSound(paramString2);
    if (!localSound.load())
      Debug.println("Sound.load() failed on " + localSound);
    else
      this.audioCache.put(paramString1, localSound);
    return localSound;
  }

  public void play(String paramString)
  {
    play(paramString, null);
  }

  public void play(String paramString, SoundListener paramSoundListener)
  {
    if (this.muted)
    {
      if (paramSoundListener != null) {
        paramSoundListener.soundStopped(paramString, 2);
      }
      return;
    }
    Sound localSound = (Sound)this.audioCache.get(paramString);
    if (localSound != null)
    {
      SoundRecord localSoundRecord = new SoundRecord(paramString, localSound, this, paramSoundListener, false);
      this.soundRecords.addElement(localSoundRecord);
      this.device.playMethod(localSoundRecord);

      return;
    }

    System.err.println("AudioManager: play(): No such sound: " + paramString);
  }

  public void loop(String paramString)
  {
    loop(paramString, null);
  }

  public void loop(String paramString, SoundListener paramSoundListener)
  {
    if (this.muted)
    {
      if (paramSoundListener != null)
        paramSoundListener.soundStopped(paramString, 2);
    }
    Sound localSound = (Sound)this.audioCache.get(paramString);
    if (localSound != null)
    {
      SoundRecord localSoundRecord = new SoundRecord(paramString, localSound, this, paramSoundListener, true);
      this.soundRecords.addElement(localSoundRecord);
      if (!this.muted) {
        this.device.loopMethod(localSoundRecord);

        return;
      }

    }
    else
    {
      System.err.println("AudioManager: loop(): No such sound: " + paramString);
    }
  }

  public void stop(String paramString) {
    stop(paramString, null);
  }

  public void stop(String paramString, SoundListener paramSoundListener)
  {
    Enumeration localEnumeration = this.soundRecords.elements();
    SoundRecord localSoundRecord = null;
    while (localEnumeration.hasMoreElements())
    {
      localSoundRecord = (SoundRecord)localEnumeration.nextElement();
      if ((localSoundRecord.getName().compareTo(paramString) == 0) && (localSoundRecord.getSoundListener() == paramSoundListener))
        break;
      localSoundRecord = null;
    }

    if (localSoundRecord != null)
    {
      this.device.stopMethod(localSoundRecord);

      this.soundRecords.removeElement(localSoundRecord);
    }
  }

  public void notifyStopped(SoundRecord paramSoundRecord, boolean paramBoolean)
  {
    if (paramSoundRecord.getSoundListener() != null)
    {
      paramSoundRecord.getSoundListener().soundStopped(paramSoundRecord.getName(), 
        paramBoolean ? 
        0 : this.muted ? 2 : 
        1);
    }
    this.soundRecords.removeElement(paramSoundRecord);
  }

  public void mute()
  {
    if (!this.muted)
    {
      this.muted = true;
      Enumeration localEnumeration = this.soundRecords.elements();
      Vector localVector = new Vector();
      while (localEnumeration.hasMoreElements())
      {
        SoundRecord localSoundRecord = (SoundRecord)localEnumeration.nextElement();
        this.device.stopMethod(localSoundRecord);
        if (!localSoundRecord.isLooping())
          localVector.addElement(localSoundRecord);
      }
      localEnumeration = localVector.elements();
      while (localEnumeration.hasMoreElements())
        this.soundRecords.removeElement(localEnumeration.nextElement());
      localVector.removeAllElements();
    }
  }

  public synchronized void unMute()
  {
    if (this.muted)
    {
      this.muted = false;
      Enumeration localEnumeration = this.soundRecords.elements();
      while (localEnumeration.hasMoreElements())
      {
        SoundRecord localSoundRecord = (SoundRecord)localEnumeration.nextElement();

        this.device.loopMethod(localSoundRecord);
      }
    }
  }

  public boolean isMuted() {
    return this.muted;
  }
}