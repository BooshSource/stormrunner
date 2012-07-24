package com.templar.games.stormrunner.templarutil.audio;

import java.applet.Applet;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SunAudioDevice extends AudioDevice
{
  Applet applet;

  public SunAudioDevice(Applet paramApplet)
  {
    this.applet = paramApplet;
  }

  public Sound getSound(File paramFile)
  {
    return getSound(paramFile.toString());
  }

  public Sound getSound(URL paramURL)
  {
    try
    {
      return new SunSound(new BufferedInputStream(paramURL.openStream()));
    }
    catch (IOException localIOException)
    {
      System.err.println("SunAudioDevice: IOException generated while reading data - " + localIOException);
      localIOException.printStackTrace();
    }return null;
  }

  public Sound getSound(String paramString)
  {
    InputStream localInputStream = getClass().getResourceAsStream("/" + paramString);

    if (localInputStream == null)
    {
      try
      {
        if (this.applet != null)
          return getSound(new URL(this.applet.getDocumentBase(), paramString));
        return null;
      }
      catch (MalformedURLException localMalformedURLException)
      {
        System.err.println("SunAudioDevice: MalformedURLException generated - " + localMalformedURLException);
        localMalformedURLException.printStackTrace();
        return null;
      }

    }

    return new SunSound(new BufferedInputStream(localInputStream));
  }

  public void loopMethod(SoundRecord paramSoundRecord)
    throws IllegalArgumentException
  {
    if ((paramSoundRecord.getSound() instanceof SunSound))
    {
      ((SunSound)paramSoundRecord.getSound()).loop(paramSoundRecord);

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type SunSound to an SunAudioDevice");
  }

  public void playMethod(SoundRecord paramSoundRecord) throws IllegalArgumentException
  {
    if ((paramSoundRecord.getSound() instanceof SunSound))
    {
      ((SunSound)paramSoundRecord.getSound()).play(paramSoundRecord);

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type SunSound to an SunAudioDevice");
  }

  public void stopMethod(SoundRecord paramSoundRecord) throws IllegalArgumentException
  {
    if ((paramSoundRecord.getSound() instanceof SunSound))
    {
      ((SunSound)paramSoundRecord.getSound()).stop(paramSoundRecord);

      return;
    }

    throw new IllegalArgumentException("Must pass a Sound of type SunSound to an SunAudioDevice");
  }
}