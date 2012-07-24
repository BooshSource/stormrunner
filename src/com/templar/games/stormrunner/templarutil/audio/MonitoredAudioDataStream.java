package com.templar.games.stormrunner.templarutil.audio;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Vector;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;

public class MonitoredAudioDataStream extends AudioDataStream
{
  Vector listeners = new Vector();

  public MonitoredAudioDataStream(AudioData paramAudioData) {
    super(paramAudioData);
  }

  public void addListener(SunSound paramSunSound) {
    this.listeners.addElement(paramSunSound);
  }

  public synchronized int read() {
    int i = super.read();
    if (i == -1)
    {
      Enumeration localEnumeration = this.listeners.elements();
      while (localEnumeration.hasMoreElements())
      {
        ((SunSound)localEnumeration.nextElement()).notifyStopped(this);
      }
    }
    return i;
  }

  public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = super.read(paramArrayOfByte, paramInt1, paramInt2);

    if (i <= 0)
    {
      Enumeration localEnumeration = this.listeners.elements();
      while (localEnumeration.hasMoreElements()) {
        ((SunSound)localEnumeration.nextElement()).notifyStopped(this);
      }
    }
    return i;
  }
}