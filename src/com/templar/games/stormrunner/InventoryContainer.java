package com.templar.games.stormrunner;

import java.util.Vector;

public interface InventoryContainer
{
  public abstract PhysicalObject transferOut(String paramString);

  public abstract boolean transferIn(PhysicalObject paramPhysicalObject);

  public abstract boolean isEmpty();

  public abstract int getPolymetals();

  public abstract void setPolymetals(int paramInt);

  public abstract int getEnergyUnits();

  public abstract void setEnergyUnits(int paramInt);

  public abstract Vector getInventory();
}