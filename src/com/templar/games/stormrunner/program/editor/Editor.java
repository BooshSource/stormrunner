package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Grid;
import com.templar.games.stormrunner.Renderer;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.program.Conditional;
import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.Linkable;
import com.templar.games.stormrunner.program.Loop;
import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.program.Repeat;
import com.templar.games.stormrunner.program.RepeatForever;
import com.templar.games.stormrunner.sensor.Sensor;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.gui.TextInputContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Editor extends SimpleContainer
  implements ActionListener, FocusListener
{
  public static final int PROGRAMAREAWIDTH = 1000;
  public static final int PROGRAMAREAHEIGHT = 1000;
  public static final int WIDTH = 334;
  public static final int HEIGHT = 237;
  public static final int RIGHTINSET = 23;
  public static final int LEFTINSET = 92;
  public static final int TOPINSET = 22;
  public static final int BOTTOMINSET = 9;
  public static final Insets EditorInsets = new Insets(22, 92, 9, 23);
  public static final int TRASHTHRESHOLD = 10;
  public static final int KEYBOARDSCROLLINCREMENT = 20;
  public static final int ANCILLARYDROP = 4;
  public static final Color TEXTDARKCOLOR = new Color(204, 0, 255);
  public static final Color TEXTLIGHTCOLOR = new Color(255, 51, 255);
  public static final Font TEXTFONT = new Font("SansSerif", 0, 9);
  public static final int MINIMIZEDSHOW = 19;
  public static final int COLLAPSESTEPS = 8;
  public static final int MAXIMUM_PROGRAM_NAME_LENGTH = 30;
  protected ImageRetriever theImageRetriever;
  protected Robot CurrentRobot;
  protected Program CurrentProgram;
  protected GameApplet CurrentApplet;
  protected GameState state;
  protected Hashtable ProgramPartImageNames;
  protected Hashtable ProgramPartImages;
  protected Hashtable ProgramPartClassNames;
  protected Hashtable ClassNameProgramPartNames;
  protected DropTargetInfo[] InstructionDrops;
  protected DropTargetInfo[] ConditionalDrops;
  protected DropTargetInfo[] TallStackDrops;
  protected DropTargetInfo[] ShortStackDrops;
  protected DropTargetInfo[] RepeatEndDrops;
  protected Hashtable ParameterDisplayOffsets;
  protected EditorDisplay display;
  protected ProgramContainer programlayer;
  protected KeyHandler kh;
  protected Editor realthis;
  protected ImageButton ControlTab;
  protected TextContainer RobotNameIntro;
  protected TextContainer RobotName;
  protected TextInputContainer ParameterEditor;
  protected EditorPalette Palette;
  protected ProgramComponent CurrentlyEditing;
  protected SaveMenu CurrentlySaving;
  protected LoadMenu CurrentlyLoading;
  protected Component InternalFocus;
  protected Point LocationHolder;
  private int CurrentX;
  private UtilityThread Collapser;

  public Editor(GameState paramGameState, GameApplet paramGameApplet, Robot paramRobot, ImageRetriever paramImageRetriever)
  {
    this.state = paramGameState;
    this.CurrentApplet = paramGameApplet;
    this.theImageRetriever = paramImageRetriever;
    this.realthis = this;

    setLayout(null);

    addFocusListener(this);

    setSize(334, 237);

    enableEvents(16L);

    initProgramComponents();

    this.RobotNameIntro = new TextContainer("PROGRAMMING: ", TEXTDARKCOLOR, TEXTFONT);
    this.RobotName = new TextContainer("[none]", TEXTLIGHTCOLOR, TEXTFONT);
    this.ParameterEditor = new TextInputContainer("", TEXTDARKCOLOR, TEXTLIGHTCOLOR, TEXTFONT);

    setRobot(paramRobot);

    this.kh = new KeyHandler(this);
    addKeyListener(this.kh);

    this.RobotNameIntro.setLocation(95, 8);
    add(this.RobotNameIntro);
    this.RobotName.setLocation(this.RobotNameIntro.getLocation().x + this.RobotNameIntro.getSize().width, 8);
    add(this.RobotName);

    this.ParameterEditor.setLocation(100, 200);
    this.ParameterEditor.setBackgroundColor(Color.black);
    this.ParameterEditor.setPadding(new Insets(4, 4, 4, 4));

    Image localImage = this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_button-lit.gif");
    this.ControlTab = new ImageButton(this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_button-off.gif"), this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_button-in.gif"), localImage);
    this.ControlTab.setLocation(311, 0);
    this.ControlTab.setActionCommand("ToggleVisible");
    this.ControlTab.addActionListener(this);
    this.ControlTab.setOnImage(localImage);
    this.ControlTab.setOn(true);
    this.ControlTab.setClickSound(GameApplet.audio, "ButtonClick");
    add(this.ControlTab);

    ImageComponent localImageComponent = new ImageComponent(this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_right.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(311, 54);
    add(localImageComponent);

    localImageComponent = new ImageComponent(this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_top.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(92, 0);
    add(localImageComponent);

    this.display = new EditorDisplay(this);
    this.display.setSize(getSize().width, getSize().height);
    this.display.setLocation(0, 0);
    add(this.display);

    this.Palette = new EditorPalette(this.display);
    this.Palette.setLocation(0, 0);
    this.Palette.setSize(87, 200);
    add(this.Palette);

    this.programlayer = new ProgramContainer(this);
    this.programlayer.setSize(1000, 1000);
    this.programlayer.setLocation(0, 0);
    add(this.programlayer);

    localImageComponent = new ImageComponent(this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_bottom.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(92, 228);
    add(localImageComponent);

    localImageComponent = new ImageComponent(this.theImageRetriever.getImage("com/templar/games/stormrunner/media/images/programeditor/ppanel_left.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(85, 0);
    add(localImageComponent);
  }

  public void editParameter(String paramString1, ProgramComponent paramProgramComponent, int paramInt, String paramString2)
  {
    if (this.CurrentlyEditing == null)
    {
      this.ParameterEditor.setPrompt(paramString1);
      this.ParameterEditor.setMaxResponseLength(paramInt);
      this.ParameterEditor.setAllowedChars(paramString2);

      this.ParameterEditor.addActionListener(paramProgramComponent);

      add(this.ParameterEditor, 0);

      this.CurrentlyEditing = paramProgramComponent;

      setInternalFocus(this.ParameterEditor);
    }
  }

  public void cancelEditParameter()
  {
    if (this.CurrentlyEditing != null)
    {
      this.ParameterEditor.removeActionListener(this.CurrentlyEditing);

      this.CurrentlyEditing = null;

      this.ParameterEditor.clearResponse();

      remove(this.ParameterEditor);

      setInternalFocus(null);

      repaint();
    }
  }

  public void setCurrentlySaving(SaveMenu paramSaveMenu)
  {
    this.CurrentlySaving = paramSaveMenu;
  }

  public void setCurrentlyLoading(LoadMenu paramLoadMenu)
  {
    this.CurrentlyLoading = paramLoadMenu;
  }

  public void cancelLoadSave()
  {
    if (this.CurrentlyLoading != null)
    {
      this.CurrentlyLoading.remove();
    }
    if (this.CurrentlySaving != null)
    {
      this.CurrentlySaving.remove();
    }
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

  public Program getProgram()
  {
    return this.CurrentProgram;
  }

  public EditorPalette getPalette()
  {
    return this.Palette;
  }

  public void setProgram(Program paramProgram)
  {
    setProgram(paramProgram, true);
  }

  protected void setProgram(Program paramProgram, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      cancelEditParameter();

      cancelLoadSave();
    }

    this.CurrentProgram = paramProgram;

    if (this.programlayer != null)
    {
      setProgramLayer(new ProgramContainer(this));

      if (paramProgram.getFirstInstruction() != null)
        showInstruction(paramProgram.getFirstInstruction(), null);
      if (paramProgram.getFirstConditional() != null)
        showConditional(paramProgram.getFirstConditional(), null);
    }
  }

  public Robot getRobot()
  {
    return this.CurrentRobot;
  }

  public void setRobot(Robot paramRobot)
  {
    this.CurrentRobot = paramRobot;

    if (paramRobot != null)
    {
      this.Palette.refreshBlockLists();

      this.RobotName.setText(paramRobot.getName());

      setProgram(paramRobot.getProgram());

      return;
    }

    setProgram(new Program());

    this.RobotName.setText("[NO ROBOT SELECTED]");
  }

  public EditorDisplay getEditorDisplay()
  {
    return this.display;
  }

  public ProgramContainer getProgramLayer()
  {
    return this.programlayer;
  }

  public void setProgramLayer(ProgramContainer paramProgramContainer)
  {
    Component[] arrayOfComponent = getComponents();
    int i = -1;
    for (int j = 0; (j < arrayOfComponent.length) && (i < 0); ++j)
    {
      if (arrayOfComponent[j] == this.programlayer)
        i = j;

    }

    remove(this.programlayer);

    this.programlayer = paramProgramContainer;
    this.programlayer.setSize(1000, 1000);
    this.programlayer.setLocation(0, 0);
    add(this.programlayer, i);
  }

  public GameState getGameState()
  {
    return this.state;
  }

  protected ProgramComponent showInstruction(Instruction paramInstruction, ProgramComponent paramProgramComponent)
  {
    DropTargetInfo[] arrayOfDropTargetInfo;
    int i;
    Object localObject1;
    if (paramInstruction instanceof Repeat)
      arrayOfDropTargetInfo = this.TallStackDrops;
    else if (paramInstruction instanceof RepeatForever)
      arrayOfDropTargetInfo = this.ShortStackDrops;
    else {
      arrayOfDropTargetInfo = this.InstructionDrops;
    }

    if (paramProgramComponent == null)
    {
      i = 0;
      localObject1 = this.programlayer.getStartBlock();
    }
    else
    {
      localObject2 = paramProgramComponent.getProgramPart();
      localObject1 = paramProgramComponent;

      if (localObject2 instanceof Conditional)
        i = 5;
      else if (localObject2 instanceof Repeat)
        i = 1;
      else if (localObject2 instanceof RepeatForever)
        i = 2;
      else if (localObject2 instanceof RepeatEnd)
        i = 3;
      else
        i = 4;

    }

    Object localObject2 = getProgramComponent(paramInstruction);

    ((ProgramComponent)localObject2).manualAttachPrevious((ProgramComponent)localObject1);

    int j = ((Component)localObject1).getLocation().x + arrayOfDropTargetInfo[i].DropTarget.x;
    int k = ((Component)localObject1).getLocation().y + arrayOfDropTargetInfo[i].DropTarget.y;
    ((ProgramComponent)localObject2).setLocation(j, k);

    this.programlayer.add((Component)localObject2);

    if (paramInstruction instanceof Loop)
    {
      localObject3 = (Loop)paramInstruction;
      Object localObject4 = localObject2;
      ProgramComponent localProgramComponent = ((ProgramComponent)localObject2).getBoundingComponent();

      if (((Loop)localObject3).getDestination() != null)
      {
        ((ProgramComponent)localObject2).next.removeElement(localProgramComponent);
        localObject4 = showInstruction(((Loop)localObject3).getDestination(), (ProgramComponent)localObject2);
        localProgramComponent.manualAttachPrevious((ProgramComponent)localObject4);
      }

      this.programlayer.add(localProgramComponent);
      if (!(localProgramComponent.dropAgainst((ProgramComponent)localObject4)))
        System.out.println("Editor: showInstruction(): Failure to find a DropTarget match while placing a BoundingComponent.");

      localObject2 = localProgramComponent;
    }

    Object localObject3 = paramInstruction.getNextInstruction();

    if (localObject3 != null)
      return showInstruction((Instruction)localObject3, (ProgramComponent)localObject2);

    return ((ProgramComponent)(ProgramComponent)(ProgramComponent)(ProgramComponent)localObject2);
  }

  protected void showConditional(Conditional paramConditional, ProgramComponent paramProgramComponent)
  {
    int i;
    Object localObject;
    if (paramConditional.getPreviousConditional() == null)
    {
      i = 0;
      localObject = this.programlayer.getStartBlock();
    }
    else
    {
      i = 1;
      localObject = paramProgramComponent;
    }

    ProgramComponent localProgramComponent = getProgramComponent(paramConditional);

    if (paramProgramComponent != null)
      localProgramComponent.manualAttachPrevious(paramProgramComponent);
    else {
      localProgramComponent.manualAttachPrevious((ProgramComponent)localObject);
    }

    int j = ((Component)localObject).getLocation().x + this.ConditionalDrops[i].DropTarget.x;
    int k = ((Component)localObject).getLocation().y + this.ConditionalDrops[i].DropTarget.y;
    localProgramComponent.setLocation(j, k);

    this.programlayer.add(localProgramComponent);

    localProgramComponent.addAncillaryImage();

    Instruction localInstruction = paramConditional.getNextInstruction();
    if (localInstruction != null) {
      showInstruction(localInstruction, localProgramComponent);
    }

    Conditional localConditional = paramConditional.getNextConditional();

    if (localConditional != null)
      showConditional(localConditional, localProgramComponent);
  }

  protected void initProgramComponents()
  {
    this.ProgramPartClassNames = new Hashtable();
    this.ProgramPartClassNames.put("Forward", "com.templar.games.stormrunner.program.MoveInstruction");
    this.ProgramPartClassNames.put("TurnLeft", "com.templar.games.stormrunner.program.TurnLeftInstruction");
    this.ProgramPartClassNames.put("TurnRight", "com.templar.games.stormrunner.program.TurnRightInstruction");
    this.ProgramPartClassNames.put("PickUp", "com.templar.games.stormrunner.program.ArmPickUp");
    this.ProgramPartClassNames.put("PutDown", "com.templar.games.stormrunner.program.ArmPutDown");
    this.ProgramPartClassNames.put("Store", "com.templar.games.stormrunner.program.ArmStore");
    this.ProgramPartClassNames.put("Retrieve", "com.templar.games.stormrunner.program.ArmRetrieve");
    this.ProgramPartClassNames.put("Hammer", "com.templar.games.stormrunner.program.Hammer");
    this.ProgramPartClassNames.put("Launch", "com.templar.games.stormrunner.program.Launch");
    this.ProgramPartClassNames.put("TurnTowards", "com.templar.games.stormrunner.program.TurnTowards");
    this.ProgramPartClassNames.put("TurnAway", "com.templar.games.stormrunner.program.TurnAway");
    this.ProgramPartClassNames.put("Repeat", "com.templar.games.stormrunner.program.Repeat");
    this.ProgramPartClassNames.put("Repeat (end)", "com.templar.games.stormrunner.program.editor.RepeatEnd");
    this.ProgramPartClassNames.put("RepeatForever", "com.templar.games.stormrunner.program.RepeatForever");
    this.ProgramPartClassNames.put("RepeatForever (end)", "com.templar.games.stormrunner.program.editor.RepeatForeverEnd");
    this.ProgramPartClassNames.put("ObstacleSensor", "com.templar.games.stormrunner.sensor.ObstacleSensor");
    this.ProgramPartClassNames.put("HeatSensor", "com.templar.games.stormrunner.sensor.HeatSensor");
    this.ProgramPartClassNames.put("EnergySensor", "com.templar.games.stormrunner.sensor.EnergySensor");
    this.ProgramPartClassNames.put("GeolabSensor", "com.templar.games.stormrunner.sensor.GeolabSensor");

    this.ParameterDisplayOffsets = new Hashtable();
    this.ParameterDisplayOffsets.put("Forward", new Integer(30));
    this.ParameterDisplayOffsets.put("TurnLeft", new Integer(-1));
    this.ParameterDisplayOffsets.put("TurnRight", new Integer(-1));
    this.ParameterDisplayOffsets.put("PickUp", new Integer(-1));
    this.ParameterDisplayOffsets.put("PutDown", new Integer(-1));
    this.ParameterDisplayOffsets.put("Store", new Integer(-1));
    this.ParameterDisplayOffsets.put("Retrieve", new Integer(-1));
    this.ParameterDisplayOffsets.put("Hammer", new Integer(-1));
    this.ParameterDisplayOffsets.put("Launch", new Integer(-1));
    this.ParameterDisplayOffsets.put("TurnTowards", new Integer(-1));
    this.ParameterDisplayOffsets.put("TurnAway", new Integer(-1));
    this.ParameterDisplayOffsets.put("Repeat", new Integer(42));
    this.ParameterDisplayOffsets.put("RepeatForever", new Integer(-1));
    this.ParameterDisplayOffsets.put("ObstacleSensor", new Integer(-1));
    this.ParameterDisplayOffsets.put("HeatSensor", new Integer(-1));
    this.ParameterDisplayOffsets.put("EnergySensor", new Integer(-1));
    this.ParameterDisplayOffsets.put("GeolabSensor", new Integer(-1));

    this.ClassNameProgramPartNames = new Hashtable();
    Enumeration localEnumeration = this.ProgramPartClassNames.keys();
    while (localEnumeration.hasMoreElements())
    {
      localObject1 = localEnumeration.nextElement();
      this.ClassNameProgramPartNames.put(this.ProgramPartClassNames.get(localObject1), localObject1);
    }

    this.ProgramPartImageNames = new Hashtable();

    this.ProgramPartImageNames.put("StartBlock", "com/templar/games/stormrunner/media/images/programblocks/block_program.gif");
    this.ProgramPartImageNames.put("Forward", "com/templar/games/stormrunner/media/images/programblocks/block_forward.gif");
    this.ProgramPartImageNames.put("TurnLeft", "com/templar/games/stormrunner/media/images/programblocks/block_turnleft.gif");
    this.ProgramPartImageNames.put("TurnRight", "com/templar/games/stormrunner/media/images/programblocks/block_turnright.gif");
    this.ProgramPartImageNames.put("PickUp", "com/templar/games/stormrunner/media/images/programblocks/block_pickup.gif");
    this.ProgramPartImageNames.put("PutDown", "com/templar/games/stormrunner/media/images/programblocks/block_putdown.gif");
    this.ProgramPartImageNames.put("Store", "com/templar/games/stormrunner/media/images/programblocks/block_store.gif");
    this.ProgramPartImageNames.put("Retrieve", "com/templar/games/stormrunner/media/images/programblocks/block_retrieve.gif");
    this.ProgramPartImageNames.put("Hammer", "com/templar/games/stormrunner/media/images/programblocks/block_hammer.gif");
    this.ProgramPartImageNames.put("Launch", "com/templar/games/stormrunner/media/images/programblocks/block_launcher.gif");
    this.ProgramPartImageNames.put("TurnTowards", "com/templar/games/stormrunner/media/images/programblocks/block_turntoward.gif");
    this.ProgramPartImageNames.put("TurnAway", "com/templar/games/stormrunner/media/images/programblocks/block_turnaway.gif");
    this.ProgramPartImageNames.put("ObstacleSensor", "com/templar/games/stormrunner/media/images/programblocks/sensor_obstacle.gif");
    this.ProgramPartImageNames.put("ObstacleSensor (icon)", "com/templar/games/stormrunner/media/images/programblocks/sensor-s_obstacle.gif");
    this.ProgramPartImageNames.put("HeatSensor", "com/templar/games/stormrunner/media/images/programblocks/sensor_heat.gif");
    this.ProgramPartImageNames.put("HeatSensor (icon)", "com/templar/games/stormrunner/media/images/programblocks/sensor-s_heat.gif");
    this.ProgramPartImageNames.put("EnergySensor", "com/templar/games/stormrunner/media/images/programblocks/sensor_energy.gif");
    this.ProgramPartImageNames.put("EnergySensor (icon)", "com/templar/games/stormrunner/media/images/programblocks/sensor-s_energy.gif");
    this.ProgramPartImageNames.put("GeolabSensor", "com/templar/games/stormrunner/media/images/programblocks/sensor_geolab.gif");
    this.ProgramPartImageNames.put("GeolabSensor (icon)", "com/templar/games/stormrunner/media/images/programblocks/sensor-s_geolab.gif");
    this.ProgramPartImageNames.put("Repeat", "com/templar/games/stormrunner/media/images/programblocks/stackcon_repeat.gif");
    this.ProgramPartImageNames.put("Repeat (icon)", "com/templar/games/stormrunner/media/images/programblocks/stackcon-s_repeat.gif");
    this.ProgramPartImageNames.put("Repeat (end)", "com/templar/games/stormrunner/media/images/programblocks/stackcon_repeatend.gif");
    this.ProgramPartImageNames.put("RepeatForever", "com/templar/games/stormrunner/media/images/programblocks/stackcon_repeatfe.gif");
    this.ProgramPartImageNames.put("RepeatForever (icon)", "com/templar/games/stormrunner/media/images/programblocks/stackcon-s_repeatfe.gif");
    this.ProgramPartImageNames.put("RepeatForever (end)", "com/templar/games/stormrunner/media/images/programblocks/stackcon_repeatfeend.gif");
    this.ProgramPartImageNames.put("Conditional Bar", "com/templar/games/stormrunner/media/images/programblocks/ppanel_connect.gif");

    this.ProgramPartImages = new Hashtable();
    Object localObject1 = this.ProgramPartImageNames.keys();
    while (((Enumeration)localObject1).hasMoreElements())
    {
      localObject2 = (String)((Enumeration)localObject1).nextElement();
      this.ProgramPartImages.put(localObject2, this.theImageRetriever.getImage((String)this.ProgramPartImageNames.get(localObject2)));
    }

    this.InstructionDrops = new DropTargetInfo[6];
    this.ConditionalDrops = new DropTargetInfo[2];
    this.TallStackDrops = new DropTargetInfo[6];
    this.ShortStackDrops = new DropTargetInfo[6];
    this.RepeatEndDrops = new DropTargetInfo[4];

    Object localObject2 = new ImageComponent((Image)this.ProgramPartImages.get("StartBlock"));
    ImageComponent localImageComponent1 = new ImageComponent((Image)this.ProgramPartImages.get("Forward"));
    ImageComponent localImageComponent2 = new ImageComponent((Image)this.ProgramPartImages.get("ObstacleSensor"));
    ImageComponent localImageComponent3 = new ImageComponent((Image)this.ProgramPartImages.get("Repeat"));
    ImageComponent localImageComponent4 = new ImageComponent((Image)this.ProgramPartImages.get("RepeatForever"));
    ImageComponent localImageComponent5 = new ImageComponent((Image)this.ProgramPartImages.get("RepeatEnd"));

    this.InstructionDrops[0] = new DropTargetInfo(((ImageComponent)localObject2).getSize(), new Point(7, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.StartBlock");
    this.InstructionDrops[1] = new DropTargetInfo(localImageComponent3.getSize(), new Point(25, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Repeat");
    this.InstructionDrops[2] = new DropTargetInfo(localImageComponent4.getSize(), new Point(24, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.RepeatForever");
    this.InstructionDrops[3] = new DropTargetInfo(localImageComponent5.getSize(), new Point(24, 24), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.RepeatEnd");
    this.InstructionDrops[4] = new DropTargetInfo(localImageComponent1.getSize(), new Point(0, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.Instruction");
    this.InstructionDrops[5] = new DropTargetInfo(localImageComponent2.getSize(), new Point(25, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Conditional");

    this.ConditionalDrops[0] = new DropTargetInfo(((ImageComponent)localObject2).getSize(), new Point(92, 0), new Dimension(62, 44), "com.templar.games.stormrunner.program.editor.StartBlock");
    this.ConditionalDrops[1] = new DropTargetInfo(localImageComponent2.getSize(), new Point(126, 0), new Dimension(96, 81), "com.templar.games.stormrunner.program.Conditional");

    this.TallStackDrops[0] = new DropTargetInfo(((ImageComponent)localObject2).getSize(), new Point(-17, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.StartBlock");
    this.TallStackDrops[1] = new DropTargetInfo(localImageComponent3.getSize(), new Point(0, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Repeat");
    this.TallStackDrops[2] = new DropTargetInfo(localImageComponent4.getSize(), new Point(0, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.RepeatForever");
    this.TallStackDrops[3] = new DropTargetInfo(localImageComponent5.getSize(), new Point(0, 24), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.RepeatEnd");
    this.TallStackDrops[4] = new DropTargetInfo(localImageComponent1.getSize(), new Point(-24, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.Instruction");
    this.TallStackDrops[5] = new DropTargetInfo(localImageComponent2.getSize(), new Point(1, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Conditional");

    this.ShortStackDrops[0] = new DropTargetInfo(((ImageComponent)localObject2).getSize(), new Point(-17, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.StartBlock");
    this.ShortStackDrops[1] = new DropTargetInfo(localImageComponent3.getSize(), new Point(0, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Repeat");
    this.ShortStackDrops[2] = new DropTargetInfo(localImageComponent4.getSize(), new Point(0, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.RepeatForever");
    this.ShortStackDrops[3] = new DropTargetInfo(localImageComponent5.getSize(), new Point(0, 24), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.RepeatEnd");
    this.ShortStackDrops[4] = new DropTargetInfo(localImageComponent1.getSize(), new Point(-24, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.Instruction");
    this.ShortStackDrops[5] = new DropTargetInfo(localImageComponent2.getSize(), new Point(1, 74), new Dimension(40, 40), "com.templar.games.stormrunner.program.Conditional");

    this.RepeatEndDrops[0] = new DropTargetInfo(localImageComponent3.getSize(), new Point(0, 89), new Dimension(40, 40), "com.templar.games.stormrunner.program.Repeat");
    this.RepeatEndDrops[1] = new DropTargetInfo(localImageComponent4.getSize(), new Point(0, 52), new Dimension(40, 40), "com.templar.games.stormrunner.program.RepeatForever");
    this.RepeatEndDrops[2] = new DropTargetInfo(localImageComponent5.getSize(), new Point(0, 24), new Dimension(40, 40), "com.templar.games.stormrunner.program.editor.RepeatEnd");
    this.RepeatEndDrops[3] = new DropTargetInfo(localImageComponent1.getSize(), new Point(-24, 37), new Dimension(40, 40), "com.templar.games.stormrunner.program.Instruction");
  }

  public ProgramComponent getProgramComponent(String paramString)
  {
    return getProgramComponent(paramString, null);
  }

  public ProgramComponent getProgramComponent(String paramString, EditorPalette paramEditorPalette)
  {
    ProgramComponent localProgramComponent = null;

    if (this.ProgramPartClassNames.containsKey(paramString))
    {
      Object localObject = null;
      String str = (String)this.ProgramPartClassNames.get(paramString);
      try
      {
        Class localClass1 = Class.forName("com.templar.games.stormrunner.program.Instruction");
        Class localClass2 = Class.forName("com.templar.games.stormrunner.program.Conditional");
        Class localClass3 = Class.forName(str);

        if (localClass1.isAssignableFrom(localClass3))
        {
          localObject = (Linkable)localClass3.newInstance();
        }
        else
        {
          localObject = new Conditional((Sensor)localClass3.newInstance());
        }

        localProgramComponent = getProgramComponent((Linkable)localObject, paramEditorPalette);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
      }
      catch (InstantiationException localInstantiationException)
      {
        localInstantiationException.printStackTrace();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
      }
    }

    return ((ProgramComponent)localProgramComponent);
  }

  public ProgramComponent getProgramComponent(Linkable paramLinkable)
  {
    return getProgramComponent(paramLinkable, null);
  }

  public ProgramComponent getProgramComponent(Linkable paramLinkable, EditorPalette paramEditorPalette)
  {
    String str1;
    ProgramComponent localProgramComponent = null;

    if (paramLinkable instanceof Conditional)
      str1 = ((Conditional)paramLinkable).getSensor().getClass().getName();
    else {
      str1 = paramLinkable.getClass().getName();
    }

    if (this.ClassNameProgramPartNames.containsKey(str1))
    {
      Image localImage2;
      Class localClass1;
      Class localClass2;
      Class localClass3;
      Class localClass4;
      Class localClass5;
      EditorPalette localEditorPalette;
      DropTargetInfo[] arrayOfDropTargetInfo;
      String str2 = (String)this.ClassNameProgramPartNames.get(str1);

      Image localImage1 = (Image)this.ProgramPartImages.get(str2);
      if (this.ProgramPartImages.containsKey(str2 + " (icon)"))
        localImage2 = (Image)this.ProgramPartImages.get(str2 + " (icon)");
      else {
        localImage2 = localImage1;
      }

      try
      {
        localClass1 = Class.forName("com.templar.games.stormrunner.program.Instruction");
        localClass2 = Class.forName("com.templar.games.stormrunner.program.Conditional");
        localClass3 = Class.forName("com.templar.games.stormrunner.program.Repeat");
        localClass4 = Class.forName("com.templar.games.stormrunner.program.RepeatForever");
        localClass5 = Class.forName(str1);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
        localClass1 = localClass2 = localClass3 = localClass4 = localClass5 = null;
      }

      if (paramEditorPalette == null)
        localEditorPalette = this.Palette;
      else {
        localEditorPalette = paramEditorPalette;
      }

      int i = 0;
      Object localObject = null;
      if (localClass3.isAssignableFrom(localClass5))
      {
        arrayOfDropTargetInfo = this.TallStackDrops;

        localObject = new RepeatEnd((Image)this.ProgramPartImages.get("Repeat (end)"), localEditorPalette);
        for (int j = 0; j < this.RepeatEndDrops.length; ++j)
        {
          ((ProgramComponent)localObject).addDropTargetInfo(this.RepeatEndDrops[j]);
        }
      }
      else if (localClass4.isAssignableFrom(localClass5))
      {
        arrayOfDropTargetInfo = this.ShortStackDrops;

        localObject = new RepeatForeverEnd((Image)this.ProgramPartImages.get("RepeatForever (end)"), localEditorPalette);
        for (int j = 0; j < this.RepeatEndDrops.length; ++j)
        {
          ((ProgramComponent)localObject).addDropTargetInfo(this.RepeatEndDrops[j]);
        }
      }
      else if (localClass1.isAssignableFrom(localClass5))
      {
        arrayOfDropTargetInfo = this.InstructionDrops;
      }
      else
      {
        arrayOfDropTargetInfo = this.ConditionalDrops;

        i = 1;
      }

      localProgramComponent = new ProgramComponent(paramLinkable, localImage1, localImage2, ((Integer)this.ParameterDisplayOffsets.get(str2)).intValue(), localEditorPalette);
      for (int j = 0; j < arrayOfDropTargetInfo.length; ++j)
      {
        localProgramComponent.addDropTargetInfo(arrayOfDropTargetInfo[j]);
      }

      if (i != 0)
      {
        localProgramComponent.setAncillaryImage((Image)this.ProgramPartImages.get("Conditional Bar"), 4);
      }

      if (localObject != null)
      {
        localProgramComponent.setBoundingComponent((ProgramComponent)localObject);
      }
    }
    else {
      System.err.println("Editor: Unable to name class " + str1);
    }
    return ((ProgramComponent)localProgramComponent);
  }

  public Hashtable getProgramPartImages()
  {
    return this.ProgramPartImages;
  }

  public ImageRetriever getImageRetriever()
  {
    return this.theImageRetriever;
  }

  public void stopProgram()
  {
    if (this.CurrentRobot != null)
      this.CurrentProgram.restart(this.CurrentRobot);
  }

  public boolean load(Program paramProgram)
  {
    stopProgram();

    if (verify(paramProgram))
    {
      if (this.CurrentRobot != null) {
        this.CurrentRobot.setProgram(paramProgram);
      }

      setProgram(paramProgram, false);

      return true;
    }

    return false;
  }

  public boolean verify(Program paramProgram)
  {
    if (this.CurrentRobot == null) {
      return true;
    }

    int i = 0;

    if ((verifyInstruction(paramProgram.getFirstInstruction())) && (verifyConditional(paramProgram.getFirstConditional())))
      i = 1;

    return i;
  }

  public boolean verifyInstruction(Instruction paramInstruction)
  {
    if (paramInstruction == null) {
      return true;
    }

    boolean bool = false;

    bool = paramInstruction.verifyRobot(this.CurrentRobot);

    if (paramInstruction instanceof Loop)
      bool = (bool) && (verifyInstruction(((Loop)paramInstruction).getDestination()));

    if (bool)
      bool = (bool) && (verifyInstruction(paramInstruction.getNextInstruction()));

    return bool;
  }

  public boolean verifyConditional(Conditional paramConditional)
  {
    if (paramConditional == null) {
      return true;
    }

    int i = 0;

    if (this.CurrentRobot.getSensor(paramConditional.getSensor().getID()) != null)
      i = 1;

    i = ((i == 0) || (!(verifyInstruction(paramConditional.getNextInstruction())))) ? 0 : 1;

    if (i != 0)
      i = ((i == 0) || (!(verifyConditional(paramConditional.getNextConditional())))) ? 0 : 1;

    if(i==0) return false; else return true;
  }

  public void setLocation(Point paramPoint)
  {
    setLocation(paramPoint.x, paramPoint.y);
  }

  public void setLocation(int paramInt1, int paramInt2)
  {
    if (getParent() == null)
      this.LocationHolder = new Point(paramInt1, paramInt2);

    setLocation(paramInt1, paramInt2);
  }

  public boolean isMinimized()
  {
    return (!(this.ControlTab.getOn()));
  }

  public void setMinimized(boolean paramBoolean)
  {
    this.ControlTab.setEnabled(false);

    GameApplet.audio.play("PanelZip");
    try
    {
      if (!(paramBoolean))
      {
        this.CurrentX = getLocation().x;
        this.Collapser = new UtilityThread(10, this, getClass().getMethod("maximizeStep", null), false);
        this.Collapser.start();

        this.CurrentApplet.getGrid().burnGridOn();

        localRenderer = this.state.getRenderer();
        localRenderer.setInternalFocus(this);

        return;
      }

      this.CurrentX = this.LocationHolder.x;
      this.Collapser = new UtilityThread(10, this, getClass().getMethod("minimizeStep", null), false);
      this.Collapser.start();

      this.CurrentApplet.getGrid().burnGridOff();

      Renderer localRenderer = this.state.getRenderer();
      localRenderer.setInternalFocus(null);

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
      System.err.println("Editor: Check the setMinimized method - some IDIOT munged it up.");
    }
  }

  public boolean minimizeStep()
  {
    if (this.CurrentX > getSize().width * -1 + 19)
    {
      setLocation(this.CurrentX, getLocation().y);
      this.CurrentX -= getSize().width / 8;
      return true;
    }

    setLocation(this.LocationHolder.x - getSize().width + 19, getLocation().y);

    this.ControlTab.setEnabled(true);

    return false;
  }

  public boolean maximizeStep()
  {
    if (this.CurrentX < this.LocationHolder.x)
    {
      setLocation(this.CurrentX, getLocation().y);
      this.CurrentX += getSize().width / 8;
      return true;
    }

    setLocation(this.LocationHolder.x, getLocation().y);

    this.ControlTab.setEnabled(true);

    return false;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getActionCommand().compareTo("ToggleVisible") == 0)
    {
      if (getLocation().x < this.LocationHolder.x)
      {
        setMinimized(false);

        return;
      }

      setMinimized(true);
    }
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
    this.state = paramGameState;
  }
}