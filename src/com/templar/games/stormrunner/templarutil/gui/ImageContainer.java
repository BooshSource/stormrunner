package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.PrintStream;
import java.util.Vector;

public class ImageContainer extends Container
  implements ReportingContainerListener, ReportingComponentListener, ImageListener
{
  private Vector Children = new Vector();
  private Vector ImageContainerChildren = new Vector();
  private ImageContainer ImageContainerParent;
  protected Dimension BufferSize;
  protected Image BufferImage;
  protected Graphics BufferGraphics;
  private boolean Added = false;
  private boolean Frozen = false;
  protected Object FreezeLock = new Object();
  private boolean BufferTainted = true;
  private boolean TaintEntireScreen = true;
  private Rectangle TaintArea;
  private Point CurrentBlit;
  private Object BlitLock = new Object();

  public void setBufferSize(Dimension paramDimension)
  {
    this.BufferSize = paramDimension;

    if (this.BufferImage != null)
      initializeBuffer();
  }

  public void setBlit(int paramInt1, int paramInt2)
  {
    this.CurrentBlit = new Point(paramInt1, paramInt2);
  }

  public Point getBlit()
  {
    return this.CurrentBlit;
  }

  public boolean isBufferTainted()
  {
    return this.BufferTainted;
  }

  public boolean isEntireScreenTainted()
  {
    return this.TaintEntireScreen;
  }

  public Rectangle getTaintArea()
  {
    return this.TaintArea;
  }

  public void translateTaintArea(int paramInt1, int paramInt2)
  {
    this.TaintArea.translate(paramInt1, paramInt2);
  }

  public void register(Component paramComponent)
  {
    register(paramComponent, -1);
  }

  public void register(Component paramComponent, int paramInt)
  {
    if (parentCheck(paramComponent))
    {
      if (paramComponent instanceof ImageContainer)
      {
        localObject = (ImageContainer)paramComponent;
        this.ImageContainerChildren.addElement(paramComponent);
        ((ImageContainer)localObject).setImageContainerParent(this);
        ((ImageContainer)localObject).initializeBuffer();
      }

      attachTo(paramComponent);

      if (paramInt == -1)
        this.Children.addElement(paramComponent);
      else
        this.Children.insertElementAt(paramComponent, paramInt);

      Object localObject = paramComponent.getBounds();
      taintBuffer((Rectangle)localObject);

      if (this.Frozen == false)
        repaint(((Rectangle)localObject).x, ((Rectangle)localObject).y, ((Rectangle)localObject).width, ((Rectangle)localObject).height);
    }
  }

  public void unregister(Component paramComponent)
  {
    detachFrom(paramComponent);
    this.Children.removeElement(paramComponent);

    Rectangle localRectangle = paramComponent.getBounds();
    taintBuffer(localRectangle);

    if (this.Frozen == false)
      repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
  }

  public void unregisterAll()
  {
    for (int i = 0; i < this.Children.size(); ++i)
    {
      Component localComponent = (Component)this.Children.elementAt(i);
      detachFrom(localComponent);
    }

    this.Children.removeAllElements();

    taintBuffer();
  }

  public void setImageContainerParent(ImageContainer paramImageContainer)
  {
    this.ImageContainerParent = paramImageContainer;
  }

  public ImageContainer getImageContainerParent()
  {
    return this.ImageContainerParent;
  }

  public Component[] getChildren()
  {
    Component[] arrayOfComponent = new Component[this.Children.size()];
    for (int i = 0; i < this.Children.size(); ++i)
    {
      arrayOfComponent[i] = ((Component)this.Children.elementAt(i));
    }

    return arrayOfComponent;
  }

  public void setFrozen(boolean paramBoolean)
  {
    synchronized (this.FreezeLock)
    {
      this.Frozen = paramBoolean;

      if ((paramBoolean == false) && (this.BufferTainted))
      {
        if (this.TaintEntireScreen)
          repaint();
        else
          repaint(this.TaintArea.x, this.TaintArea.y, this.TaintArea.width, this.TaintArea.height);
      }
      return;
    }
  }

  public boolean getFrozen()
  {
    synchronized (localObject1)
    {
      return this.Frozen;
    }
  }

  public void taintBuffer()
  {
    this.BufferTainted = true;
    this.TaintEntireScreen = true;

    if (this.ImageContainerParent != null)
    {
      int i = (this.BufferSize == null) ? getSize().width : this.BufferSize.width;
      int j = (this.BufferSize == null) ? getSize().height : this.BufferSize.height;
      Rectangle localRectangle = new Rectangle(getLocation().x, getLocation().y, i, j);
      this.ImageContainerParent.taintBuffer(localRectangle);
    }
  }

  public void taintBuffer(Rectangle paramRectangle)
  {
    this.BufferTainted = true;

    if (!(this.TaintEntireScreen))
    {
      if (this.TaintArea == null)
      {
        this.TaintArea = paramRectangle;
      }
      else
      {
        this.TaintArea.add(paramRectangle);
      }

    }

    if (this.ImageContainerParent != null)
    {
      Rectangle localRectangle = new Rectangle(paramRectangle);
      localRectangle.translate(getLocation().x, getLocation().y);
      this.ImageContainerParent.taintBuffer(localRectangle);
    }
  }

  private boolean parentCheck(Component paramComponent)
  {
    if (paramComponent.getParent() != null)
    {
      System.err.println("ImageContainer: Attempt to register a Component that is already added somewhere else: " + paramComponent);
      return false;
    }

    return true;
  }

  private void attachTo(Component paramComponent)
  {
    if (paramComponent instanceof ImageComponent) {
      ((ImageComponent)paramComponent).addImageListener(this);
    } else if (paramComponent instanceof ImageComposite) {
      ((ImageComposite)paramComponent).addImageListener(this);
    } else if ((paramComponent instanceof Container) && (paramComponent instanceof ReportingContainer))
    {
      Container localContainer = (Container)paramComponent;
      ((ReportingContainer)paramComponent).addReportingContainerListener(this);
      Component[] arrayOfComponent = localContainer.getComponents();
      for (int i = 0; i < arrayOfComponent.length; ++i)
        attachTo(arrayOfComponent[i]);
    }

    if (paramComponent instanceof ReportingComponent)
      ((ReportingComponent)paramComponent).addReportingComponentListener(this);
  }

  private void detachFrom(Component paramComponent)
  {
    if (paramComponent instanceof ImageComponent) {
      ((ImageComponent)paramComponent).removeImageListener(this);
    } else if (paramComponent instanceof ImageComposite) {
      ((ImageComposite)paramComponent).removeImageListener(this);
    } else if ((paramComponent instanceof Container) && (paramComponent instanceof ReportingContainer))
    {
      Container localContainer = (Container)paramComponent;
      ((ReportingContainer)paramComponent).removeReportingContainerListener(this);
      Component[] arrayOfComponent = localContainer.getComponents();
      for (int i = 0; i < arrayOfComponent.length; ++i)
        detachFrom(arrayOfComponent[i]);
    }

    if (paramComponent instanceof ReportingComponent)
      ((ReportingComponent)paramComponent).removeReportingComponentListener(this);
  }

  public Rectangle translateToScreen(Component paramComponent)
  {
    return translateToScreen(paramComponent, paramComponent.getBounds());
  }

  public Rectangle translateToScreen(Component paramComponent, Rectangle paramRectangle)
  {
    return translateToScreen(paramComponent, paramRectangle, null);
  }

  public Rectangle translateToScreen(Component paramComponent, Rectangle paramRectangle, Container paramContainer)
  {
    Rectangle localRectangle = new Rectangle(paramRectangle);
    Container localContainer = (paramContainer == null) ? paramComponent.getParent() : paramContainer;

    while (localContainer != null)
    {
      localRectangle.translate(localContainer.getLocation().x, localContainer.getLocation().y);
      localContainer = localContainer.getParent();
    }

    return localRectangle;
  }

  public void componentAdded(Container paramContainer, Component paramComponent)
  {
    attachTo(paramComponent);
    Rectangle localRectangle = translateToScreen(paramComponent);

    processChange(localRectangle);
  }

  public void componentRemoved(Container paramContainer, Component paramComponent)
  {
    detachFrom(paramComponent);
    Rectangle localRectangle = translateToScreen(paramComponent);

    processChange(localRectangle);
  }

  public void allComponentsRemoved(Container paramContainer)
  {
    Component[] arrayOfComponent = paramContainer.getComponents();
    for (int i = 0; i < arrayOfComponent.length; ++i)
    {
      componentRemoved(paramContainer, arrayOfComponent[i]);
    }
  }

  public void componentReshaped(Component paramComponent, Rectangle paramRectangle1, Rectangle paramRectangle2)
  {
    Rectangle localRectangle1 = translateToScreen(paramComponent, paramRectangle1);
    Rectangle localRectangle2 = translateToScreen(paramComponent, paramRectangle2);
    localRectangle1.add(localRectangle2);

    processChange(localRectangle1);
  }

  public void imageChanged(ImageEvent paramImageEvent)
  {
    Rectangle localRectangle = translateToScreen(paramImageEvent.getSource());

    processChange(localRectangle);
  }

  protected void processChange(Rectangle paramRectangle)
  {
    Rectangle localRectangle1 = getBounds();
    if (this.BufferSize != null)
      localRectangle1.setSize(this.BufferSize);

    if (paramRectangle.intersects(localRectangle1))
    {
      paramRectangle = paramRectangle.intersection(localRectangle1);

      taintBuffer(paramRectangle);

      if (this.Frozen == false)
      {
        if (this.ImageContainerParent == null)
        {
          repaint(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);

          return;
        }

        Rectangle localRectangle2 = new Rectangle(paramRectangle);
        ImageContainer localImageContainer = this;
        while (localImageContainer.getImageContainerParent() != null)
        {
          localRectangle2.translate(localImageContainer.getLocation().x, localImageContainer.getLocation().y);
          localImageContainer = localImageContainer.getImageContainerParent();
        }

        localImageContainer.repaint(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
      }
    }
  }

  public void addNotify()
  {
    super.addNotify();

    this.Added = true;

    if (this.BufferImage == null)
      initializeBuffer();

    for (int i = 0; i < this.ImageContainerChildren.size(); ++i)
    {
      ImageContainer localImageContainer = (ImageContainer)this.ImageContainerChildren.elementAt(i);
      if (localImageContainer.BufferImage == null)
        localImageContainer.initializeBuffer();
    }
  }

  public void removeNotify()
  {
    super.removeNotify();

    this.Added = false;
  }

  public void invalidate()
  {
    super.invalidate();

    if (this.Added)
    {
      initializeBuffer();
      repaint();
    }
  }

  private void initializeBuffer()
  {
    Object localObject;
    if (this.BufferGraphics != null)
      this.BufferGraphics.dispose();

    if (this.BufferImage != null) {
      this.BufferImage.flush();
    }

    if (this.ImageContainerParent == null)
    {
      localObject = this;
    }
    else
    {
      ImageContainer localImageContainer = this.ImageContainerParent;
      while (localImageContainer.getImageContainerParent() != null)
        localImageContainer = localImageContainer.getImageContainerParent();

      localObject = localImageContainer;

      if (!(localImageContainer.Added))
      {
        return;
      }
    }

    if (this.BufferSize == null)
      this.BufferImage = ((Component)localObject).createImage(getSize().width, getSize().height);
    else
      this.BufferImage = ((Component)localObject).createImage(this.BufferSize.width, this.BufferSize.height);

    this.BufferGraphics = this.BufferImage.getGraphics();

    for (int i = 0; i < this.ImageContainerChildren.size(); ++i) {
      ((ImageContainer)this.ImageContainerChildren.elementAt(i)).initializeBuffer();
    }

    taintBuffer();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    synchronized (this.FreezeLock)
    {
      refreshBuffer();

      paramGraphics.drawImage(this.BufferImage, 0, 0, this);

      return;
    }
  }

  public void refreshBuffer()
  {
    if ((this.Frozen == false) && (this.BufferTainted))
    {
      Object localObject;
      if (this.CurrentBlit != null)
      {
        localObject = (this.BufferSize == null) ? getSize() : this.BufferSize;
        localRectangle1 = this.BufferGraphics.getClipBounds();
        this.BufferGraphics.setClip(0, 0, ((Dimension)localObject).width, ((Dimension)localObject).height);
        this.BufferGraphics.copyArea(0, 0, ((Dimension)localObject).width, ((Dimension)localObject).height, this.CurrentBlit.x, this.CurrentBlit.y);
        this.BufferGraphics.setClip(localRectangle1);

        this.CurrentBlit = null;
      }

      Rectangle localRectangle1 = new Rectangle(0, 0, this.BufferSize.width, this.BufferSize.height);
      if (this.TaintEntireScreen) {
        localObject = localRectangle1;
      }
      else
      {
        localObject = this.TaintArea.intersection(localRectangle1);
      }

      this.BufferTainted = false;
      this.TaintEntireScreen = false;
      this.TaintArea = null;

      this.BufferGraphics.setClip((Shape)localObject);

      for (int i = this.Children.size() - 1; i >= 0; --i)
      {
        Component localComponent = (Component)this.Children.elementAt(i);
        if (localComponent instanceof ImageContainer)
        {
          ImageContainer localImageContainer = (ImageContainer)localComponent;
          localImageContainer.refreshBuffer();
          this.BufferGraphics.drawImage(localImageContainer.BufferImage, localComponent.getLocation().x, localComponent.getLocation().y, null);
        } else {
          Rectangle localRectangle2;
          if (localComponent instanceof Container)
          {
            Component[] arrayOfComponent = ((Container)this.Children.elementAt(i)).getComponents();

            for (int j = 0; j < arrayOfComponent.length; ++j)
            {
              localComponent = arrayOfComponent[j];

              localRectangle2 = translateToScreen(localComponent);
              if (localRectangle2.intersects((Rectangle)localObject))
              {
                drawComponent(this.BufferGraphics, localComponent, localRectangle2);
              }
            }
          }
          else
          {
            localRectangle2 = translateToScreen(localComponent);
            if (localRectangle2.intersects((Rectangle)localObject))
            {
              drawComponent(this.BufferGraphics, localComponent, localRectangle2);
            }
          }
        }
      }
    }
  }

  private void drawComponent(Graphics paramGraphics, Component paramComponent, Rectangle paramRectangle) {
    if (paramComponent.isVisible())
    {
      ImageComponent localImageComponent;
      if (paramComponent instanceof ImageComponent)
      {
        localImageComponent = (ImageComponent)paramComponent;
        if (localImageComponent.getImage() == null) { return;
        }

        localImageComponent.deliverImagePaintedEvent();

        paramGraphics.drawImage(localImageComponent.getImage(), paramRectangle.x, paramRectangle.y, localImageComponent);

        return;
      }

      if (paramComponent instanceof ImageComposite)
      {
        ImageComposite localImageComposite = (ImageComposite)paramComponent;
        ImageComponent[] arrayOfImageComponent = localImageComposite.getImageComponents();
        if ((arrayOfImageComponent == null) || (arrayOfImageComponent.length <= 0)) { return;
        }

        for (int i = arrayOfImageComponent.length - 1; i >= 0; --i)
        {
          localImageComponent = arrayOfImageComponent[i];

          if (localImageComponent.getImage() != null)
          {
            localImageComponent.deliverImagePaintedEvent();

            paramGraphics.drawImage(localImageComponent.getImage(), paramRectangle.x, paramRectangle.y, localImageComponent);
          }
        }
        return;
      }

      paramComponent.paint(paramGraphics);
    }
  }
}