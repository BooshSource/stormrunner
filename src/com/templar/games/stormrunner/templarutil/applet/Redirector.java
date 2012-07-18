package com.templar.games.stormrunner.templarutil.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Redirector extends Applet
{
  private static final String[][] pinfo = { 
    { "URL", "URL", "URL to rediret to on applet start." } };

  public void start()
  {
    String str = super.getParameter("URL");
    if (str == null) {
      System.err.println("Redirector: Unable to redirect - no URL specified.");

      return;
    }

    try
    {
      super.getAppletContext().showDocument(new URL(str));

      return;
    }
    catch (MalformedURLException localMalformedURLException)
    {
      System.err.println("Redirector: URL parameter is malformed.");
    }
  }

  public String[][] getParameterInfo()
  {
    return pinfo;
  }
}