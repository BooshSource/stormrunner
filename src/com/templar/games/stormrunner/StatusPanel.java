package com.templar.games.stormrunner;

import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ScrollMenu;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import com.templar.games.stormrunner.util.NotificationPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class StatusPanel extends SimpleContainer
  implements ActionListener
{
  public static final int NOTIFY_DELAY = 6000;
  public static final int MINIMIZEDSHOW = 20;
  public static final int COLLAPSESTEPS = 3;
  public static final int ROSTER_VISIBLE = 0;
  public static final int STAT_VISIBLE = 1;
  public static final int SCROLLUP = 0;
  public static final int SCROLLDN = 1;
  public static final int NOSCROLL = -1;
  public static final Color TEXTCOLOUR = new Color(255, 204, 0);
  public static final Font TEXTFONT = new Font("SansSerif", 0, 9);
  ImageButton run;
  ImageButton stop;
  ImageButton up;
  ImageButton down;
  ImageButton stat;
  ImageButton button;
  ImageButton roster;
  ImageComponent back;
  Point LocationHolder;
  int CurrentY;
  UtilityThread Collapser;
  UtilityThread Scroller;
  UtilityThread Notifier;
  int scrolling = -1;
  int ScrollY;
  GameState state;
  Vector listeners = new Vector();
  ScrollMenu RosterMenu;
  ScrollMenu StatMenu;
  NotificationPanel MessagesPanel;
  public int PanelState;

  public StatusPanel(ImageRetriever paramImageRetriever, GameState paramGameState)
  {
    AudioManager localAudioManager = GameApplet.audio;
    this.state = paramGameState;

    enableEvents(16L);

    this.run = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_run-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_run-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_run-lit.gif"));
    this.stop = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stop-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stop-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stop-lit.gif"));
    this.up = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrollup-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrollup-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrollup-lit.gif"));
    this.down = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrolldn-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrolldn-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_scrolldn-lit.gif"));
    this.stat = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stat-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stat-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stat-lit.gif"));
    this.button = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_button-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_button-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_button-lit.gif"));
    this.roster = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_roster-off.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_roster-in.gif"), 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_roster-lit.gif"));
    this.back = new ImageComponent(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_back.gif"));
    this.back.setSize(this.back.getSize());
    this.back.setLocation(0, 0);
    setSize(this.back.getSize());
    setLayout(null);
    add(this.back);
    this.run.setLocation(68, 72);
    this.run.setSize(this.run.getSize());
    this.run.addActionListener(this);
    this.run.setActionCommand("run");
    this.run.setClickSound(localAudioManager, "ButtonRun");
    add(this.run);
    this.button.setLocation(141, 3);
    this.button.setSize(this.button.getSize());
    this.button.addActionListener(this);
    this.button.setActionCommand("button");
    this.button.setOnImage(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_button-lit.gif"));
    this.button.setOn(false);
    this.button.setClickSound(localAudioManager, "ButtonClick");
    add(this.button, 0);
    this.roster.setLocation(9, 49);
    this.roster.setSize(this.roster.getSize());
    this.roster.addActionListener(this);
    this.roster.setActionCommand("roster");
    this.roster.setOnImage(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_roster-lit.gif"));
    this.roster.setOn(true);
    this.roster.setClickSound(localAudioManager, "ButtonClick");
    add(this.roster);
    this.stat.setLocation(178, 49);
    this.stat.setSize(this.stat.getSize());
    this.stat.addActionListener(this);
    Polygon localPolygon1 = new Polygon();
    localPolygon1.addPoint(1, 33);
    localPolygon1.addPoint(19, 26);
    localPolygon1.addPoint(31, 12);
    localPolygon1.addPoint(35, 1);
    localPolygon1.addPoint(50, 33);
    localPolygon1.addPoint(50, 38);
    localPolygon1.addPoint(1, 38);
    Hashtable localHashtable1 = new Hashtable();
    localHashtable1.put(localPolygon1, "stat");
    this.stat.setImageMap(localHashtable1);
    this.stat.setEventTransparent(true);
    this.stat.setClickSound(localAudioManager, "ButtonClick");
    this.stat.setActionCommand("stat");
    this.stat.setOnImage(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/statuspanel/spanel_stat-lit.gif"));
    this.stat.setOn(false);
    add(this.stat);
    this.down.setLocation(185, 48);
    this.down.setSize(this.down.getSize());
    this.down.addActionListener(this);
    Polygon localPolygon2 = new Polygon();
    localPolygon2.addPoint(10, 0);
    localPolygon2.addPoint(26, 0);
    localPolygon2.addPoint(18, 18);
    localPolygon2.addPoint(1, 28);
    localPolygon2.addPoint(8, 16);
    Hashtable localHashtable2 = new Hashtable();
    localHashtable2.put(localPolygon2, "down");
    this.down.setImageMap(localHashtable2);
    this.down.setClickSound(localAudioManager, "ButtonClick");
    this.down.setActionCommand("down");
    this.down.setVerboseEvents(true);
    add(this.down);
    this.up.setLocation(188, 20);
    this.up.setSize(this.up.getSize());
    this.up.addActionListener(this);
    this.up.setActionCommand("up");
    this.up.setClickSound(localAudioManager, "ButtonClick");
    this.up.setVerboseEvents(true);
    add(this.up);
    this.stop.setLocation(127, 72);
    this.stop.setSize(this.stop.getSize());
    this.stop.addActionListener(this);
    this.stop.setActionCommand("stop");
    this.stop.setClickSound(localAudioManager, "ButtonStop");
    add(this.stop);

    this.RosterMenu = new ScrollMenu();
    this.StatMenu = new ScrollMenu();
    this.PanelState = 0;
    this.RosterMenu.setSize(140, 43);
    this.RosterMenu.setLocation(50, 26);
    this.RosterMenu.setHighlightColors(TEXTCOLOUR, Color.black);
    this.RosterMenu.addActionListener(this);
    this.StatMenu.setSize(140, 43);
    this.StatMenu.setLocation(50, 26);
    this.StatMenu.addActionListener(this);
    add(this.RosterMenu, 0);

    this.MessagesPanel = new NotificationPanel(this);

    populateRosterPanel();
    populateStatPanel();
  }

  public void setLocationHolder(int paramInt1, int paramInt2)
  {
    this.LocationHolder = new Point(paramInt1, paramInt2);
  }

  public boolean isMinimized()
  {
    return (!(this.button.getOn()));
  }

  public void setMinimized(boolean paramBoolean)
  {
    try
    {
      if (!(paramBoolean))
      {
        if (this.Collapser != null)
          this.Collapser.politeStop();

        this.button.setOn(true);

        if (getLocation().y <= this.LocationHolder.y)
          return;

        GameApplet.audio.play("PanelZip");

        if (this.PanelState == 0)
        {
          this.roster.setOn(true);
          this.stat.setOn(false);
        }
        else
        {
          this.roster.setOn(false);
          this.stat.setOn(true);
        }

        this.CurrentY = getLocation().y;

        this.Collapser = new UtilityThread(10, this, getClass().getMethod("maximizeStep", null), false);
        this.Collapser.start();

        return;
      }

      if (this.Collapser != null)
        this.Collapser.politeStop();

      this.button.setOn(false);

      if (getLocation().y > this.LocationHolder.y) { return;
      }

      Enumeration localEnumeration = this.listeners.elements();
      while (localEnumeration.hasMoreElements()) {
        ((ActionListener)localEnumeration.nextElement()).actionPerformed(
          new ActionEvent(this, 1001, "Roster Minimized"));
      }

      GameApplet.audio.play("PanelZip");

      this.CurrentY = this.LocationHolder.y;
      this.Collapser = new UtilityThread(10, this, getClass().getMethod("minimizeStep", null), false);
      this.Collapser.start();

      label274: return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public synchronized void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
    if ((str.compareTo("run") == 0) && 
      (this.state.getCurrentRobot() != null) && 
      (this.state.getCurrentRobot().getProgram() != null))
    {
      this.state.currentRobot.getProgram().setExecuting(true);
    }

    if (str.compareTo("P:up") == 0)
    {
      if ((this.scrolling == 1) || (this.ScrollY != 0))
      {
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException1)
        {
          return;
        }
      }

      if (this.scrolling == -1)
      {
        this.scrolling = 0;
        try
        {
          this.Scroller = new UtilityThread(25, this, getClass().getMethod("scrollUp", null), false);
          this.Scroller.start();
        }
        catch (NoSuchMethodException localNoSuchMethodException1)
        {
          this.scrolling = -1;
          notify();
        }
      }
    }
    if ((str.compareTo("R:up") == 0) || (str.compareTo("R:down") == 0) || 
      (str.compareTo("O:up") == 0) || (str.compareTo("O:down") == 0))
    {
      this.scrolling = -1;
    }
    if (str.compareTo("P:down") == 0)
    {
      if ((this.scrolling == 0) || (this.ScrollY != 0))
      {
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException2)
        {
          return;
        }
      }

      if (this.scrolling == -1)
      {
        this.scrolling = 1;
        try
        {
          this.Scroller = new UtilityThread(25, this, getClass().getMethod("scrollDown", null), false);
          this.Scroller.start();
        }
        catch (NoSuchMethodException localNoSuchMethodException2)
        {
          this.scrolling = -1;
          notify();
        }
      }
    }
    if ((str.compareTo("stop") == 0) && 
      (this.state.getCurrentRobot() != null) && 
      (this.state.getCurrentRobot().getProgram() != null))
    {
      this.state.getCurrentRobot().stop();
    }

    if (str.compareTo("button") == 0)
    {
      if (getLocation().y > this.LocationHolder.y)
      {
        setMinimized(false);
      }
      else
      {
        setMinimized(true);
      }
    }
    if (str.compareTo("roster") == 0)
    {
      this.roster.setOn(true);
      this.stat.setOn(false);
      showRoster();
    }
    if (str.compareTo("stat") == 0)
    {
      this.stat.setOn(true);
      this.roster.setOn(false);
      showStatus();
    }
    if (str.compareTo("Item Selected") == 0)
    {
      populateStatPanel();

      if (this.PanelState == 0)
      {
        if (this.RosterMenu.getSelection() instanceof Robot)
        {
          localObject = (Robot)this.RosterMenu.getSelection();

          if (localObject != this.state.getCurrentRobot())
          {
            this.state.setCurrentRobot((Robot)localObject);
          }

          this.state.getCurrentScene().getRenderer().setObjectToFollow((PhysicalObject)localObject);

          Enumeration localEnumeration = this.listeners.elements();
          while (localEnumeration.hasMoreElements())
            ((ActionListener)localEnumeration.nextElement()).actionPerformed(
              new ActionEvent(this, 1001, "Roster Selected"));
          return;
        }

        Object localObject = (PhysicalObject)this.RosterMenu.getSelection();
        ((PhysicalObject)localObject).handleMouseClick(
          new MouseEvent((Component)localObject, 501, System.currentTimeMillis(), 
          0, 0, 0, 1, false));
      }
    }
  }

  public void showRoster()
  {
    if (this.Scroller != null) return;

    if (this.MessagesPanel.getAdded())
      remove(this.MessagesPanel);

    remove(this.StatMenu);
    add(this.RosterMenu, 0);
    repaint();
    this.PanelState = 0;
  }

  public void showStatus()
  {
    if (this.Scroller != null) return;

    if (this.MessagesPanel.getAdded())
      remove(this.MessagesPanel);

    remove(this.RosterMenu);
    add(this.StatMenu, 0);
    repaint();
    this.PanelState = 1;
  }

  public synchronized boolean scrollDown()
  {
    ScrollMenu localScrollMenu = (this.PanelState == 0) ? this.RosterMenu : this.StatMenu;
    int i = (localScrollMenu.stepDown(2)) ? 0 : 1;
    this.ScrollY += 1;
    if ((((i != 0) || (this.scrolling == -1))) && ((
      (i != 0) || (this.ScrollY % 5 == 0))))
    {
      this.Scroller = null;
      this.ScrollY = 0;
      notify();
      return false;
    }

    return true;
  }

  public synchronized boolean scrollUp() {
    ScrollMenu localScrollMenu = (this.PanelState == 0) ? this.RosterMenu : this.StatMenu;
    int i = (localScrollMenu.stepUp(2)) ? 0 : 1;
    this.ScrollY -= 1;
    if ((((i != 0) || (this.scrolling == -1))) && ((
      (i != 0) || (this.ScrollY % 5 == 0))))
    {
      this.Scroller = null;
      this.ScrollY = 0;
      notify();
      return false;
    }

    return true;
  }

  public boolean minimizeStep() {
    if (this.CurrentY < this.LocationHolder.y + getSize().height - 20)
    {
      setLocation(getLocation().x, this.CurrentY);
      this.CurrentY += getSize().height / 3;
      return true;
    }

    setLocation(getLocation().x, this.LocationHolder.y + getSize().height - 20);
    this.Collapser = null;
    return false;
  }

  public boolean maximizeStep()
  {
    if (this.CurrentY > this.LocationHolder.y)
    {
      setLocation(getLocation().x, this.CurrentY);
      this.CurrentY -= getSize().height / 3;
      return true;
    }

    setLocation(getLocation().x, this.LocationHolder.y);
    this.Collapser = null;
    return false;
  }

  public void UpdateContents()
  {
    if (this.PanelState == 0) {
      populateRosterPanel();

      return;
    }

    populateStatPanel();
  }

  public void reportNewCurrentRobot(Robot paramRobot)
  {
    if (paramRobot == null)
    {
      this.RosterMenu.deselect();

      return;
    }

    if (this.RosterMenu.getSelection() != paramRobot)
    {
      this.RosterMenu.deselect();
      this.RosterMenu.selectByLogical(paramRobot);
    }
  }

  public void populateRosterPanel()
  {
    Object localObject;
    String str;
    TextContainer localTextContainer;
    Enumeration localEnumeration = this.state.getAllRobots().elements();
    OrderedTable localOrderedTable = new OrderedTable();
    while (localEnumeration.hasMoreElements())
    {
      localObject = (Robot)localEnumeration.nextElement();
      str = ((Robot)localObject).toString();
      str = str.substring(0, str.indexOf(10));
      str = str + ((this.state.isRobotActive((Robot)localObject)) ? " - Active" : " - Stored");
      localTextContainer = new TextContainer(str, TEXTCOLOUR, TEXTFONT);
      localTextContainer.setPadding(new Insets(0, 0, 0, this.RosterMenu.getSize().width - localTextContainer.getSize().width));
      localOrderedTable.put(localTextContainer, localObject);
    }
    localEnumeration = this.state.getInventory().elements();
    while (localEnumeration.hasMoreElements())
    {
      localObject = (PhysicalObject)localEnumeration.nextElement();
      str = ((PhysicalObject)localObject).toString();

      localTextContainer = new TextContainer(str, TEXTCOLOUR, TEXTFONT);
      localTextContainer.setPadding(new Insets(0, 0, 0, this.RosterMenu.getSize().width - localTextContainer.getSize().width));
      localOrderedTable.put(localTextContainer, localObject);
    }
    this.RosterMenu.setContents(localOrderedTable);
  }

  public void populateStatPanel() {
    if (this.RosterMenu.getSelection() instanceof Robot)
    {
      Robot localRobot = (Robot)this.RosterMenu.getSelection();
      if (localRobot != null)
      {
        OrderedTable localOrderedTable = new OrderedTable();
        TextContainer localTextContainer = localRobot.getDetailContainer();
        localTextContainer.setHardWidth(this.StatMenu.getSize().width);
        localTextContainer.setColor(TEXTCOLOUR);
        localTextContainer.setFont(TEXTFONT);
        localOrderedTable.put(localRobot.getDetailContainer(), "");
        this.StatMenu.setContents(localOrderedTable);
        this.StatMenu.makeVisible(localTextContainer);
      }
    }
  }

  public void addRosterListener(ActionListener paramActionListener) {
    if (!(this.listeners.contains(paramActionListener)))
      this.listeners.addElement(paramActionListener);
  }

  public void removeRosterListener(ActionListener paramActionListener) {
    if (this.listeners.contains(paramActionListener))
      this.listeners.removeElement(paramActionListener);  }

  public int getPanelState() {
    return this.PanelState; } 
  public void setGameState(GameState paramGameState) { this.state = paramGameState;
  }

  public synchronized void addStatusMessage(String paramString) {
    setMinimized(false);
    GameApplet.audio.play("ButtonError");

    if (!(this.MessagesPanel.getAdded()))
    {
      super.remove(0);
      add(this.MessagesPanel, 0);
      repaint();
    }

    this.MessagesPanel.addMessage(paramString);
  }
}