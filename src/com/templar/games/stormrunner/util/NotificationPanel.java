package com.templar.games.stormrunner.util;

import com.templar.games.stormrunner.StatusPanel;
import com.templar.games.stormrunner.templarutil.gui.ListLayout;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

public class NotificationPanel extends SimpleContainer
  implements ActionListener
{
  private final int BLINK_TIME = 250;
  private Vector CurrentMessages = new Vector();
  private long TopMessageFinishedAt;
  private String LastMessage;
  private StatusPanel parent;
  private TextContainer blinked;
  private UtilityThread waitforremove;
  private boolean added = false;

  public NotificationPanel(StatusPanel paramStatusPanel)
  {
    this.parent = paramStatusPanel;

    setLayout(new ListLayout(false, 4));

    setSize(140, 43);
    setLocation(50, 26);
  }

  public synchronized void addMessage(String paramString)
  {
    if ((this.LastMessage == null) || (paramString.compareTo(this.LastMessage) != 0))
    {
      this.LastMessage = paramString;

      TextContainer localTextContainer = new TextContainer();
      localTextContainer.setPackedHeight(true);
      localTextContainer.setSize(140, 43);
      localTextContainer.setLocation(0, 0);
      localTextContainer.setFont(StatusPanel.TEXTFONT);
      localTextContainer.setColor(Color.red);
      localTextContainer.setHardSize(140, 43);
      localTextContainer.setStreak(true, 8, 7, 50);
      localTextContainer.addEffectsCompletionListener(this);
      localTextContainer.setText(paramString);
      add(localTextContainer);
      doLayout();

      this.CurrentMessages.addElement(localTextContainer);

      return;
    }

    blinkLastMessage();

    if (this.CurrentMessages.size() == 1)
    {
      this.TopMessageFinishedAt = System.currentTimeMillis();
    }
  }

  public boolean getAdded()
  {
    return this.added;
  }

  private void blinkLastMessage()
  {
    this.blinked = ((TextContainer)this.CurrentMessages.elementAt(this.CurrentMessages.size() - 1));

    this.blinked.setColor(TextContainer.streakBright(Color.red, 150));

    UtilityThread localUtilityThread = null;
    try
    {
      localUtilityThread = new UtilityThread(250, this, getClass().getMethod("blinkOff", null), false);
    } catch (NoSuchMethodException localNoSuchMethodException) {
    }
    localUtilityThread.setInitialDelay(true);
    localUtilityThread.start();
  }

  public boolean blinkOff()
  {
    this.blinked.setColor(Color.red);

    return false;
  }

  public synchronized void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == this.CurrentMessages.elementAt(0))
    {
      this.TopMessageFinishedAt = System.currentTimeMillis();

      startRemovalDelay(6000);
    }
  }

  public synchronized boolean removalStep()
  {
    long l1 = this.TopMessageFinishedAt + 6000L;
    long l2 = System.currentTimeMillis();
    if (l1 > l2)
    {
      startRemovalDelay((int)(l1 - l2));
    }
    else if (this.CurrentMessages.size() > 0)
    {
      remove((Component)this.CurrentMessages.elementAt(0));
      doLayout();

      this.CurrentMessages.removeElementAt(0);

      if (this.CurrentMessages.size() == 0) {
        disappear();
      }
      else
      {
        this.TopMessageFinishedAt = l2;
        startRemovalDelay(6000);
      }
    }

    return false;
  }

  private void startRemovalDelay(int paramInt)
  {
    if (this.waitforremove != null)
    {
      this.waitforremove.politeStop();
    }

    try
    {
      this.waitforremove = new UtilityThread(paramInt, this, getClass().getMethod("removalStep", null), false);
    } catch (NoSuchMethodException localNoSuchMethodException) {
    }
    this.waitforremove.setInitialDelay(true);
    this.waitforremove.start();
  }

  private void disappear()
  {
    this.LastMessage = null;
    this.waitforremove = null;

    this.parent.remove(this);

    if (this.parent.PanelState == 0) {
      this.parent.showRoster();

      return;
    }

    this.parent.showStatus();
  }

  public void addNotify()
  {
    addNotify();

    this.added = true;
  }

  public void removeNotify()
  {
    removeNotify();

    this.added = false;
  }
}