package com.templar.games.stormrunner.program;

public interface Contextualized
{
  public abstract boolean checkContext(Object paramObject);

  public abstract void setContext(Object paramObject);

  public abstract Object getContext();
}