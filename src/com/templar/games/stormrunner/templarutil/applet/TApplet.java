package com.templar.games.stormrunner.templarutil.applet;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

public class TApplet extends Applet
{
  public Image getImage(URL paramURL, String paramString)
  {
    InputStream localInputStream = getClass().getResourceAsStream("/" + paramString);

    if (localInputStream == null)
    {
      return super.getImage(paramURL, paramString);
    }

    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();

    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream);
    try
    {
      byte[] arrayOfByte = new byte[4096];
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte)) > 0)
      {
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
      }

      Image localImage = getToolkit().createImage(localByteArrayOutputStream.toByteArray());

      return localImage;
    }
    catch (IOException localIOException)
    {
      System.err.println("getImage(): Failed attempt to acquire Image Resource:");
      localIOException.printStackTrace();
    }
    return null;
  }
}