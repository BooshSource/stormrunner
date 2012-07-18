package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.PrintStream;
import java.util.Vector;

public class ImageComponent extends Component
  implements ReportingComponent
{
  protected boolean SelfSizing;
  protected boolean ProgressiveDraw;
  protected ImageRetriever ir;
  protected String ImageFilename;
  public boolean ImageError;
  protected Image PreviousImage;
  protected Dimension PreviousImageSize;
  protected boolean PreviousImageAnimated;
  protected boolean PreviousImageFlushed;
  protected Image CurrentImage;
  protected Dimension CurrentImageSize;
  protected boolean CurrentImageAnimated;
  protected boolean CurrentImageReady;
  private boolean Stopped;
  private boolean Added;
  private boolean DisplayCompleted;
  private boolean WaitingOnSize;
  private Vector ImageListeners;
  private static Vector ImagePaintListeners = new Vector();
  private Vector ReportingComponentListeners;
  private static ImageFilenameProvider ImageNamer = null;

  public ImageComponent()
  {
    this.SelfSizing = false;
    this.ProgressiveDraw = false;

    this.ImageError = false;

    this.PreviousImageAnimated = false;
    this.PreviousImageFlushed = true;

    this.CurrentImageAnimated = false;
    this.CurrentImageReady = false;

    this.Stopped = false;
    this.Added = false;

    this.DisplayCompleted = false;

    this.WaitingOnSize = false;

    this.ImageListeners = new Vector();

    setImage(null);
  }

  public ImageComponent(Image paramImage)
  {
    this.SelfSizing = false;
    this.ProgressiveDraw = false;

    this.ImageError = false;

    this.PreviousImageAnimated = false;
    this.PreviousImageFlushed = true;

    this.CurrentImageAnimated = false;
    this.CurrentImageReady = false;

    this.Stopped = false;
    this.Added = false;

    this.DisplayCompleted = false;

    this.WaitingOnSize = false;

    this.ImageListeners = new Vector();

    setImage(paramImage);
  }

  public ImageComponent(ImageRetriever paramImageRetriever, String paramString)
  {
    this(paramImageRetriever.getImage(paramString));

    this.ir = paramImageRetriever;
    this.ImageFilename = paramString;
  }

  public ImageComponent(Image paramImage, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.SelfSizing = false;
    this.ProgressiveDraw = false;

    this.ImageError = false;

    this.PreviousImageAnimated = false;
    this.PreviousImageFlushed = true;

    this.CurrentImageAnimated = false;
    this.CurrentImageReady = false;

    this.Stopped = false;
    this.Added = false;

    this.DisplayCompleted = false;

    this.WaitingOnSize = false;

    this.ImageListeners = new Vector();

    this.SelfSizing = paramBoolean1;
    this.ProgressiveDraw = paramBoolean2;
    setImage(paramImage);
  }

  public ImageComponent(ImageRetriever paramImageRetriever, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramImageRetriever.getImage(paramString), paramBoolean1, paramBoolean2);

    this.ir = paramImageRetriever;
    this.ImageFilename = paramString;
  }

  public synchronized void setImage(Image paramImage)
  {
    if (this.CurrentImage != paramImage)
    {
      this.PreviousImage = this.CurrentImage;
      this.PreviousImageFlushed = false;
      this.PreviousImageAnimated = this.CurrentImageAnimated;
      if (this.CurrentImageSize != null)
        this.PreviousImageSize = this.CurrentImageSize;
      else
        this.PreviousImageSize = new Dimension(0, 0);
      this.CurrentImage = paramImage;

      if (this.CurrentImage != null)
      {
        this.CurrentImageSize = null;
        this.CurrentImageReady = false;
        this.CurrentImageAnimated = false;

        if ((this.Added) && (!(this.Stopped)))
        {
          int i;
          if (ImagePaintListeners.size() > 0)
            for (i = 0; i < ImagePaintListeners.size(); ++i)
            {
              ImagePaintListener localImagePaintListener = (ImagePaintListener)ImagePaintListeners.elementAt(i);
              localImagePaintListener.imagePainted(this, this.CurrentImage);
            }

          if (!(this.ProgressiveDraw))
          {
            if (!(super.prepareImage(this.CurrentImage, this)))
            {
              try
              {
                i = 0;
                int j = super.checkImage(this.CurrentImage, this);
                while ((j & 0xF0) == 0)
                {
                  if (i > 2)
                  {
                    System.out.println("ImageComponent: Potential deadlock waiting for Image.");
                    break label249:
                  }

                  ++i;

                  wait(1000L);

                  j = super.checkImage(this.CurrentImage, this);
                }
              }
              catch (InterruptedException localInterruptedException)
              {
                System.out.println("Someday I'd like to meet the kind of thread that interrupts this sort of thing.");
                localInterruptedException.printStackTrace();
              }

            }

            label249: this.CurrentImageReady = true;
          }
          else
          {
            this.CurrentImageReady = true;
          }
        }
        else {
          this.CurrentImageReady = true;
        }
        if (this.SelfSizing)
        {
          Dimension localDimension = getSize();
          if ((localDimension.height != this.PreviousImageSize.height) || (localDimension.width != this.PreviousImageSize.width))
            super.setSize(localDimension);
          else
            super.repaint();
        }
        else {
          super.repaint();
        }

        deliverImageEvent(2);
      }
    }
  }

  public Image getImage()
  {
    return this.CurrentImage;
  }

  public void setSelfSizing(boolean paramBoolean)
  {
    this.SelfSizing = paramBoolean;
  }

  public void setProgressiveDrawing(boolean paramBoolean)
  {
    this.ProgressiveDraw = paramBoolean;
  }

  public void setVisible(boolean paramBoolean)
  {
    deliverImageEvent(7);

    super.setVisible(paramBoolean);
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

  public boolean isCompleted()
  {
    return this.DisplayCompleted;
  }

  public void addNotify()
  {
    super.addNotify();

    this.Added = true;
  }

  public void removeNotify()
  {
    super.removeNotify();

    this.Added = false;
  }

  public synchronized boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramImage != this.CurrentImage)
    {
      return false;
    }

    if ((paramInt1 & 0xC0) != 0)
    {
      if ((paramInt1 & 0x40) != 0)
      {
        if (ImageNamer != null)
          System.err.println("Image Error! (\"" + ImageNamer.getImageFilename(paramImage) + "\")");
        else if (this.ImageFilename == null)
          System.err.println("Image Error! (Filename unknown)");
        else
          System.err.println("Image Error! (\"" + this.ImageFilename + "\")");
      }

      this.ImageError = true;

      if (this.WaitingOnSize)
        this.WaitingOnSize = false;

      notifyAll();
    }

    if (((paramInt1 & 0x3) != 0) && 
      (this.WaitingOnSize))
    {
      this.WaitingOnSize = false;
      notifyAll();
    }

    int i = ((paramInt1 & 0x20) != 0) ? 1 : 0;
    int j = ((paramInt1 & 0x10) != 0) ? 1 : 0;

    if (j != 0)
    {
      this.CurrentImageAnimated = true;
      deliverImageEvent(1);
    }

    if (!(this.Stopped))
    {
      k = ((j != 0) || (i != 0)) ? 1 : 0;

      if (k != 0)
      {
        this.CurrentImageReady = true;

        if ((this.PreviousImage != null) && (this.PreviousImageAnimated) && (!(this.PreviousImageFlushed)))
        {
          this.PreviousImageFlushed = true;
          this.PreviousImage.flush();
        }

        notifyAll();

        super.repaint();
      }
      else if (this.ProgressiveDraw)
      {
        this.CurrentImageReady = true;
        deliverImageEvent(1);
        super.repaint(500L);
      }
    }
    int k = ((i != 0) || (this.ImageError)) ? 0 : 1;

    this.DisplayCompleted = (k == 0);

    return k;
  }

  public Dimension getSize()
  {
    return getMinimumSize();
  }

  public Dimension getMaximumSize()
  {
    return getMinimumSize();
  }

  public Dimension getPreferredSize() {
    return getMinimumSize();
  }

  public synchronized Dimension getMinimumSize()
  {
    if (this.CurrentImage != null)
    {
      if (this.CurrentImageSize == null)
      {
        this.CurrentImageSize = new Dimension(0, 0);
        int i = 0; int j = 0;
        label82: 
        do
        {
          try
          {
            i = this.CurrentImage.getWidth(this);
            j = this.CurrentImage.getHeight(this);

            if ((this.ImageError) || (i >= 1) || (j >= 1))
              break label82;
            this.WaitingOnSize = true;
            wait();
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }

        while ((!(this.ImageError)) && (((i < 1) || (j < 1))));

        if ((i > 0) || (j > 0))
          this.CurrentImageSize = new Dimension(i, j);
      }
    }
    else
      this.CurrentImageSize = new Dimension(0, 0);

    return this.CurrentImageSize;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    if ((!(this.Stopped)) && (this.CurrentImage != null))
    {
      deliverImagePaintedEvent();

      if ((this.CurrentImageReady) || (this.ProgressiveDraw)) {
        paramGraphics.drawImage(this.CurrentImage, 0, 0, this);

        return;
      }
      if (this.PreviousImage != null)
        paramGraphics.drawImage(this.PreviousImage, 0, 0, this);
    }
  }

  public void addImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.addElement(paramImageListener);
  }

  public void removeImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.removeElement(paramImageListener);
  }

  public static void addImagePaintListener(ImagePaintListener paramImagePaintListener)
  {
    ImagePaintListeners.addElement(paramImagePaintListener);
  }

  public static void removeImagePaintListener(ImagePaintListener paramImagePaintListener)
  {
    ImagePaintListeners.removeElement(paramImagePaintListener);
  }

  public void deliverImageEvent(int paramInt)
  {
    if (this.ImageListeners.size() > 0)
      for (int i = 0; i < this.ImageListeners.size(); ++i)
      {
        ImageListener localImageListener = (ImageListener)this.ImageListeners.elementAt(i);
        localImageListener.imageChanged(new ImageEvent(this, paramInt));
      }
  }

  public void deliverImagePaintedEvent()
  {
    if (ImagePaintListeners.size() > 0)
      for (int i = 0; i < ImagePaintListeners.size(); ++i)
      {
        ImagePaintListener localImagePaintListener = (ImagePaintListener)ImagePaintListeners.elementAt(i);
        localImagePaintListener.imagePainted(this, this.CurrentImage);
      }
  }

  public void addReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners == null)
      this.ReportingComponentListeners = new Vector();

    this.ReportingComponentListeners.addElement(paramReportingComponentListener);
  }

  public void removeReportingComponentListener(ReportingComponentListener paramReportingComponentListener)
  {
    if (this.ReportingComponentListeners != null)
      this.ReportingComponentListeners.removeElement(paramReportingComponentListener);
  }

  protected void reportReshape(Rectangle paramRectangle1, Rectangle paramRectangle2)
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
      Rectangle localRectangle1 = super.getBounds();
      Rectangle localRectangle2 = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
      reportReshape(localRectangle1, localRectangle2);
    }

    super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public static void setImageFilenameProvider(ImageFilenameProvider paramImageFilenameProvider)
  {
    ImageNamer = paramImageFilenameProvider;
  }

  public static ImageFilenameProvider getImageFilenameProvider()
  {
    return ImageNamer;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("ImageComponent[");
    if (this.CurrentImage == null) {
      localStringBuffer.append("null image");
    }
    else if (ImageNamer != null)
    {
      localStringBuffer.append("(\"");
      localStringBuffer.append(ImageNamer.getImageFilename(this.CurrentImage));
      localStringBuffer.append("\")");
    }
    else if (this.ImageFilename != null) {
      localStringBuffer.append(this.ImageFilename);
    } else {
      localStringBuffer.append("unknown filename");
    }

    localStringBuffer.append(",");
    localStringBuffer.append(getSize());
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }
}