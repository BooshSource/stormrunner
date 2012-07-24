package com.templar.games.stormrunner.assembly;

import com.templar.games.stormrunner.InventoryContainer;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.objects.EnergyCell;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CargoPod extends Assembly
  implements InventoryContainer, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected static Hashtable animationList;
  Vector Inventory = new Vector();
  int Polymetals;
  int EnergyUnits;
  static String[] states = { "closed", "open" };
  public static final int STILL = 0;
  public static final int OPEN = 1;
  public static final int CLOSE = 2;
  static final int[][] animationSequences = { 
    new int[1], 
    { 0, 1, 2, 3 }, 
    { 3, 2, 1 } };

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.Polymetals = paramObjectInput.readInt();
    this.EnergyUnits = paramObjectInput.readInt();
    this.Inventory = ((Vector)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.Polymetals);
    paramObjectOutput.writeInt(this.EnergyUnits);
    paramObjectOutput.writeObject(this.Inventory);
  }
  public int getPolymetals() { return this.Polymetals; }

  public void setPolymetals(int paramInt) {
    this.Polymetals = paramInt;
  }
  public int getEnergyUnits() {
    return this.EnergyUnits;
  }
  public void setEnergyUnits(int paramInt) {
    this.EnergyUnits = paramInt;
  }
  public boolean isEmpty() {
    return this.Inventory.size() == 0;
  }
  public PhysicalObject transferOut(String paramString) {
    if ((paramString == null) || (paramString == ""))
    {
      if (isEmpty())
        return null;
      PhysicalObject localPhysicalObject1 = (PhysicalObject)this.Inventory.lastElement();
      this.Inventory.removeElement(this.Inventory.lastElement());
      return localPhysicalObject1;
    }

    for (int i = 0; i < this.Inventory.size(); i++)
    {
      PhysicalObject localPhysicalObject2 = (PhysicalObject)this.Inventory.elementAt(i);
      if (paramString.compareTo(localPhysicalObject2.getID()) != 0)
        continue;
      this.Inventory.removeElementAt(i);
      return localPhysicalObject2;
    }

    return null;
  }

  public boolean transferIn(PhysicalObject paramPhysicalObject) {
    this.Inventory.addElement(paramPhysicalObject);
    if ((paramPhysicalObject instanceof Salvage))
      this.Polymetals += ((Salvage)paramPhysicalObject).getPolymetals();
    if ((paramPhysicalObject instanceof EnergyCell))
      this.EnergyUnits += ((EnergyCell)paramPhysicalObject).getEnergyUnits();
    return true;
  }
  public Vector getInventory() { return this.Inventory; } 
  public int getAnimationFrames() { return 3; } 
  public String getID() { return "CargoPod"; } 
  public double getWeight() { return 0.39D; } 
  public int getSalvageCost() { return 15; } 
  public int getEnergyCost() { return 1; } 
  public int getSecurityLevel() { return 2; } 
  public String getIconAppearance() { return "assembly/bicon_cargopod"; } 
  public String getBayAppearance() { return "assembly/B_cargopod.gif"; } 
  public int getPlacement() { return 1; }

  public String getDescription() {
    return "Cargo Pod\n\nPolymetals: 15\nEnergy Units: 1\nSecurity Level: 2\nWeight: 0.39\n\td4000]\nManufactured by Dania Container Svcs. Miami, Florida, 33131. The Cargo Pod is a multi-purpose storage system, fully integrated for use with RCX equipment, that is capable of holding a considerable variety (by both composition and dimension) of materials. Utilizing an inert, dry-liquid management system, it is able to separate and protect both biologically active field samples and radioactive fuel materials alike. Conforms to LIASO 404-2418 and 404-2551.";
  }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer("Cargo Pod: ");
    if (this.Inventory.size() == 0) {
      localStringBuffer.append("Empty.\n");
    }
    else {
      Enumeration localEnumeration = this.Inventory.elements();
      while (localEnumeration.hasMoreElements())
      {
        localStringBuffer.append(localEnumeration.nextElement().toString());
        localStringBuffer.append("\n");
      }
    }
    localStringBuffer.append(this.Polymetals);
    localStringBuffer.append(" Poly Metals\n");
    localStringBuffer.append(this.EnergyUnits);
    localStringBuffer.append(" Energy Units");
    return localStringBuffer.toString();
  }

  public String stateName(int paramInt)
  {
    return states[paramInt];
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
      localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
        "/" + stateName(0) + "_" + paramInt1 + ".gif"));
      if (paramInt1 % 90 == 0)
        for (int i = 0; i < getAnimationFrames(); i++)
          localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
            "/" + stateName(1) + i + "_" + paramInt1 + ".gif"));
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
    return animationSequences[paramInt];
  }
}