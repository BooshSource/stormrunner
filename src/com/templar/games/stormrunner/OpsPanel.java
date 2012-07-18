package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpsPanel extends SimpleContainer
  implements ActionListener
{
  public static final int BAY = 0;
  public static final int VIEW = 1;
  public static final int OPTIONS = 2;
  protected GameApplet applet;
  protected int State = 2;
  protected ImageComponent Background;
  protected ImageButton View;
  protected ImageButton Bay;
  protected ImageButton Options;
  protected ImageButton[] Buttons;
  protected Image[][] Images;

  public OpsPanel(GameApplet paramGameApplet, ImageRetriever paramImageRetriever)
  {
    this.applet = paramGameApplet;

    setLayout(null);

    this.Images = new Image[3][3];
    this.Images[0][0] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_bay-off.gif");
    this.Images[0][1] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_bay-in.gif");
    this.Images[0][2] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_bay-on.gif");
    this.Images[1][0] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_view-off.gif");
    this.Images[1][1] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_view-in.gif");
    this.Images[1][2] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_view-on.gif");
    this.Images[2][0] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_options-off.gif");
    this.Images[2][1] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_options-in.gif");
    this.Images[2][2] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_options-on.gif");

    this.Background = new ImageComponent(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/opspanel/opspanel_back.gif"), true, false);
    this.Background.setLocation(0, 0);
    this.Bay = new ImageButton(this.Images[0][0], this.Images[0][1], null);
    this.Bay.setActionCommand("bay");
    this.Bay.addActionListener(this);
    this.Bay.setLocation(5, 3);
    this.View = new ImageButton(this.Images[1][0], this.Images[1][1], null);
    this.View.setActionCommand("view");
    this.View.addActionListener(this);
    this.View.setLocation(47, 3);
    this.Options = new ImageButton(this.Images[2][0], this.Images[2][1], null);
    this.Options.setActionCommand("options");
    this.Options.addActionListener(this);
    this.Options.setLocation(90, 3);

    this.Buttons = new ImageButton[3];
    this.Buttons[0] = this.Bay;
    this.Buttons[1] = this.View;
    this.Buttons[2] = this.Options;

    setState(this.State);

    add(this.Bay);
    add(this.View);
    add(this.Options);
    add(this.Background);

    setSize(this.Background.getSize());
  }

  public void setState(int paramInt)
  {
    if (this.State != paramInt)
    {
      this.Buttons[this.State].setImages(this.Images[this.State][0], this.Images[this.State][1], null);
      this.State = paramInt;
      this.Buttons[this.State].setImage(this.Images[this.State][2]);

      this.applet.internalSetState(this.State);
    }
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getActionCommand().compareTo("bay") == 0)
    {
      setState(0);

      return;
    }

    if (paramActionEvent.getActionCommand().compareTo("view") == 0)
    {
      setState(1);

      return;
    }

    if (paramActionEvent.getActionCommand().compareTo("options") == 0)
    {
      setState(2);
    }
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
    return this.Background.getSize();
  }
}