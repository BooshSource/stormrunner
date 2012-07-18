package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.program.ArmPickUp;
import com.templar.games.stormrunner.program.ArmPutDown;
import com.templar.games.stormrunner.program.ArmRetrieve;
import com.templar.games.stormrunner.program.ArmStore;
import com.templar.games.stormrunner.program.Hammer;
import com.templar.games.stormrunner.program.Launch;
import com.templar.games.stormrunner.program.TurnAway;
import com.templar.games.stormrunner.program.TurnTowards;
import com.templar.games.stormrunner.sensor.Sensor;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EditorPalette extends SimpleContainer
  implements ActionListener
{
  public static final int SPACING = 3;
  protected Editor CurrentEditor;
  protected EditorDisplay CurrentDisplay;
  protected ProgramContainer ProgramLayer;
  protected ProgramComponent CurrentActive;
  protected PaletteSection CurrentOpen;
  protected boolean Disabled = false;
  protected ImageButton Save;
  protected ImageButton Load;
  protected ImageButton Delete;
  protected ImageRetriever ir;
  protected PaletteSection Commands;
  protected PaletteSection Sensors;
  protected PaletteSection Stack;
  protected Vector CommandsList;
  protected Vector SensorsList;
  protected Vector StackList;

  public EditorPalette(EditorDisplay paramEditorDisplay)
  {
    this.CurrentDisplay = paramEditorDisplay;
    this.CurrentEditor = paramEditorDisplay.getEditor();
    this.ProgramLayer = this.CurrentEditor.getProgramLayer();
    this.CommandsList = new Vector();
    this.SensorsList = new Vector();
    this.StackList = new Vector();

    this.ir = this.CurrentEditor.getImageRetriever();

    Image localImage1 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_commands-off.gif");
    Image localImage2 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_commands-in.gif");
    Image localImage3 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_watchers-off.gif");
    Image localImage4 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_watchers-in.gif");
    Image localImage5 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_stackcons-off.gif");
    Image localImage6 = this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_stackcons-in.gif");
    this.Commands = new PaletteSection(this.CurrentDisplay, this.CommandsList, this, localImage1, localImage2);
    this.Sensors = new PaletteSection(this.CurrentDisplay, this.SensorsList, this, localImage3, localImage4);
    this.Stack = new PaletteSection(this.CurrentDisplay, this.StackList, this, localImage5, localImage6);

    refreshBlockLists();

    add(this.Commands);
    add(this.Sensors);
    add(this.Stack);

    this.Save = new ImageButton(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_save-off.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_save-in.gif"), null);
    this.Save.setLocation(17, 2);
    this.Save.setActionCommand("save");
    this.Save.addActionListener(this);
    this.Save.setClickSound(GameApplet.audio, "ButtonClick");

    this.Load = new ImageButton(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_load-off.gif"), this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_load-in.gif"), null);
    this.Load.setLocation(17, 16);
    this.Load.setActionCommand("load");
    this.Load.addActionListener(this);
    this.Load.setClickSound(GameApplet.audio, "ButtonClick");

    this.Delete = new ImageButton(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_trash-off.gif"));
    this.Delete.setOnImage(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_trash-in.gif"));
    this.Delete.setOn(false);
    this.Delete.setLocation(47, 2);
    this.Delete.setClickable(false);

    ImageComponent localImageComponent = new ImageComponent(this.ir.getImage("com/templar/games/stormrunner/media/images/programeditor/ptools_buttons-back.gif"));
    localImageComponent.setSize(localImageComponent.getSize());
    localImageComponent.setLocation(0, 0);

    SimpleContainer localSimpleContainer = new SimpleContainer();
    localSimpleContainer.setLayout(null);
    localSimpleContainer.setSize(localImageComponent.getSize());
    localSimpleContainer.add(this.Save);
    localSimpleContainer.add(this.Load);
    localSimpleContainer.add(this.Delete);
    localSimpleContainer.add(localImageComponent);

    add(localSimpleContainer);

    doLayout();

    enableEvents(16L);
  }

  public void setDisabled(boolean paramBoolean)
  {
    this.Disabled = paramBoolean;
  }

  public boolean getDisabled()
  {
    return this.Disabled;
  }

  public void refreshBlockLists()
  {
    this.CommandsList.removeAllElements();
    this.SensorsList.removeAllElements();
    this.StackList.removeAllElements();

    this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("Forward", this));
    this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("TurnLeft", this));
    this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("TurnRight", this));

    this.StackList.addElement(this.CurrentEditor.getProgramComponent("Repeat", this));
    this.StackList.addElement(this.CurrentEditor.getProgramComponent("RepeatForever", this));

    Robot localRobot = this.CurrentEditor.getRobot();
    if (localRobot != null)
    {
      if (new ArmPickUp().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("PickUp"));

      if (new ArmPutDown().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("PutDown"));

      if (new ArmStore().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("Store"));

      if (new ArmRetrieve().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("Retrieve"));

      if (new TurnTowards().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("TurnTowards"));

      if (new TurnAway().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("TurnAway"));

      if (new Hammer().verifyRobot(localRobot))
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("Hammer"));

      if (new Launch().verifyRobot(localRobot)) {
        this.CommandsList.addElement(this.CurrentEditor.getProgramComponent("Launch"));
      }

      Sensor localSensor = localRobot.getSensor("ObstacleSensor");
      if (localSensor != null)
      {
        this.SensorsList.addElement(this.CurrentEditor.getProgramComponent("ObstacleSensor", this));
      }

      localSensor = localRobot.getSensor("HeatSensor");
      if (localSensor != null)
      {
        this.SensorsList.addElement(this.CurrentEditor.getProgramComponent("HeatSensor", this));
      }

      localSensor = localRobot.getSensor("EnergySensor");
      if (localSensor != null)
      {
        this.SensorsList.addElement(this.CurrentEditor.getProgramComponent("EnergySensor", this));
      }

      localSensor = localRobot.getSensor("GeolabSensor");
      if (localSensor != null)
      {
        this.SensorsList.addElement(this.CurrentEditor.getProgramComponent("GeolabSensor", this));
      }

    }

    this.Commands.setContents(this.CommandsList);
    this.Sensors.setContents(this.SensorsList);
    this.Stack.setContents(this.StackList);
  }

  public void doLayout()
  {
    Component[] arrayOfComponent = getComponents();

    int i = 0;
    for (int j = 0; j < arrayOfComponent.length; ++j)
    {
      Component localComponent = getComponent(j);
      localComponent.setLocation(0, i);
      localComponent.setSize(localComponent.getSize());
      i += localComponent.getSize().height + 3;
    }

    setSize(getSize().width, i);
  }

  public PaletteSection getOpenSection()
  {
    return this.CurrentOpen;
  }

  public void setOpenSection(PaletteSection paramPaletteSection)
  {
    this.CurrentOpen = paramPaletteSection;
  }

  public ProgramComponent getCurrentActiveProgramComponent()
  {
    return this.CurrentActive;
  }

  public void setCurrentActiveProgramComponent(ProgramComponent paramProgramComponent)
  {
    this.CurrentActive = paramProgramComponent;
  }

  public EditorDisplay getEditorDisplay()
  {
    return this.CurrentDisplay;
  }

  public Editor getEditor()
  {
    return this.CurrentEditor;
  }

  public ProgramContainer getProgramLayer()
  {
    return this.ProgramLayer;
  }

  public ImageButton getDelete()
  {
    return this.Delete;
  }

  public Rectangle getDeleteBounds()
  {
    Rectangle localRectangle = null;

    if (this.Delete != null)
    {
      localRectangle = this.Delete.getBounds();

      Container localContainer = this.Delete.getParent();
      int i = 0; int j = 0;
      while (localContainer != this.CurrentEditor)
      {
        i += localContainer.getLocation().x;
        j += localContainer.getLocation().y;
        localContainer = localContainer.getParent();
      }

      localRectangle.translate(i, j);
    }

    return localRectangle;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (!(this.Disabled))
    {
      Object localObject;
      this.CurrentEditor.stopProgram();

      this.CurrentEditor.cancelEditParameter();

      if (paramActionEvent.getActionCommand().compareTo("save") == 0)
      {
        localObject = new SaveMenu(this.CurrentEditor);
        this.CurrentEditor.add((Component)localObject, 0);
        ((Component)localObject).repaint();

        return;
      }

      if (paramActionEvent.getActionCommand().compareTo("load") == 0)
      {
        localObject = new LoadMenu(this.CurrentEditor);
        this.CurrentEditor.add((Component)localObject, 0);
        ((Component)localObject).repaint();
      }
    }
  }

  public void setLocation(Point paramPoint)
  {
    setLocation(paramPoint.x, paramPoint.y);
  }

  public void setLocation(int paramInt1, int paramInt2)
  {
    if (this.CurrentActive != null)
    {
      int i = this.CurrentActive.getLocation().x + paramInt1 - getLocation().x;
      int j = this.CurrentActive.getLocation().y + paramInt2 - getLocation().y;
      this.CurrentActive.setLocationWhileDragging(i, j);
    }

    setLocation(paramInt1, paramInt2);
  }
}