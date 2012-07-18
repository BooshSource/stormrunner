package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;

public abstract class TurnInstruction
{
  public static boolean updateState(Robot paramRobot)
  {
    int i = paramRobot.getOrientation();
    int j = paramRobot.getDestOrientation() - i;

    switch (j)
    {
    case 0:
      paramRobot.setState(0, -1);
      return false;
    case -315:
    case -270:
    case -225:
    case 45:
    case 90:
    case 135:
    case 180:
      paramRobot.setOrientation((i + 45 == 360) ? 0 : i + 45);
      return true;
    case -180:
    case -135:
    case -90:
    case -45:
    case 225:
    case 270:
    case 315:
      paramRobot.setOrientation((i - 45 < 0) ? 315 : i - 45);
      return true;
    }
    return false;
  }
}