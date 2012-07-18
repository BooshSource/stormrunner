package com.templar.games.stormrunner.program.editor;

public interface BoundingComponent
{
  public abstract ProgramComponent getBoundSource();

  public abstract void setBoundSource(ProgramComponent paramProgramComponent);
}