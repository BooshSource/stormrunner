package com.templar.games.stormrunner;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class ClickDelegator extends Component
{
  protected Renderer CurrentRenderer;
  protected Scene CurrentScene;
  protected MouseHandler mh;

  public ClickDelegator(Renderer paramRenderer, Scene paramScene)
  {
    this.mh = new MouseHandler();
    addMouseListener(this.mh);

    this.CurrentRenderer = paramRenderer;
    this.CurrentScene = paramScene;
  }
  public void update(Graphics paramGraphics) {
  }
  public void paint(Graphics paramGraphics) {
  }
  protected class MouseHandler extends MouseAdapter {
    public void mousePressed(MouseEvent paramMouseEvent) {
      Point localPoint1 = ClickDelegator.this.CurrentRenderer.getOffset();
      Point localPoint2 = new Point(paramMouseEvent.getX() / 50 + localPoint1.x, paramMouseEvent.getY() / 50 + localPoint1.y);

      if (ClickDelegator.this.CurrentScene.getShroud().isVisible(localPoint2))
      {
        Vector localVector = ClickDelegator.this.CurrentScene.getObjectAt(localPoint2);

        if (localVector != null)
        {
          for (int i = 0; i < localVector.size(); i++)
          {
            ((PhysicalObject)localVector.elementAt(i)).handleMouseClick(paramMouseEvent);
          }

        }

      }

      ClickDelegator.this.CurrentRenderer.requestFocus();
    }

    protected MouseHandler()
    {
    }
  }
}