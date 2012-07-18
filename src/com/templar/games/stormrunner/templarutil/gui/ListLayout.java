package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class ListLayout
  implements LayoutManager
{
  protected int ItemGap;
  protected int counter;
  protected int max;
  protected boolean horizontal;

  public ListLayout()
  {
    this.horizontal = false;
  }

  public ListLayout(boolean paramBoolean)
  {
    this.horizontal = paramBoolean;
  }

  public ListLayout(boolean paramBoolean, int paramInt)
  {
    this(paramBoolean);
    this.ItemGap = paramInt;
  }

  public void setGap(int paramInt)
  {
    this.ItemGap = paramInt; }

  public void addLayoutComponent(String paramString, Component paramComponent) { }

  public void removeLayoutComponent(Component paramComponent) {
  }

  public void layoutContainer(Container paramContainer) {
    Component[] arrayOfComponent = paramContainer.getComponents();

    this.counter = 0;
    for (int i = 0; i < arrayOfComponent.length; ++i)
    {
      Component localComponent = paramContainer.getComponent(i);

      if (this.horizontal)
        localComponent.setLocation(this.counter, 0);
      else
        localComponent.setLocation(0, this.counter);

      localComponent.setSize(localComponent.getSize());

      if (this.horizontal)
      {
        this.counter += localComponent.getSize().width + this.ItemGap;
        this.max = Math.max(this.max, localComponent.getSize().height);
      }
      else
      {
        this.counter += localComponent.getSize().height + this.ItemGap;
        this.max = Math.max(this.max, localComponent.getSize().width);
      }
    }
  }

  public Dimension preferredLayoutSize(Container paramContainer)
  {
    return minimumLayoutSize(paramContainer);
  }

  public Dimension minimumLayoutSize(Container paramContainer)
  {
    if (this.horizontal)
      return new Dimension(this.counter, this.max);

    return new Dimension(this.max, this.counter);
  }
}