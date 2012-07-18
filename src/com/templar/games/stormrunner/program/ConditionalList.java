package com.templar.games.stormrunner.program;

public interface ConditionalList
{
  public abstract Conditional getPreviousConditional();

  public abstract Conditional getNextConditional();

  public abstract void setPreviousConditional(Conditional paramConditional);

  public abstract void setNextConditional(Conditional paramConditional);
}