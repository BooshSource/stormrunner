package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

public class TextButton extends TextContainer
{
  protected Color NormalTextColor;
  protected Color NormalBackgroundColor;
  protected Color MouseOverTextColor;
  protected Color MouseOverBackgroundColor;
  protected Color MousePressedTextColor;
  protected Color MousePressedBackgroundColor;
  protected boolean Clickable = true;

  protected boolean VerboseEvents = false;
  protected AudioManager player;
  protected String sound;
  protected Vector ActionRecipients;
  protected String ActionCommand = "ImageButton";
  protected MouseHandler mh;

  public TextButton()
  {
    initialize();
  }

  public TextButton(String paramString)
  {
    super(paramString);

    initialize();
  }

  public TextButton(String paramString, Color paramColor, Font paramFont)
  {
    super(paramString, paramColor, paramFont);

    initialize();
  }

  protected void initialize()
  {
    this.ActionRecipients = new Vector();

    this.mh = new MouseHandler();
    addMouseListener(this.mh);
  }

  public void setColors(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4, Color paramColor5, Color paramColor6)
  {
    this.NormalTextColor = paramColor2;
    this.NormalBackgroundColor = paramColor1;
    this.MouseOverTextColor = paramColor4;
    this.MouseOverBackgroundColor = paramColor3;
    this.MousePressedTextColor = paramColor6;
    this.MousePressedBackgroundColor = paramColor5;

    setColor(paramColor2);
    setBackgroundColor(paramColor1);
  }

  public void setVerboseEvents(boolean paramBoolean)
  {
    this.VerboseEvents = paramBoolean;
  }

  public void setClickable(boolean paramBoolean)
  {
    this.Clickable = paramBoolean;
  }

  public boolean getClickable()
  {
    return this.Clickable;
  }

  public void setClickSound(AudioManager paramAudioManager, String paramString)
  {
    this.player = paramAudioManager;
    this.sound = paramString;
  }

  public void removeNotify()
  {
    super.removeNotify();

    if (this.NormalBackgroundColor != null)
    {
      setBackgroundColor(this.NormalBackgroundColor);
      setColor(this.NormalTextColor);
    }
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

  protected void event(MouseEvent paramMouseEvent)
  {
    String str = "";

    if (this.VerboseEvents) {
      if (paramMouseEvent.getID() == 501)
        str = "P:";
      else if (paramMouseEvent.getID() == 502) {
        str = "R:";
      }
    }
    processActionEvent(new ActionEvent(this, 1001, str + this.ActionCommand));
  }

  protected class MouseHandler extends MouseAdapter
  {
    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if (TextButton.this.Clickable)
      {
        if ((TextButton.this.player != null) && (TextButton.this.sound != null))
        {
          TextButton.this.player.play(TextButton.this.sound);
        }

        if (TextButton.this.MousePressedBackgroundColor != null)
        {
          TextButton.this.setBackgroundColor(TextButton.this.MousePressedBackgroundColor);
          TextButton.this.setColor(TextButton.this.MousePressedTextColor);
        }

        if (TextButton.this.VerboseEvents)
          TextButton.this.event(paramMouseEvent);
      }
    }

    public void mouseEntered(MouseEvent paramMouseEvent)
    {
      if (TextButton.this.MouseOverBackgroundColor != null)
      {
        TextButton.this.setBackgroundColor(TextButton.this.MouseOverBackgroundColor);
        TextButton.this.setColor(TextButton.this.MouseOverTextColor);
      }
    }

    public void mouseExited(MouseEvent paramMouseEvent)
    {
      if (TextButton.this.MouseOverBackgroundColor != null)
      {
        TextButton.this.setBackgroundColor(TextButton.this.NormalBackgroundColor);
        TextButton.this.setColor(TextButton.this.NormalTextColor);
      }
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (TextButton.this.Clickable)
      {
        if ((TextButton.this.NormalBackgroundColor != null) && (TextButton.this.MousePressedBackgroundColor != null) && (TextButton.this.getBackgroundColor() != TextButton.this.NormalBackgroundColor)) {
          if (TextButton.this.MouseOverBackgroundColor != null)
          {
            TextButton.this.setBackgroundColor(TextButton.this.MouseOverBackgroundColor);
            TextButton.this.setColor(TextButton.this.MouseOverTextColor);
          }
          else
          {
            TextButton.this.setBackgroundColor(TextButton.this.NormalBackgroundColor);
            TextButton.this.setColor(TextButton.this.NormalTextColor);
          }
        }
        TextButton.this.event(paramMouseEvent);
      }
    }

    protected MouseHandler()
    {
    }
  }
}