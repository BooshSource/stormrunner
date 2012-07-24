package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.InstructionList;
import com.templar.games.stormrunner.program.Linkable;
import java.awt.Component;
import java.awt.Image;
import java.io.PrintStream;

public class RepeatEnd extends ProgramComponent
  implements Linkable, InstructionList, BoundingComponent
{
  protected ProgramComponent BoundSource;

  public RepeatEnd(Image paramImage, EditorPalette paramEditorPalette)
  {
    super(null, paramImage, null, -1, paramEditorPalette);
    this.ProgramPart = this;

    removeMouseMotionListener(this.mh);
    removeMouseListener(this.ch);
  }

  public ProgramComponent copy()
  {
    RepeatEnd localRepeatEnd = new RepeatEnd(getNormalImage(), this.OurPalette);
    localRepeatEnd.DropTargets = this.DropTargets;
    return localRepeatEnd;
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
    return ((InstructionList)this.BoundSource.getProgramPart()).getNextInstruction();
  }

  public void setNextInstruction(Instruction paramInstruction)
  {
    ((InstructionList)this.BoundSource.getProgramPart()).setNextInstruction(paramInstruction);
  }

  public Instruction getPreviousInstruction() {
    return null;
  }
  public void setPreviousInstruction(Instruction paramInstruction) {
  }

  public boolean detachNext(Linkable paramLinkable) {
    boolean b = false;

    if ((paramLinkable instanceof Instruction))
    {
      Instruction localInstruction = (Instruction)paramLinkable;
      if ((localInstruction.getPreviousInstruction() == null) && (getNextInstruction() == localInstruction))
      {
        localInstruction.setPreviousInstruction(null);
        setNextInstruction(null);

        b = true;
      }
      else {
        System.out.println("RepeatEnd: Asked to detachNext() from something we are not attached to.");
      }
    } else {
      System.out.println("RepeatEnd: Asked to detachNext() from something that's not an Instruction.");
    }
    return b;
  }

  public boolean attachNext(Linkable paramLinkable)
  {
    boolean b = false;

    if ((paramLinkable instanceof Instruction))
    {
      Instruction localInstruction = (Instruction)paramLinkable;
      if ((localInstruction.getPreviousInstruction() == null) && (getNextInstruction() == null))
      {
        localInstruction.setPreviousInstruction((Instruction)this.BoundSource.getProgramPart());
        setNextInstruction(localInstruction);

        b=true;
      }
      else {
        System.out.println("RepeatEnd: Asked to attachNext() to something that's already attached.");
      }
    } else {
      System.out.println("RepeatEnd: Asked to attachNext() to something that's not an Instruction.");
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