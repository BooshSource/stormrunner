package com.templar.games.stormrunner;

//import com.ms.security.PermissionID;
//import com.ms.security.PolicyEngine;
import com.templar.games.stormrunner.build.BuildPanel;
import com.templar.games.stormrunner.build.CargoBay;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.program.editor.Editor;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.applet.TApplet;
import com.templar.games.stormrunner.templarutil.audio.AppletAudioDevice;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.audio.NullAudioDevice;
import com.templar.games.stormrunner.templarutil.audio.SunAudioDevice;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageFilenameProvider;
import com.templar.games.stormrunner.templarutil.gui.ImagePaintListener;
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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
  implements ImageRetriever, ImagePaintListener, ImageFilenameProvider
{
  public static final double VERSION = 1.1D;
  public static final double SAVE_FORMAT_VERSION = 0.5D;
  public static final double MINIMUM_SAVE_FORMAT_VERSION = 0.4D;
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
  protected MediaTracker CacheTracker = new MediaTracker(this);

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
    if (j != 0) {
      this.optionspanel.add(this.progressDialog, 0);
    }
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
      break;
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
      break;
    case 2:
      this.CurrentGameState.stop();
      this.TopLayer.remove(this.statpanel);
      this.TopLayer.remove(this.opspanel);
      this.TopLayer.add(this.OptionsScreen, 0);
      

      switch (paramInt)
      {
      case 0:
        this.TopLayer.remove(this.BayScreen);
       break;
      case 1:
        this.TopLayer.remove(this.ViewScreen);
        this.CurrentRenderer.setVisible(false);
       break;
      case 2:
        this.CurrentGameState.start();
        this.TopLayer.remove(this.OptionsScreen);
        this.TopLayer.add(this.statpanel, 0);
        this.TopLayer.add(this.opspanel, 0);
       break;
      }
      break;
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
      ImageTrack localImageTrack = (ImageTrack)localEnumeration.nextElement();

      int i = checkImage(localImageTrack.image, this);
      int j = (i & 0x20) != 0 ? 1 : 0;
      String str1;
      if ((str1 = (String)this.imageFilename.get(localImageTrack.image)) != null) {
        for (int k = 0; (j != 0) && (k < this.ImageCacheActiveList.size()); k++)
        {
          String str2 = (String)this.ImageCacheActiveList.elementAt(k);
          if (str1.indexOf(str2) >= 0)
            j = 0;
        }
      }
      if (j == 0)
        continue;
      localImageTrack.time = -1L;
      localImageTrack.image.flush();
    }

    System.gc();
  }

  public synchronized boolean cacheClean()
  {
    long l = System.currentTimeMillis();

    Enumeration localEnumeration = this.images.elements();
    while (localEnumeration.hasMoreElements())
    {
      ImageTrack localImageTrack = (ImageTrack)localEnumeration.nextElement();

      int i = checkImage(localImageTrack.image, this);

      int j = (i & 0x20) != 0 ? 1 : 0;
      String str1;
      if ((str1 = (String)this.imageFilename.get(localImageTrack.image)) != null) {
        for (int k = 0; (j != 0) && (k < this.ImageCacheActiveList.size()); k++)
        {
          String str2 = (String)this.ImageCacheActiveList.elementAt(k);
          if (str1.indexOf(str2) >= 0)
            j = 0;
        }
      }
      if ((localImageTrack.time == -1L) || (j == 0) || (localImageTrack.time + 20000L >= l))
      {
        continue;
      }

      localImageTrack.time = -1L;
      localImageTrack.image.flush();
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
     ImageTrack localObject2 = (ImageTrack)localObject1;

      return ((ImageTrack)localObject2).image;
    }

    Object localObject2 = super.getImage(paramURL, paramString);
    if (localObject2 == null)
    {
      Debug.println("super.getImage(" + paramURL + "," + paramString + ") returned null");
    }
    else
    {
      ImageTrack localImageTrack = new ImageTrack((Image)localObject2);
      this.images.put(paramString, localImageTrack);
      this.imageFilename.put(localObject2, paramString);
    }
    return (Image)localObject2;
  }

  public Image getImage(String paramString)
  {
    if ((paramString != "") && (paramString != null))
      return getImage(getDocumentBase(), paramString);
    return null;
  }

  public String getImageFilename(Image paramImage)
  {
    return (String)this.imageFilename.get(paramImage);
  }

  public void imagePainted(ImageComponent paramImageComponent, Image paramImage)
  {
    hitCache(paramImage, paramImageComponent);
  }

  public void hitCache(Image paramImage)
  {
    hitCache(paramImage, null);
  }

  public synchronized void hitCache(Image paramImage, ImageComponent paramImageComponent)
  {
    if (paramImage == null)
    {
      Debug.println("Image passed to hitCache is null");
      return;
    }

    try
    {
      Object localObject = this.imageFilename.get(paramImage);
      if (localObject != null)
      {
        String str = (String)localObject;
        ImageTrack localImageTrack = (ImageTrack)this.images.get(str);
        if (localImageTrack == null)
        {
          Debug.println("There's a null entry in the ImageCache.");
          Debug.println(str + "==" + localImageTrack);
        }

        if (paramImageComponent != null)
        {
          synchronized (paramImageComponent)
          {
            if (localImageTrack.time == -1L)
            {
              prepareImage(localImageTrack.image, paramImageComponent);
              try
              {
                int i = 0;
                int j = checkImage(localImageTrack.image, paramImageComponent);
                while ((j & 0xF0) == 0)
                {
                  if (i > 2)
                  {
                    System.out.println("Potential deadlock waiting for Image:");
                    System.out.println(str);
                    System.out.print("Flags: ");
                    if ((j & 0x80) != 0) System.out.print(" ABORT ");
                    if ((j & 0x40) != 0) System.out.print(" ERROR ");
                    if ((j & 0x20) != 0) System.out.print(" ALLBITS ");
                    if ((j & 0x10) != 0) System.out.print(" FRAMEBITS ");
                    if ((j & 0x8) != 0) System.out.print(" SOMEBITS ");
                    if ((j & 0x2) != 0) System.out.print(" HEIGHT ");
                    if ((j & 0x1) != 0) System.out.print(" WIDTH ");
                    if ((j & 0x4) != 0) System.out.print(" PROPERTIES ");
                    System.out.println("");
                    break;
                  }

                  i++;

                  paramImageComponent.wait(1000L);

                  j = checkImage(localImageTrack.image, paramImageComponent);
                }
              }
              catch (InterruptedException localInterruptedException2)
              {
                System.out.println("Someday I'd like to meet the kind of thread that interrupts this sort of thing.");
                localInterruptedException2.printStackTrace();
              }

            }

          }

        }

        this.CacheTracker.addImage(localImageTrack.image, 0);
        try
        {
          this.CacheTracker.waitForID(0, 1000L);
        }
        catch (InterruptedException localInterruptedException1)
        {
          localInterruptedException1.printStackTrace();
        }
        this.CacheTracker.removeImage(localImageTrack.image, 0);

        localImageTrack.time = System.currentTimeMillis();

        return;
      }

    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      System.out.println("           OOME Exception caught in hitcache!");
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

      throw localOutOfMemoryError;
    }
  }

  protected void setupBuffer()
  {
    this.buffer = createImage(getSize().width, getSize().height);
    this.graphics = this.buffer.getGraphics();
  }
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
      super.paint(this.graphics);
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
    if (!this.wasMuted)
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
      Loader localLoader = new Loader(this, localBufferedInputStream);
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
    if (!this.playing)
    {
      Debug.println("Can't save if you're not playing.");
      audio.play("ButtonError");
      return;
    }

    try
    {
      //PrivilegeManager.enablePrivilege("UniversalFileAccess");
      //PrivilegeManager.enablePrivilege("UniversalPropertyRead");
      //PolicyEngine.assertPermission(PermissionID.FILEIO);
    }
    //catch (ForbiddenTargetException localForbiddenTargetException)
    //{
    //  System.err.println("Stormrunner: saveGame(): User clicked Deny.");
    //  return;
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

    while ((localObject != null) && (!(localObject instanceof Frame))) {
      localObject = ((Component)localObject).getParent();
    }
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

      Loader localLoader = new Loader(this, localObjectOutputStream, this.CurrentGameState.getTickCount(), this.savegameWindow.getDescription());
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
     // PrivilegeManager.enablePrivilege("UniversalFileAccess");
     // PrivilegeManager.enablePrivilege("UniversalPropertyRead");
     // PolicyEngine.assertPermission(PermissionID.FILEIO);
    //}
    //catch (ForbiddenTargetException localForbiddenTargetException)
    //{
     // System.err.println("Stormrunner: loadGame(): User clicked Deny.");
     // return;
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
    else {
      this.savegameWindow.toFront();
    }
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

      localLoader = new Loader(this, localObjectInputStream);
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
     // PrivilegeManager.revertPrivilege("UniversalFileAccess");
      //PrivilegeManager.revertPrivilege("UniversalPropertyRead");
     // PolicyEngine.revertPermission(PermissionID.FILEIO);
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

  class ImageTrack
  {
    public Image image;
    public long time;

    public ImageTrack(Image arg2)
    {
      this.image = arg2;
      this.time = -1L;
    }

    public String toString() {
      StringBuffer localStringBuffer = new StringBuffer("ImageTrack[");
      if (GameApplet.this.imageFilename.containsKey(this.image))
        localStringBuffer.append(GameApplet.this.imageFilename.get(this.image));
      else
        localStringBuffer.append("null");
      localStringBuffer.append(",");
      localStringBuffer.append(this.time);
      localStringBuffer.append("]");
      return localStringBuffer.toString();
    }
  }

  public class Loader
  {
    int which;
    InputStream inp;
    OutputStream out;
    GameApplet applet;
    long tick;
    String desc;

    public Loader(GameApplet paramInputStream, InputStream arg3)
    {
      this.inp = arg3;
      this.applet = paramInputStream;
      this.which = 1;
    }

    public Loader(GameApplet paramOutputStream, OutputStream paramLong, long arg4, String arg6)
    {////
      this.out = paramLong;
      this.which = 0;
      this.applet = paramOutputStream;
      this.desc = arg6;
      this.tick = arg4;
    }

    public boolean newgame()
    {
      Scene localScene = null;
      try
      {
        localScene = SceneBuilder.readScene(this.applet, this.inp, GameApplet.this.progressDialog);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      GameState localGameState = new GameState(this.applet);
      World localWorld = new World();
      localWorld.addScene(localScene);
      Debug.println("adding scene to empty world");
      localGameState.setWorld(localWorld);
      Debug.println("adding 2048 here");
      GameApplet.this.progressDialog.notifyProgress(25000);

      if (GameApplet.this.CurrentGameState != null)
      {
        GameApplet.this.CurrentGameState.dispose();

        GameApplet.this.CurrentGameState = null;
      }
      this.applet.removeAll();
      GameApplet.this.CurrentGrid = null;
      GameApplet.this.statpanel = null;
      GameApplet.this.CurrentRenderer = null;
      GameApplet.this.CurrentEditor = null;
      GameApplet.this.opspanel = null;
      GameApplet.this.bay.clearRamp();
      GameApplet.this.bay = null;
      GameApplet.this.buildpanel = null;
      System.gc();
      GameApplet.this.cacheThread.politeStop();

      GameApplet.this.startup(localGameState);
      GameApplet.this.progressDialog.setValue(GameApplet.this.progressDialog.getMaximum());

      String str = GameApplet.this.getParameter("username");
      if (str == null)
        localGameState.setUsername("Unknown");
      else {
        localGameState.setUsername(str);
      }
      localGameState.setUserRank("Maint. Spec. 5th Class");

      if (GameApplet.this.getParameter("cheats") == null)
      {
        localGameState.setSecurityLevel(1);
        localGameState.setPolymetals(80);
        localGameState.setEnergyUnits(80);
      }
      else
      {
        localGameState.setSecurityLevel(5);
        localGameState.setPolymetals(999);
        localGameState.setEnergyUnits(999);
      }

      GameApplet.this.setState(0);

      GameApplet.this.playing = true;
      GameApplet.this.optionspanel.setEnabled(true);
      GameApplet.this.optionspanel.remove(GameApplet.this.progressDialog);
      GameApplet.this.progressDialog = null;
      return false;
    }

    public boolean activate()
    {
      String str = null;
      try
      {
        Object localObject;
        if (this.which == 0)
        {
          str = "Error saving game! Contact bugs@legomindstorm.com for assistance.";

          localObject = new ByteArrayOutputStream();
          ObjectOutputStream localObjectOutputStream;
          if (!(this.out instanceof ObjectOutputStream))
            localObjectOutputStream = new ObjectOutputStream((OutputStream)localObject);
          else {
            localObjectOutputStream = (ObjectOutputStream)this.out;
          }
          localObjectOutputStream.writeDouble(0.5D);
          localObjectOutputStream.writeDouble(1.1D);
          localObjectOutputStream.writeObject(this.desc);
          localObjectOutputStream.writeLong(this.tick);
          localObjectOutputStream.writeObject(GameApplet.this.CurrentGameState);
          localObjectOutputStream.writeInt(GameApplet.this.statpanel.getPanelState());
          localObjectOutputStream.writeInt(GameApplet.this.LastState);
          localObjectOutputStream.close();
        }
        else
        {
          str = "Error loading game! Contact bugs@legomindstorm.com for assistance.";

          if (!(this.inp instanceof ObjectInputStream))
            localObject = new ObjectInputStream(this.inp);
          else {
            localObject = (ObjectInputStream)this.inp;
          }

          double d = ((ObjectInputStream)localObject).readDouble();

          if (d < 0.4D)
          {
            failout("That save file doesn't work with this version of the game.", "Save file version (" + d + ") < MINIMUM_SAVE_FORMAT_VERSION (" + 0.4D + ")\nUnable to load.", null);
            return false;
          }

          GameApplet.this.VersionOfCurrentSavegame = ((ObjectInputStream)localObject).readDouble();
          ((ObjectInputStream)localObject).readObject();
          this.tick = ((ObjectInputStream)localObject).readLong();
          GameState localGameState = (GameState)((ObjectInputStream)localObject).readObject();
          ((ObjectInputStream)localObject).readInt();
          int i = ((ObjectInputStream)localObject).readInt();
          ((ObjectInputStream)localObject).close();

          localGameState.setTickCount(this.tick);

          if (GameApplet.this.CurrentGameState != null) {
            GameApplet.this.CurrentGameState.dispose();
          }
          GameApplet.this.CurrentGameState = null;
          GameApplet.this.CurrentRenderer = null;

          GameApplet.thisApplet.removeAll();

          GameApplet.this.CurrentGrid = null;
          GameApplet.this.statpanel = null;
          GameApplet.this.CurrentEditor = null;
          GameApplet.this.opspanel = null;
          GameApplet.this.bay.clearRamp();
          GameApplet.this.bay = null;
          GameApplet.this.buildpanel = null;
          System.gc();
          GameApplet.this.cacheThread.politeStop();

          GameApplet.this.progressDialog.notifyProgress(4096);

          GameApplet.this.startup(localGameState);

          GameApplet.this.buildpanel.setUsername(localGameState.getUsername());
          GameApplet.this.buildpanel.setUserRank(localGameState.getUserRank());
          GameApplet.this.buildpanel.setSecurityLevel(localGameState.getSecurityLevel());
          GameApplet.this.buildpanel.setPolymetals(localGameState.getPolymetals());
          GameApplet.this.buildpanel.setEnergyUnits(localGameState.getEnergyUnits());

          Enumeration localEnumeration = localGameState.getCurrentScene().getObjects().elements();
          while (localEnumeration.hasMoreElements())
          {
            PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
            if ((localPhysicalObject instanceof Trigger)) {
              ((Trigger)localPhysicalObject).setGameState(localGameState);
            }
          }
          GameApplet.this.progressDialog.notifyProgress(2048);

          GameApplet.this.statpanel.reportNewCurrentRobot(GameApplet.this.CurrentGameState.getCurrentRobot());

          GameApplet.this.CurrentEditor.setRobot(GameApplet.this.CurrentGameState.getCurrentRobot());

          this.applet.setState(i);

          GameApplet.this.playing = true;

          GameApplet.this.progressDialog.notifyProgress(2048);
          GameApplet.this.optionspanel.remove(GameApplet.this.progressDialog);
          GameApplet.this.progressDialog = null;
          GameApplet.this.optionspanel.setEnabled(true);
        }
      }
      catch (Exception localException)
      {
        failout(str, str, localException);
      }

      return false;
    }

    public void failout(String paramString1, String paramString2, Throwable paramThrowable)
    {
      System.err.println(paramString2);
      if (paramThrowable != null) {
        paramThrowable.printStackTrace();
      }

      Frame localFrame = null;
      Object localObject = GameApplet.thisApplet;
      while ((localObject != null) && (!(localObject instanceof Frame)))
        localObject = ((Component)localObject).getParent();
      if (localObject == null)
      {
        System.err.println("Stormrunner: Loader: failout(): Cant find a frame!");
        return;
      }
      localFrame = (Frame)localObject;
      new MessageDialog(localFrame, "Error!", paramString1, "Abort");

      if (GameApplet.this.progressDialog != null)
      {
        GameApplet.this.optionspanel.remove(GameApplet.this.progressDialog);
        GameApplet.this.progressDialog = null;
      }
      GameApplet.this.optionspanel.setEnabled(true);
      GameApplet.this.optionspanel.repaint();
    }
  }
}