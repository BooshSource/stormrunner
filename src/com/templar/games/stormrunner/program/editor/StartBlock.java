package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.program.Conditional;
import com.templar.games.stormrunner.program.ConditionalList;
import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.InstructionList;
import com.templar.games.stormrunner.program.Linkable;
import com.templar.games.stormrunner.program.Program;
import java.awt.Component;
import java.awt.Image;

public class StartBlock extends ProgramComponent
  implements Linkable, InstructionList, ConditionalList
{
  protected Program CurrentProgram;

  public StartBlock(Program paramProgram, Image paramImage, EditorPalette paramEditorPalette)
  {
    super(null, paramImage, paramImage, -1, paramEditorPalette);
    this.ProgramPart = this;

    this.CurrentProgram = paramProgram;

    removeMouseMotionListener(this.mh);
    removeMouseListener(this.ch);
  }

  public Instruction getNextInstruction()
  {
    return this.CurrentProgram.getFirstInstruction();
  }

  public Conditional getNextConditional()
  {
    return this.CurrentProgram.getFirstConditional();
  }

  public void setNextInstruction(Instruction paramInstruction)
  {
    this.CurrentProgram.setFirstInstruction(paramInstruction);
  }

  public void setNextConditional(Conditional paramConditional)
  {
    this.CurrentProgram.setFirstConditional(paramConditional);
  }

  public Instruction getPreviousInstruction() {
    return null; } 
  public Conditional getPreviousConditional() { return null; } 
  public void setPreviousInstruction(Instruction paramInstruction) {
  }
  public void setPreviousConditional(Conditional paramConditional) {
  }

  public boolean detachNext(Linkable paramLinkable) {
    boolean b = false;
    Object localObject;
    if ((paramLinkable instanceof Instruction))
    {
      localObject = (Instruction)paramLinkable;
      if ((((Instruction)localObject).getPreviousInstruction() == null) && (this.CurrentProgram.getFirstInstruction() == localObject))
      {
        this.CurrentProgram.setFirstInstruction(null);

        b = true;
      }
    }
    else if ((paramLinkable instanceof Conditional))
    {
      localObject = (Conditional)paramLinkable;
      if ((((Conditional)localObject).getPreviousConditional() == null) && (this.CurrentProgram.getFirstConditional() == localObject))
      {
        this.CurrentProgram.setFirstConditional(null);

        b = true;
      }
    }

    return b;
  }

  public boolean attachNext(Linkable paramLinkable)
  {
    boolean b=false;
    Object localObject;
    if ((paramLinkable instanceof Instruction))
    {
      localObject = (Instruction)paramLinkable;
      if ((((Instruction)localObject).getPreviousInstruction() == null) && (this.CurrentProgram.getFirstInstruction() == null))
      {
        this.CurrentProgram.setFirstInstruction((Instruction)localObject);

        b = true;
      }
    }
    else if ((paramLinkable instanceof Conditional))
    {
      localObject = (Conditional)paramLinkable;
      if ((((Conditional)localObject).getPreviousConditional() == null) && (this.CurrentProgram.getFirstConditional() == null))
      {
        this.CurrentProgram.setFirstConditional((Conditional)localObject);

        b = true;
      }
    }

    return b;
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