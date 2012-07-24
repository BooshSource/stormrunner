package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutScreen extends SimpleContainer
  implements ActionListener
{
  protected static int TEXT_MARGINS = 50;
  protected String AboutMessage = "\tc64,128,128]Stormrunner\nwas created by\nTemplar Studios, L.L.C.\nfor\nLEGO MINDSTORMS\na brand of THE LEGO GROUP\n\td1000]\n\tc64,128,128]Cast\nClark Winter: Mather Zickel\nJune Castle: Jane Casserly\n\td1000]\n\tc64,128,128]Produced by\nJim Lahren and Russell Stoll\n\td1000]\n\tc64,128,128]Game Design\nPeter Mack\n\n\tc64,128,128]Art and Animation\nGordon Klimes\n\n\tc64,128,128]Software Architecture\nDavid Wood\n\n\tc64,128,128]Art Production\nAlexi Logothetis\nVincent Arnone\n\n\tc64,128,128]Software Production\nCraig Venz\n\n\tc64,128,128]Sound Design and\n\tc64,128,128]Original Score\nJustin Luchter\n\n\tc64,128,128]Project Manager\nBrian Fisher\n\td1000]\n\tc64,128,128]Testing\nNate Acheson\n\td1000]\n\n\n\n\n\n\nCopyright MM\nTHE LEGO GROUP\nThe LEGO MINDSTORMS logo is a trademark of THE LEGO GROUP.\n\n\n\n";
  protected ActionListener listener;
  protected ImageButton BackButton;
  protected TextContainer TextDisplay;

  public AboutScreen(ActionListener paramActionListener, ImageRetriever paramImageRetriever)
  {
    this.listener = paramActionListener;

    this.TextDisplay = new TextContainer(this.AboutMessage, Color.lightGray, new Font("SansSerif", 1, 16));
    this.TextDisplay.setStreak(true, 25, 7, 50);
    this.TextDisplay.setVerticalLineSpacing(5);
    this.TextDisplay.setCenterAligned(true);
    this.BackButton = new ImageButton(paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_back.jpg"), 
      null, 
      paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/titlescreen/title_back-ro.jpg"));
    this.BackButton.addActionListener(this);

    setLayout(null);

    add(this.TextDisplay);
    add(this.BackButton);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    this.listener.actionPerformed(new ActionEvent(this, 1001, "AboutScreen Back"));
    this.BackButton.resetImage();
  }

  public void invalidate()
  {
    super.invalidate();

    Rectangle localRectangle = new Rectangle(TEXT_MARGINS, 0, getSize().width - TEXT_MARGINS * 2, getSize().height - this.BackButton.getSize().height);
    this.TextDisplay.setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
    this.TextDisplay.setHardSize(localRectangle.width, localRectangle.height);
    this.BackButton.setLocation(0, getSize().height - this.BackButton.getSize().height);
  }
}