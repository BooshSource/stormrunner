package com.templar.games.stormrunner.util;

public interface ProgressListener
{
  public abstract void notifyProgress();

  public abstract void notifyProgress(int paramInt);

  public abstract void notifyProgress(String paramString, int paramInt);
}