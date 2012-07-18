package com.templar.games.stormrunner.templarutil.util;

public interface SortData
{
  public abstract void setData(Object paramObject);

  public abstract Object getData();

  public abstract boolean isGreater(SortData paramSortData);

  public abstract void swap(SortData paramSortData1, SortData paramSortData2);
}