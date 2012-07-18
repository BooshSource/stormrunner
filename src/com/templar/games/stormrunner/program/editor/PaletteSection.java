package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Vector;

public class PaletteSection extends SimpleContainer
  implements ActionListener
{
  protected ImageRetriever ir;
  protected ImageComponent TitleBackground;
  protected Image TitleImageOff;
  protected Image TitleImagePressed;
  protected ImageButton TitleButton;
  protected ImageButton ScrollUp;
  protected ImageButton ScrollDown;
  protected UtilityThread ScrollThread;
  protected EditorPalette OurPalette;
  protected Container AddTarget;
  protected Container ScrollArea;
  protected Container PaletteSpace;
  protected Vector Contents;
  protected boolean minimized = true;

  public PaletteSection(Container paramContainer, Vector paramVector, EditorPalette paramEditorPalette, Image paramImage1, Image paramImage2)
  {
    this.OurPalette = paramEditorPalette;
    this.AddTarget = paramContainer;

    setLayout(null);

    this.ir = paramEditorPalette.getEditorDisplay().getEditor().getImageRetriever();

    this.TitleBackground = new ImageComponent(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_commands-back.gif"));
    this.TitleBackground.setSize(this.TitleBackground.getSize());
    this.TitleBackground.setLocation(0, 0);
    add(this.TitleBackground);

    this.TitleButton = new ImageButton(paramImage1, paramImage2, null);
    this.TitleButton.setLocation(16, 3);
    this.TitleButton.setActionCommand("title");
    this.TitleButton.addActionListener(this);
    this.TitleButton.setClickSound(GameApplet.audio, "ButtonClick");
    add(this.TitleButton, 0);

    this.ScrollUp = new ImageButton(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowup-off.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowup-in.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowup-lit.gif"));
    this.ScrollUp.setLocation(16, 27);
    this.ScrollUp.setActionCommand("up");
    this.ScrollUp.setVerboseEvents(true);
    this.ScrollUp.addActionListener(this);
    this.ScrollUp.setClickSound(GameApplet.audio, "ButtonClick");
    add(this.ScrollUp);

    ImageComponent localImageComponent = new ImageComponent(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_leftrim.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(16, 44);
    add(localImageComponent);

    localImageComponent = new ImageComponent(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_rightrim.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(67, 44);
    add(localImageComponent);

    this.ScrollDown = new ImageButton(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowdown-off.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowdown-in.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_arrowdown-lit.gif"));
    this.ScrollDown.setLocation(16, 120);
    this.ScrollDown.setActionCommand("down");
    this.ScrollDown.setVerboseEvents(true);
    this.ScrollDown.addActionListener(this);
    this.ScrollDown.setClickSound(GameApplet.audio, "ButtonClick");
    add(this.ScrollDown);

    localImageComponent = new ImageComponent(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_purplepanel.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(19, 44);
    add(localImageComponent);

    this.ScrollArea = new SimpleContainer();
    this.ScrollArea.setLayout(null);
    this.ScrollArea.setSize(localImageComponent.getSize());
    this.ScrollArea.setLocation(localImageComponent.getLocation());
    add(this.ScrollArea, 0);

    this.PaletteSpace = new SimpleContainer();
    this.PaletteSpace.setLocation(0, 0);
    this.PaletteSpace.setLayout(new GridLayout(0, 1, 0, 6));
    this.ScrollArea.add(this.PaletteSpace, 0);

    setContents(paramVector);
  }

  public void setContents(Vector paramVector)
  {
    this.Contents = paramVector;

    this.PaletteSpace.removeAll();
    this.PaletteSpace.setLocation(0, 0);

    for (int i = 0; i < this.Contents.size(); ++i)
    {
      ProgramComponent localProgramComponent = (ProgramComponent)this.Contents.elementAt(i);
      PaletteComponent localPaletteComponent = new PaletteComponent(localProgramComponent.getIconImage(), localProgramComponent, this.AddTarget, this.OurPalette);
      this.PaletteSpace.add(localPaletteComponent);
    }

    this.PaletteSpace.setSize(this.PaletteSpace.getLayout().preferredLayoutSize(this.PaletteSpace));

    this.PaletteSpace.doLayout();
  }

  public void setMinimized(boolean paramBoolean)
  {
    if (!(paramBoolean))
    {
      this.minimized = false;

      PaletteSection localPaletteSection = this.OurPalette.getOpenSection();
      if (localPaletteSection != null)
        localPaletteSection.setMinimized(true);
      this.OurPalette.setOpenSection(this);

      invalidate();
      getParent().validate();

      return;
    }

    this.minimized = true;
    this.OurPalette.setOpenSection(null);

    invalidate();
    getParent().validate();
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      if (paramActionEvent.getActionCommand().compareTo("title") == 0)
      {
        if (this.minimized)
        {
          setMinimized(false);

          return;
        }

        setMinimized(true);

        return;
      }

      if (paramActionEvent.getActionCommand().compareTo("P:down") == 0)
      {
        this.ScrollThread = new UtilityThread(50, this, getClass().getMethod("stepDown", null), false);
        this.ScrollThread.start();

        return;
      }

      if (paramActionEvent.getActionCommand().compareTo("R:down") == 0)
      {
        if (this.ScrollThread == null) break label174;
        this.ScrollThread.politeStop();

        return;
      }

      if (paramActionEvent.getActionCommand().compareTo("P:up") == 0)
      {
        this.ScrollThread = new UtilityThread(50, this, getClass().getMethod("stepUp", null), false);
        this.ScrollThread.start();

        return;
      }

      if ((paramActionEvent.getActionCommand().compareTo("R:up") != 0) || 
        (this.ScrollThread == null)) {
        return;
      }

      this.ScrollThread.politeStop();

      label174: return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
      System.err.println("PaletteSection: Some idiot programmer mistyped something in the Palette scrolling code.");
    }
  }

  public boolean stepUp()
  {
    int i = Math.min(this.PaletteSpace.getLocation().y + 10, 0);
    this.PaletteSpace.setLocation(this.PaletteSpace.getLocation().x, i);

    if (i != 0) {
      return true;
    }

    this.ScrollThread = null;
    repaint();
    return false;
  }

  public boolean stepDown()
  {
    int i = Math.max(this.PaletteSpace.getLocation().y - 10, this.ScrollArea.getSize().height - this.PaletteSpace.getSize().height);
    this.PaletteSpace.setLocation(this.PaletteSpace.getLocation().x, i);

    if (i != this.ScrollArea.getSize().height - this.PaletteSpace.getSize().height) {
      return true;
    }

    this.ScrollThread = null;
    repaint();
    return false;
  }

  public Dimension getSize()
  {
    return getMinimumSize();
  }

  public Dimension getMaximumSize()
  {
    return getMinimumSize();
  }

  public Dimension getPreferredSize()
  {
    return getMinimumSize();
  }

  public Dimension getMinimumSize()
  {
    Dimension localDimension;
    if (this.minimized) {
      localDimension = this.TitleBackground.getSize();
    }
    else
      localDimension = new Dimension(this.TitleBackground.getSize().width, this.TitleBackground.getSize().height + 112);

    return localDimension;
  }
}