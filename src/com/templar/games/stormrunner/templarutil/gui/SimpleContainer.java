package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.peer.LightweightPeer;
import java.util.Vector;

public class SimpleContainer extends Container
  implements ReportingComponent, ReportingContainer
{
  private Vector ReportingComponentListeners;
  private Vector ReportingContainerListeners;

  public void addReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners == null) {
      this.ReportingComponentListeners = new Vector();
    }
    this.ReportingComponentListeners.addElement(paramReportingComponentListener);
  }

  public void removeReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners != null)
      this.ReportingComponentListeners.removeElement(paramReportingComponentListener);
  }

  protected void reportReshape(Rectangle paramRectangle1, Rectangle paramRectangle2)
  {
    for (int i = 0; i < this.ReportingComponentListeners.size(); i++)
    {
      ReportingComponentListener localReportingComponentListener = (ReportingComponentListener)this.ReportingComponentListeners.elementAt(i);
      localReportingComponentListener.componentReshaped(this, new Rectangle(paramRectangle1), new Rectangle(paramRectangle2));
    }
  }

  public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.ReportingComponentListeners != null)
    {
      Rectangle localRectangle1 = getBounds();
      Rectangle localRectangle2 = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
      reportReshape(localRectangle1, localRectangle2);
    }

    super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void addReportingContainerListener(ReportingContainerListener paramReportingContainerListener)
  {
    if (this.ReportingContainerListeners == null) {
      this.ReportingContainerListeners = new Vector();
    }
    this.ReportingContainerListeners.addElement(paramReportingContainerListener);
  }

  public void removeReportingContainerListener(ReportingContainerListener paramReportingContainerListener)
  {
    if (this.ReportingContainerListeners != null)
      this.ReportingContainerListeners.removeElement(paramReportingContainerListener);
  }

  public void addImpl(Component paramComponent, Object paramObject, int paramInt)
  {
    if (this.ReportingContainerListeners != null) {
      reportAdd(paramComponent);
    }
    super.addImpl(paramComponent, paramObject, paramInt);
  }

  public void remove(int paramInt)
  {
    if (this.ReportingContainerListeners != null) {
      reportRemove(getComponent(paramInt));
    }
    super.remove(paramInt);
  }

  public void removeAll()
  {
    if (this.ReportingContainerListeners != null) {
      reportRemoveAll();
    }
    super.removeAll();
  }

  protected void reportAdd(Component paramComponent)
  {
    for (int i = 0; i < this.ReportingContainerListeners.size(); i++)
    {
      ReportingContainerListener localReportingContainerListener = (ReportingContainerListener)this.ReportingContainerListeners.elementAt(i);
      localReportingContainerListener.componentAdded(this, paramComponent);
    }
  }

  protected void reportRemove(Component paramComponent)
  {
    for (int i = 0; i < this.ReportingContainerListeners.size(); i++)
    {
      ReportingContainerListener localReportingContainerListener = (ReportingContainerListener)this.ReportingContainerListeners.elementAt(i);
      localReportingContainerListener.componentRemoved(this, paramComponent);
    }
  }

  protected void reportRemoveAll()
  {
    for (int i = 0; i < this.ReportingContainerListeners.size(); i++)
    {
      ReportingContainerListener localReportingContainerListener = (ReportingContainerListener)this.ReportingContainerListeners.elementAt(i);
      localReportingContainerListener.allComponentsRemoved(this);
    }
  }

  public void paint(Graphics paramGraphics)
  {
    if (isShowing())
    {
      int i = getComponentCount();
      Component[] arrayOfComponent = getComponents();
      Rectangle localRectangle1 = paramGraphics.getClipBounds();
      for (int j = i - 1; j >= 0; j--)
      {
        Component localComponent = arrayOfComponent[j];
        if ((localComponent == null) || (!(localComponent.getPeer() instanceof LightweightPeer)) || (localComponent.isVisible() != true))
          continue;
        Rectangle localRectangle2 = localComponent.getBounds();
        if ((localRectangle1 != null) && (!localRectangle2.intersects(localRectangle1)))
          continue;
        Graphics localGraphics = paramGraphics.create(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);

        Rectangle localRectangle3 = localRectangle1.intersection(localRectangle2);
        localRectangle3.translate(-localRectangle2.x, -localRectangle2.y);

        localGraphics.setClip(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);

        localGraphics.setFont(localComponent.getFont());
        try
        {
          localComponent.paint(localGraphics);
        } finally {
          localGraphics.dispose();
        }
      }
    }
  }
}