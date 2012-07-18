package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;

public class MapCell
{
  Image appearance;
  int impassibility;
  boolean storm;
  String category;
  String filename;

  public MapCell()
  {
    this.appearance = null;
    this.impassibility = 0;
    this.storm = false;
    this.category = "";
    this.filename = "";
  }

  public MapCell(Image paramImage, int paramInt)
  {
    this.appearance = paramImage;
    this.impassibility = paramInt;
    this.storm = false;
    this.category = "";
    this.filename = "";
  }

  public MapCell(Image paramImage, int paramInt, boolean paramBoolean)
  {
    this(paramImage, paramInt);
    this.storm = paramBoolean;
    this.category = "";
    this.filename = "";
  }

  public MapCell(Image paramImage, int paramInt, boolean paramBoolean, String paramString)
  {
    this(paramImage, paramInt, paramBoolean);
    this.category = paramString;
  }

  public MapCell(ImageRetriever paramImageRetriever, String paramString1, int paramInt, boolean paramBoolean, String paramString2)
  {
    this(paramImageRetriever.getImage(paramString1), paramInt, paramBoolean, paramString2);
    this.filename = paramString1;
  }

  public void setAppearance(Image paramImage)
  {
    this.appearance = paramImage;
  }

  public Image getAppearance()
  {
    return this.appearance;
  }

  public void setImpassibility(int paramInt)
  {
    this.impassibility = paramInt;
  }

  public int getImpassibility()
  {
    return this.impassibility;
  }

  public String getCategory()
  {
    return this.category;
  }

  public void setCategory(String paramString)
  {
    this.category = paramString;
  }

  public boolean getStorm()
  {
    return this.storm;
  }

  public void setStorm(boolean paramBoolean)
  {
    this.storm = paramBoolean;
  }

  public String getFilename()
  {
    return this.filename;
  }

  public void setFilename(String paramString)
  {
    this.filename = paramString;
  }
}