package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;
import java.awt.Container;

public interface ReportingContainerListener
{
  public abstract void componentAdded(Container paramContainer, Component paramComponent);

  public abstract void componentRemoved(Container paramContainer, Component paramComponent);

  public abstract void allComponentsRemoved(Container paramContainer);
}