package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class OptionsPanel extends SimpleContainer
  implements ActionListener
{
  private GameApplet appletRef;
  private ImageComponent background;
  private ImageButton start;
  private ImageButton load;
  private ImageButton save;
  private ImageButton mute;
  private ImageButton help;
  private ImageButton about;
  private ImageButton cont;
  private TextContainer version;
  private AboutScreen aboutscreen;
  private TextComponent text;
  private SimpleContainer MainScreen;
  private boolean Enabled = true;

  public OptionsPanel(GameApplet paramGameApplet)
  {
    GameApplet localGameApplet = paramGameApplet;
    this.appletRef = paramGameApplet;
    setSize(480, 360);
    this.background = new ImageComponent(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/titlescreen.jpg"));

    this.MainScreen = new SimpleContainer();
    this.MainScreen.setBounds(getBounds());

    this.start = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_start.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_start-ro.jpg"));
    this.load = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_load.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_load-ro.jpg"));
    this.save = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_save.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_save-ro.jpg"));
    this.mute = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_mute.jpg"), 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_mute-ro.jpg"), 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_mute-ro.jpg"));
    this.mute.setOnImage(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_mute-ro.jpg"));
    this.about = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_about.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_about-ro.jpg"));
    this.help = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_help.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_help-ro.jpg"));
    this.cont = new ImageButton(localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_continue.jpg"), 
      null, 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_continue-ro.jpg"));
    String str = String.valueOf(1.1000000000000001D);
    this.version = new TextContainer(str, Color.yellow, new Font("SansSerif", 0, 9));
    this.background.setLocation(0, 0);
    this.background.setSize(480, 360);
    add(this.background);
    this.start.setLocation(101, 133);
    this.start.setSize(this.start.getSize());
    this.start.setClickSound(GameApplet.audio, "ButtonClick");
    this.load.setLocation(101, 201);
    this.load.setSize(this.load.getSize());
    this.load.setClickSound(GameApplet.audio, "ButtonClick");
    this.save.setLocation(101, 167);
    this.save.setSize(this.save.getSize());
    this.save.setClickSound(GameApplet.audio, "ButtonClick");
    this.mute.setLocation(101, 235);
    this.mute.setSize(this.mute.getSize());
    this.mute.setClickSound(GameApplet.audio, "ButtonClick");
    this.help.setLocation(101, 269);
    this.help.setSize(this.mute.getSize());
    this.help.setClickSound(GameApplet.audio, "ButtonClick");
    this.about.setLocation(101, 302);
    this.about.setSize(this.mute.getSize());
    this.about.setClickSound(GameApplet.audio, "ButtonClick");

    this.aboutscreen = new AboutScreen(this, this.appletRef);
    this.aboutscreen.setBounds(0, 110, 480, 250);

    this.cont.setLocation(345, 339);
    this.cont.setSize(this.cont.getSize());
    this.cont.setClickSound(GameApplet.audio, "ButtonClick");

    this.version.setLocation(480 - this.version.getSize().width - 5, 
      360 - this.version.getSize().height - 5);
    this.MainScreen.add(this.version, 0);

    this.MainScreen.add(this.load, 0);
    this.MainScreen.add(this.save, 0);
    this.MainScreen.add(this.mute, 0);
    this.MainScreen.add(this.start, 0);
    this.MainScreen.add(this.help, 0);
    this.MainScreen.add(this.about, 0);
    add(this.MainScreen, 0);

    this.start.addActionListener(this);
    this.start.setActionCommand("new");
    this.load.addActionListener(this);
    this.load.setActionCommand("load");
    this.save.addActionListener(this);
    this.save.setActionCommand("save");
    this.mute.addActionListener(this);
    this.mute.setActionCommand("mute");
    this.help.addActionListener(this);
    this.help.setActionCommand("help");
    this.about.addActionListener(this);
    this.about.setActionCommand("about");
    this.cont.addActionListener(this);
    this.cont.setActionCommand("cont");
  }

  public void actionPerformed(ActionEvent paramActionEvent) {
    String str1 = paramActionEvent.getActionCommand();
    if (str1.compareTo("save") == 0)
    {
      this.appletRef.saveGame();
    }

    if (str1.compareTo("load") == 0)
    {
      this.appletRef.loadGame();
    }

    if (str1.compareTo("new") == 0)
    {
      this.appletRef.newGame();
    }

    if (str1.compareTo("mute") == 0)
    {
      if (GameApplet.audio.isMuted())
      {
        GameApplet.audio.unMute();
        this.mute.setOn(false);
      }
      else
      {
        GameApplet.audio.mute();
        this.mute.setOn(true);
      }
    }
    if (str1.compareTo("cont") == 0)
    {
      this.appletRef.setState(this.appletRef.getLastState());
    }
    if (str1.compareTo("help") == 0)
    {
      URL localURL = this.appletRef.getHelpURL();
      String str2 = this.appletRef.getHelpTarget();
      if (localURL != null)
        if (str2 == null)
          this.appletRef.getAppletContext().showDocument(localURL);
        else
          this.appletRef.getAppletContext().showDocument(localURL, str2);
    }
    if (str1.compareTo("about") == 0)
    {
      remove(this.MainScreen);
      this.about.resetImage();
      add(this.aboutscreen, 0);
      repaint();
    }
    if (str1.compareTo("AboutScreen Back") == 0)
    {
      remove(this.aboutscreen);
      add(this.MainScreen, 0);
      repaint();
    }
  }

  public void addNotify()
  {
    addNotify();

    if (this.appletRef.isMidGame())
    {
      add(this.cont, 0);
      this.version.setLocation(480 - this.version.getSize().width - 5, 
        360 - this.version.getSize().height - 20);
    }
    else
    {
      this.version.setLocation(480 - this.version.getSize().width - 5, 
        360 - this.version.getSize().height - 5);
    }

    this.cont.repaint();
  }

  public void removeNotify()
  {
    remove(this.cont);

    this.start.resetImage();
    this.cont.resetImage();
    this.load.resetImage();
    this.save.resetImage();
    this.help.resetImage();
    this.about.resetImage();
    removeNotify();
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.Enabled = paramBoolean;
    this.start.setEnabled(paramBoolean);
    this.load.setEnabled(paramBoolean);
    this.save.setEnabled(paramBoolean);
    this.mute.setEnabled(paramBoolean);
    this.help.setEnabled(paramBoolean);
    this.about.setEnabled(paramBoolean);
    this.cont.setEnabled(paramBoolean);
  }

  public boolean getEnabled()
  {
    return this.Enabled;
  }
}