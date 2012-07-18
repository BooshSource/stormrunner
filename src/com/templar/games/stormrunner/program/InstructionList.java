package com.templar.games.stormrunner.program;

public interface InstructionList
{
  public abstract Instruction getNextInstruction();

  public abstract Instruction getPreviousInstruction();

  public abstract void setNextInstruction(Instruction paramInstruction);

  public abstract void setPreviousInstruction(Instruction paramInstruction);
}