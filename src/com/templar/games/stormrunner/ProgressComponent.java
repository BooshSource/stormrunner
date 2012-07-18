package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ProgressBar;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.util.ProgressListener;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

public class ProgressComponent extends SimpleContainer
  implements ProgressListener
{
  ImageComponent Background;
  ProgressBar Bar;

  public ProgressComponent(String paramString, int paramInt, ImageRetriever paramImageRetriever)
  {
    setLayout(null);

    this.Background = new ImageComponent(paramImageRetriever, "com/templar/games/stormrunner/media/images/titlescreen/progresspanel.jpg", true, false);
    setSize(this.Background.getSize());
    this.Background.setLocation(0, 0);

    this.Bar = new ProgressBar(0, paramInt, paramString);
    this.Bar.setBounds(5, 4, getSize().width - 10, getSize().height - 8);

    add(this.Bar);
    add(this.Background);
  }

  public void notifyProgress() {
    notifyProgress(1);
  }

  public void notifyProgress(String paramString, int paramInt) {
    this.Bar.setMessage(paramString);
    notifyProgress(paramInt);
  }

  public void notifyProgress(int paramInt) {
    this.Bar.setValue(this.Bar.getValue() + paramInt);
    this.Bar.repaint();
  }

  public void setValue(int paramInt) {
    this.Bar.setValue(paramInt);
    this.Bar.repaint(); }

  public int getValue() { return this.Bar.getValue(); } 
  public int getMaximum() { return this.Bar.getMaximum();
  }

  public void addNotify()
  {
    addNotify();

    repaint();
  }
}