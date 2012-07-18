package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;

public class PaletteComponent extends ImageComponent
{
  protected ProgramComponent Sample;
  protected Container AddTarget;
  protected EditorPalette OurPalette;
  protected ClickHandler ch;
  protected DragHandler dh;
  protected PaletteComponent realthis;

  public PaletteComponent(Image paramImage, ProgramComponent paramProgramComponent, Container paramContainer, EditorPalette paramEditorPalette)
  {
    super(paramImage, true, false);

    this.realthis = this;

    this.Sample = paramProgramComponent;
    this.AddTarget = paramContainer;
    this.OurPalette = paramEditorPalette;

    this.ch = new ClickHandler(this);
    this.dh = new DragHandler(this);
    addMouseListener(this.ch);
    addMouseMotionListener(this.dh);
  }
}