package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.util.SortData;
import java.util.Enumeration;

class RobotPartWrapper
  implements SortData
{
  RobotPart data;

  public RobotPartWrapper(RobotPart paramRobotPart)
  {
    this.data = paramRobotPart;
  }

  public void setData(Object paramObject) {
    this.data = ((RobotPart)paramObject);
  }

  public Object getData() {
    return this.data;
  }

  public boolean isGreater(SortData paramSortData) {
    if ((paramSortData.getData() instanceof RobotPart))
    {
      return this.data.getPlacement() < ((RobotPart)paramSortData.getData()).getPlacement();
    }

    throw new IllegalArgumentException("Wrong SortData type passed to isGreater");
  }

  public void swap(SortData paramSortData1, SortData paramSortData2)
  {
    Object localObject = paramSortData1.getData();
    paramSortData1.setData(paramSortData2.getData());
    paramSortData2.setData(localObject);
  }

  public String toString() {
    return this.data.toString();
  }

  public static RobotPartWrapper[] convert(Enumeration paramEnumeration, int paramInt) {
    RobotPartWrapper[] arrayOfRobotPartWrapper = new RobotPartWrapper[paramInt];
    int i = 0;
    while (paramEnumeration.hasMoreElements())
      arrayOfRobotPartWrapper[(i++)] = new RobotPartWrapper((RobotPart)paramEnumeration.nextElement());
    return arrayOfRobotPartWrapper;
  }

  public boolean equals(Object paramObject) {
    return this.data.equals(paramObject);
  }
}