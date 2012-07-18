package com.templar.games.stormrunner.util;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class FocusCatcher extends Component
{
  private Component FocusTarget;
  private Component EventRecipient;
  private boolean exitedlast = true;
  private boolean skipdelivery = false;

  public FocusCatcher(Component paramComponent1, Component paramComponent2)
  {
    this.FocusTarget = paramComponent1;
    this.EventRecipient = paramComponent2;

    super.setBounds(paramComponent2.getBounds());

    super.enableEvents(48L);
  }

  public void setFocusTarget(Component paramComponent)
  {
    this.FocusTarget = paramComponent;
  }

  public void processEvent(AWTEvent paramAWTEvent)
  {
    MouseEvent localMouseEvent1 = null;
    int i = 0;

    super.processEvent(paramAWTEvent);

    if (paramAWTEvent.getID() == 505) {
      this.exitedlast = true;
    }
    else if (paramAWTEvent.getID() == 504)
    {
      localMouseEvent1 = (MouseEvent)paramAWTEvent;

      i = localMouseEvent1.getClickCount();
      if ((i > 0) && (this.FocusTarget != null))
      {
        this.FocusTarget.requestFocus();
      }

      if (!(this.exitedlast))
      {
        this.skipdelivery = true;
      }
      else
      {
        this.skipdelivery = false;
        this.exitedlast = false;
      }
    }
    else {
      this.skipdelivery = false;
    }
    super.disableEvents(48L);

    if (!(this.skipdelivery))
      this.EventRecipient.dispatchEvent(paramAWTEvent);

    if ((localMouseEvent1 != null) && (i > 0))
    {
      MouseEvent localMouseEvent2 = new MouseEvent(localMouseEvent1.getComponent(), 
        504, 
        localMouseEvent1.getWhen(), 
        localMouseEvent1.getModifiers(), 
        localMouseEvent1.getX(), 
        localMouseEvent1.getY(), 
        i, 
        false);
      MouseEvent localMouseEvent3 = new MouseEvent(localMouseEvent1.getComponent(), 
        503, 
        localMouseEvent1.getWhen(), 
        localMouseEvent1.getModifiers(), 
        localMouseEvent1.getX(), 
        localMouseEvent1.getY(), 
        i, 
        false);

      this.EventRecipient.dispatchEvent(localMouseEvent2);
      this.EventRecipient.dispatchEvent(localMouseEvent3);
    }

    super.enableEvents(48L);
  }
}