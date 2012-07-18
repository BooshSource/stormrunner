package com.templar.games.stormrunner.templarutil.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class Starter extends Applet
  implements ActionListener
{
  Button Start;
  String ButtonTitle;
  URL BaseURL;
  Vector Pages = new Vector();
  Vector PageTargets = new Vector();

  public void init()
  {
    try
    {
      this.ButtonTitle = super.getParameter("ButtonTitle");
      if (this.ButtonTitle == null) {
        this.ButtonTitle = "Start Game";
      }

      String str1 = super.getParameter("BaseURL");
      if (str1 != null)
        this.BaseURL = new URL(str1);
      else {
        this.BaseURL = super.getDocumentBase();
      }

      int i = 1;
      int j = 1;
      do
      {
        String str2 = "URL " + String.valueOf(i);
        String str3 = "Target " + String.valueOf(i);
        String str5 = super.getParameter(str3);
        if (str5 == null) {
          j = 0;
        }
        else {
          String str4 = super.getParameter(str2);
          this.Pages.addElement(new URL(this.BaseURL, str4));
          this.PageTargets.addElement(str5);
        }

        ++i;
      }
      while (j != 0);
    }
    catch (MalformedURLException localMalformedURLException)
    {
      System.err.println("Starter: Got a URL it couldn't understand.");
      this.ButtonTitle = "Check Parameters!";
    }

    this.Start = new Button(this.ButtonTitle);
    this.Start.addActionListener(this);
    add(this.Start);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    for (int i = 0; i < this.Pages.size(); ++i)
    {
      super.getAppletContext().showDocument((URL)this.Pages.elementAt(i), (String)this.PageTargets.elementAt(i));
    }
  }
}