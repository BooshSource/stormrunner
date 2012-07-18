package com.templar.games.stormrunner.templarutil.util;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UtilityThread extends Thread
{
  protected int Delay;
  protected Object Target;
  protected Method TargetMethod;
  protected boolean SuspendOnFalse;
  protected boolean InclusiveDelay = false;
  protected long StartTime;
  protected boolean InitialDelay = false;
  protected boolean Stopped = false;

  public UtilityThread(int paramInt, Object paramObject, Method paramMethod, boolean paramBoolean)
  {
    super("UtilityThread Executing: " + paramMethod);

    this.Delay = paramInt;
    this.Target = paramObject;
    this.TargetMethod = paramMethod;
    this.SuspendOnFalse = paramBoolean;
  }

  public void setInclusiveDelay(boolean paramBoolean)
  {
    this.InclusiveDelay = paramBoolean;
  }

  public void setInitialDelay(boolean paramBoolean)
  {
    this.InitialDelay = paramBoolean;
  }

  public void politeStop()
  {
    this.Stopped = true;
  }

  public void run()
  {
    Boolean localBoolean = null;

    if (this.Stopped) return;

    if (this.InitialDelay)
      try {
        Thread.sleep(this.Delay);
      }
      catch (InterruptedException localInterruptedException2)
      {
      }
    if (this.Stopped) return;

    try
    {
      this.StartTime = System.currentTimeMillis();

      localBoolean = (Boolean)this.TargetMethod.invoke(this.Target, null);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      if (localInvocationTargetException.getTargetException() instanceof ThreadDeath)
      {
        throw ((ThreadDeath)localInvocationTargetException.getTargetException());
      }

      System.out.println(localInvocationTargetException);
      localInvocationTargetException.getTargetException().printStackTrace();

      return;
    }
    catch (ClassCastException localClassCastException)
    {
      localClassCastException.printStackTrace();
      System.err.println("All methods called by UtilityThread must return a boolean!");
      return;
    }

    if (this.Stopped) return;

    if (localBoolean.booleanValue())
    {
      Thread.yield();

      if (this.Stopped) return;
    }
    try
    {
      do {
        int i;
        if (this.InclusiveDelay)
          i = this.Delay - (int)(System.currentTimeMillis() - this.StartTime);
        else
          i = this.Delay;

        if (i > 0)
          Thread.sleep(i);
      }
      while (!(this.Stopped)); return;
    }
    catch (InterruptedException localInterruptedException1)
    {
      while (true) {
        localInterruptedException1.printStackTrace();
      }

      if (!(this.SuspendOnFalse)) return;
      super.suspend();
    }
  }
}