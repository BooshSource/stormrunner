package com.templar.games.stormrunner;

import java.awt.Dimension;
import java.awt.Point;

public class Map
{
  public static final double VERSION = 0.2D;
  MapCell[][] mapdata;
  Dimension mapsize;

  public Map(Dimension paramDimension)
  {
    this.mapsize = paramDimension;
    this.mapdata = new MapCell[paramDimension.width][paramDimension.height];
    for (int i = 0; i < paramDimension.width; i++)
      for (int j = 0; j < paramDimension.height; j++)
        this.mapdata[i][j] = new MapCell();
  }

  public Dimension getSize()
  {
    return this.mapsize;
  }

  public void setSize(Dimension paramDimension)
  {
    MapCell[][] arrayOfMapCell = new MapCell[paramDimension.width][paramDimension.height];
    for (int i = 0; i < paramDimension.width; i++)
      for (int j = 0; j < paramDimension.height; j++)
        if ((i >= this.mapsize.width) || (j >= this.mapsize.height))
          arrayOfMapCell[i][j] = new MapCell();
        else
          arrayOfMapCell[i][j] = this.mapdata[i][j];
    this.mapdata = arrayOfMapCell;
    this.mapsize = paramDimension;
  }

  public void setCell(Point paramPoint, MapCell paramMapCell)
  {
    setCell(paramPoint.x, paramPoint.y, paramMapCell);
  }

  public void setCell(int paramInt1, int paramInt2, MapCell paramMapCell)
  {
    try
    {
      this.mapdata[paramInt1][paramInt2] = paramMapCell;

      return;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
  }

  public MapCell getCell(Point paramPoint)
  {
    return getCell(paramPoint.x, paramPoint.y);
  }

  public MapCell getCell(int paramInt1, int paramInt2)
  {
    try
    {
      return this.mapdata[paramInt1][paramInt2];
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
    }
    return null;
  }
}