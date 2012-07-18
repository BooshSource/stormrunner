package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.util.BubbleSort;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;

public abstract class RobotPartSorter
{
  public static OrderedTable sortTable(OrderedTable paramOrderedTable)
  {
    RobotPartWrapper[] arrayOfRobotPartWrapper = RobotPartWrapper.convert(paramOrderedTable.elements(), 
      paramOrderedTable.size());
    new BubbleSort().sort(arrayOfRobotPartWrapper, false);
    OrderedTable localOrderedTable = new OrderedTable();
    for (int i = 0; i < arrayOfRobotPartWrapper.length; ++i)
    {
      int j = paramOrderedTable.indexOf(arrayOfRobotPartWrapper[i]);
      if (j == -1)
        Debug.println("BARF!!");
      else
        localOrderedTable.put(paramOrderedTable.getKey(j), arrayOfRobotPartWrapper[i].getData());
    }
    return localOrderedTable;
  }
}