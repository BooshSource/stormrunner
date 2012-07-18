package com.templar.games.stormrunner.assembly;

import com.templar.games.stormrunner.InventoryContainer;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.objects.EnergyCell;
import com.templar.games.stormrunner.objects.PortableObject;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.objects.Satellite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Launcher extends Assembly
  implements InventoryContainer, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected static final int[][] animationSequences = { 
    new int[1], 
    new int[1], 
    new int[1], 
    new int[1] };
  public static final int STILL = 0;
  public static final int OPEN = 1;
  public static final int CLOSE = 2;
  public static final int LAUNCH = 3;
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
    if (paramPhysicalObject instanceof Satellite)
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

  public int getPlacement() { return 1; }

  public String getID() { return "Launcher"; }

  public String getDescription() {
    return "Launcher\n\nPolymetals: 10\nEnergy Units: 5\nSecurity Level: 4\nWeight: 0.40\n\td4000]\nManufactured by Lockheed Martin, Bethesda, Maryland, 20817. The Aries VI multi-role linear magnetic launcher is designed for rapid, low-impact deployment of orbital and sub-orbital materials from the safety of a remotely controlled RCX unit. Compatible with all profile C and C2 devices, it is equipped with reflex magnetic shielding to minimize collateral electromagnetic emissions during firing, and a precision hydraulic impact dampening system."; }

  public double getWeight() {
    return 0.29999999999999999D; } 
  public int getEnergyCost() { return 5; } 
  public int getSalvageCost() { return 10; } 
  public int getSecurityLevel() { return 3; } 
  public String getIconAppearance() { return "assembly/bicon_launcher"; } 
  public String getBayAppearance() { return "assembly/B_launcher.gif";
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
      localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
        "/" + paramInt1 + ".gif"));
      arrayOfImage = new Image[localVector.size()];
      localVector.copyInto(arrayOfImage);
      animationList.put(localInteger, arrayOfImage);
    }
    else {
      arrayOfImage = (Image[])localObject; }
    return arrayOfImage;
  }

  public int[] getAnimationSequence(int paramInt) {
    return animationSequences[0]; }

  public int getAnimationFrames() { return 1;
  }

  public String toString() {
    String str = "Launcher: ";
    if (this.Carrying == null)
      str = str + "Empty.\n";
    else
      str = str + "\n  " + this.Carrying.toString();

    return str;
  }
}