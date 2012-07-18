package com.templar.games.stormrunner.build;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.StatusPanel;
import com.templar.games.stormrunner.assembly.Assembly;
import com.templar.games.stormrunner.chassis.Chassis;
import com.templar.games.stormrunner.sensor.Sensor;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ControllableBufferContainer;
import com.templar.games.stormrunner.templarutil.gui.DigitalGauge;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageEvent;
import com.templar.games.stormrunner.templarutil.gui.ImageListener;
import com.templar.games.stormrunner.templarutil.gui.ScrollMenu;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.gui.TextInputContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

public class BuildPanel extends ControllableBufferContainer
  implements ActionListener, FocusListener, ImageListener
{
  public static final int RCX_POLYMETALS = 20;
  public static final int RCX_ENERGY_UNITS = 45;
  public static final String ROBOT_PART_IMAGES_PATH = "com/templar/games/stormrunner/media/images/build/robotparts/";
  public static final Font SMALL_FONT = new Font("SansSerif", 0, 9);
  public static final Font LARGE_FONT = new Font("SansSerif", 0, 18);
  public static final Color TEXT_COLOR = new Color(255, 255, 0);
  public static final int NUMERICAL_CHANGE_STEPS = 10;
  public static final int NUMERICAL_CHANGE_STEP_LENGTH = 50;
  protected ImageRetriever ir;
  protected GameApplet applet;
  protected GameState state;
  protected CargoBay bay;
  protected boolean SequenceComplete = true;
  protected String LastActionCommand;
  protected Robot RobotOnRamp;
  protected boolean BayOccupied = false;
  protected boolean FrontArmEngaged = false;
  protected boolean RampUp = false;
  protected int SensorSlots;
  protected int PolymetalChange;
  protected int EnergyUnitChange;
  protected String ChassisFront;
  protected String ChassisRear;
  protected String SensorChange;
  protected int SensorRemoved;
  protected RobotPart[] SensorList = new RobotPart[3];
  protected RobotPart RearAssembly;
  protected RobotPart TopAssembly;
  protected RobotPart FrontAssembly;
  protected boolean Retrieving = false;
  protected boolean Naming = false;
  protected int PolymetalCount;
  protected int NewPolymetalCount;
  protected int PolymetalInterval;
  protected int PolymetalStep;
  protected int EnergyUnitCount;
  protected int NewEnergyUnitCount;
  protected int EnergyUnitStep;
  protected int EnergyUnitInterval;
  protected UtilityThread ScrollThread;
  protected ImageComponent Background;
  protected ImageButton LeftArrow;
  protected ImageButton RightArrow;
  protected ImageButton Chassis;
  protected ImageButton Dismantle;
  protected ImageButton Engage;
  protected ImageButton NewRCX;
  protected ImageButton Retrieve;
  protected ImageButton Sensors;
  protected ImageButton Store;
  protected ImageButton Tools;
  protected DigitalGauge WeightGauge;
  protected TextContainer Operator;
  protected TextContainer Rank;
  protected TextContainer SecurityLevel;
  protected TextContainer RCXName;
  protected TextContainer Polymetals;
  protected TextContainer EnergyUnits;
  protected TextContainer Description;
  protected TextInputContainer RobotNamer;
  protected ScrollMenu PartsList;
  protected Component InternalFocus;

  public BuildPanel(ImageRetriever paramImageRetriever, CargoBay paramCargoBay, GameApplet paramGameApplet)
  {
    this.ir = paramImageRetriever;
    this.bay = paramCargoBay;
    this.applet = paramGameApplet;
    this.state = paramGameApplet.getGameState();

    setLayout(null);

    addFocusListener(this);

    this.Background = new ImageComponent(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_panel.gif"), true, false);
    this.Background.setLocation(0, 0);
    this.LeftArrow = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwleft-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwleft-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwleft-lit.gif"));
    this.LeftArrow.setLocation(23, 253);
    this.LeftArrow.setActionCommand("scroll left");
    this.LeftArrow.setVerboseEvents(true);
    this.LeftArrow.addActionListener(this);
    this.LeftArrow.addImageListener(this);
    this.RightArrow = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwright-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwright-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_arrwright-lit.gif"));
    this.RightArrow.setLocation(119, 253);
    this.RightArrow.setActionCommand("scroll right");
    this.RightArrow.setVerboseEvents(true);
    this.RightArrow.addActionListener(this);
    this.RightArrow.addImageListener(this);
    this.Chassis = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_chassis-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_chassis-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_chassis-lit.gif"));
    this.Chassis.setLocation(19, 265);
    this.Chassis.setActionCommand("chassis");
    this.Chassis.addActionListener(this);
    this.Chassis.addImageListener(this);
    this.Dismantle = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_dismantle-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_dismantle-in.gif"), null);
    this.Dismantle.setLocation(9, 101);
    this.Dismantle.setActionCommand("dismantle");
    this.Dismantle.addActionListener(this);
    this.Dismantle.addImageListener(this);
    this.Engage = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_engage-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_engage-in.gif"), null);
    this.Engage.setLocation(9, 120);
    this.Engage.setActionCommand("engage");
    this.Engage.addActionListener(this);
    this.Engage.addImageListener(this);
    this.NewRCX = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_newrcx-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_newrcx-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_newrcx-lit.gif"));
    this.NewRCX.setLocation(9, 317);
    this.NewRCX.setActionCommand("new rcx");
    this.NewRCX.addActionListener(this);
    this.NewRCX.addImageListener(this);
    this.Retrieve = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_retrieve-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_retrieve-in.gif"), null);
    this.Retrieve.setLocation(9, 83);
    this.Retrieve.setActionCommand("retrieve");
    this.Retrieve.addActionListener(this);
    this.Retrieve.addImageListener(this);
    this.Sensors = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_sensors-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_sensors-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_sensors-lit.gif"));
    this.Sensors.setLocation(125, 265);
    this.Sensors.setActionCommand("sensors");
    this.Sensors.addActionListener(this);
    this.Sensors.addImageListener(this);
    this.Store = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_store-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_store-in.gif"), null);
    this.Store.setLocation(9, 65);
    this.Store.setActionCommand("store");
    this.Store.addActionListener(this);
    this.Store.addImageListener(this);
    this.Tools = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_tools-off.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_tools-in.gif"), paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/build/interface/bpanel_tools-lit.gif"));
    this.Tools.setLocation(71, 265);
    this.Tools.setActionCommand("tools");
    this.Tools.addActionListener(this);
    this.Tools.addImageListener(this);

    this.Operator = new TextContainer(this.state.getUsername(), TEXT_COLOR, SMALL_FONT);
    this.Operator.setLocation(73, 7);
    this.Operator.addImageListener(this);
    this.Rank = new TextContainer(this.state.getUserRank(), TEXT_COLOR, SMALL_FONT);
    this.Rank.setLocation(73, 17);
    this.Rank.addImageListener(this);
    this.SecurityLevel = new TextContainer(String.valueOf(this.state.getSecurityLevel()), TEXT_COLOR, SMALL_FONT);
    this.SecurityLevel.setLocation(157, 29);
    this.SecurityLevel.addImageListener(this);
    this.RCXName = new TextContainer("", TEXT_COLOR, SMALL_FONT);
    this.RCXName.setLocation(88, 47);
    this.RCXName.addImageListener(this);
    this.Polymetals = new TextContainer(String.valueOf(this.state.getPolymetals()), TEXT_COLOR, LARGE_FONT);
    this.Polymetals.setLocation(101, 187);
    this.Polymetals.addImageListener(this);
    this.EnergyUnits = new TextContainer(String.valueOf(this.state.getEnergyUnits()), TEXT_COLOR, LARGE_FONT);
    this.EnergyUnits.setLocation(140, 187);
    this.EnergyUnits.addImageListener(this);
    this.Description = new TextContainer("", TEXT_COLOR, SMALL_FONT);
    this.Description.setLocation(85, 70);
    this.Description.setHardSize(89, 86);
    this.Description.setPadding(new Insets(1, 1, 1, 1));
    this.Description.setStreak(true, 15, 7, 50);
    this.Description.addImageListener(this);

    this.RobotNamer = new TextInputContainer("", TEXT_COLOR, TEXT_COLOR, SMALL_FONT);
    this.RobotNamer.setLocation(82, 44);
    this.RobotNamer.setMaxResponseLength(20);
    this.RobotNamer.addActionListener(this);
    this.RobotNamer.addImageListener(this);

    Color[] arrayOfColor = { Color.green, Color.green, Color.green, Color.green, Color.green, Color.green, Color.green, Color.yellow, Color.yellow, Color.red };
    this.WeightGauge = new DigitalGauge(0D, 10.0D, 10, arrayOfColor);
    this.WeightGauge.setBoundingAngles(190, 350);
    this.WeightGauge.setBackground(Color.black);
    this.WeightGauge.setBounds(16, 142, 55, 55);
    this.WeightGauge.addImageListener(this);

    this.PartsList = new ScrollMenu(true);
    this.PartsList.setBounds(25, 220, 138, 28);
    this.PartsList.setBorderPadding(new Insets(3, 3, 3, 3));
    this.PartsList.setItemGap(3);
    this.PartsList.addImageListener(this);

    add(this.Operator);
    add(this.Rank);
    add(this.SecurityLevel);
    add(this.RCXName);
    add(this.Polymetals);
    add(this.EnergyUnits);
    add(this.Description);
    add(this.LeftArrow);
    add(this.RightArrow);
    add(this.Chassis);
    add(this.Dismantle);
    add(this.Engage);
    add(this.NewRCX);
    add(this.Retrieve);
    add(this.Sensors);
    add(this.Store);
    add(this.Tools);
    add(this.PartsList);
    add(this.Background);
    add(this.WeightGauge);

    setSize(this.Background.getSize());

    this.PolymetalCount = this.state.getPolymetals();
    this.EnergyUnitCount = this.state.getEnergyUnits();
  }

  public void setUsername(String paramString)
  {
    this.Operator.setText(paramString);
  }

  public void setUserRank(String paramString)
  {
    this.Rank.setText(paramString);
  }

  public void setSecurityLevel(int paramInt)
  {
    this.SecurityLevel.setText(String.valueOf(paramInt));
  }

  public void setPolymetals(int paramInt)
  {
    this.NewPolymetalCount = Math.min(paramInt, 999);
    this.PolymetalStep = 0;
    this.PolymetalInterval = ((this.NewPolymetalCount - this.PolymetalCount) / 10);
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(50, this, getClass().getMethod("setPolymetalsStep", null), false);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public boolean setPolymetalsStep()
  {
    this.PolymetalStep += 1;

    if (this.PolymetalStep < 10)
    {
      int i = this.PolymetalCount + this.PolymetalInterval * this.PolymetalStep;

      this.Polymetals.setText(String.valueOf(i));

      return true;
    }

    this.Polymetals.setText(String.valueOf(this.NewPolymetalCount));

    this.PolymetalCount = this.NewPolymetalCount;

    return false;
  }

  public void setEnergyUnits(int paramInt)
  {
    this.NewEnergyUnitCount = Math.min(paramInt, 999);
    this.EnergyUnitStep = 0;
    this.EnergyUnitInterval = ((this.NewEnergyUnitCount - this.EnergyUnitCount) / 10);
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(50, this, getClass().getMethod("setEnergyUnitsStep", null), false);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public boolean setEnergyUnitsStep()
  {
    this.EnergyUnitStep += 1;

    if (this.EnergyUnitStep < 10)
    {
      int i = this.EnergyUnitCount + this.EnergyUnitInterval * this.EnergyUnitStep;

      this.EnergyUnits.setText(String.valueOf(i));

      return true;
    }

    this.EnergyUnits.setText(String.valueOf(this.NewEnergyUnitCount));

    this.EnergyUnitCount = this.NewEnergyUnitCount;

    return false;
  }

  public void setDescription(String paramString)
  {
    this.Description.setText(paramString);
  }

  public void clearPartsList()
  {
    this.PartsList.removeAll();
  }

  protected void error()
  {
    GameApplet.audio.play("ButtonError");
  }

  protected void error(String paramString)
  {
    error();

    this.Description.setText(paramString);
  }

  protected double currentRobotWeight()
  {
    if (this.RobotOnRamp != null)
    {
      RobotPart[] arrayOfRobotPart = this.RobotOnRamp.getRobotParts();
      double d = 0D;
      for (int i = 0; i < arrayOfRobotPart.length; ++i)
        if (!(arrayOfRobotPart[i] instanceof Chassis))
          d += arrayOfRobotPart[i].getWeight();

      return d;
    }

    return 0D;
  }

  protected boolean checkWeightCapacity(double paramDouble)
  {
    double d = currentRobotWeight();

    return (d <= paramDouble);
  }

  protected boolean checkWeight(double paramDouble)
  {
    Chassis localChassis = this.RobotOnRamp.getChassis();
    if (localChassis != null)
    {
      return (localChassis.getWeightCapacity() >= paramDouble + currentRobotWeight());
    }

    return false;
  }

  protected boolean checkTransaction(int paramInt1, int paramInt2)
  {
    int i = this.state.getPolymetals();
    int j = this.state.getEnergyUnits();

    if (i + paramInt1 >= 0)
    {
      return (j + paramInt2 >= 0);
    }

    return false;
  }

  protected boolean transact(int paramInt1, int paramInt2)
  {
    int i = this.state.getPolymetals();
    int j = this.state.getEnergyUnits();

    if (i + paramInt1 >= 0)
    {
      if (j + paramInt2 >= 0)
      {
        this.state.setPolymetals(i + paramInt1);
        this.state.setEnergyUnits(j + paramInt2);
        return true;
      }

      error("Error: Cannot supply " + (paramInt2 * -1) + " Energy Units.");
      return false;
    }

    error("Error: Cannot supply " + (paramInt1 * -1) + " Polymetals.");
    return false;
  }

  protected void populateMenu(String paramString)
  {
    Class localClass1;
    try
    {
      localClass1 = Class.forName(paramString);

      Vector localVector = this.state.getRobotPartPatterns();
      OrderedTable localOrderedTable = new OrderedTable();

      for (int i = 0; i < localVector.size(); ++i)
      {
        RobotPart localRobotPart = (RobotPart)localVector.elementAt(i);
        if (localClass1.isInstance(localRobotPart))
        {
          ImageButton localImageButton = null;
          String str = "com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart.getIconAppearance();
          int j = localRobotPart.getSecurityLevel();

          if (j > this.state.getSecurityLevel())
          {
            localImageButton = new ImageButton(this.ir.getImage(str + "-no.gif"));
            localImageButton.setActionCommand("disabled part");
            localImageButton.addActionListener(this);
          }
          else if (this.RobotOnRamp != null)
          {
            RobotPart[] arrayOfRobotPart = this.RobotOnRamp.getRobotParts();
            Class localClass2 = localRobotPart.getClass();
            int k = 0;
            for (int l = 0; l < arrayOfRobotPart.length; ++l)
            {
              if (localClass2.isInstance(arrayOfRobotPart[l]))
              {
                localImageButton = new ImageButton(this.ir.getImage(str + "-on.gif"));

                localImageButton.setActionCommand("installed part");
                localImageButton.addActionListener(this);
                localImageButton.setVerboseEvents(true);

                k = 1;
              }
            }

            if (k == 0)
            {
              localImageButton = new ImageButton(this.ir.getImage(str + "-ok.gif"), null, this.ir.getImage(str + "-ro.gif"));

              localImageButton.setActionCommand("available part");
              localImageButton.addActionListener(this);
              localImageButton.setVerboseEvents(true);
            }

          }
          else
          {
            localImageButton = new ImageButton(this.ir.getImage(str + "-ok.gif"), null, this.ir.getImage(str + "-ro.gif"));

            localImageButton.setActionCommand("available part");
            localImageButton.addActionListener(this);
            localImageButton.setVerboseEvents(true);
          }

          localImageButton.addImageListener(this);

          localOrderedTable.put(localImageButton, localRobotPart);
        }

      }

      this.PartsList.resetScroll();

      this.PartsList.setContents(localOrderedTable);

      this.PartsList.repaint();

      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
  }

  protected void clearRamp()
  {
    this.BayOccupied = false;
    this.RobotOnRamp = null;
    this.RampUp = false;
    this.RCXName.setText("");
    this.bay.clearRamp();
    this.PartsList.setContents(null);
    this.WeightGauge.setValue(0D);
    this.SensorList = new RobotPart[3];
    this.SensorSlots = 0;
    this.RearAssembly = null;
    this.TopAssembly = null;
    this.FrontAssembly = null;
  }

  protected void storeRobot()
  {
    this.SequenceComplete = false;

    this.bay.start("LowerRCX", this);

    this.state.addRobot(this.RobotOnRamp);

    this.LastActionCommand = "store robot";
  }

  protected void retrieveRobot(Robot paramRobot)
  {
    this.BayOccupied = true;
    this.RobotOnRamp = paramRobot;
    this.RampUp = true;
    this.RCXName.setText(paramRobot.getName());

    this.PartsList.setContents(null);

    this.state.removeRobot(paramRobot);

    this.WeightGauge.setMax(paramRobot.getChassis().getWeightCapacity());
    this.WeightGauge.setValue(currentRobotWeight());
    try
    {
      Class localClass1 = Class.forName("com.templar.games.stormrunner.sensor.Sensor");
      Class localClass2 = Class.forName("com.templar.games.stormrunner.chassis.Chassis");
      Class localClass3 = Class.forName("com.templar.games.stormrunner.assembly.Assembly");

      RobotPart[] arrayOfRobotPart = paramRobot.getRobotParts();
      for (int i = 0; i < arrayOfRobotPart.length; ++i)
      {
        RobotPart localRobotPart = arrayOfRobotPart[i];
        if (localClass1.isInstance(localRobotPart))
        {
          this.SensorList[this.SensorSlots] = localRobotPart;
          this.SensorSlots += 1;
          this.bay.setSensorImage(this.SensorSlots, localRobotPart.getID());
        }
        else if (localClass2.isInstance(localRobotPart))
        {
          this.bay.setChassisImages(localRobotPart.getID() + "Front", localRobotPart.getID() + "Rear");
        }
        else if (localClass3.isInstance(localRobotPart))
        {
          Assembly localAssembly = (Assembly)localRobotPart;
          if (localAssembly.getPlacement() == 1)
          {
            this.bay.setRearAssemblyImage(localAssembly.getID());
            this.RearAssembly = localAssembly;
          }
          else if (localAssembly.getPlacement() == 4)
          {
            this.bay.setTopAssemblyImage(localAssembly.getID());
            this.TopAssembly = localAssembly;
          }
          else if (localAssembly.getPlacement() == 2)
          {
            this.bay.setFrontAssemblyImage(localAssembly.getID());
            this.FrontAssembly = localAssembly;
          }
        }
      }

      this.Retrieving = false;

      this.SequenceComplete = false;

      this.bay.start("RaiseRCX", this);

      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
  }

  protected void engageRobot()
  {
    boolean bool = this.state.activateRobot(this.RobotOnRamp);

    if (bool)
    {
      this.applet.setState(1);

      GameApplet.audio.play("RobotStart");

      clearRamp();

      return;
    }

    error("Error: Exit ramp obstructed. You must move any obstacles blocking the Bay exit.");
  }

  public synchronized void actionPerformed(ActionEvent paramActionEvent)
  {
    String str;
    try
    {
      Object localObject2;
      int l;
      str = paramActionEvent.getActionCommand();
      Object localObject1 = paramActionEvent.getSource();

      if (this.Retrieving)
      {
        if (str.compareTo("Roster Minimized") == 0)
        {
          this.Retrieving = false;

          this.Description.setText("Retrieval canceled.");

          return;
        }

        if (str.compareTo("Roster Selected") != 0)
          break label3517;

        localObject2 = this.state.getCurrentRobot();

        if (this.state.isRobotStored((Robot)localObject2))
        {
          this.Retrieving = false;

          this.applet.getStatusPanel().setMinimized(true);

          this.Description.setText("");

          retrieveRobot((Robot)localObject2);

          return;
        }

        error("Error: Robot is not in the storage area.");

        return;
      }

      if (localObject1 == this.RobotNamer)
      {
        if (str.length() == 0)
        {
          this.Description.setText("RCX must be named properly.");

          return;
        }

        localObject2 = this.state.getAllRobots();
        int j = 0;
        for (l = 0; l < ((Vector)localObject2).size(); ++l)
          if (((Robot)((Vector)localObject2).elementAt(l)).getName().compareTo(str) == 0)
            j = 1;

        if (j != 0)
        {
          error("Error: A robot named " + str + " already exists.");

          return;
        }

        this.RobotOnRamp.setName(str);
        this.RCXName.setText(str);

        remove(this.RobotNamer);
        add(this.RCXName, 0);
        this.Description.setText("");

        setInternalFocus(null);

        this.Naming = false;

        if (this.LastActionCommand.compareTo("naming for engage") == 0)
        {
          engageRobot();
        }
        if (this.LastActionCommand.compareTo("naming for storage") != 0)
          break label3517;
        storeRobot();

        return;
      }

      if (this.Naming) { return;
      }

      if (str.startsWith("I:"))
      {
        if ((localObject1 == this.LeftArrow) || (localObject1 == this.RightArrow))
          break label3517;
        localObject2 = (ImageButton)localObject1;
        RobotPart localRobotPart1 = (RobotPart)this.PartsList.getContents().get(localObject2);

        this.Description.setText(localRobotPart1.getDescription());

        return;
      }

      if (str.startsWith("O:"))
      {
        if ((localObject1 == this.LeftArrow) || (localObject1 == this.RightArrow))
          break label3517;
        this.Description.setText("");

        return;
      }

      if (str.compareTo("P:scroll left") == 0)
      {
        this.ScrollThread = new UtilityThread(50, this.PartsList, this.PartsList.getClass().getMethod("stepLeft", null), false);
        this.ScrollThread.start();

        return;
      }

      if (str.compareTo("R:scroll left") == 0)
      {
        if (this.ScrollThread == null) break label3517;
        this.ScrollThread.politeStop();

        return;
      }

      if (str.compareTo("P:scroll right") == 0)
      {
        this.ScrollThread = new UtilityThread(50, this.PartsList, this.PartsList.getClass().getMethod("stepRight", null), false);
        this.ScrollThread.start();

        return;
      }

      if (str.compareTo("R:scroll right") == 0)
      {
        if (this.ScrollThread == null) break label3517;
        this.ScrollThread.politeStop();

        return;
      }

      if (str.startsWith("R:")) { return;
      }

      if (str.compareTo("Sequence Complete.") == 0)
      {
        int i = 0;

        if (this.LastActionCommand != null)
        {
          int k;
          if (this.LastActionCommand.compareTo("dismantle") == 0)
          {
            k = 20;
            l = 45;
            localObject4 = this.RobotOnRamp.getRobotParts();
            for (int i1 = 0; i1 < localObject4.length; ++i1)
            {
              l += localObject4[i1].getEnergyCost();
              k += localObject4[i1].getSalvageCost();
            }
            transact(k, l);

            this.bay.setChassisImages(null, null);
            this.bay.setSensorImages(null, null, null);

            clearRamp();
          }
          else if (this.LastActionCommand.compareTo("store robot") == 0)
          {
            clearRamp();
          }
          else if (this.LastActionCommand.compareTo("changed chassis") == 0)
          {
            if ((this.PolymetalChange != 0) || (this.EnergyUnitChange != 0))
            {
              transact(this.PolymetalChange, this.EnergyUnitChange);
              this.PolymetalChange = 0;
              this.EnergyUnitChange = 0;
            }

            if (this.RobotOnRamp.getChassis() != null)
            {
              this.bay.setChassisImages(this.ChassisFront, this.ChassisRear);

              this.bay.start("AddChassis", this);

              this.FrontArmEngaged = false;

              i = 1;

              this.RampUp = true;
            }
            else
            {
              this.FrontArmEngaged = true;

              this.RampUp = false;
            }
          }
          else if (this.LastActionCommand.compareTo("changed sensor") == 0)
          {
            if (this.PolymetalChange < 0)
              k = 1;
            else {
              k = 0;
            }

            if ((this.PolymetalChange != 0) || (this.EnergyUnitChange != 0))
            {
              transact(this.PolymetalChange, this.EnergyUnitChange);
              this.PolymetalChange = 0;
              this.EnergyUnitChange = 0;
            }

            if (k != 0)
              this.bay.setSensorImage(this.SensorSlots, this.SensorChange);
            else {
              this.bay.setSensorImage(this.SensorRemoved, null);
            }

            this.bay.start("RetractSensorArm", this);

            i = 1;
          }
          else if (this.LastActionCommand.startsWith("changed assembly"))
          {
            if (this.PolymetalChange < 0)
              k = 1;
            else {
              k = 0;
            }

            if ((this.PolymetalChange != 0) || (this.EnergyUnitChange != 0))
            {
              transact(this.PolymetalChange, this.EnergyUnitChange);
              this.PolymetalChange = 0;
              this.EnergyUnitChange = 0;
            }

            if (k != 0)
            {
              if (this.LastActionCommand.endsWith("rear"))
                this.bay.start("RaiseBackArm", this);
              else if (this.LastActionCommand.endsWith("top"))
                this.bay.start("RaiseTopArm", this);
              else if (this.LastActionCommand.endsWith("front")) {
                this.bay.start("RetractFrontAssemblyArm", this);
              }

            }
            else if (this.LastActionCommand.endsWith("rear"))
              this.bay.start("RaiseBackAssembly", this);
            else if (this.LastActionCommand.endsWith("top"))
              this.bay.start("RaiseTopAssembly", this);
            else if (this.LastActionCommand.endsWith("front")) {
              this.bay.start("RetractFrontAssembly", this);
            }

            i = 1;
          }

          this.LastActionCommand = null;
        }

        if ((this.RobotOnRamp != null) && (this.RobotOnRamp.getChassis() != null))
          this.WeightGauge.setValue(currentRobotWeight());

        if (i != 0) break label3517;
        this.SequenceComplete = true;

        return;
      }

      if (!(this.SequenceComplete))
      {
        error("Error: Bay in use.");

        return;
      }

      if (str.compareTo("new rcx") == 0)
      {
        if (this.BayOccupied)
        {
          error("Error: Bay occupied.");

          return;
        }

        if (!(transact(-20, -45)))
          break label3517;

        this.SequenceComplete = false;
        this.bay.start("NewRCX", this);

        this.BayOccupied = true;

        this.FrontArmEngaged = true;

        this.RobotOnRamp = new Robot(this.ir);

        return;
      }

      if (str.compareTo("tools") == 0)
      {
        populateMenu("com.templar.games.stormrunner.assembly.Assembly");

        return;
      }

      if (str.compareTo("chassis") == 0)
      {
        populateMenu("com.templar.games.stormrunner.chassis.Chassis");

        return;
      }

      if (str.compareTo("sensors") == 0)
      {
        populateMenu("com.templar.games.stormrunner.sensor.Sensor");

        return;
      }

      if (str.compareTo("store") == 0)
      {
        if (this.BayOccupied)
        {
          if (this.RobotOnRamp.getChassis() != null)
          {
            if (this.RobotOnRamp.getName().compareTo("[UNNAMED]") == 0)
            {
              this.Naming = true;

              remove(this.RCXName);
              add(this.RobotNamer, 0);

              setInternalFocus(this.RobotNamer);

              this.Description.setText("Please identify the new RCX.");

              this.LastActionCommand = "naming for storage";

              return;
            }

            storeRobot();

            return;
          }

          error("Error: Robots must have a chassis to enter the storage area.");

          return;
        }

        error("Error: No equipment to store.");

        return;
      }

      if (str.compareTo("retrieve") == 0)
      {
        if (!(this.BayOccupied))
        {
          if (this.state.getStoredRobots().size() > 0)
          {
            this.Retrieving = true;

            this.Description.setText("Select a robot from the Roster below.");

            this.applet.getStatusPanel().setMinimized(false);
            this.applet.getStatusPanel().showRoster();

            this.applet.getStatusPanel().addRosterListener(this);

            return;
          }

          error("Error: No robots to retrieve.");

          return;
        }

        error("Error: Bay is occupied.");

        return;
      }

      if (str.compareTo("dismantle") == 0)
      {
        if (this.BayOccupied)
        {
          this.PartsList.setContents(null);

          this.SequenceComplete = false;

          if (this.FrontArmEngaged)
            this.bay.start("DismantleSuspendedRCX", this);
          else {
            this.bay.start("DismantleRCX", this);
          }

          this.LastActionCommand = str;

          return;
        }

        error("Error: No equipment to dismantle.");

        return;
      }

      if (str.compareTo("engage") == 0)
      {
        if (this.BayOccupied)
        {
          if (this.RobotOnRamp.getChassis() != null)
          {
            if (this.InternalFocus == null)
            {
              if (this.RobotOnRamp.getName().compareTo("[UNNAMED]") == 0)
              {
                this.Naming = true;

                remove(this.RCXName);
                add(this.RobotNamer, 0);

                setInternalFocus(this.RobotNamer);

                this.Description.setText("Please identify the new RCX.");

                this.LastActionCommand = "naming for engage";

                return;
              }

              engageRobot();

              return;
            }

            System.err.println("Inconsistency: attempting to engage, InternalFocus not null.");

            return;
          }

          error("Error: RCX cannot be engaged without a chassis.");

          return;
        }

        error("Error: No RCX to engage.");

        return;
      }

      if (str.compareTo("disabled part") == 0)
      {
        error("Error: Insufficient security level.");

        return;
      }

      if (str.compareTo("P:available part") == 0)
      {
        if (this.RobotOnRamp != null)
        {
          ImageButton localImageButton1;
          Object localObject5;
          localOrderedTable = this.PartsList.getContents();
          localRobotPart2 = (RobotPart)localOrderedTable.get(paramActionEvent.getSource());
          localObject3 = localRobotPart2.getClass();
          localObject4 = (RobotPart)((Class)localObject3).newInstance();

          localClass1 = Class.forName("com.templar.games.stormrunner.sensor.Sensor");
          localClass2 = Class.forName("com.templar.games.stormrunner.chassis.Chassis");
          Class localClass3 = Class.forName("com.templar.games.stormrunner.assembly.Assembly");

          Chassis localChassis = this.RobotOnRamp.getChassis();
          if ((localClass2.isInstance(localObject4)) && (localChassis != null))
          {
            this.PolymetalChange = (localChassis.getSalvageCost() - ((RobotPart)localObject4).getSalvageCost());
            this.EnergyUnitChange = (localChassis.getEnergyCost() - ((RobotPart)localObject4).getEnergyCost());
          }
          else
          {
            this.PolymetalChange = (-((RobotPart)localObject4).getSalvageCost());
            this.EnergyUnitChange = (-((RobotPart)localObject4).getEnergyCost());
          }

          if (!(checkTransaction(this.PolymetalChange, this.EnergyUnitChange)))
            break label2999;

          if (localClass2.isInstance(localObject4))
          {
            if (checkWeightCapacity(((Chassis)localObject4).getWeightCapacity()))
            {
              localImageButton1 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
              localImageButton1.setImage(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-on.gif"));
              localImageButton1.setActionCommand("installed part");

              localObject5 = ((RobotPart)localObject4).getID();
              this.ChassisFront = localObject5 + "Front";
              this.ChassisRear = localObject5 + "Rear";

              this.SequenceComplete = false;

              if (localChassis != null)
              {
                Enumeration localEnumeration = localOrderedTable.elements();
                RobotPart localRobotPart3 = (RobotPart)localEnumeration.nextElement();
                while ((localEnumeration.hasMoreElements()) && (localRobotPart3.getID().compareTo(localChassis.getID()) != 0))
                {
                  localRobotPart3 = (RobotPart)localEnumeration.nextElement();
                }
                ImageButton localImageButton2 = (ImageButton)localOrderedTable.getKey(localRobotPart3);
                localImageButton2.setImages(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localChassis.getIconAppearance() + "-ok.gif"), null, this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localChassis.getIconAppearance() + "-ro.gif"));
                localImageButton2.setActionCommand("available part");
              }

              this.RobotOnRamp.setChassis((Chassis)localObject4);

              this.WeightGauge.setMax(((Chassis)localObject4).getWeightCapacity());

              this.LastActionCommand = "changed chassis";

              if (!(this.FrontArmEngaged)) {
                this.bay.start("RemoveChassis", this);

                return;
              }

              actionPerformed(new ActionEvent(this, 1001, "Sequence Complete."));

              return;
            }

            error("Error: This chassis cannot support the weight of the components attached to the RCX. Please choose a chassis with a higher capacity or remove RCX components.");

            return;
          }

          if (localClass1.isInstance(localObject4))
          {
            if (this.SensorSlots >= 3)
            {
              error("Error: RCX can only hold 3 sensors.");

              return;
            }

            if ((localChassis == null) || (checkWeight(((RobotPart)localObject4).getWeight())))
            {
              this.SequenceComplete = false;

              localImageButton1 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
              localImageButton1.setImage(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-on.gif"));
              localImageButton1.setActionCommand("installed part");

              this.RobotOnRamp.addSensor((Sensor)localObject4);

              this.SensorChange = ((RobotPart)localObject4).getID();

              this.SensorList[this.SensorSlots] = localRobotPart2;
              this.SensorSlots += 1;

              this.LastActionCommand = "changed sensor";

              this.bay.start("ExtendSensorArm", this);

              return;
            }

            error("Error: The current chassis cannot support the weight of this component.");

            return;
          }

          if (!(localClass3.isInstance(localObject4)))
            break label2999;

          if ((localChassis == null) || (checkWeight(((RobotPart)localObject4).getWeight())))
          {
            int i4 = ((Assembly)localObject4).getPlacement();
            if (i4 == 2)
            {
              if (this.FrontAssembly != null)
              {
                error("Error: You must remove the " + this.FrontAssembly.getID() + " in order to instll this component.");
              }
              else
              {
                this.SequenceComplete = false;
                this.FrontAssembly = ((RobotPart)localObject4);

                localObject5 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
                ((ImageButton)localObject5).setImage(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-on.gif"));
                ((ImageButton)localObject5).setActionCommand("installed part");

                this.RobotOnRamp.addAssembly((Assembly)localObject4);

                this.LastActionCommand = "changed assembly: front";

                this.bay.resetFrontAssemblyLayers();
                this.bay.setFrontAssemblyImage(((RobotPart)localObject4).getID());
                this.bay.start("ExtendFrontAssembly", this);
              }
            }
            if (i4 == 1)
            {
              if (this.RearAssembly != null)
              {
                error("Error: You must remove the " + this.RearAssembly.getID() + " in order to install this component.");

                return;
              }

              this.SequenceComplete = false;
              this.RearAssembly = ((RobotPart)localObject4);

              localObject5 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
              ((ImageButton)localObject5).setImage(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-on.gif"));
              ((ImageButton)localObject5).setActionCommand("installed part");

              this.RobotOnRamp.addAssembly((Assembly)localObject4);

              this.LastActionCommand = "changed assembly: rear";

              this.bay.resetRearAssemblyLayers();
              this.bay.setRearAssemblyImage(((RobotPart)localObject4).getID());
              this.bay.start("LowerBackAssembly", this);

              return;
            }

            if (i4 != 4)
              break label2999;
            if (this.TopAssembly != null)
            {
              error("Error: You must remove the " + this.TopAssembly.getID() + " in order to install this component.");

              return;
            }

            this.SequenceComplete = false;
            this.TopAssembly = ((RobotPart)localObject4);

            localObject5 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
            ((ImageButton)localObject5).setImage(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-on.gif"));
            ((ImageButton)localObject5).setActionCommand("installed part");

            this.RobotOnRamp.addAssembly((Assembly)localObject4);

            this.LastActionCommand = "changed assembly: top";

            this.bay.resetTopAssemblyLayers();
            this.bay.setTopAssemblyImage(((RobotPart)localObject4).getID());
            this.bay.start("LowerTopAssembly", this);

            return;
          }

          error("Error: The current chassis cannot support the weight of this component.");

          return;
        }

        error("Error: Must create or retrieve an RCX to install components.");

        label2999: return;
      }

      if (str.compareTo("P:installed part") != 0) { return;
      }

      OrderedTable localOrderedTable = this.PartsList.getContents();
      RobotPart localRobotPart2 = (RobotPart)localOrderedTable.get(paramActionEvent.getSource());

      this.PolymetalChange = localRobotPart2.getSalvageCost();
      this.EnergyUnitChange = localRobotPart2.getEnergyCost();

      Object localObject3 = (ImageButton)localOrderedTable.getKey(localRobotPart2);
      ((ImageButton)localObject3).setImages(this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-ok.gif"), null, this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/" + localRobotPart2.getIconAppearance() + "-ro.gif"));
      ((ImageButton)localObject3).setActionCommand("available part");

      Object localObject4 = Class.forName("com.templar.games.stormrunner.chassis.Chassis");
      Class localClass1 = Class.forName("com.templar.games.stormrunner.sensor.Sensor");
      Class localClass2 = Class.forName("com.templar.games.stormrunner.assembly.Assembly");
      if (((Class)localObject4).isInstance(localRobotPart2))
      {
        this.ChassisFront = null;
        this.ChassisRear = null;

        this.SequenceComplete = false;

        this.bay.start("RemoveChassis", this);

        this.RobotOnRamp.setChassis(null);

        this.LastActionCommand = "changed chassis";

        return;
      }

      if (localClass1.isInstance(localRobotPart2))
      {
        this.SequenceComplete = false;

        this.RobotOnRamp.removeSensor(((Sensor)localRobotPart2).getID());

        this.SensorChange = null;

        this.SensorSlots -= 1;

        for (int i2 = 0; i2 <= this.SensorSlots; ++i2)
          if (this.SensorList[i2].getID().compareTo(localRobotPart2.getID()) == 0)
            this.SensorRemoved = (i2 + 1);


        for (int i3 = this.SensorRemoved - 1; i3 < this.SensorList.length; ++i3)
          if (i3 + 1 < this.SensorList.length)
            this.SensorList[i3] = this.SensorList[(i3 + 1)];
          else
            this.SensorList[i3] = null;


        this.LastActionCommand = "changed sensor";

        this.bay.start("ExtendSensorArm", this);

        return;
      }

      if (!(localClass2.isInstance(localRobotPart2))) { return;
      }

      this.SequenceComplete = false;

      this.RobotOnRamp.removeAssembly(((Assembly)localRobotPart2).getID());

      if (((Assembly)localRobotPart2).getPlacement() == 1)
      {
        this.RearAssembly = null;

        this.LastActionCommand = "changed assembly: rear";

        this.bay.start("LowerBackArm", this);

        return;
      }

      if (((Assembly)localRobotPart2).getPlacement() == 4)
      {
        this.TopAssembly = null;

        this.LastActionCommand = "changed assembly: top";

        this.bay.start("LowerTopArm", this);

        return;
      }

      if (((Assembly)localRobotPart2).getPlacement() != 2) return;

      this.FrontAssembly = null;

      this.LastActionCommand = "changed assembly: front";

      this.bay.start("ExtendFrontAssemblyArm", this);

      label3517: return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();

      return;
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();

      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
  }

  public void imageChanged(ImageEvent paramImageEvent)
  {
    Component localComponent = paramImageEvent.getSource();

    if ((localComponent.getParent() != null) && (localComponent.getParent().getParent() == this.PartsList)) {
      super.taintBuffer(this.PartsList.getBounds());

      return;
    }

    super.taintBuffer(paramImageEvent.getSource().getBounds());
  }

  public Component getInternalFocus()
  {
    return this.InternalFocus;
  }

  public void setInternalFocus(Component paramComponent)
  {
    this.InternalFocus = paramComponent;

    if (paramComponent != null) {
      paramComponent.requestFocus();

      return;
    }

    requestFocus();
  }

  public void focusGained(FocusEvent paramFocusEvent)
  {
    if (this.InternalFocus != null)
      this.InternalFocus.requestFocus();
  }

  public void focusLost(FocusEvent paramFocusEvent)
  {
  }

  public void setGameState(GameState paramGameState)
  {
    this.state = paramGameState; }

  public boolean isBayOccupied() { return this.BayOccupied; } 
  public Robot getRobotOnRamp() { return this.RobotOnRamp;
  }
}