package com.templar.games.stormrunner.chassis;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Achilles extends Chassis
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
    return 100;
  }

  public int getSpeed() {
    return 10; }

  public String getID() { return "Achilles"; } 
  public int getAnimationFrames() { return 4; } 
  public String toString() { return getID(); }

  public String getDescription() {
    return "Achilles Class\n\nPolymetals: 25\nEnergy Units: 15\nSecurity Level: 1\nWeight: 2.42\nWeight Cap.: 1.52\n\td4000]\nManufactured by Technodyne Far Arm Corp. Gangway B, Io Station 11604. The Achilles chassis is the industry standard for all-purpose mobile robotic labor. With its large weight capacity and reliable mechanics, it is versatile workhorse both on- and off-world. Adapts easily to a wide range of terrain types including rough hills, wetlands, and dustplains, and features two low-yield fusion motivators and duralite musculature on all four suspension racks."; }

  public double getWeight() { return 2.4199999999999999D; } 
  public double getWeightCapacity() { return 1.52D; } 
  public int getSalvageCost() { return 25; } 
  public int getEnergyCost() { return 15; } 
  public int getSecurityLevel() { return 1; } 
  public String getIconAppearance() { return "chassis/bicon_achilles"; } 
  public String getBayAppearance() { return "chassis/B_tracks";
  }

  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2) {
    Image[] arrayOfImage;
    Integer localInteger = new Integer(paramInt1);
    if (animationList == null)
      animationList = new Hashtable();
    Object localObject = animationList.get(localInteger);
    if (localObject == null)
    {
      Vector localVector = new Vector();
      for (int i = 0; i < ((paramInt1 % 90 == 0) ? getAnimationFrames() : 1); ++i)
        localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/chassis/" + getID() + "/walk" + i + "_" + paramInt1 + ".gif"));
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