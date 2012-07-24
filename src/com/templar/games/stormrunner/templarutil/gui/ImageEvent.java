package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;

public class ImageEvent
{
  public static final int ANIMATED_GIF_FRAME = 1;
  public static final int SETIMAGE = 2;
  public static final int SETIMAGES = 3;
  public static final int TEXTCONTAINER_CHANGED = 4;
  public static final int DIGITALGAUGE_CHANGED = 5;
  public static final int SCROLLMENU_CHANGED = 6;
  public static final int VISIBILITY_CHANGED = 7;
  protected Component Source;
  protected int Type;

  public ImageEvent(Component paramComponent, int paramInt)
  {
    this.Source = paramComponent;
    this.Type = paramInt;
  }
  public Component getSource() {
    return this.Source; } 
  public int getType() { return this.Type; } 
  public void setSource(Component paramComponent) {
    this.Source = paramComponent;
  }
}