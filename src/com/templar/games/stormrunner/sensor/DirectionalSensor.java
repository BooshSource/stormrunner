package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.Map;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.Debug;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

public abstract class DirectionalSensor extends Sensor
{
  protected static final int[] ANGLE_LOOKUP = { 
    90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 
    90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 
    90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 
    90, 90, 90, 90, 90 };

  protected int lastDirection = -1;

  protected boolean checkRadius(Robot paramRobot, int paramInt, Class paramClass)
  {
    Scene localScene = paramRobot.getEnvironment();
    Dimension localDimension = paramRobot.getEnvironment().getMap().getSize();
    Point localPoint = paramRobot.getPosition().getMapPoint();
    int i = -1; for (int j = 1; j < paramInt; j = j == i ? i * -1 : j - 1)
    {
      int k = Math.abs(i);
      for (int m = i; m <= k; m = m = k + 1)
      {
        if ((localPoint.x + m <= 0) || (localPoint.x + m >= localDimension.width) || (localPoint.y + j <= 0) || (localPoint.y + j >= localDimension.height))
          continue;
        Vector localVector = localScene.getObjectOfTypeAt(localPoint.x + m, localPoint.y + j, paramClass);
        Debug.println("Found at " + (localPoint.x + m) + "," + (localPoint.y + j) + ",(" + m + "," + j + ")");
        if (localVector == null)
        {
          continue;
        }

        int n = (int)Math.rint(Math.atan2(-j, m) * 57.295779513082323D);
        if (n < 0)
          n = 360 + n;
        Debug.println("angle = " + n);
        this.lastDirection = ANGLE_LOOKUP[n];
        return true;
      }
      i--;
    }

    return false;
  }

  public int getDirection(Robot paramRobot)
  {
    return this.lastDirection;
  }
}