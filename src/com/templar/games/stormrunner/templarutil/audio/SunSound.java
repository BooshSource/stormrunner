package com.templar.games.stormrunner.templarutil.audio;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.ContinuousAudioDataStream;

public class SunSound
  implements Sound
{
  private static final int AUDIO_FILE_MAGIC = 779316836;
  private static final int AUDIO_FILE_ENCODING_MULAW_8 = 1;
  protected InputStream rawdata;
  protected AudioData ad;
  protected boolean Valid = false;

  Hashtable playing = new Hashtable();

  public SunSound(InputStream paramInputStream)
  {
    if (paramInputStream != null)
    {
      this.rawdata = paramInputStream;
      this.Valid = true;
    }
  }

  public void play(SoundRecord paramSoundRecord)
  {
    MonitoredAudioDataStream localMonitoredAudioDataStream = getMonitoredAudioDataStream();
    localMonitoredAudioDataStream.addListener(this);

    this.playing.put(localMonitoredAudioDataStream, paramSoundRecord);

    AudioPlayer.player.start(localMonitoredAudioDataStream);
  }

  public void loop(SoundRecord paramSoundRecord)
  {
    ContinuousAudioDataStream localContinuousAudioDataStream = getContinuousAudioDataStream();

    this.playing.put(localContinuousAudioDataStream, paramSoundRecord);

    AudioPlayer.player.start(localContinuousAudioDataStream);
  }

  public void stop(SoundRecord paramSoundRecord)
  {
    Enumeration localEnumeration = this.playing.keys();
    while (localEnumeration.hasMoreElements())
    {
      AudioDataStream localAudioDataStream = (AudioDataStream)localEnumeration.nextElement();
      SoundRecord localSoundRecord = (SoundRecord)this.playing.get(localAudioDataStream);

      if (localSoundRecord != paramSoundRecord) {
        continue;
      }
      AudioPlayer.player.stop(localAudioDataStream);

      this.playing.remove(localAudioDataStream);

      paramSoundRecord.getAudioManager().notifyStopped(paramSoundRecord, true);

      return;
    }
  }

  public AudioDataStream getAudioDataStream()
  {
    return new AudioDataStream(this.ad);
  }

  public MonitoredAudioDataStream getMonitoredAudioDataStream()
  {
    MonitoredAudioDataStream localMonitoredAudioDataStream = new MonitoredAudioDataStream(this.ad);
    return localMonitoredAudioDataStream;
  }

  public void notifyStopped(MonitoredAudioDataStream paramMonitoredAudioDataStream)
  {
    SoundRecord localSoundRecord = (SoundRecord)this.playing.get(paramMonitoredAudioDataStream);
    localSoundRecord.getAudioManager().notifyStopped(localSoundRecord, false);
  }

  public ContinuousAudioDataStream getContinuousAudioDataStream()
  {
    return new ContinuousAudioDataStream(this.ad);
  }

  public boolean load()
  {
    if (!this.Valid) {
      return false;
    }

    int i2 = 24;
    try
    {
      DataInputStream localDataInputStream = new DataInputStream(this.rawdata);

      int i = localDataInputStream.readInt();
      int j = localDataInputStream.readInt();
      int k = localDataInputStream.readInt();
      int m = localDataInputStream.readInt();
      int n = localDataInputStream.readInt();
      int i1 = localDataInputStream.readInt();

      byte[] arrayOfByte1 = new byte[j - i2];
      localDataInputStream.read(arrayOfByte1);

      if (i != 779316836)
      {
        throw new IOException("Not an audio file");
      }

      if (m != 1)
      {
        throw new IOException("Not correct encoding");
      }

      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();

      byte[] arrayOfByte2 = new byte[4096];
      int i3;
      while ((i3 = localDataInputStream.read(arrayOfByte2)) > 0)
      {
        localByteArrayOutputStream.write(arrayOfByte2, 0, i3);
      }

      this.ad = new AudioData(localByteArrayOutputStream.toByteArray());

      return true;
    }
    catch (IOException localIOException)
    {
      System.err.println("SunSound.load(): Failed attempt to read Audio Resource:");
      localIOException.printStackTrace();
    }
    return false;
  }
}