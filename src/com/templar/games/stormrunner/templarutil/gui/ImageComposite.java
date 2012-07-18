package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

public class ImageComposite extends SimpleContainer
  implements ImageListener, ReportingComponent
{
  private Vector ErroredImages;
  private Image[] Images;
  private Vector ImageComponents;
  private Dimension FinalSize = new Dimension(0, 0);
  private boolean Stopped = false;
  private Vector ImageListeners = new Vector();
  private Vector ReportingComponentListeners;

  public ImageComposite()
  {
    initialize();
  }

  public ImageComposite(Image[] paramArrayOfImage)
  {
    initialize();

    setImages(paramArrayOfImage);
  }

  public ImageComposite(OrderedTable paramOrderedTable)
  {
    initialize();

    setImages(paramOrderedTable);
  }

  public ImageComposite(ImageComponent[] paramArrayOfImageComponent)
  {
    initialize();

    setImages(paramArrayOfImageComponent);
  }

  private void initialize()
  {
    this.Images = null;
    this.ImageComponents = new Vector();
    setLayout(null);
  }

  public void setImages(ImageComponent[] paramArrayOfImageComponent)
  {
    deliverImageEvent(3);

    this.Images = null;

    if (paramArrayOfImageComponent != null)
    {
      this.FinalSize = null;

      this.ErroredImages = new Vector();
      i = this.ImageComponents.size();

      for (int j = paramArrayOfImageComponent.length; j < i; ++j)
      {
        ImageComponent localImageComponent2 = (ImageComponent)this.ImageComponents.elementAt(j);
        remove(localImageComponent2);
        localImageComponent2.removeImageListener(this);
      }

      for (int k = 0; k < paramArrayOfImageComponent.length; ++k)
      {
        if (k < this.ImageComponents.size())
        {
          this.ImageComponents.setElementAt(paramArrayOfImageComponent[k], k);
          l = getComponentCount();
          add(paramArrayOfImageComponent[k], k);
          paramArrayOfImageComponent[k].addImageListener(this);

          if ((l < getComponentCount()) && (l != 0))
          {
            super.remove(k + 1);
          }

        }
        else
        {
          this.ImageComponents.addElement(paramArrayOfImageComponent[k]);
          add(paramArrayOfImageComponent[k]);
          paramArrayOfImageComponent[k].addImageListener(this);
        }
      }

      int l = 0;
      int i1 = 0;

      Enumeration localEnumeration = this.ImageComponents.elements();

      while (localEnumeration.hasMoreElements())
      {
        Dimension localDimension = ((ImageComponent)localEnumeration.nextElement()).getSize();
        l = Math.max(localDimension.width, l);
        i1 = Math.max(localDimension.height, i1);
      }

      this.FinalSize = new Dimension(l, i1);

      return;
    }

    this.FinalSize = new Dimension(0, 0);
    this.ErroredImages = new Vector();
    for (int i = 0; i < this.ImageComponents.size(); ++i)
    {
      ImageComponent localImageComponent1 = (ImageComponent)this.ImageComponents.elementAt(i);
      remove(localImageComponent1);
      localImageComponent1.removeImageListener(this);
    }
  }

  public void setImages(Image[] paramArrayOfImage)
  {
    ImageComponent localImageComponent1;
    deliverImageEvent(3);

    this.Images = paramArrayOfImage;

    if (paramArrayOfImage != null)
    {
      this.FinalSize = null;

      this.ErroredImages = new Vector();
      i = this.ImageComponents.size();
      localImageComponent1 = null;
      for (int j = 0; j < paramArrayOfImage.length; ++j)
      {
        if (j < i)
        {
          ImageComponent localImageComponent2 = (ImageComponent)this.ImageComponents.elementAt(j);
          if (localImageComponent2 instanceof AnimationComponent)
          {
            localImageComponent1 = new ImageComponent(paramArrayOfImage[j], true, false);
            this.ImageComponents.setElementAt(localImageComponent1, j);
            add(localImageComponent1);
            super.remove(j);

            localImageComponent1.addImageListener(this);
          }
          else {
            localImageComponent2.setImage(paramArrayOfImage[j]);
          }
        }
        else
        {
          localImageComponent1 = new ImageComponent(paramArrayOfImage[j], true, false);
          this.ImageComponents.addElement(localImageComponent1);
          add(localImageComponent1);
          localImageComponent1.addImageListener(this);
        }

      }

      i = this.ImageComponents.size();
      if (paramArrayOfImage.length < i)
      {
        for (k = paramArrayOfImage.length; k < i; ++k)
        {
          ImageComponent localImageComponent3 = (ImageComponent)this.ImageComponents.elementAt(k);
          remove(localImageComponent3);
          localImageComponent3.removeImageListener(this);
        }

      }

      int k = 0;
      int l = 0;

      Enumeration localEnumeration = this.ImageComponents.elements();

      while (localEnumeration.hasMoreElements())
      {
        Dimension localDimension = ((ImageComponent)localEnumeration.nextElement()).getSize();
        k = Math.max(localDimension.width, k);
        l = Math.max(localDimension.height, l);
      }

      this.FinalSize = new Dimension(k, l);

      return;
    }

    this.FinalSize = new Dimension(0, 0);
    this.ErroredImages = new Vector();
    for (int i = 0; i < this.ImageComponents.size(); ++i)
    {
      localImageComponent1 = (ImageComponent)this.ImageComponents.elementAt(i);
      remove(localImageComponent1);
      localImageComponent1.removeImageListener(this);
    }
  }

  public OrderedTable setImages(OrderedTable paramOrderedTable)
  {
    int i;
    Object localObject;
    deliverImageEvent(3);

    OrderedTable localOrderedTable = null;

    if (paramOrderedTable != null)
    {
      localOrderedTable = new OrderedTable();

      this.FinalSize = null;

      this.ErroredImages = new Vector();

      i = this.ImageComponents.size();
      localObject = null;

      Enumeration localEnumeration1 = paramOrderedTable.keys();
      for (int j = 0; j < paramOrderedTable.size(); ++j)
      {
        String str = (String)localEnumeration1.nextElement();
        Image[] arrayOfImage = (Image[])paramOrderedTable.get(str);
        if (j < i)
        {
          if (!(this.ImageComponents.elementAt(j) instanceof AnimationComponent))
          {
            remove((Component)this.ImageComponents.elementAt(j));
            localObject = new AnimationComponent(arrayOfImage);
            this.ImageComponents.setElementAt(localObject, j);
            add((Component)localObject, j);
            ((ImageComponent)localObject).addImageListener(this);
          }
          else
          {
            localObject = (ImageComponent)this.ImageComponents.elementAt(j);
            ((AnimationComponent)localObject).setCells(arrayOfImage);
            ((AnimationComponent)localObject).reset();
          }

        }
        else
        {
          localObject = new AnimationComponent(arrayOfImage);
          this.ImageComponents.addElement(localObject);
          add((Component)localObject);
          ((ImageComponent)localObject).addImageListener(this);
        }

        localOrderedTable.put(str, localObject);
      }

      i = this.ImageComponents.size();
      if (paramOrderedTable.size() < i)
      {
        for (k = paramOrderedTable.size(); k < i; ++k)
        {
          ImageComponent localImageComponent = (ImageComponent)this.ImageComponents.elementAt(k);
          this.ImageComponents.removeElement(localImageComponent);
          remove(localImageComponent);
          localImageComponent.removeImageListener(this);
        }

      }

      int k = 0;
      int l = 0;

      Enumeration localEnumeration2 = this.ImageComponents.elements();

      while (localEnumeration2.hasMoreElements())
      {
        Dimension localDimension = ((ImageComponent)localEnumeration2.nextElement()).getSize();
        k = Math.max(localDimension.width, k);
        l = Math.max(localDimension.height, l);
      }

      this.FinalSize = new Dimension(k, l);
    }
    else
    {
      this.FinalSize = new Dimension(0, 0);
      this.ErroredImages = new Vector();
      for (i = 0; i < this.ImageComponents.size(); ++i)
      {
        localObject = (ImageComponent)this.ImageComponents.elementAt(i);
        remove((Component)localObject);
        this.ImageComponents.removeElement(localObject);
        ((ImageComponent)localObject).removeImageListener(this);
      }
    }
    return ((OrderedTable)localOrderedTable);
  }

  public Image[] getImages()
  {
    return this.Images;
  }

  public ImageComponent[] getImageComponents()
  {
    ImageComponent[] arrayOfImageComponent = new ImageComponent[this.ImageComponents.size()];
    this.ImageComponents.copyInto(arrayOfImageComponent);
    return arrayOfImageComponent;
  }

  public void setVisible(boolean paramBoolean)
  {
    if (isVisible() != paramBoolean)
    {
      deliverImageEvent(7);

      setVisible(paramBoolean);
    }
  }

  public synchronized void stopDrawing()
  {
    this.Stopped = true;
  }

  public synchronized void startDrawing()
  {
    this.Stopped = false;
  }

  public boolean isDrawing()
  {
    return (!(this.Stopped));
  }

  public Dimension getSize()
  {
    return getMinimumSize();
  }

  public Dimension getMaximumSize() {
    return getMinimumSize();
  }

  public Dimension getPreferredSize() {
    return getMinimumSize();
  }

  public synchronized Dimension getMinimumSize()
  {
    return this.FinalSize;
  }

  public synchronized void addImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.addElement(paramImageListener);
  }

  public synchronized void removeImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.removeElement(paramImageListener);
  }

  public void imageChanged(ImageEvent paramImageEvent)
  {
    paramImageEvent.setSource(this);

    deliverImageEvent(paramImageEvent);
  }

  public synchronized void deliverImageEvent(int paramInt)
  {
    deliverImageEvent(new ImageEvent(this, paramInt));
  }

  public synchronized void deliverImageEvent(ImageEvent paramImageEvent)
  {
    if (this.ImageListeners.size() > 0)
      for (int i = 0; i < this.ImageListeners.size(); ++i)
      {
        ImageListener localImageListener = (ReportingComponent)this.ImageListeners.elementAt(i);
        localImageListener.imageChanged(paramImageEvent);
      }
  }

  public synchronized void addReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners == null)
      this.ReportingComponentListeners = new Vector();

    this.ReportingComponentListeners.addElement(paramReportingComponentListener);
  }

  public synchronized void removeReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners != null)
      this.ReportingComponentListeners.removeElement(paramReportingComponentListener);
  }

  protected synchronized void reportReshape(Rectangle paramRectangle1, Rectangle paramRectangle2)
  {
    for (int i = 0; i < this.ReportingComponentListeners.size(); ++i)
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
}