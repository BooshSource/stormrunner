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
    int i = -1; for (int j = 1; j < paramInt; j = (j == i) ? --i * -1 : j - 1)
    {
      int k = Math.abs(i);
      for (int l = i; l <= k; l = ++k)
      {
        if ((localPoint.x + l > 0) && (localPoint.x + l < localDimension.width) && (localPoint.y + j > 0) && (localPoint.y + j < localDimension.height))
        {
          Vector localVector = localScene.getObjectOfTypeAt(localPoint.x + l, localPoint.y + j, paramClass);
          Debug.println("Found at " + (localPoint.x + l) + "," + (localPoint.y + j) + ",(" + l + "," + j + ")");
          if (localVector != null)
          {
            int i1 = (int)Math.rint(Math.atan2(-j, l) * 57.295779513082323D);
            if (i1 < 0)
              i1 = 360 + i1;
            Debug.println("angle = " + i1);
            this.lastDirection = ANGLE_LOOKUP[i1];
            return true;
          }
        }
      }
    }
    return false;
  }

  public int getDirection(Robot paramRobot)
  {
    return this.lastDirection;
  }
}