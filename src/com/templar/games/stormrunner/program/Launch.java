package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.assembly.Launcher;
import com.templar.games.stormrunner.objects.LaunchSite;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

public class Launch extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    if (paramRobot.getAssembly("Launcher") == null) {
      return false;
    }

    Enumeration localEnumeration = GameApplet.thisApplet.getGameState().getSpecialProgramParts().elements();
    while (localEnumeration.hasMoreElements())
      if (localEnumeration.nextElement() instanceof Launch)
        return true;

    return false;
  }

  public static String getDescription() {
    return "";
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt) {
    return false; }

  public boolean boundaryCheck(Robot paramRobot) { return false; }

  public void terminate(Robot paramRobot) {
  }

  public String toString() {
    return "Launch";
  }

  public boolean execute(Robot paramRobot)
  {
    if (paramRobot.getAssembly(1) instanceof Launcher)
    {
      Launcher localLauncher = (Launcher)paramRobot.getAssembly(1);
      if (!(localLauncher.isEmpty()))
      {
        Vector localVector = paramRobot.getEnvironment().getObjectAt(paramRobot.getPosition().getMapPoint());
        if (localVector != null)
        {
          Enumeration localEnumeration = localVector.elements();
          while (localEnumeration.hasMoreElements())
            if (localEnumeration.nextElement() instanceof LaunchSite)
            {
              GameApplet.thisApplet.setBackground(Color.black);
              GameApplet.thisApplet.removeAll();
              GameApplet.thisApplet.getGameState().setFinished();
              if (GameApplet.appletContext != null)
                try
                {
                  GameApplet.appletContext.showDocument(
                    new URL(GameApplet.thisApplet.getDocumentBase(), "victory.html"), 
                    "_self");
                }
                catch (MalformedURLException localMalformedURLException)
                {
                }
              return true;
            }
        }
        paramRobot.playSound("Robot-Deny");
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + " cannot launch from this site.");
      }
    }
    paramRobot.playSound("Robot-Deny");
    GameApplet.thisApplet.sendStatusMessage(
      "RCX: " + paramRobot.getName() + " cannot launch from this site.");
    return true;
  }
}