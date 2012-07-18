package com.templar.games.stormrunner;

import java.awt.Rectangle;

public class ShroudEvent
{
  protected Shroud AffectedShroud;
  protected Rectangle SetArea;
  protected boolean SetVisible;
  protected Rectangle AffectedArea;

  public ShroudEvent(Shroud paramShroud, Rectangle paramRectangle1, boolean paramBoolean, Rectangle paramRectangle2)
  {
    this.AffectedShroud = paramShroud;
    this.SetArea = paramRectangle1;
    this.SetVisible = paramBoolean;
    this.AffectedArea = paramRectangle2;
  }

  public Shroud getSource()
  {
    return this.AffectedShroud;
  }

  public Rectangle getSetArea()
  {
    return this.SetArea;
  }

  public boolean getSetVisible()
  {
    return this.SetVisible;
  }

  public Rectangle getAffectedArea()
  {
    return this.AffectedArea;
  }
}