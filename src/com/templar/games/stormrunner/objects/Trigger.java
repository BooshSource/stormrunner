package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Robot;

public interface Trigger
{
  public static final int PICKED_UP = 1;
  public static final int DROPPED = 2;
  public static final int ENTERED = 4;
  public static final int EXITED = 8;
  public static final int BASHED = 16;

  public abstract int activateOnEvent();

  public abstract void activate(Robot paramRobot, int paramInt);

  public abstract void setGameState(GameState paramGameState);
}