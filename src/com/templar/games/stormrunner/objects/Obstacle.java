package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.Robot;

public interface Obstacle
{
  public abstract boolean isObstructing(Robot paramRobot);
}