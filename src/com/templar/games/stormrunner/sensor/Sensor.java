package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;

public abstract class Sensor extends RobotPart
{
  public abstract boolean check(Robot paramRobot);
}