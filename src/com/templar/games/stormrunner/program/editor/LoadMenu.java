package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.templarutil.gui.ScrollMenu;
import com.templar.games.stormrunner.templarutil.gui.TextButton;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
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
import java.util.Enumeration;

public class LoadMenu extends Container
  implements ActionListener
{
  public static final Color BACKGROUND_COLOR = Color.black;
  public static final String TITLE = "RCX CODE LIBRARY - CESS STORMRUNNER";
  public static final String CONFIRMATION = "TRANSFER COMPLETED.";
  public static final String MISMATCH = "ROBOT CANNOT LOAD THIS PROGRAM.";
  public static final String ABORT_NOTIFICATION = "TRANSFER ABORTED.";
  public static final String DELETE_NOTIFICATION = "FILE DELETED.";
  public static final Color DELETE_COLOR = new Color(255, 255, 0);
  public static final int CONFIRMATION_DELAY = 2000;
  public static final int SCROLL_STEP = 20;
  protected Editor editor;
  protected Color TextDark;
  protected Color TextLight;
  protected Font TextFont;
  protected ScrollMenu FileList;
  protected TextButton Load;
  protected TextButton Cancel;
  protected TextButton Delete;
  protected TextContainer Response;
  protected KeyHandler kh;
  protected int DisplayStep;

  public LoadMenu(Editor paramEditor)
  {
    this.editor = paramEditor;
    Insets localInsets = Editor.EditorInsets;
    paramEditor.setCurrentlyLoading(this);

    setLocation(localInsets.left, localInsets.top);
    setSize(paramEditor.getSize().width - localInsets.left - localInsets.right, paramEditor.getSize().height - localInsets.top - localInsets.bottom);

    this.editor.getPalette().setDisabled(true);

    this.editor.setInternalFocus(this);

    this.kh = new KeyHandler(this);
    addKeyListener(this.kh);

    this.TextDark = Editor.TEXTDARKCOLOR;
    this.TextLight = Editor.TEXTLIGHTCOLOR;
    this.TextFont = Editor.TEXTFONT;

    TextContainer localTextContainer = new TextContainer("RCX CODE LIBRARY - CESS STORMRUNNER", this.TextDark, this.TextFont);
    localTextContainer.setLocation(3, 3);
    localTextContainer.setStreak(true, 10, 7, 50);
    localTextContainer.setBurnLine(true, getSize().width - 6, 1, 1, 25, 5, this.TextDark, 50);

    this.FileList = new ScrollMenu();
    this.FileList.setLocation(7, 25);
    this.FileList.setSize(getSize().width - 14, getSize().height - 50);
    this.FileList.setBorderPadding(new Insets(5, 5, 5, 5));
    this.FileList.setListDelay(150);
    this.FileList.setHighlightColors(this.TextLight, Color.black);
    this.FileList.setBorder(this.TextDark);

    this.Load = new TextButton("LOAD");
    this.Load.setColors(Color.black, this.TextDark, this.TextDark, Color.black, this.TextLight, Color.black);
    this.Load.setStreak(true, 30, 7, 50);
    this.Load.setPadding(new Insets(2, 10, 2, 10));
    this.Load.setLocation(10, getSize().height - 20);
    this.Load.setActionCommand("Load");
    this.Load.addActionListener(this);

    this.Cancel = new TextButton("CANCEL");
    this.Cancel.setColors(Color.black, this.TextDark, this.TextDark, Color.black, this.TextLight, Color.black);
    this.Cancel.setStreak(true, 30, 7, 50);
    this.Cancel.setPadding(new Insets(2, 10, 2, 10));
    this.Cancel.setLocation(getSize().width / 2 - this.Cancel.getSize().width / 2, getSize().height - 20);
    this.Cancel.setActionCommand("Cancel");
    this.Cancel.addActionListener(this);

    this.Delete = new TextButton("DELETE");
    this.Delete.setColors(Color.black, this.TextDark, this.TextDark, Color.black, this.TextLight, Color.black);
    this.Delete.setStreak(true, 30, 7, 50);
    this.Delete.setPadding(new Insets(2, 10, 2, 10));
    this.Delete.setLocation(getSize().width - this.Delete.getSize().width - 5, getSize().height - 20);
    this.Delete.setActionCommand("Delete");
    this.Delete.addActionListener(this);

    super.add(localTextContainer);
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(200, this, getClass().getMethod("displayStep", null), false);
      localUtilityThread.setInitialDelay(true);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  protected OrderedTable generateLoadList()
  {
    OrderedTable localOrderedTable1 = this.editor.getGameState().getProgramLibrary();
    OrderedTable localOrderedTable2 = new OrderedTable();

    Enumeration localEnumeration = localOrderedTable1.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      TextContainer localTextContainer = new TextContainer(str, this.TextLight, this.TextFont);
      localTextContainer.setStreak(true, 10, 7, 50);
      localTextContainer.setPadding(new Insets(3, 5, 3, this.FileList.getSize().width - localTextContainer.getSize().width - 6));
      localOrderedTable2.put(localTextContainer, str);
    }

    return localOrderedTable2;
  }

  public boolean refresh()
  {
    this.FileList.setContents(generateLoadList());

    this.FileList.repaint();

    if ((this.Response != null) && (this.Response.getParent() != null))
      super.remove(this.Response);

    if (this.Load.getParent() == null)
    {
      this.DisplayStep = 1;
      try
      {
        UtilityThread localUtilityThread = new UtilityThread(200, this, getClass().getMethod("displayStep", null), false);
        localUtilityThread.setInitialDelay(true);
        localUtilityThread.start();
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localNoSuchMethodException.printStackTrace();
      }
    }

    return false;
  }

  public synchronized void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject;
    OrderedTable localOrderedTable;
    if (paramActionEvent.getActionCommand().compareTo("Cancel") == 0)
    {
      remove();

      return;
    }

    if (paramActionEvent.getActionCommand().compareTo("Load") == 0)
    {
      localObject = this.FileList.getSelection();
      if (localObject == null) { return;
      }

      localOrderedTable = this.editor.getGameState().getProgramLibrary();

      boolean bool = this.editor.load(((Program)localOrderedTable.get(localObject)).copy());

      super.remove(this.Load);
      super.remove(this.Cancel);
      super.remove(this.Delete);

      repaint();

      if (bool)
      {
        this.Response = new TextContainer("TRANSFER COMPLETED.", this.TextDark, this.TextFont);
      }
      else
      {
        this.Response = new TextContainer("ROBOT CANNOT LOAD THIS PROGRAM.", DELETE_COLOR, this.TextFont);
      }

      this.Response.setStreak(true, 10, 7, 50);
      this.Response.setLocation(10, getSize().height - this.Response.getSize().height - 3);
      super.add(this.Response);
      try
      {
        UtilityThread localUtilityThread2 = new UtilityThread(2000, this, getClass().getMethod("remove", null), false);
        localUtilityThread2.setInitialDelay(true);
        localUtilityThread2.start();

        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException2)
      {
        localNoSuchMethodException2.printStackTrace();

        return;
      }

    }

    if (paramActionEvent.getActionCommand().compareTo("Delete") == 0)
    {
      localObject = this.FileList.getSelection();
      if (localObject != null)
      {
        localOrderedTable = this.editor.getGameState().getProgramLibrary();

        localOrderedTable.remove(localObject);

        super.remove(this.Load);
        super.remove(this.Cancel);
        super.remove(this.Delete);

        repaint();

        this.Response = new TextContainer("FILE DELETED.", DELETE_COLOR, this.TextFont);
        this.Response.setStreak(true, 10, 7, 50);
        this.Response.setLocation(10, getSize().height - this.Response.getSize().height - 3);
        super.add(this.Response);
        try
        {
          UtilityThread localUtilityThread1 = new UtilityThread(2000, this, getClass().getMethod("refresh", null), false);
          localUtilityThread1.setInitialDelay(true);
          localUtilityThread1.start();

          return;
        }
        catch (NoSuchMethodException localNoSuchMethodException1)
        {
          localNoSuchMethodException1.printStackTrace();

          return;
        }
      }
    }
  }

  public synchronized boolean displayStep()
  {
    switch (this.DisplayStep)
    {
    case 0:
      super.add(this.FileList);

      OrderedTable localOrderedTable = generateLoadList();
      this.FileList.setContents(localOrderedTable);

      break;
    case 1:
      super.add(this.Load);
      break;
    case 2:
      super.add(this.Cancel);
      break;
    case 3:
      super.add(this.Delete);
    }

    this.DisplayStep += 1;

    return (this.DisplayStep < 4);
  }

  public boolean remove()
  {
    this.editor.setCurrentlyLoading(null);

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

  public void addNotify()
  {
    super.addNotify();

    requestFocus();
  }
}