package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class HeatSensor extends ObstacleSensor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

  public HeatSensor()
  {
    try
    {
      this.ObstacleClass = Class.forName("com.templar.games.stormrunner.objects.LavaTrigger");

      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
  }

  public String getID() {
    return "HeatSensor";
  }
  public String getDescription() {
    return "Heat Sensor\n\nPolymetals: 3\nEnergy Units: 2\nSecurity Level: 2\nWeight. 0.05\n\td4000]\nManufactured by Park Diagnostics, Inc. Gangway E, Io Station 11607. This geological survey system, designed to facilitate operations in volcanically active areas, allows the RCX to detect and respond to sources of intense heat. Making use of passive IR photoreceptor arrays, it's triggering threshold is 1020 C (1868 degrees Fahrenheit).";
  }
  public String toString() {
    return "Heat Sensor"; } 
  public double getWeight() { return 0.05D; } 
  public int getSalvageCost() { return 3; } 
  public int getEnergyCost() { return 2; } 
  public int getSecurityLevel() { return 2; } 
  public String getIconAppearance() { return "sensor/bicon_s-heat"; } 
  public String getBayAppearance() { return "sensor/B_sensor_heat.gif"; } 
  public int[] getAnimationSequence(int paramInt) { return null; } 
  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) { return null; } 
  public int getPlacement() { return -1;
  }
}