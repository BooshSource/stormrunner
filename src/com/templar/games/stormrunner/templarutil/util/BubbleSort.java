package com.templar.games.stormrunner.templarutil.util;

public class BubbleSort
  implements Sorter
{
  public void sort(SortData[] paramArrayOfSortData, boolean paramBoolean)
  {
    int i = 1;
    while (i != 0)
    {
      i = 0;

      for (int j = 0; j < paramArrayOfSortData.length - 1; j++)
      {
        SortData localSortData1;
        SortData localSortData2;
        if (paramBoolean)
        {
          localSortData1 = paramArrayOfSortData[(j + 1)];
          localSortData2 = paramArrayOfSortData[j];
        }
        else
        {
          localSortData1 = paramArrayOfSortData[j];
          localSortData2 = paramArrayOfSortData[(j + 1)];
        }
        if (!localSortData1.isGreater(localSortData2))
          continue;
        localSortData1.swap(localSortData1, localSortData2);
        i++;
      }
    }
  }
}