package com.templar.games.stormrunner.templarutil.animation;

import java.awt.Point;

public class Step
{
  public static final int RUN_SEQUENCE = 1000;
  public static final int CREATE_ACTOR = 100;
  public static final int ADD_ACTOR = 110;
  public static final int REMOVE_ACTOR = 120;
  public static final int MOVE_ACTOR = 130;
  public static final int SET_ACTOR_IMAGE = 140;
  public static final int ACTOR_SWITCH_LAYER = 150;
  public static final int PLAY_SOUND = 200;
  public static final int LOOP_SOUND = 210;
  public static final int STOP_SOUND = 220;
  public static final int CREATE_LAYER = 300;
  public static final int ADD_LAYER = 310;
  public static final int REMOVE_LAYER = 320;
  public static final int MOVE_LAYER = 330;
  public static final int REORDER_LAYER = 340;
  public static final int FREEZE_DISPLAY = 400;
  public static final int UNFREEZE_DISPLAY = 410;
  public static final int NOP = 500;
  public int Type;
  public String Argument1;
  public String Argument2;
  public Point Point1;
  public int Number1;
  public int Delay;

  public Step(int paramInt1, int paramInt2)
  {
    this.Type = paramInt1;
    this.Delay = paramInt2;
  }

  public Step(int paramInt1, int paramInt2, String paramString)
  {
    this.Type = paramInt1;
    this.Delay = paramInt2;
    this.Argument1 = paramString;
  }

  public Step(int paramInt1, int paramInt2, String paramString, Point paramPoint)
  {
    this.Type = paramInt1;
    this.Delay = paramInt2;
    this.Argument1 = paramString;
    this.Point1 = paramPoint;
  }

  public Step(int paramInt1, int paramInt2, String paramString1, String paramString2)
  {
    this.Type = paramInt1;
    this.Delay = paramInt2;
    this.Argument1 = paramString1;
    this.Argument2 = paramString2;
  }

  public Step(int paramInt1, int paramInt2, String paramString1, String paramString2, Point paramPoint, int paramInt3)
  {
    this.Type = paramInt1;
    this.Delay = paramInt2;
    this.Argument1 = paramString1;
    this.Argument2 = paramString2;
    this.Point1 = paramPoint;
    this.Number1 = paramInt3;
  }

  public String toString()
  {
    String str = "Step: [" + this.Type + ",";
    if (this.Argument1 != null)
      str = str + this.Argument1 + ",";
    if (this.Argument2 != null)
      str = str + this.Argument2 + ",";
    if (this.Point1 != null)
      str = str + this.Point1 + ",";
    str = str + this.Number1 + ",Delay=" + this.Delay + "]";

    return str;
  }
}