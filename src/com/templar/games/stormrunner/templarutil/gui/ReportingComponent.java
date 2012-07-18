package com.templar.games.stormrunner.templarutil.gui;

public interface ReportingComponent
{
  public abstract void addReportingComponentListener(ReportingComponentListener paramReportingComponentListener);

  public abstract void removeReportingComponentListener(ReportingComponentListener paramReportingComponentListener);

  public abstract void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}