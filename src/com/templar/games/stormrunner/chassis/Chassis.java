package com.templar.games.stormrunner.chassis;

import com.templar.games.stormrunner.RobotPart;

public abstract class Chassis extends RobotPart
{
  public abstract int getImpassibility();

  public abstract int getSpeed();

  public abstract double getWeightCapacity();
}