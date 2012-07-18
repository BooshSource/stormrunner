package com.templar.games.stormrunner.program.editor;

import java.awt.Dimension;
import java.awt.Point;

public class DropTargetInfo
{
  public Dimension TargetComponentSize;
  public Point DropTarget;
  public Dimension DropAreaSize;
  public String TargetPart;
  public int HalfTargetAreaX;
  public int HalfTargetAreaY;
  public int TargetResolveX;
  public int TargetResolveY;

  public DropTargetInfo(Dimension paramDimension1, Point paramPoint, Dimension paramDimension2, String paramString)
  {
    this.TargetComponentSize = paramDimension1;
    this.DropTarget = paramPoint;
    this.DropAreaSize = paramDimension2;
    this.TargetPart = paramString;

    this.TargetResolveX = (paramPoint.x * -1);
    this.TargetResolveY = (paramPoint.y * -1);

    this.HalfTargetAreaX = (int)Math.ceil(paramDimension2.width / 2);
    this.HalfTargetAreaY = (int)Math.ceil(paramDimension2.height / 2);
  }
}