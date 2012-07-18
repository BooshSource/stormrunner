package com.templar.games.stormrunner.chassis;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Hermes extends Chassis
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected static Hashtable animationList;
  static final int[] animationSequence = { 0, 1, 2, 3 };

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
  }

  public int getPlacement()
  {
    return 0; }

  public int getImpassibility() {
    return 50;
  }

  public int getSpeed() {
    return 15; }

  public String getID() { return "Hermes"; } 
  public int getAnimationFrames() { return 4; } 
  public String toString() { return getID(); } 
  public double getWeight() { return 2.27D; } 
  public double getWeightCapacity() { return 1.4099999999999999D; } 
  public int getSalvageCost() { return 20; } 
  public int getEnergyCost() { return 12; } 
  public int getSecurityLevel() { return 2; } 
  public String getIconAppearance() { return "chassis/bicon_hermes"; } 
  public String getBayAppearance() { return "chassis/B_wheels"; }

  public String getDescription() {
    return "Hermes class\n\nPolymetals: 20\nEnergy Units: 12\nSecurity Level: 2\nWeight: 2.27\nWeight Cap.: 1.41\n\td4000]\nManufactured by Mitsubishi Motor Corp. 5-33-8, Shiba, Minato-ku, Tokyo, 108-8410. The Hermes is a high-speed high-maneuverability RCX chassis designed to operate on flat and unobstructed terrain. A mainstay in operations that demand speed and precision handling, it features a transmissionless 4.6-liter direct-drive fluid magnetic engine, adaptive suspension, and anti-lock brakes. Rated 0-100 kph in 5.2 seconds (unladen).";
  }

  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2)
  {
    Image[] arrayOfImage;
    Integer localInteger = new Integer(paramInt1 << 3 | paramInt2);
    if (animationList == null)
      animationList = new Hashtable();
    Object localObject = animationList.get(localInteger);
    if (localObject == null)
    {
      Vector localVector = new Vector();
      for (int i = 0; i < ((paramInt1 % 90 == 0) ? getAnimationFrames() : 1); ++i)
        localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/chassis/" + getID() + 
          "/walk" + i + "_" + paramInt1 + Robot.convertBias(paramInt2) + ".gif"));
      arrayOfImage = new Image[localVector.size()];
      localVector.copyInto(arrayOfImage);
      animationList.put(localInteger, arrayOfImage);
    }
    else {
      arrayOfImage = (Image[])localObject; }
    return arrayOfImage;
  }

  public int[] getAnimationSequence(int paramInt)
  {
    return animationSequence;
  }
}