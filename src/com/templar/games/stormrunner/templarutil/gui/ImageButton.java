package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ImageButton extends ImageComponent
{
  protected Image NormalImage;
  protected Image MouseOverImage;
  protected Image PressedImage;
  protected Image OnImage;
  protected boolean On = false;

  protected boolean Clickable = true;
  protected boolean Enabled = true;
  protected Hashtable ImageMap;
  protected boolean VerboseEvents = false;
  protected Vector ActionRecipients;
  protected String ActionCommand = "ImageButton";

  protected boolean EventTransparent = false;
  protected Vector EventChildren;
  protected AudioManager player;
  protected String sound;
  protected MouseHandler mh;
  protected MouseMotionHandler mmh;
  protected ImageButton realthis;

  public ImageButton(Image paramImage)
  {
    this(paramImage, null);
  }

  public ImageButton(Image paramImage, Hashtable paramHashtable)
  {
    super(paramImage);

    this.realthis = this;

    this.ActionRecipients = new Vector();

    this.ImageMap = paramHashtable;

    this.mh = new MouseHandler();
    addMouseListener(this.mh);

    setSize(getSize());
  }

  public ImageButton(Image paramImage1, Image paramImage2, Image paramImage3)
  {
    super(paramImage1);

    this.realthis = this;

    this.NormalImage = paramImage1;
    this.PressedImage = paramImage2;
    this.MouseOverImage = paramImage3;

    this.ActionRecipients = new Vector();

    this.ImageMap = null;

    this.mh = new MouseHandler();
    addMouseListener(this.mh);

    setSize(getSize());
  }

  protected void rawSetImage(Image paramImage)
  {
    super.setImage(paramImage);

    repaint();
  }

  public void setImage(Image paramImage)
  {
    this.NormalImage = null;
    this.PressedImage = null;
    this.MouseOverImage = null;

    super.setImage(paramImage);

    setSize(getSize());

    repaint();
  }

  public void resetImage()
  {
    rawSetImage(this.NormalImage);
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.Enabled = paramBoolean;
  }

  public void setClickable(boolean paramBoolean)
  {
    this.Clickable = paramBoolean;
  }

  public boolean getClickable()
  {
    return this.Clickable;
  }

  public void setImageMap(Hashtable paramHashtable)
  {
    this.ImageMap = paramHashtable;

    if (paramHashtable != null)
    {
      this.mmh = new MouseMotionHandler();
      addMouseMotionListener(this.mmh);
    }
  }

  public void setClickSound(AudioManager paramAudioManager, String paramString)
  {
    this.player = paramAudioManager;
    this.sound = paramString;
  }

  public void setVerboseEvents(boolean paramBoolean)
  {
    this.VerboseEvents = paramBoolean;
  }

  public void setImages(Image paramImage1, Image paramImage2, Image paramImage3)
  {
    this.NormalImage = paramImage1;
    this.PressedImage = paramImage2;
    this.MouseOverImage = paramImage3;

    super.setImage(paramImage1);

    setSize(getSize());
  }

  public void setOnImage(Image paramImage)
  {
    this.OnImage = paramImage;

    if (this.NormalImage == null)
      this.NormalImage = this.CurrentImage;
  }

  public boolean getOn()
  {
    return this.On;
  }

  public void setOn(boolean paramBoolean)
  {
    if (this.OnImage != null)
    {
      if ((paramBoolean) && (!this.On))
      {
        this.On = true;
        super.setImage(this.OnImage);

        return;
      }

      if ((!paramBoolean) && (this.On))
      {
        this.On = false;
        super.setImage(this.NormalImage);

        return;
      }

    }
    else
    {
      System.out.println("ImageButton: Attempt to setOn without an onImage.");
    }
  }

  public boolean getEventTransparent() {
    return this.EventTransparent;
  }

  public void setEventTransparent(boolean paramBoolean)
  {
    this.EventTransparent = paramBoolean;
    this.EventChildren = new Vector();
  }

  protected synchronized void processActionEvent(ActionEvent paramActionEvent)
  {
    if (this.ActionRecipients.size() > 0)
    {
      Enumeration localEnumeration = this.ActionRecipients.elements();

      while (localEnumeration.hasMoreElements())
      {
        ActionListener localActionListener = (ActionListener)localEnumeration.nextElement();
        localActionListener.actionPerformed(paramActionEvent);
      }
    }
  }

  public void setActionCommand(String paramString)
  {
    this.ActionCommand = paramString;
  }

  public synchronized void addActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.addElement(paramActionListener);
  }

  public synchronized void removeActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.removeElement(paramActionListener);
  }

  public void processEvent(AWTEvent paramAWTEvent)
  {
    super.processEvent(paramAWTEvent);

    if (((paramAWTEvent instanceof MouseEvent)) && (this.EventTransparent))
    {
      MouseEvent localMouseEvent1 = (MouseEvent)paramAWTEvent;

      Container localContainer = getParent();
      Component[] arrayOfComponent = localContainer.getComponents();

      int i = -1;

      for (int j = 0; j < arrayOfComponent.length; j++)
      {
        Component localComponent = localContainer.getComponent(j);
        if (localComponent == this)
        {
          i = j;
        }
        else
        {
          int k = localMouseEvent1.getX() + getLocation().x - localComponent.getLocation().x;
          int m = localMouseEvent1.getY() + getLocation().y - localComponent.getLocation().y;

          if (i > -1)
            if (localComponent.contains(k, m))
            {
              localMouseEvent1.translatePoint(getLocation().x - localComponent.getLocation().x, getLocation().y - localComponent.getLocation().y);

              localComponent.dispatchEvent(localMouseEvent1);

              if (!this.EventChildren.contains(localComponent)) {
                this.EventChildren.addElement(localComponent);
              }

              j = arrayOfComponent.length;
            }
            else
            {
              if (!this.EventChildren.contains(localComponent)) {
                continue;
              }
              MouseEvent localMouseEvent2 = new MouseEvent(localComponent, 
                505, 
                System.currentTimeMillis(), 
                0, 
                k, 
                m, 
                0, 
                false);

              localComponent.dispatchEvent(localMouseEvent2);

              this.EventChildren.removeElement(localComponent);
            }
        }
      }
    }
  }

  protected String checkMap(int paramInt1, int paramInt2)
  {
    if (this.ImageMap != null)
    {
      Enumeration localEnumeration = this.ImageMap.keys();
      Object localObject = null;
      int i = 0;
      while ((localEnumeration.hasMoreElements()) && (i == 0))
      {
        localObject = localEnumeration.nextElement();
        if ((localObject instanceof Rectangle))
        {
          if (((Rectangle)localObject).contains(paramInt1, paramInt2))
            i = 1;
        }
        else if ((localObject instanceof Polygon))
        {
          if (!((Polygon)localObject).contains(paramInt1, paramInt2))
            continue;
          i = 1;
        }
        else
        {
          System.out.println("ImageButton: Somebody put some weird object in my ImageMap.");
        }
      }
      if (i != 0) {
        return (String)this.ImageMap.get(localObject);
      }
      return null;
    }

    return null;
  }

  protected void event(MouseEvent paramMouseEvent)
  {
    String str1 = "";

    if (this.VerboseEvents) {
      if (paramMouseEvent.getID() == 501)
        str1 = "P:";
      else if (paramMouseEvent.getID() == 502)
        str1 = "R:";
      else if (paramMouseEvent.getID() == 504)
        str1 = "I:";
      else if (paramMouseEvent.getID() == 505)
        str1 = "O:";
    }
    if (this.ImageMap == null)
    {
      processActionEvent(new ActionEvent(this.realthis, 1001, str1 + this.ActionCommand));

      return;
    }

    String str2 = checkMap(paramMouseEvent.getX(), paramMouseEvent.getY());

    if (str2 != null)
      processActionEvent(new ActionEvent(this.realthis, 1001, str1 + str2));
  }

  protected class MouseHandler extends MouseAdapter
  {
    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if ((ImageButton.this.Clickable) && (ImageButton.this.Enabled))
      {
        String str = ImageButton.this.checkMap(paramMouseEvent.getX(), paramMouseEvent.getY());

        if ((ImageButton.this.player != null) && (ImageButton.this.sound != null))
        {
          if (ImageButton.this.ImageMap != null)
          {
            if (str != null)
            {
              ImageButton.this.player.play(ImageButton.this.sound);
            }
          }
          else
          {
            ImageButton.this.player.play(ImageButton.this.sound);
          }
        }

        if (ImageButton.this.PressedImage != null)
        {
          if (ImageButton.this.ImageMap != null)
          {
            if (str != null)
              ImageButton.this.rawSetImage(ImageButton.this.PressedImage);
          }
          else {
            ImageButton.this.rawSetImage(ImageButton.this.PressedImage);
          }
        }
        if (ImageButton.this.VerboseEvents)
          if (ImageButton.this.ImageMap != null)
          {
            if (str != null) {
              ImageButton.this.event(paramMouseEvent);

              return;
            }

          }
          else
          {
            ImageButton.this.event(paramMouseEvent);
          }
      }
    }

    public void mouseEntered(MouseEvent paramMouseEvent) {
      String str = null;

      if (ImageButton.this.Enabled)
      {
        if (ImageButton.this.MouseOverImage != null)
        {
          if (ImageButton.this.ImageMap != null)
          {
            str = ImageButton.this.checkMap(paramMouseEvent.getX(), paramMouseEvent.getY());
            if (str != null)
              ImageButton.this.rawSetImage(ImageButton.this.MouseOverImage);
          }
          else {
            ImageButton.this.rawSetImage(ImageButton.this.MouseOverImage);
          }
        }
        if (ImageButton.this.VerboseEvents)
          if (ImageButton.this.ImageMap != null)
          {
            if (str != null) {
              ImageButton.this.event(paramMouseEvent);

              return;
            }

          }
          else
          {
            ImageButton.this.event(paramMouseEvent);
          }
      }
    }

    public void mouseExited(MouseEvent paramMouseEvent) {
      if (ImageButton.this.Enabled)
      {
        if (ImageButton.this.MouseOverImage != null)
        {
          if ((ImageButton.this.On) && (ImageButton.this.OnImage != null))
            ImageButton.this.rawSetImage(ImageButton.this.OnImage);
          else {
            ImageButton.this.rawSetImage(ImageButton.this.NormalImage);
          }
        }
        if (ImageButton.this.VerboseEvents)
          ImageButton.this.event(paramMouseEvent);
      }
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if ((ImageButton.this.Clickable) && (ImageButton.this.Enabled))
      {
        if ((ImageButton.this.NormalImage != null) && (ImageButton.this.PressedImage != null) && (ImageButton.this.CurrentImage != ImageButton.this.NormalImage)) {
          if (ImageButton.this.MouseOverImage != null)
            ImageButton.this.rawSetImage(ImageButton.this.MouseOverImage);
          else {
            ImageButton.this.rawSetImage(ImageButton.this.NormalImage);
          }
        }

        if (ImageButton.this.OnImage != null)
        {
          if ((ImageButton.this.ImageMap == null) || ((ImageButton.this.ImageMap != null) && (ImageButton.this.checkMap(paramMouseEvent.getX(), paramMouseEvent.getY()) != null)))
          {
            if (ImageButton.this.On)
              ImageButton.this.setOn(false);
            else {
              ImageButton.this.setOn(true);
            }
          }
        }
        ImageButton.this.event(paramMouseEvent);
      }
    }

    protected MouseHandler() {
    }
  }

  protected class MouseMotionHandler extends MouseMotionAdapter {
    public void mouseMoved(MouseEvent paramMouseEvent) {
      if (ImageButton.this.Enabled)
      {
        if ((ImageButton.this.MouseOverImage != null) && 
          (ImageButton.this.ImageMap != null))
        {
          if (ImageButton.this.checkMap(paramMouseEvent.getX(), paramMouseEvent.getY()) != null)
          {
            if (ImageButton.this.CurrentImage != ImageButton.this.MouseOverImage) {
              ImageButton.this.rawSetImage(ImageButton.this.MouseOverImage);

              return;
            }

          }
          else if ((ImageButton.this.On) && (ImageButton.this.OnImage != null))
          {
            if (ImageButton.this.CurrentImage != ImageButton.this.OnImage) {
              ImageButton.this.rawSetImage(ImageButton.this.OnImage);

              return;
            }

          }
          else if (ImageButton.this.CurrentImage != ImageButton.this.NormalImage)
            ImageButton.this.rawSetImage(ImageButton.this.NormalImage);
        }
      }
    }

    protected MouseMotionHandler()
    {
    }
  }
}