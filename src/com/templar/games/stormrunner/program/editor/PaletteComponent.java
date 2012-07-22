package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

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
  
  class ClickHandler extends MouseAdapter
  {
    private final PaletteComponent this$0;

    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if (!(this.this$0.OurPalette.getDisabled()))
      {
        ProgramComponent localProgramComponent = this.this$0.Sample.copy();

        Object localObject = this.this$0.realthis;
        paramMouseEvent.getPoint(); paramMouseEvent.getPoint();
        int i = 0; int j = 0;
        while (localObject != this.this$0.OurPalette.getEditor())
        {
          i += ((Component)localObject).getLocation().x;
          j += ((Component)localObject).getLocation().y;
          localObject = ((Component)localObject).getParent();
        }

        localProgramComponent.setLocation(i, j);
        this.this$0.AddTarget.add(localProgramComponent, 0);

        if (localProgramComponent.getBoundingComponent() != null)
        {
          this.this$0.AddTarget.add(localProgramComponent.getBoundingComponent(), 0);
        }

        paramMouseEvent.translatePoint(-i, -j);
        localProgramComponent.ch.mousePressed(paramMouseEvent);
      }
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (!(this.this$0.OurPalette.getDisabled()))
      {
        this.this$0.OurPalette.getCurrentActiveProgramComponent().ch.mouseReleased(paramMouseEvent);
      }
    }

    protected ClickHandler(PaletteComponent paramPaletteComponent)
    {
      this.this$0 = paramPaletteComponent;

    }
  }
  
  class DragHandler extends MouseMotionAdapter
  {
    private final PaletteComponent this$0;

    public void mouseDragged(MouseEvent paramMouseEvent)
    {
      if (!(this.this$0.OurPalette.getDisabled()))
      {
        paramMouseEvent.translatePoint(this.this$0.OurPalette.getCurrentActiveProgramComponent().getLocation().x * -1, this.this$0.OurPalette.getCurrentActiveProgramComponent().getLocation().y * -1);
        this.this$0.OurPalette.getCurrentActiveProgramComponent().mh.mouseDragged(paramMouseEvent);
      }
    }

    protected DragHandler(PaletteComponent paramPaletteComponent)
    {
      this.this$0 = paramPaletteComponent;

    }
  }
}