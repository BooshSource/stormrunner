package com.templar.games.stormrunner.templarutil.util;

import java.awt.Image;
import java.net.URL;

public interface ImageRetriever
{
  public abstract Image getImage(URL paramURL, String paramString);

  public abstract Image getImage(String paramString);

  public abstract void hitCache(Image paramImage);
}