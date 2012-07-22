package com.templar.games.stormrunner;

import com.templar.games.stormrunner.build.BuildPanel;
import com.templar.games.stormrunner.build.CargoBay;
import com.templar.games.stormrunner.program.editor.Editor;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.applet.TApplet;
import com.templar.games.stormrunner.templarutil.audio.AppletAudioDevice;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.audio.NullAudioDevice;
import com.templar.games.stormrunner.templarutil.audio.SunAudioDevice;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageFilenameProvider;
import com.templar.games.stormrunner.templarutil.gui.MessageDialog;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import com.templar.games.stormrunner.util.FocusCatcher;
import com.templar.games.stormrunner.util.MonitoredInputStream;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
//import netscape.security.ForbiddenTargetException;
//import netscape.security.PrivilegeManager;

public class GameApplet extends TApplet
  implements ImageRetriever, com.templar.games.stormrunner.templarutil.gui.ImagePaintListener, ImageFilenameProvider
{
  public static final double VERSION = 1.1000000000000001D;
  public static final double SAVE_FORMAT_VERSION = 0.5D;
  public static final double MINIMUM_SAVE_FORMAT_VERSION = 0.40000000000000002D;
  public static final String NEWGAME_SCENE = "com/templar/games/stormrunner/media/scenes/newgame.pac";
  public static final int IMAGECACHE_DELAY = 20000;
  public static final int BAY_STATE = 0;
  public static final int VIEW_STATE = 1;
  public static final int OPT_STATE = 2;
  public static final Color GRID_COLOR = new Color(255, 255, 0);
  public static GameApplet thisApplet;
  public static AppletContext appletContext;
  public static AudioManager audio;
  protected URL HelpURL;
  protected String HelpTarget;
  protected ProgressComponent progressDialog;
  protected GameState CurrentGameState;
  protected StatusPanel statpanel;
  protected OpsPanel opspanel;
  protected Renderer CurrentRenderer;
  protected Editor CurrentEditor;
  protected Grid CurrentGrid;
  protected CargoBay bay;
  protected BuildPanel buildpanel;
  protected OptionsPanel optionspanel;
  protected SimpleContainer OptionsScreen;
  protected SimpleContainer ViewScreen;
  protected SimpleContainer BayScreen;
  protected SimpleContainer TopLayer;
  protected int State = 2;
  protected int LastState;
  protected boolean[] StatusMinimized;
  protected MediaTracker CacheTracker = new MediaTracker(modalComp); ////
  protected Hashtable images = new Hashtable();
  protected Hashtable imageFilename = new Hashtable();
  protected Vector ImageCacheActiveList = new Vector();
  protected boolean playing;
  protected Object PaintLock = new Object();
  private Image buffer;
  private Graphics graphics;
  private UtilityThread cacheThread;
  private LoadSaveFrame savegameWindow;
  protected double VersionOfCurrentSavegame = -1.0D;
  private transient boolean wasMuted;
  private FocusCatcher FocusFixer;

  public void init()
  {
    thisApplet = this;
    appletContext = getAppletContext();

    enableEvents(16L);

    Hashtable localHashtable = new Hashtable();
    localHashtable.put("ButtonClick", "com/templar/games/stormrunner/media/sounds/interface/button_click-16.au");
    localHashtable.put("ButtonRun", "com/templar/games/stormrunner/media/sounds/interface/button_run.au");
    localHashtable.put("ButtonStop", "com/templar/games/stormrunner/media/sounds/interface/button_stop.au");
    localHashtable.put("ButtonError", "com/templar/games/stormrunner/media/sounds/interface/error.au");
    localHashtable.put("DeathAlarm", "com/templar/games/stormrunner/media/sounds/interface/beep_death.au");
    localHashtable.put("PanelZip", "com/templar/games/stormrunner/media/sounds/interface/panelzip.au");
    localHashtable.put("ProgramClick", "com/templar/games/stormrunner/media/sounds/interface/program_click.au");
    localHashtable.put("Achilles", "com/templar/games/stormrunner/media/sounds/robots/achilles.au");
    localHashtable.put("Arachnae", "com/templar/games/stormrunner/media/sounds/robots/arachne.au");
    localHashtable.put("Hermes", "com/templar/games/stormrunner/media/sounds/robots/hermes.au");
    localHashtable.put("AchillesCover", "com/templar/games/stormrunner/media/sounds/robots/achilles-cut.au");
    localHashtable.put("ArachnaeCover", "com/templar/games/stormrunner/media/sounds/robots/arachne-cut.au");
    localHashtable.put("HermesCover", "com/templar/games/stormrunner/media/sounds/robots/hermes-cut.au");
    localHashtable.put("RobotStart", "com/templar/games/stormrunner/media/sounds/effects/startup-running.au");
    localHashtable.put("Robot-Happy", "com/templar/games/stormrunner/media/sounds/robotvoices/happy.au");
    localHashtable.put("Robot-Scared", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-scared_1.au");
    localHashtable.put("Robot-ReplyScared", "com/templar/games/stormrunner/media/sounds/robotvoices/reply-scared.au");
    localHashtable.put("Robot-Alarm", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-alarm.au");
    localHashtable.put("Robot-Alert", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-alert.au");
    localHashtable.put("Robot-Angry", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-angry.au");
    localHashtable.put("Robot-Deny", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-deny.au");
    localHashtable.put("Robot-Question", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-question.au");
    localHashtable.put("Robot-Scream", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-scream.au");
    localHashtable.put("Robot-NotCompute", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nocompute.au");
    localHashtable.put("Robot-Nonsense-1", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense01.au");
    localHashtable.put("Robot-Nonsense-2", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense02.au");
    localHashtable.put("Robot-Nonsense-3", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense03.au");
    localHashtable.put("Robot-Nonsense-4", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense04.au");
    localHashtable.put("Robot-Nonsense-5", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense05.au");
    localHashtable.put("Robot-Nonsense-6", "com/templar/games/stormrunner/media/sounds/robotvoices/rcx-nonsense06.au");
    localHashtable.put("Build-Arm-Tools", "com/templar/games/stormrunner/media/sounds/effects/build_arm-tools.au");
    localHashtable.put("Build-Dismantle", "com/templar/games/stormrunner/media/sounds/effects/build_dismantle.au");
    localHashtable.put("Build-Elevator", "com/templar/games/stormrunner/media/sounds/effects/build_elevator.au");
    localHashtable.put("Build-ArmWrist", "com/templar/games/stormrunner/media/sounds/effects/build_armwrist.au");
    localHashtable.put("Build-FrontArm", "com/templar/games/stormrunner/media/sounds/effects/build_front-assarm.au");
    localHashtable.put("Build-RearTopArm-Move", "com/templar/games/stormrunner/media/sounds/effects/build_reartop-arm-move.au");
    localHashtable.put("Build-RearTopArm-End", "com/templar/games/stormrunner/media/sounds/effects/build_reartop-arm-end.au");
    localHashtable.put("Build-SensorArm-Beat", "com/templar/games/stormrunner/media/sounds/effects/build_sensorarm-beat.au");
    localHashtable.put("Build-SensorArm-End", "com/templar/games/stormrunner/media/sounds/effects/build_sensorarm-end.au");
    localHashtable.put("Build-SensorArm-Move-Fadeout", "com/templar/games/stormrunner/media/sounds/effects/build_sensorarm-move-fadeout.au");
    localHashtable.put("ShipDoor", "com/templar/games/stormrunner/media/sounds/effects/shipdoor.au");
    localHashtable.put("Piledriver-Strike", "com/templar/games/stormrunner/media/sounds/assemblies/piledriver-strike.au");
    localHashtable.put("Arm-Up", "com/templar/games/stormrunner/media/sounds/assemblies/arm-up.au");
    localHashtable.put("Arm-Down", "com/templar/games/stormrunner/media/sounds/assemblies/arm-down.au");
    localHashtable.put("Arm-Store", "com/templar/games/stormrunner/media/sounds/assemblies/arm-store.au");
    localHashtable.put("Arm-Retrieve", "com/templar/games/stormrunner/media/sounds/assemblies/arm-retrieve.au");
    localHashtable.put("CannonFire", "com/templar/games/stormrunner/media/sounds/effects/cannon-fire.au");
    localHashtable.put("CannonMiss", "com/templar/games/stormrunner/media/sounds/effects/cannon-miss.au");
    localHashtable.put("CannonHit", "com/templar/games/stormrunner/media/sounds/effects/cannon-hit.au");
    localHashtable.put("CannonScan", "com/templar/games/stormrunner/media/sounds/effects/cannon-scan.au");
    localHashtable.put("FieryDeath", "com/templar/games/stormrunner/media/sounds/deaths/death-lava.au");
    localHashtable.put("PlantNotice", "com/templar/games/stormrunner/media/sounds/effects/plant-notice.au");
    localHashtable.put("PlantDeath", "com/templar/games/stormrunner/media/sounds/deaths/death-plant.au");
    localHashtable.put("CliffSlip", "com/templar/games/stormrunner/media/sounds/deaths/death-cliff-slip.au");
    try
    {
      audio = new AudioManager(new SunAudioDevice(this), localHashtable);
    }
    catch (Exception localException1)
    {
      System.err.println("Stormrunner: Error initializing AudioManager. Falling back to AppletAudioDevice.");
      try
      {
        audio = new AudioManager(new AppletAudioDevice(this), localHashtable);
      }
      catch (Exception localException2)
      {
        System.err.println("Stormrunner: Error initializing AppletAudioDevice. Sounds disabled.");
        audio = new AudioManager(new NullAudioDevice(), null);
      }
    }

    startup();
  }

  public void startup()
  {
    setLayout(null);

    String str = getParameter("HelpURL");
    if (str != null)
    {
      try
      {
        try
        {
          this.HelpURL = new URL(str);
        }
        catch (MalformedURLException localMalformedURLException1)
        {
          this.HelpURL = new URL(getDocumentBase(), str);
        }

        this.HelpTarget = getParameter("HelpTarget");
      }
      catch (MalformedURLException localMalformedURLException2)
      {
        System.err.println("Stormrunner: GameApplet: Parameter HelpURL is malformed. Help button will not function.");
      }
    }
    else {
      System.err.println("Stormrunner: GameApplet: Parameter HelpURL was not found in the Applet tag. Help button will not function.");
    }

    ImageComponent.addImagePaintListener(this);
    ImageComponent.setImageFilenameProvider(this);

    ImageComponent localImageComponent1 = new ImageComponent(getImage("com/templar/games/stormrunner/media/images/mapinterface/mappanel_logo.gif"));
    ImageComponent localImageComponent2 = new ImageComponent(getImage("com/templar/games/stormrunner/media/images/mapinterface/mappanel_top.gif"));
    ImageComponent localImageComponent3 = new ImageComponent(getImage("com/templar/games/stormrunner/media/images/mapinterface/mappanel_bottom.gif"));
    ImageComponent localImageComponent4 = new ImageComponent(getImage("com/templar/games/stormrunner/media/images/mapinterface/mappanel_left.gif"));
    ImageComponent localImageComponent5 = new ImageComponent(getImage("com/templar/games/stormrunner/media/images/mapinterface/mappanel_right.gif"));
    localImageComponent1.setLocation(0, 0);
    localImageComponent1.setSize(localImageComponent1.getSize());
    localImageComponent2.setSize(localImageComponent2.getSize());
    localImageComponent2.setLocation(localImageComponent1.getSize().width, 0);
    localImageComponent4.setSize(localImageComponent4.getSize());
    localImageComponent4.setLocation(0, localImageComponent1.getSize().height);
    localImageComponent5.setSize(localImageComponent5.getSize());
    localImageComponent5.setLocation(getSize().width - localImageComponent5.getSize().width, 0);
    localImageComponent3.setSize(localImageComponent3.getSize());
    localImageComponent3.setLocation(localImageComponent4.getSize().width, getSize().height - localImageComponent3.getSize().height);

    this.StatusMinimized = new boolean[3];
    this.StatusMinimized[0] = true;
    this.StatusMinimized[1] = false;
    this.StatusMinimized[2] = true;

    this.CurrentGrid = new Grid(50, 50, GRID_COLOR);
    this.CurrentGrid.setBounds(0, 0, getSize().width, getSize().height);
    this.CurrentGrid.setOn(true);

    this.opspanel = new OpsPanel(this, this);
    this.opspanel.setLocation(0, getSize().height - this.opspanel.getSize().height);

    int i = 0;
    int j = 0;
    if (this.optionspanel != null)
    {
      if (this.optionspanel.getEnabled() == false) {
        i = 1;
      }

      if (this.progressDialog != null)
        j = 1;
    }
    this.optionspanel = new OptionsPanel(this);
    this.optionspanel.setLocation(0, 0);
    if (i != 0)
      this.optionspanel.setEnabled(false);
    if (j != 0)
      this.optionspanel.add(this.progressDialog, 0);

    this.bay = new CargoBay(this);
    this.bay.setLocation(0, 0);

    this.ViewScreen = new SimpleContainer();
    this.ViewScreen.setBounds(0, 0, getSize().width, getSize().height);
    this.BayScreen = new SimpleContainer();
    this.BayScreen.setBounds(0, 0, getSize().width, getSize().height);
    this.OptionsScreen = new SimpleContainer();
    this.OptionsScreen.setBounds(0, 0, getSize().width, getSize().height);
    this.TopLayer = new SimpleContainer();
    this.TopLayer.setBounds(0, 0, getSize().width, getSize().height);

    this.OptionsScreen.add(this.optionspanel);

    this.ViewScreen.add(localImageComponent1);
    this.ViewScreen.add(localImageComponent2);
    this.ViewScreen.add(localImageComponent4);
    this.ViewScreen.add(localImageComponent5);
    this.ViewScreen.add(localImageComponent3);

    this.BayScreen.add(this.bay);

    this.TopLayer.add(this.OptionsScreen, 0);

    add(this.TopLayer);

    if (System.getProperty("java.vendor").indexOf("Netscape") != -1)
    {
      this.FocusFixer = new FocusCatcher(this.CurrentRenderer, this);
      add(this.FocusFixer, 0);
    }

    try
    {
      this.cacheThread = new UtilityThread(20000, this, getClass().getMethod("cacheClean", null), false);
      this.cacheThread.start();

      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void startup(GameState paramGameState)
  {
    startup();

    this.CurrentGameState = paramGameState;
    this.CurrentGameState.setAppletRef(this);

    this.CurrentRenderer = this.CurrentGameState.getRenderer();

    this.CurrentRenderer.setLocation(0, 0);
    this.CurrentRenderer.setSize(480, 360);
    this.CurrentGameState.getCurrentScene().setRenderer(this.CurrentRenderer);
    this.CurrentRenderer.setOffsetToCenter(this.CurrentGameState.getCurrentScene().getRobotStart());

    this.buildpanel = new BuildPanel(this, this.bay, this);
    this.buildpanel.setLocation(0, 0);

    this.statpanel = new StatusPanel(this, this.CurrentGameState);
    this.statpanel.setLocationHolder(getSize().width - this.statpanel.getSize().width - 5, 
      getSize().height - this.statpanel.getSize().height);
    this.statpanel.setLocation(getSize().width - this.statpanel.getSize().width - 5, 
      getSize().height - 20);

    this.CurrentEditor = new Editor(this.CurrentGameState, this, null, this);
    this.CurrentEditor.setLocation(0, 35);

    this.CurrentRenderer.setInternalFocus(this.CurrentEditor);

    this.BayScreen.add(this.buildpanel, 0);

    this.ViewScreen.add(this.CurrentEditor, 0);
    this.ViewScreen.add(this.CurrentGrid);
    this.ViewScreen.add(this.CurrentRenderer);
  }

  public void setState(int paramInt)
  {
    this.opspanel.setState(paramInt);
  }

  public void internalSetState(int paramInt)
  {
    if (this.State != paramInt)
    {
      this.StatusMinimized[this.State] = this.statpanel.isMinimized();

      this.LastState = this.State;

      this.State = paramInt;

      toggleScreen(this.LastState, false);
      toggleScreen(this.State, true);

      repaint();
    }
  }

  protected void toggleScreen(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean);
    switch (paramInt)
    {
    case 0:
      this.ImageCacheActiveList.removeAllElements();
      this.ImageCacheActiveList.addElement("opspanel");
      this.ImageCacheActiveList.addElement("statuspanel");
      this.TopLayer.add(this.BayScreen);
      if (this.FocusFixer != null)
        this.FocusFixer.setFocusTarget(this.buildpanel);
      this.buildpanel.requestFocus();
      this.statpanel.setMinimized(this.StatusMinimized[0]);
      return;
    case 1:
      this.ImageCacheActiveList.removeAllElements();
      this.ImageCacheActiveList.addElement("opspanel");
      this.ImageCacheActiveList.addElement("statuspanel");
      this.TopLayer.add(this.ViewScreen);
      this.CurrentRenderer.setVisible(true);
      if (this.FocusFixer != null)
        this.FocusFixer.setFocusTarget(this.CurrentRenderer);
      this.CurrentRenderer.requestFocus();
      this.statpanel.setMinimized(this.StatusMinimized[1]);
      return;
    case 2:
      this.CurrentGameState.stop();
      this.TopLayer.remove(this.statpanel);
      this.TopLayer.remove(this.opspanel);
      this.TopLayer.add(this.OptionsScreen, 0);
      return;

      switch (paramInt)
      {
      case 0:
        this.TopLayer.remove(this.BayScreen);
        return;
      case 1:
        this.TopLayer.remove(this.ViewScreen);
        this.CurrentRenderer.setVisible(false);
        return;
      case 2:
        this.CurrentGameState.start();
        this.TopLayer.remove(this.OptionsScreen);
        this.TopLayer.add(this.statpanel, 0);
        this.TopLayer.add(this.opspanel, 0);
        return;
      }
    }
  }

  public int getState()
  {
    return this.State;
  }

  public GameState getGameState()
  {
    return this.CurrentGameState;
  }

  public BuildPanel getBuildPanel()
  {
    return this.buildpanel;
  }

  public Editor getEditor()
  {
    return this.CurrentEditor;
  }

  public URL getHelpURL()
  {
    return this.HelpURL;
  }

  public String getHelpTarget()
  {
    return this.HelpTarget;
  }

  public void start()
  {
    if (this.CurrentGameState != null)
      this.CurrentGameState.start();
  }

  public void stop()
  {
    if (this.CurrentGameState != null)
      this.CurrentGameState.stop();
  }

  public Grid getGrid()
  {
    return this.CurrentGrid;
  }

  public StatusPanel getStatusPanel()
  {
    return this.statpanel;
  }

  public Vector getImageCacheActiveList()
  {
    return this.ImageCacheActiveList;
  }

  public void flushImageCache()
  {
    Enumeration localEnumeration = this.images.elements();
    while (localEnumeration.hasMoreElements())
    {
      String str1;
      ImageTrack localImageTrack = (ImageTrack)localEnumeration.nextElement();

      int i = checkImage(localImageTrack.image, this);
      int j = ((i & 0x20) != 0) ? 1 : 0;

      if ((str1 = (String)this.imageFilename.get(localImageTrack.image)) != null)
        for (int k = 0; (j != 0) && (k < this.ImageCacheActiveList.size()); ++k)
        {
          String str2 = (String)this.ImageCacheActiveList.elementAt(k);
          if (str1.indexOf(str2) >= 0)
            j = 0;
        }

      if (j != 0)
      {
        localImageTrack.time = -1L;
        localImageTrack.image.flush();
      }
    }

    System.gc();
  }

  public synchronized boolean cacheClean()
  {
    long l = System.currentTimeMillis();

    Enumeration localEnumeration = this.images.elements();
    while (localEnumeration.hasMoreElements())
    {
      String str1;
      ImageTrack localImageTrack = (ImageTrack)localEnumeration.nextElement();

      int i = checkImage(localImageTrack.image, this);

      int j = ((i & 0x20) != 0) ? 1 : 0;

      if ((str1 = (String)this.imageFilename.get(localImageTrack.image)) != null)
        for (int k = 0; (j != 0) && (k < this.ImageCacheActiveList.size()); ++k)
        {
          String str2 = (String)this.ImageCacheActiveList.elementAt(k);
          if (str1.indexOf(str2) >= 0)
            j = 0;
        }

      if ((localImageTrack.time != -1L) && (j != 0) && (localImageTrack.time + 20000L < l))
      {
        localImageTrack.time = -1L;
        localImageTrack.image.flush();
      }

    }

    Runtime.getRuntime().gc();

    return true;
  }

  public Image getImage(URL paramURL, String paramString)
  {
    if (paramString == null)
    {
      Debug.println("getImage(null,null) called.");
      return null;
    }
    Object localObject1 = this.images.get(paramString);
    if (localObject1 != null)
    {
      localObject2 = (ImageTrack)localObject1;

      return ((ImageTrack)localObject2).image;
    }

    Object localObject2 = super.getImage(paramURL, paramString);
    if (localObject2 == null)
    {
      Debug.println("super.getImage(" + paramURL + "," + paramString + ") returned null");
    }
    else
    {
      ImageTrack localImageTrack = new ImageTrack(this, (Image)localObject2);
      this.images.put(paramString, localImageTrack);
      this.imageFilename.put(localObject2, paramString);
    }
    return ((Image)localObject2);
  }

  public Image getImage(String paramString)
  {
    if ((paramString != "") && (paramString != null))
      return getImage(getDocumentBase(), paramString);
    return null;
  }

  public String getImageFilename(Image paramImage)
  {
    return ((String)this.imageFilename.get(paramImage));
  }

  public void imagePainted(ImageComponent paramImageComponent, Image paramImage)
  {
    hitCache(paramImage, paramImageComponent);
  }

  public void hitCache(Image paramImage)
  {
    hitCache(paramImage, null); } 
  // ERROR //
  ////
  public synchronized void hitCache(Image paramImage, ImageComponent paramImageComponent) { // Byte code:
    //   0: aload_1
    //   1: ifnonnull +9 -> 10
    //   4: ldc 67
    //   6: invokestatic 394	com/templar/games/stormrunner/templarutil/Debug:println	(Ljava/lang/String;)V
    //   9: return
    //   10: aload_0
    //   11: getfield 373	com/templar/games/stormrunner/GameApplet:imageFilename	Ljava/util/Hashtable;
    //   14: aload_1
    //   15: invokevirtual 342	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   18: astore_3
    //   19: aload_3
    //   20: ifnull +441 -> 461
    //   23: aload_3
    //   24: checkcast 238	java/lang/String
    //   27: astore 4
    //   29: aload_0
    //   30: getfield 374	com/templar/games/stormrunner/GameApplet:images	Ljava/util/Hashtable;
    //   33: aload 4
    //   35: invokevirtual 342	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   38: checkcast 180	com/templar/games/stormrunner/GameApplet$ImageTrack
    //   41: astore 5
    //   43: aload 5
    //   45: ifnonnull +36 -> 81
    //   48: ldc 105
    //   50: invokestatic 394	com/templar/games/stormrunner/templarutil/Debug:println	(Ljava/lang/String;)V
    //   53: new 239	java/lang/StringBuffer
    //   56: dup
    //   57: aload 4
    //   59: invokestatic 444	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   62: invokespecial 288	java/lang/StringBuffer:<init>	(Ljava/lang/String;)V
    //   65: ldc 22
    //   67: invokevirtual 321	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   70: aload 5
    //   72: invokevirtual 320	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   75: invokevirtual 441	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   78: invokestatic 394	com/templar/games/stormrunner/templarutil/Debug:println	(Ljava/lang/String;)V
    //   81: aload_2
    //   82: ifnull +279 -> 361
    //   85: aload_2
    //   86: astore 6
    //   88: aload 6
    //   90: monitorenter
    //   91: aload 5
    //   93: getfield 439	com/templar/games/stormrunner/GameApplet$ImageTrack:time	J
    //   96: ldc2_w 449
    //   99: lcmp
    //   100: ifne +251 -> 351
    //   103: aload_0
    //   104: aload 5
    //   106: getfield 372	com/templar/games/stormrunner/GameApplet$ImageTrack:image	Ljava/awt/Image;
    //   109: aload_2
    //   110: invokevirtual 390	java/awt/Component:prepareImage	(Ljava/awt/Image;Ljava/awt/image/ImageObserver;)Z
    //   113: pop
    //   114: iconst_0
    //   115: istore 8
    //   117: aload_0
    //   118: aload 5
    //   120: getfield 372	com/templar/games/stormrunner/GameApplet$ImageTrack:image	Ljava/awt/Image;
    //   123: aload_2
    //   124: invokevirtual 329	java/awt/Component:checkImage	(Ljava/awt/Image;Ljava/awt/image/ImageObserver;)I
    //   127: istore 9
    //   129: goto +192 -> 321
    //   132: iload 8
    //   134: iconst_2
    //   135: if_icmple +164 -> 299
    //   138: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   141: ldc 74
    //   143: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   146: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   149: aload 4
    //   151: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   154: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   157: ldc 62
    //   159: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   162: iload 9
    //   164: sipush 128
    //   167: iand
    //   168: ifeq +11 -> 179
    //   171: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   174: ldc 8
    //   176: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   179: iload 9
    //   181: bipush 64
    //   183: iand
    //   184: ifeq +11 -> 195
    //   187: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   190: ldc 10
    //   192: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   195: iload 9
    //   197: bipush 32
    //   199: iand
    //   200: ifeq +11 -> 211
    //   203: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   206: ldc 9
    //   208: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   211: iload 9
    //   213: bipush 16
    //   215: iand
    //   216: ifeq +11 -> 227
    //   219: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   222: ldc 11
    //   224: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   227: iload 9
    //   229: bipush 8
    //   231: iand
    //   232: ifeq +11 -> 243
    //   235: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   238: ldc 14
    //   240: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   243: iload 9
    //   245: iconst_2
    //   246: iand
    //   247: ifeq +11 -> 258
    //   250: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   253: ldc 12
    //   255: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   258: iload 9
    //   260: iconst_1
    //   261: iand
    //   262: ifeq +11 -> 273
    //   265: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   268: ldc 15
    //   270: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   273: iload 9
    //   275: iconst_4
    //   276: iand
    //   277: ifeq +11 -> 288
    //   280: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   283: ldc 13
    //   285: invokevirtual 391	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   288: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   291: ldc 1
    //   293: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   296: goto +55 -> 351
    //   299: iinc 8 1
    //   302: aload_2
    //   303: ldc2_w 459
    //   306: invokevirtual 445	java/lang/Object:wait	(J)V
    //   309: aload_0
    //   310: aload 5
    //   312: getfield 372	com/templar/games/stormrunner/GameApplet$ImageTrack:image	Ljava/awt/Image;
    //   315: aload_2
    //   316: invokevirtual 329	java/awt/Component:checkImage	(Ljava/awt/Image;Ljava/awt/image/ImageObserver;)I
    //   319: istore 9
    //   321: iload 9
    //   323: sipush 240
    //   326: iand
    //   327: ifeq -195 -> 132
    //   330: goto +21 -> 351
    //   333: astore 10
    //   335: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   338: ldc 94
    //   340: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   343: aload 10
    //   345: invokevirtual 392	java/lang/Throwable:printStackTrace	()V
    //   348: goto +3 -> 351
    //   351: aload 6
    //   353: monitorexit
    //   354: goto +55 -> 409
    //   357: aload 6
    //   359: monitorexit
    //   360: athrow
    //   361: aload_0
    //   362: getfield 294	com/templar/games/stormrunner/GameApplet:CacheTracker	Ljava/awt/MediaTracker;
    //   365: aload 5
    //   367: getfield 372	com/templar/games/stormrunner/GameApplet$ImageTrack:image	Ljava/awt/Image;
    //   370: iconst_0
    //   371: invokevirtual 317	java/awt/MediaTracker:addImage	(Ljava/awt/Image;I)V
    //   374: aload_0
    //   375: getfield 294	com/templar/games/stormrunner/GameApplet:CacheTracker	Ljava/awt/MediaTracker;
    //   378: iconst_0
    //   379: ldc2_w 459
    //   382: invokevirtual 446	java/awt/MediaTracker:waitForID	(IJ)Z
    //   385: pop
    //   386: goto +10 -> 396
    //   389: astore 6
    //   391: aload 6
    //   393: invokevirtual 392	java/lang/Throwable:printStackTrace	()V
    //   396: aload_0
    //   397: getfield 294	com/templar/games/stormrunner/GameApplet:CacheTracker	Ljava/awt/MediaTracker;
    //   400: aload 5
    //   402: getfield 372	com/templar/games/stormrunner/GameApplet$ImageTrack:image	Ljava/awt/Image;
    //   405: iconst_0
    //   406: invokevirtual 402	java/awt/MediaTracker:removeImage	(Ljava/awt/Image;I)V
    //   409: aload 5
    //   411: invokestatic 331	java/lang/System:currentTimeMillis	()J
    //   414: putfield 439	com/templar/games/stormrunner/GameApplet$ImageTrack:time	J
    //   417: return
    //   418: astore_3
    //   419: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   422: ldc 16
    //   424: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   427: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   430: ldc 16
    //   432: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   435: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   438: ldc 7
    //   440: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   443: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   446: ldc 16
    //   448: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   451: getstatic 385	java/lang/System:out	Ljava/io/PrintStream;
    //   454: ldc 16
    //   456: invokevirtual 395	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   459: aload_3
    //   460: athrow
    //   461: return
    //
    // Exception table:
    //   from	to	target	type
    //   114	330	333	java/lang/InterruptedException
    //   91	351	357	finally
    //   374	386	389	java/lang/InterruptedException
    //   10	417	418	java/lang/OutOfMemoryError } 
  protected void setupBuffer() { this.buffer = createImage(getSize().width, getSize().height);
    this.graphics = this.buffer.getGraphics(); }

  public Object getPaintLock() {
    return this.PaintLock;
  }

  public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    return false;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    synchronized (this.PaintLock)
    {
      if (this.graphics == null) {
        setupBuffer();
      }

      this.graphics.setClip(paramGraphics.getClip());
      paint(this.graphics);
      paramGraphics.drawImage(this.buffer, 0, 0, null);

      getToolkit().sync();

      return;
    }
  }

  public int getLastState()
  {
    return this.LastState; } 
  public OptionsPanel getOptionsPanel() { return this.optionspanel;
  }

  public void mute()
  {
    this.wasMuted = audio.isMuted();
    audio.mute();
  }

  public void unMute()
  {
    if (!(this.wasMuted))
      audio.unMute(); 
  }

  public boolean isMidGame() {
    return this.playing;
  }

  public void setMidGame(boolean paramBoolean) {
    this.playing = paramBoolean;
  }

  public void sendStatusMessage(String paramString)
  {
    sendStatusMessage(paramString, true);
  }

  public void sendStatusMessage(String paramString, boolean paramBoolean)
  {
    this.statpanel.addStatusMessage(paramString);

    if ((paramBoolean) && (this.State == 2))
      setState(this.LastState);
  }

  public void newGame()
  {
    URL localURL = null;
    InputStream localInputStream = getClass().getResourceAsStream("/com/templar/games/stormrunner/media/scenes/newgame.pac");
    try
    {
      if (localInputStream == null)
      {
        localURL = new URL(getDocumentBase(), "com/templar/games/stormrunner/media/scenes/newgame.pac");
        localInputStream = localURL.openStream();
      }
      this.optionspanel.setEnabled(false);
      DataInputStream localDataInputStream = new DataInputStream(new MonitoredInputStream(localInputStream, null));
      int i = localDataInputStream.readInt();
      byte[] arrayOfByte = new byte[i];
      localDataInputStream.readFully(arrayOfByte);
      this.progressDialog = new ProgressComponent("Please Wait...", i + 100000, this);
      this.progressDialog.setLocation(5, 337);
      this.optionspanel.add(this.progressDialog, 0);
      this.progressDialog.setVisible(true);

      BufferedInputStream localBufferedInputStream = new BufferedInputStream(new MonitoredInputStream(
        new ByteArrayInputStream(arrayOfByte), this.progressDialog));
      Loader localLoader = new Loader(this, this, localBufferedInputStream);
      UtilityThread localUtilityThread = new UtilityThread(0, localLoader, localLoader.getClass().getMethod("newgame", null), false);
      localUtilityThread.start();
      Debug.println(localUtilityThread);

      return;
    }
    catch (Exception localException)
    {
      Debug.println("Couldn't load " + localURL + ".\n" + localException);
      localException.printStackTrace();
    }
  }

  public void saveGame()
  {
    if (!(this.playing))
    {
      Debug.println("Can't save if you're not playing.");
      audio.play("ButtonError");
      return;
    }

    try
    {
     // PrivilegeManager.enablePrivilege("UniversalFileAccess");
     // PrivilegeManager.enablePrivilege("UniversalPropertyRead");
      //PolicyEngine.assertPermission(PermissionID.FILEIO);
    }
    //catch (ForbiddenTargetException localForbiddenTargetException)
    //{
     // System.err.println("Stormrunner: saveGame(): User clicked Deny.");
     // return;
    //}
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      return;
    }

    if (this.buildpanel.isBayOccupied())
    {
      setState(0);
      sendStatusMessage("Cannot save while a robot is in the bay. Store, Engage, or Dismantle this robot first.", false);
      return;
    }

    Frame localFrame = null;
    Object localObject = this;

    while ((localObject != null) && (!(localObject instanceof Frame)))
      localObject = ((Component)localObject).getParent();

    if (localObject == null)
    {
      Debug.println("cant find a frame");
      return;
    }

    localFrame = (Frame)localObject;
    if (this.savegameWindow == null)
      this.savegameWindow = new LoadSaveFrame(localFrame, 1);
    else {
      this.savegameWindow.toFront();
    }

    File localFile = this.savegameWindow.getResponse();

    if (localFile == null)
    {
      Debug.println("Aborting save");
      this.savegameWindow = null;
      return;
    }

    try
    {
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(
        new BufferedOutputStream(
        new GZIPOutputStream(
        new FileOutputStream(localFile))));

      Loader localLoader = new Loader(this, this, localObjectOutputStream, this.CurrentGameState.getTickCount(), this.savegameWindow.getDescription());
      localLoader.activate();
    }
    catch (Exception localException2)
    {
      System.err.println("Stormrunner: EXCEPTION DURING SAVE ATTEMPT! (" + localFile + ")");
      localException2.printStackTrace();

      new MessageDialog(localFrame, "Error!", "Error saving game!", "Abort");
    }

    try
    {
      ////
    	//PrivilegeManager.revertPrivilege("UniversalFileAccess");
      //PrivilegeManager.revertPrivilege("UniversalPropertyRead");
      //PolicyEngine.revertPermission(PermissionID.FILEIO);
    }
    catch (Exception localException3)
    {
      System.err.println("Stormrunner: Sanity check: failure during revertPrivilege following save.");
      localException3.printStackTrace();
    }

    this.savegameWindow = null;
  }

  public void loadGame()
  {
    try
    {
////
    //	PrivilegeManager.enablePrivilege("UniversalFileAccess");
      //PrivilegeManager.enablePrivilege("UniversalPropertyRead");
      //PolicyEngine.assertPermission(PermissionID.FILEIO);
    }
    catch (SystemException e)//ForbiddenTargetException localForbiddenTargetException)
    {
      System.err.println("Stormrunner: loadGame(): User clicked Deny.");
      return;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      return;
    }

    Frame localFrame = null;
    Object localObject = this;
    while ((localObject != null) && (!(localObject instanceof Frame)))
      localObject = ((Component)localObject).getParent();
    if (localObject == null)
    {
      Debug.println("cant find a frame");
      return;
    }
    localFrame = (Frame)localObject;
    if (this.savegameWindow == null)
      this.savegameWindow = new LoadSaveFrame(localFrame, 0);
    else
      this.savegameWindow.toFront();

    File localFile = this.savegameWindow.getResponse();
    if (localFile == null)
    {
      Debug.println("Aborting load");
      this.savegameWindow = null;
      return;
    }

    Loader localLoader = null;
    try
    {
      this.optionspanel.setEnabled(false);

      this.progressDialog = new ProgressComponent("Please Wait...", (int)localFile.length() + 8192, this);
      this.progressDialog.setLocation(5, 337);
      this.optionspanel.add(this.progressDialog, 0);
      this.progressDialog.setVisible(true);

      ObjectInputStream localObjectInputStream = new ObjectInputStream(
        new BufferedInputStream(
        new GZIPInputStream(
        new MonitoredInputStream(
        new FileInputStream(localFile), this.progressDialog))));

      localLoader = new Loader(this, this, localObjectInputStream);
      new UtilityThread(0, localLoader, localLoader.getClass().getMethod("activate", null), false).start();
    }
    catch (Exception localException2)
    {
      if (localLoader != null) {
        localLoader.failout("Error loading game! Contact bugs@legomindstorm.com for assistance.", "Stormrunner: Error loading saved game from file: " + localFile, localException2);

        return;
      }

      System.err.println("Stormrunner: EXCEPTION DURING SAVE ATTEMPT! (" + localFile + ")");
      localException2.printStackTrace();

      new MessageDialog(localFrame, "Error!", "Error loading saved game!", "Abort");

      return;
    }

    try
    {
     ////
    	//PrivilegeManager.revertPrivilege("UniversalFileAccess");
      //PrivilegeManager.revertPrivilege("UniversalPropertyRead");
      //PolicyEngine.revertPermission(PermissionID.FILEIO);
    }
    catch (Exception localException3)
    {
      System.err.println("Stormrunner: Sanity check: failure during revertPrivilege following load.");
      localException3.printStackTrace();
    }

    this.savegameWindow = null;
  }

  public double getLoadingVersion()
  {
    return this.VersionOfCurrentSavegame;
  }

  static UtilityThread access$0(GameApplet paramGameApplet)
  {
    return paramGameApplet.cacheThread;
  }
  
  
  
  
  class ImageTrack
  {
    private final GameApplet this$0;
    public Image image;
    public long time;

    public ImageTrack(GameApplet paramGameApplet, Image paramImage)
    {
      this.this$0 = paramGameApplet;

      this.this$0 = 
        paramGameApplet;

      this.image = paramImage;
      this.time = -1L;
    }

    public String toString() {
      StringBuffer localStringBuffer = new StringBuffer("ImageTrack[");
      if (this.this$0.imageFilename.containsKey(this.image))
        localStringBuffer.append(this.this$0.imageFilename.get(this.image));
      else
        localStringBuffer.append("null");
      localStringBuffer.append(",");
      localStringBuffer.append(this.time);
      localStringBuffer.append("]");
      return localStringBuffer.toString();
    }
  }
}