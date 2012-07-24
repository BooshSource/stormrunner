package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
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

    this.ch = new ClickHandler();
    this.dh = new DragHandler();
    addMouseListener(this.ch);
    addMouseMotionListener(this.dh);
  }

  protected class ClickHandler extends MouseAdapter
  {
    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if (!PaletteComponent.this.OurPalette.getDisabled())
      {
        ProgramComponent localProgramComponent = PaletteComponent.this.Sample.copy();

        Object localObject = PaletteComponent.this.realthis;
        paramMouseEvent.getPoint(); paramMouseEvent.getPoint();
        int i = 0; int j = 0;
        while (localObject != PaletteComponent.this.OurPalette.getEditor())
        {
          i += ((Component)localObject).getLocation().x;
          j += ((Component)localObject).getLocation().y;
          localObject = ((Component)localObject).getParent();
        }

        localProgramComponent.setLocation(i, j);
        PaletteComponent.this.AddTarget.add(localProgramComponent, 0);

        if (localProgramComponent.getBoundingComponent() != null)
        {
          PaletteComponent.this.AddTarget.add(localProgramComponent.getBoundingComponent(), 0);
        }

        paramMouseEvent.translatePoint(-i, -j);
        localProgramComponent.ch.mousePressed(paramMouseEvent);
      }
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (!PaletteComponent.this.OurPalette.getDisabled())
      {
        PaletteComponent.this.OurPalette.getCurrentActiveProgramComponent().ch.mouseReleased(paramMouseEvent);
      }
    }

    protected ClickHandler() {
    }
  }

  protected class DragHandler extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent paramMouseEvent) {
      if (!PaletteComponent.this.OurPalette.getDisabled())
      {
        paramMouseEvent.translatePoint(PaletteComponent.this.OurPalette.getCurrentActiveProgramComponent().getLocation().x * -1, PaletteComponent.this.OurPalette.getCurrentActiveProgramComponent().getLocation().y * -1);
        PaletteComponent.this.OurPalette.getCurrentActiveProgramComponent().mh.mouseDragged(paramMouseEvent);
      }
    }

    protected DragHandler()
    {
    }
  }
}