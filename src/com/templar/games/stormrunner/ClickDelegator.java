package com.templar.games.stormrunner;

import java.awt.Component;
import java.awt.Graphics;

public class ClickDelegator extends Component
{
  protected Renderer CurrentRenderer;
  protected Scene CurrentScene;
  protected MouseHandler mh;

  public ClickDelegator(Renderer paramRenderer, Scene paramScene)
  {
    this.mh = new MouseHandler(this);
    super.addMouseListener(this.mh);

    this.CurrentRenderer = paramRenderer;
    this.CurrentScene = paramScene;
  }

  public void update(Graphics paramGraphics)
  {
  }

  public void paint(Graphics paramGraphics)
  {
  }
}