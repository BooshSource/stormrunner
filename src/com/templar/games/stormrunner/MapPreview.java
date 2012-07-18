package com.templar.games.stormrunner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Hashtable;

public class MapPreview extends Component
{
  Scene mapref;
  Image dblbuffer;
  Dimension screenSize;
  Dimension mySize;
  Hashtable cache;
  Point scrollpos;

  public MapPreview()
  {
    this.mapref = null;
  }

  public MapPreview(Scene paramScene, Dimension paramDimension)
  {
    this.mapref = paramScene;
    this.screenSize = paramDimension;
    this.cache = new Hashtable(); }

  public Dimension getMinimumSize() {
    return this.mySize; } 
  public Dimension getPreferredSize() { return this.mySize; } 
  public Dimension getMaximumSize() { return this.mySize;
  }

  public void setSize(int paramInt1, int paramInt2) {
    this.mySize = new Dimension(paramInt1, paramInt2);
    super.setSize(paramInt1, paramInt2);
  }

  public void setSize(Dimension paramDimension) {
    setSize(paramDimension.width, paramDimension.height);
  }

  public void nullCheck()
  {
    if (this.dblbuffer == null)
    {
      this.dblbuffer = super.createImage(super.getSize().width, super.getSize().height);
    }
  }

  public void addNotify()
  {
    super.addNotify();
    nullCheck();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    nullCheck();
    Graphics localGraphics = this.dblbuffer.getGraphics();
    int i = super.getSize().width / 50;
    int j = super.getSize().height / 50;

    Map localMap = this.mapref.getMap();

    for (int k = 0; k < localMap.getSize().width; ++k)
    {
      for (int l = 0; l < localMap.getSize().height; ++l)
      {
        if (this.mapref.getObjectAt(k, l) != null)
        {
          localGraphics.setColor(new Color(255, 255, 0));
          localGraphics.fillRect(k * i, l * j, i, j);
        }
        else
        {
          Object localObject = localMap.getCell(k, l).getAppearance();
          if (this.cache.containsKey(localObject)) {
            localObject = (Image)this.cache.get(localObject);
          }
          else {
            Image localImage = ((Image)localObject).getScaledInstance(i, j, 2);
            this.cache.put(localObject, localImage);
            localObject = localImage;
          }
          localGraphics.drawImage((Image)localObject, k * i, l * j, null);
        }
      }
    }
    localGraphics.setColor(new Color(255, 255, 255));

    localGraphics.drawRect(this.scrollpos.x * i, this.scrollpos.y * j, 
      this.screenSize.width / super.getSize().width, 
      this.screenSize.height / super.getSize().height);
    paramGraphics.drawImage(this.dblbuffer, 0, 0, null);
    localGraphics.dispose();
  }

  public void setScrollPosition(int paramInt1, int paramInt2)
  {
    setScrollPosition(new Point(paramInt1, paramInt2));
  }

  public void setScrollPosition(Point paramPoint)
  {
    this.scrollpos = paramPoint;
    super.repaint();
  }

  public void setScrollPosition(Position paramPosition)
  {
    setScrollPosition(paramPosition.getMapPoint());
  }
}