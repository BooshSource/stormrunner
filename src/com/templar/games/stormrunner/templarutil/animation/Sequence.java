package com.templar.games.stormrunner.templarutil.animation;

import com.templar.games.stormrunner.templarutil.gui.ImageContainer;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

public class Sequence
  implements Runnable
{
  protected Vector Steps = new Vector();
  protected AnimationManager Manager;
  protected transient ThreadGroup OurGroup;
  protected transient Thread Director;
  protected int StepIndex;
  protected boolean Looping = false;

  protected Vector ActionRecipients = new Vector();

  public void setManager(AnimationManager paramAnimationManager)
  {
    this.Manager = paramAnimationManager;
  }

  public void setThreadGroup(ThreadGroup paramThreadGroup)
  {
    this.OurGroup = paramThreadGroup;
  }

  public void setLooping(boolean paramBoolean)
  {
    this.Looping = paramBoolean;
  }

  public void addStep(Step paramStep)
  {
    this.Steps.addElement(paramStep);
  }

  public void moveActor(String paramString, Point paramPoint1, Point paramPoint2, int paramInt1, int paramInt2)
  {
    float f = paramInt1 / paramInt2;
    int i = (int)Math.floor(f);
    int j = paramPoint2.x - paramPoint1.x;
    int k = paramPoint2.y - paramPoint1.y;

    addStep(new Step(130, 0, paramString, paramPoint1));

    for (int i1 = 0; i1 < i; i1++)
    {
      int m = paramPoint1.x + Math.round(j / f * i1);
      int n = paramPoint1.y + Math.round(k / f * i1);
      addStep(new Step(130, paramInt2, paramString, new Point(m, n)));
    }

    addStep(new Step(130, 0, paramString, paramPoint2));
  }

  public void moveLayers(Vector paramVector, Point paramPoint1, Point paramPoint2, int paramInt1, int paramInt2)
  {
    addStep(new Step(400, 0));
    for (int i = 0; i < paramVector.size(); i++)
      addStep(new Step(330, 0, (String)paramVector.elementAt(i), paramPoint1));
    addStep(new Step(410, 0));

    if (paramInt1 > 0)
    {
      float f = paramInt1 / paramInt2;
      int j = (int)Math.floor(f);
      ////(paramInt1 % paramInt2);
      int k = paramPoint2.x - paramPoint1.x;
      int m = paramPoint2.y - paramPoint1.y;

      for (int i2 = 0; i2 < j; i2++)
      {
        addStep(new Step(400, 0));
        int n = paramPoint1.x + Math.round(k / f * i2);
        int i1 = paramPoint1.y + Math.round(m / f * i2);
        for (int i3 = 0; i3 < paramVector.size(); i3++)
          addStep(new Step(330, 0, (String)paramVector.elementAt(i3), new Point(n, i1)));
        addStep(new Step(410, 0));

        addStep(new Step(500, paramInt2, null));
      }

      addStep(new Step(400, 0));
      for (int i3 = 0; i3 < paramVector.size(); i3++)
        addStep(new Step(330, 0, (String)paramVector.elementAt(i3), paramPoint2));
      addStep(new Step(410, 0));
    }
  }

  public synchronized void start()
  {
    this.Director = new Thread(this, "Animation Sequence");
    this.Director.start();
  }

  public synchronized void stop()
  {
    if (this.Director != null)
    {
      this.Director.stop();
      this.Director = null;
    }
  }

  public void run()
  {
    this.StepIndex = 0;

    while ((this.Looping) || (this.StepIndex < this.Steps.size()))
    {
      if (this.StepIndex == this.Steps.size()) {
        this.StepIndex = 0;
      }

      Step localStep = (Step)this.Steps.elementAt(this.StepIndex);

      switch (localStep.Type)
      {
      case 1000:
        this.Manager.run(localStep.Argument1);
        break;
      case 100:
        this.Manager.createActor(localStep.Argument1);
        break;
      case 110:
        this.Manager.addActor(localStep.Argument1, localStep.Argument2);
        break;
      case 120:
        this.Manager.removeActor(localStep.Argument1);
        break;
      case 130:
        this.Manager.moveActor(localStep.Argument1, localStep.Point1);
        break;
      case 140:
        this.Manager.setActorImage(localStep.Argument1, localStep.Argument2);
        break;
      case 150:
        this.Manager.actorSwitchLayer(localStep.Argument1, localStep.Argument2);
        break;
      case 200:
        this.Manager.playSound(localStep.Argument1);
        break;
      case 210:
        this.Manager.loopSound(localStep.Argument1);
        break;
      case 220:
        this.Manager.stopSound(localStep.Argument1);
        break;
      case 300:
        this.Manager.createLayer(localStep.Argument1);
        break;
      case 310:
        this.Manager.addLayer(localStep.Argument1, localStep.Number1);
        break;
      case 320:
        this.Manager.removeLayer(localStep.Argument1);
        break;
      case 330:
        this.Manager.moveLayer(localStep.Argument1, localStep.Point1);
        break;
      case 340:
        if (localStep.Argument2 == null)
          this.Manager.reorderLayer(localStep.Argument1, localStep.Number1);
        else
          this.Manager.reorderLayer(localStep.Argument1, localStep.Argument2);
        break;
      case 400:
        this.Manager.setFrozen(true);
        break;
      case 410:
        this.Manager.setFrozen(false);
        break;
      default:
        System.out.println("Sequence: Unknown Step Type.");
      case 500:
      }

      try
      {
        if (localStep.Delay > 0) {
          Thread.currentThread(); Thread.sleep(localStep.Delay);
        }

      }
      catch (InterruptedException localInterruptedException)
      {
      }

      this.StepIndex += 1;
    }

    processActionEvent(new ActionEvent(this, 1001, "Sequence Complete."));

    this.ActionRecipients.removeAllElements();
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

  public synchronized void addActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.addElement(paramActionListener);
  }

  public synchronized void removeActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.removeElement(paramActionListener);
  }

  public String toString()
  {
    String str = "Sequence on Step " + this.StepIndex + " (looping=" + this.Looping + ")\n";
    for (int i = 0; i < this.Steps.size(); i++)
    {
      str = str + "Step " + i + ": " + (Step)this.Steps.elementAt(i) + "\n";
    }

    return str;
  }
}