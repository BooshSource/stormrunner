package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.gui.TextInputContainer;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveMenu extends Container
  implements ActionListener
{
  public static final Color BACKGROUND_COLOR = Color.black;
  public static final String TITLE = "RCX CODE LIBRARY - CESS STORMRUNNER";
  public static final String PROMPT = "SAVE FILENAME:";
  public static final String CONFIRMATION = "TRANSFER COMPLETED.";
  public static final String ABORT_NOTIFICATION = "TRANSFER ABORTED.";
  public static final Color ABORT_COLOR = new Color(255, 255, 0);
  public static final int CONFIRMATION_DELAY = 2000;
  protected Editor editor;
  protected Color TextDark;
  protected Color TextLight;
  protected Font TextFont;
  protected TextInputContainer NameInput;

  public SaveMenu(Editor paramEditor)
  {
    this.editor = paramEditor;
    Insets localInsets = Editor.EditorInsets;
    paramEditor.setCurrentlySaving(this);

    setLocation(localInsets.left, localInsets.top);
    setSize(paramEditor.getSize().width - localInsets.left - localInsets.right, paramEditor.getSize().height - localInsets.top - localInsets.bottom);

    this.editor.getPalette().setDisabled(true);

    this.TextDark = Editor.TEXTDARKCOLOR;
    this.TextLight = Editor.TEXTLIGHTCOLOR;
    this.TextFont = Editor.TEXTFONT;
    TextContainer localTextContainer = new TextContainer("RCX CODE LIBRARY - CESS STORMRUNNER", this.TextDark, this.TextFont);
    localTextContainer.setLocation(3, 35);
    localTextContainer.setStreak(true, 10, 7, 50);
    localTextContainer.setBurnLine(true, getSize().width - 6, 1, 1, 25, 5, this.TextDark, 50);
    add(localTextContainer);

    this.NameInput = new TextInputContainer("SAVE FILENAME:", this.TextDark, this.TextLight, this.TextFont);
    this.NameInput.setLocation(3, 60);
    this.NameInput.setPadding(new Insets(0, 0, 0, 0));
    this.NameInput.setMaxResponseLength(30);
    this.NameInput.addActionListener(this);

    this.editor.setInternalFocus(this.NameInput);
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(500, this, getClass().getMethod("displayStep", null), false);
      localUtilityThread.setInitialDelay(true);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public synchronized void actionPerformed(ActionEvent paramActionEvent)
  {
    String str;
    Color localColor;
    if (paramActionEvent.getActionCommand().compareTo("") == 0)
    {
      str = "TRANSFER ABORTED.";
      localColor = ABORT_COLOR;
    }
    else
    {
      str = "TRANSFER COMPLETED.";
      localColor = this.TextDark;

      OrderedTable localObject = this.editor.getGameState().getProgramLibrary();
      ((OrderedTable)localObject).put(paramActionEvent.getActionCommand(), this.editor.getProgram().copy());
    }

    requestFocus();

    Object localObject = new TextContainer(str, localColor, this.TextFont);
    ((TextContainer)localObject).setStreak(true, 10, 7, 50);
    ((Component)localObject).setLocation(4, getSize().height - ((TextContainer)localObject).getSize().height - 3);
    add((Component)localObject);
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(2000, this, getClass().getMethod("remove", null), false);
      localUtilityThread.setInitialDelay(true);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public boolean displayStep()
  {
    add(this.NameInput);

    return false;
  }

  public boolean remove()
  {
    this.editor.setCurrentlySaving(null);

    this.editor.getPalette().setDisabled(false);

    Container localContainer = getParent();
    if (localContainer != null)
    {
      localContainer.remove(this);
      localContainer.repaint();
    }

    this.editor.setInternalFocus(null);

    return false;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    paramGraphics.setColor(BACKGROUND_COLOR);
    paramGraphics.fillRect(0, 0, getSize().width, getSize().height);

    super.paint(paramGraphics);
  }
}