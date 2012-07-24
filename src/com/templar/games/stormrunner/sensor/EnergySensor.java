package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.objects.EnergyCell;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class EnergySensor extends DirectionalSensor
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
    boolean bool = checkRadius(paramRobot, 5, new EnergyCell().getClass());
    return bool;
  }
  public String getID() {
    return "EnergySensor";
  }
  public String getDescription() {
    return "Energy Sensor:\n\nPolymetals: 3\nEnergy Units: 2\nSecurity Level: 2\nWeight. 0.05\n\td4000]\nManufactured by Sinclair/Clement AG, B Section, #21, Luna, C1-00024. The ARC2 ranged charged-particle detection system gives the RCX the ability to locate and seek out concentrated sources of B- and C-type Giraud particles, the signature of high-yield fusionable materials. The ARC series was originally designed for automated mining in the Van Allen Belt, but is highly versatile, with a distinguished service record in both the CES military, and the civilian waste management division, which has used the ARC2 very successfully in recovering lost or wasted fuel cells.";
  }
  public String toString() {
    return "Energy Sensor"; } 
  public double getWeight() { return 0.05D; } 
  public int getSalvageCost() { return 3; } 
  public int getEnergyCost() { return 2; } 
  public int getSecurityLevel() { return 2; } 
  public String getIconAppearance() { return "sensor/bicon_s-energy"; } 
  public String getBayAppearance() { return "sensor/B_sensor_energy.gif"; } 
  public int[] getAnimationSequence(int paramInt) { return null; } 
  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) { return null; } 
  public int getPlacement() { return -1;
  }
}