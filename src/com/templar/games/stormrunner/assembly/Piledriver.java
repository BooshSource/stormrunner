package com.templar.games.stormrunner.assembly;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Piledriver extends Assembly
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient Hashtable animationList;
  public static final int STILL = 0;
  public static final int PUNCH = 1;
  public static final int RETRACT = 2;
  static final int[][] animationFrames = { 
    new int[1], 
    { 1, 2 }, 
    { 3, 4 } };

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
  }
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
  }
  public int getPlacement() { return 2; } 
  public String getID() {
    return "Piledriver";
  }
  public String getDescription() {
    return "Piledriver\n\nPolymetals: 10\nEnergy Units: 6\nSecurity Level: 1\nWeight: 0.42\n\td4000]\nManufactured by Ingersoll-Rand Co. Bethlehem, Pennsylvania, 18017. Useful for construction, demolitions and other industrial applications, this front-mounted piledriver allows for the portable application of up to 4,318 kg (9,520 lbs) of breaking force. It features an HTM gel-activated hydraulic compressor, a shock-mounted 75 liter hydraulic reservoir, and comes equipped with a 60cm flat titanium hammer.";
  }
  public int getAnimationFrames() { return 5; } 
  public double getWeight() { return 0.42D; } 
  public int getEnergyCost() { return 6; } 
  public int getSalvageCost() { return 10; } 
  public int getSecurityLevel() { return 1; } 
  public String getIconAppearance() { return "assembly/bicon_piledriver"; } 
  public String getBayAppearance() { return "assembly/B_piledriver.gif"; }

  public Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2)
  {
    Integer localInteger = new Integer(paramInt1);
    if (this.animationList == null)
      this.animationList = new Hashtable();
    Object localObject = this.animationList.get(localInteger);
    Image[] arrayOfImage;
    if (localObject == null)
    {
      Vector localVector = new Vector();
      localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
        "/0_" + paramInt1 + ".gif"));
      if (paramInt1 % 90 == 0)
        for (int i = 1; i < getAnimationFrames(); i++)
          localVector.addElement(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/robot/assembly/" + getID() + 
            "/" + i + "_" + paramInt1 + ".gif"));
      arrayOfImage = new Image[localVector.size()];
      localVector.copyInto(arrayOfImage);
      this.animationList.put(localInteger, arrayOfImage);
    }
    else {
      arrayOfImage = (Image[])localObject;
    }return arrayOfImage;
  }

  public int[] getAnimationSequence(int paramInt) {
    return animationFrames[paramInt];
  }
  public String toString() {
    return "Piledriver";
  }
}