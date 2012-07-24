package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GeolabSensor extends DirectionalSensor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int CHECK_RADIUS = 5;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
  }

  public boolean check(Robot paramRobot)
  {
    this.lastDirection = -1;
    boolean bool = checkRadius(paramRobot, 5, new Salvage().getClass());

    Debug.println("Result: " + bool + "," + this.lastDirection);
    return bool;
  }
  public String getID() {
    return "GeolabSensor";
  }
  public String getDescription() {
    return "Geolab Sensor:\n\nPolymetals: 3\nEnergy Units: 6\nSecurity Level: 2\nWeight. 0.05\n\td4000]\n";
  }
  public String toString() {
    return "Geolab Sensor"; } 
  public double getWeight() { return 0.05D; } 
  public int getSalvageCost() { return 3; } 
  public int getEnergyCost() { return 6; } 
  public int getSecurityLevel() { return 2; } 
  public String getIconAppearance() { return "sensor/bicon_s-spectra"; } 
  public String getBayAppearance() { return "sensor/b_sensor_spectracom.gif"; } 
  public int[] getAnimationSequence(int paramInt) { return null; } 
  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) { return null; } 
  public int getPlacement() { return -1;
  }
}