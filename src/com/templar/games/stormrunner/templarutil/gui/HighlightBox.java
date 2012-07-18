package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class HighlightBox extends Component
  implements ComponentListener, ContainerListener
{
  public static final Insets DEFAULTINSETS = new Insets(5, 5, 5, 5);
  protected Component Target;
  protected Insets Space;
  protected Color HighlightColor = Color.red;
  protected int LineThickness = 3;
  protected Container TargetParent;

  public void setTarget(Component paramComponent)
  {
    setTarget(paramComponent, null);
  }

  public void setTarget(Component paramComponent, Insets paramInsets)
  {
    if (this.Target != null)
    {
      this.Target.removeComponentListener(this);
      if (this.TargetParent != null)
      {
        this.TargetParent.removeContainerListener(this);
        this.TargetParent = null;
      }
    }

    if (paramInsets == null)
      this.Space = DEFAULTINSETS;
    else
      this.Space = paramInsets;
    this.Target = paramComponent;

    super.setLocation(paramComponent.getLocation().x - this.Space.left, paramComponent.getLocation().y - this.Space.top);
    super.setSize(paramComponent.getSize().width + this.Space.left + this.Space.right, paramComponent.getSize().height + this.Space.top + this.Space.bottom);

    paramComponent.addComponentListener(this);

    if (this.Target.getParent() != null)
    {
      this.TargetParent = this.Target.getParent();
      this.TargetParent.addContainerListener(this);
    }
  }

  public void setColor(Color paramColor)
  {
    this.HighlightColor = paramColor;
  }

  public void setLineThickness(int paramInt)
  {
    this.LineThickness = paramInt;
  }

  public void componentMoved(ComponentEvent paramComponentEvent)
  {
    super.setLocation(this.Target.getLocation().x - this.Space.left, this.Target.getLocation().y - this.Space.top);
  }

  public void componentResized(ComponentEvent paramComponentEvent)
  {
    super.setSize(this.Target.getSize().width + this.Space.left + this.Space.right, this.Target.getSize().height + this.Space.top + this.Space.bottom); }

  public void componentHidden(ComponentEvent paramComponentEvent) { }

  public void componentShown(ComponentEvent paramComponentEvent) {
  }

  public void componentRemoved(ContainerEvent paramContainerEvent) {
    if ((paramContainerEvent.getChild() == this.Target) && (super.getParent() != null))
      super.getParent().remove(this);
  }

  public void componentAdded(ContainerEvent paramContainerEvent) {
  }

  public void update(Graphics paramGraphics) {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    paramGraphics.setColor(this.HighlightColor);

    for (int i = 0; i < this.LineThickness; ++i)
    {
      paramGraphics.drawRect(i, i, super.getSize().width - 1 - i * 2, super.getSize().height - 1 - i * 2);
    }
  }
}