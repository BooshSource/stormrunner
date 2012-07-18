package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.objects.Door;
import com.templar.games.stormrunner.objects.Obstacle;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class ObstacleSensor extends Sensor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient Class ObstacleClass;

  public ObstacleSensor()
  {
    try
    {
      this.ObstacleClass = Class.forName("com.templar.games.stormrunner.objects.Obstacle");

      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
  }

  public boolean check(Robot paramRobot, boolean paramBoolean)
  {
    Position localPosition = paramRobot.inFrontOf();
    Vector localVector = paramRobot.getEnvironment().getObjectOfTypeAt(localPosition.getMapPoint(), 
      this.ObstacleClass);

    if (localVector != null)
    {
      Enumeration localEnumeration = localVector.elements();
      while (localEnumeration.hasMoreElements())
      {
        PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();

        if ((!(localPhysicalObject instanceof Door)) && (paramBoolean))
          return true;

        return ((Obstacle)localPhysicalObject).isObstructing(paramRobot);
      }

    }

    if (paramRobot.checkBounds())
      return true;

    return (paramRobot.checkTerrain());
  }

  public boolean check(Robot paramRobot)
  {
    return check(paramRobot, true); }

  public String getID() {
    return "ObstacleSensor";
  }

  public String getDescription() {
    return "Obstacle Sensor\n\nPolymetals: 3\nEnergy Units: 2\nSecurity Level: 1\nWeight: 0.06\n\td4000]\nManufactured by Brookhaven Instruments Corp. Holtsville, New York 11742. The Series 4 proximity detection system is an all-pupose control solution for giving an RCX unit the ability to navigate independently. Consisting of three millimeter wave radar arrays, supplemented by IR optical and low-intensity laser emitters, and coordinated by a Motorola G7-601 microcontroller, it allows the user the ability to easily create programs which react to obstacles intelligently, preventing collisions."; }

  public String toString() { return "Obstacle Sensor"; } 
  public double getWeight() { return 0.059999999999999998D; } 
  public int getSalvageCost() { return 3; } 
  public int getEnergyCost() { return 2; } 
  public int getSecurityLevel() { return 1; } 
  public String getIconAppearance() { return "sensor/bicon_s-obstacle"; } 
  public String getBayAppearance() { return "sensor/B_sensor_obstacle.gif"; } 
  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) { return null; } 
  public int[] getAnimationSequence(int paramInt) { return null; } 
  public int getPlacement() { return -1;
  }
}