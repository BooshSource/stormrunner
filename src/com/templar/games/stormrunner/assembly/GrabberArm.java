package com.templar.games.stormrunner.assembly;

import com.templar.games.stormrunner.InventoryContainer;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.objects.EnergyCell;
import com.templar.games.stormrunner.objects.PortableObject;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class GrabberArm extends Assembly
  implements InventoryContainer, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected static final int[][] animationSequences = { 
    new int[1], 
    { 0, 1, 2, 3, 4, 5, 6 }, 
    { 6, 5, 4, 3, 2, 1 }, 
    { 0, 7, 8, 9, 10, 11, 12, 13 }, 
    { 13, 12, 11, 10, 9, 8, 7 } };
  protected static String[] states = { "still", "forward", "back" };
  public static final int STILL = 0;
  public static final int PUTDOWN = 1;
  public static final int PICKUP = 2;
  public static final int STORE = 3;
  public static final int RETRIEVE = 4;
  protected static Hashtable animationList;
  protected PortableObject Carrying;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.Carrying = ((PortableObject)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.Carrying);
  }

  public void setCarrying(PortableObject paramPortableObject) {
    this.Carrying = paramPortableObject;
  }

  public PortableObject getCarrying()
  {
    return this.Carrying;
  }

  public int getPolymetals() {
    if (this.Carrying instanceof Salvage)
      return ((Salvage)this.Carrying).getPolymetals();
    return 0;
  }

  public int getEnergyUnits() {
    if (this.Carrying instanceof EnergyCell)
      return ((EnergyCell)this.Carrying).getEnergyUnits();
    return 0; }

  public void setPolymetals(int paramInt) { }

  public void setEnergyUnits(int paramInt) { }

  public PhysicalObject transferOut(String paramString) { PhysicalObject localPhysicalObject = (PhysicalObject)this.Carrying;
    this.Carrying = null;
    return localPhysicalObject;
  }

  public boolean transferIn(PhysicalObject paramPhysicalObject) {
    if ((paramPhysicalObject instanceof PortableObject) && (this.Carrying == null))
    {
      this.Carrying = ((PortableObject)paramPhysicalObject);
      return true;
    }
    return false;
  }

  public Vector getInventory() {
    Vector localVector = new Vector(1, 1);
    localVector.addElement(this.Carrying);
    return localVector; }

  public boolean isEmpty() { return (this.Carrying == null); }

  public int getPlacement() { return 4; }

  public String getID() { return "GrabberArm"; }

  public String getDescription() {
    return "Grabber Arm\n\nPolymetals: 5\nEnergy Units: 3\nSecurity Level: 1\nWeight: 0.26\n\td4000]\nManufactured by Seiko Instruments, Inc. 8, Nakase 1-chome, Mihama-ku, Chiba-shi, Chiba 261-8507. The EHC-ARA 4501 general purpose manipulator arm is a heavy-duty yet high-precision hybrid designed to meet the diverse needs of civilian and military deployments throughout the solar system. Utilizing conventional servo motors and harmonic drives, the Arm can bear loads of up to 1.12 metric tons, and still maintain the ability to delicately manipulate small objects with a repeatability rating of 0.02mm."; }

  public double getWeight() {
    return 0.26000000000000001D; } 
  public int getEnergyCost() { return 3; } 
  public int getSalvageCost() { return 5; } 
  public int getSecurityLevel() { return 1; } 
  public String getIconAppearance() { return "assembly/bicon_grabarm"; } 
  public String getBayAppearance() { return "assembly/B_arm.gif"; }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer("Grabber Arm");
    if (this.Carrying != null)
    {
      localStringBuffer.append(" holding:\n  ");
      localStringBuffer.append(this.Carrying.toString());
    }
    return localStringBuffer.toString();
  }

  public int getAnimationFrames(int paramInt)
  {
    switch (paramInt) { case 0:
    default:
      return 1;
    case 1:
      return 6;
    case 2: } return 7;
  }

  public String stateName(int paramInt)
  {
    return states[paramInt];
  }

  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2)
  {
    Image[] arrayOfImage;
    Integer localInteger = new Integer(paramInt1);
    if (animationList == null)
      animationList = new Hashtable();
    Object localObject = animationList.get(localInteger);
    if (localObject == null)
    {
      Vector localVector = new Vector();
      localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
        "/" + stateName(0) + "0_" + paramInt1 + ".gif"));
      if (paramInt1 % 90 == 0)
        for (int i = 1; i < 3; ++i)
          for (int j = 0; j < getAnimationFrames(i); ++j)
          {
            localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
              "/" + stateName(i) + j + "_" + paramInt1 + ".gif"));
          }
      arrayOfImage = new Image[localVector.size()];
      localVector.copyInto(arrayOfImage);
      animationList.put(localInteger, arrayOfImage);
    }
    else {
      arrayOfImage = (Image[])localObject; }
    return arrayOfImage;
  }

  public int[] getAnimationSequence(int paramInt) {
    return animationSequences[paramInt];
  }
}