package com.templar.games.stormrunner.chassis;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Arachnae extends Chassis
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
    return 0;
  }
  public int getImpassibility() {
    return 200;
  }

  public int getSpeed() {
    return 5;
  }
  public String getID() { return "Arachnae"; } 
  public int getAnimationFrames() { return 4; } 
  public String toString() { return getID(); } 
  public double getWeight() { return 2.01D; } 
  public double getWeightCapacity() { return 1.13D; } 
  public int getSalvageCost() { return 23; } 
  public int getEnergyCost() { return 13; } 
  public int getSecurityLevel() { return 3; } 
  public String getIconAppearance() { return "chassis/bicon_arachne"; } 
  public String getBayAppearance() { return "chassis/B_spider"; } 
  public boolean hasTurnImages() { return false; }

  public String getDescription() {
    return "Arachnae class\n\nPolymetals: 23\nEnergy Units: 13\nSecurity Level: 3\nWeight: 2.01\nWeight Cap.: 1.13\n\td4000]\nManufactured by Hughes Douglass/General Dynamics, Latham, Maryland, 20706-4392. The Arachnae is a 3rd generation fully articulated special-duty chassis, designed for use in extremely rough terrains and rated for class C hostile environments, making it possible to deploy an RCX on cliffs and other rocky or hazardous terrain. The Arachnae frame and armatures are composed of a flow-fold carbon-polymer composite, and are equipped with integral CPS proximity sensors.";
  }

  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2)
  {
    Integer localInteger = new Integer(paramInt1);
    if (animationList == null)
      animationList = new Hashtable();
    Object localObject = animationList.get(localInteger);
    Image[] arrayOfImage;
    if (localObject == null)
    {
      Vector localVector = new Vector();
      for (int i = 0; i < (paramInt1 % 90 == 0 ? getAnimationFrames() : 1); i++)
        localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/chassis/" + getID() + "/walk" + i + "_" + paramInt1 + ".gif"));
      arrayOfImage = new Image[localVector.size()];
      localVector.copyInto(arrayOfImage);
      animationList.put(localInteger, arrayOfImage);
    }
    else {
      arrayOfImage = (Image[])localObject;
    }return arrayOfImage;
  }

  public int[] getAnimationSequence(int paramInt)
  {
    return animationSequence;
  }
}