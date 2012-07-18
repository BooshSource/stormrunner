package com.templar.games.stormrunner.program;

public interface Loop
{
  public abstract Instruction loop();

  public abstract Instruction loopPeek();

  public abstract void setDestination(Instruction paramInstruction);

  public abstract Instruction getDestination();
}