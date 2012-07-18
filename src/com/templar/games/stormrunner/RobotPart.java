package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;

public abstract class RobotPart
{
  public static final int DONT_CARE = -1;
  public static final int BOTTOM = 0;
  public static final int REAR = 1;
  public static final int TOP = 4;
  public static final int FRONT = 2;

  public abstract int getPlacement();

  public abstract String getID();

  public abstract String getDescription();

  public abstract double getWeight();

  public abstract int getEnergyCost();

  public abstract int getSalvageCost();

  public abstract int getSecurityLevel();

  public abstract String getIconAppearance();

  public abstract String getBayAppearance();

  public abstract Image[] getCells(ImageRetriever paramImageRetriever, int paramInt1, int paramInt2);

  public abstract int[] getAnimationSequence(int paramInt);

  public abstract String toString();
}