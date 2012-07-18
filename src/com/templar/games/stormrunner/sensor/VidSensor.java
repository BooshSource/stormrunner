package com.templar.games.stormrunner.sensor;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class VidSensor extends Sensor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

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
    return false; }

  public String getID() { return "VidSensor"; }

  public String getDescription() {
    return "Video Sensor\n\nPolymetals: 2\nEnergy Units: 2\nSecurity Level: 1\nWeight: 0.04\n\td4000]\nManufactured by Xerox Corp. Palo Alto, California, 94304. Specially designed for mapping and extended surveying of uncharted terrain, the PAO-G1 disperses solar-powered MNT mapping particles capable of relaying continuous information about the covered area to the RCX control system for review in the map interface, making it possible to safely deploy RCX units in unexplored areas."; }

  public String toString() {
    return "Video Sensor"; } 
  public double getWeight() { return 0.040000000000000001D; } 
  public int getSalvageCost() { return 2; } 
  public int getEnergyCost() { return 2; } 
  public int getSecurityLevel() { return 1; } 
  public String getIconAppearance() { return "sensor/bicon_s-video"; } 
  public String getBayAppearance() { return "sensor/B_sensor_video.gif"; } 
  public int getPlacement() { return -1; } 
  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) { return null; } 
  public int[] getAnimationSequence(int paramInt) { return null;
  }
}