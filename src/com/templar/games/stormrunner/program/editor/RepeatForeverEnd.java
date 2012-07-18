package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.InstructionList;
import com.templar.games.stormrunner.program.Linkable;
import java.awt.Component;
import java.awt.Image;

public class RepeatForeverEnd extends ProgramComponent
  implements Linkable, InstructionList, BoundingComponent
{
  protected ProgramComponent BoundSource;

  public RepeatForeverEnd(Image paramImage, EditorPalette paramEditorPalette)
  {
    super(null, paramImage, null, -1, paramEditorPalette);
    this.ProgramPart = this;

    removeMouseMotionListener(this.mh);
    removeMouseListener(this.ch);
  }

  public ProgramComponent copy()
  {
    RepeatForeverEnd localRepeatForeverEnd = new RepeatForeverEnd(super.getNormalImage(), this.OurPalette);
    localRepeatForeverEnd.DropTargets = this.DropTargets;
    return localRepeatForeverEnd;
  }

  public ProgramComponent getBoundSource()
  {
    return this.BoundSource;
  }

  public void setBoundSource(ProgramComponent paramProgramComponent)
  {
    this.BoundSource = paramProgramComponent;
  }

  public Instruction getNextInstruction()
  {
    return null;
  }

  public void setNextInstruction(Instruction paramInstruction)
  {
  }

  public Instruction getPreviousInstruction()
  {
    return null;
  }

  public void setPreviousInstruction(Instruction paramInstruction) {
  }

  public boolean detachNext(Linkable paramLinkable) {
    return false;
  }

  public boolean attachNext(Linkable paramLinkable)
  {
    return false;
  }

  public boolean detachPrevious(Linkable paramLinkable)
  {
    return false;
  }

  public boolean attachPrevious(Linkable paramLinkable)
  {
    return false;
  }
}