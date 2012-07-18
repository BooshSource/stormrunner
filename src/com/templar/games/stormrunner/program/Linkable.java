package com.templar.games.stormrunner.program;

public interface Linkable
{
  public abstract boolean detachNext(Linkable paramLinkable);

  public abstract boolean attachNext(Linkable paramLinkable);

  public abstract boolean detachPrevious(Linkable paramLinkable);

  public abstract boolean attachPrevious(Linkable paramLinkable);
}