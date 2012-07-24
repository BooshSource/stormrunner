package com.templar.games.stormrunner;

import com.templar.games.stormrunner.assembly.GrabberArm;
import com.templar.games.stormrunner.assembly.Launcher;
import com.templar.games.stormrunner.build.BuildPanel;
import com.templar.games.stormrunner.chassis.Arachnae;
import com.templar.games.stormrunner.chassis.Chassis;
import com.templar.games.stormrunner.chassis.Hermes;
import com.templar.games.stormrunner.objects.Satellite;
import java.util.Enumeration;
import java.util.Vector;

public abstract class GameOverChecks
{
  public static boolean check(Robot paramRobot)
  {
    return (robotContainsBeacon(paramRobot)) || (lostOnlyPart(paramRobot, new Hermes().getClass())) || 
      (lostOnlyPart(paramRobot, new Launcher().getClass())) || 
      (lostOnlyPart(paramRobot, new Arachnae().getClass())) || (
      (noRobotsLeft()) && (cantMakeACompleteRobot()));
  }

  public static boolean noRobotsLeft()
  {
    return GameApplet.thisApplet.getGameState().getAllRobots().size() == 0;
  }

  public static boolean cantMakeACompleteRobot() {
    int i = GameApplet.thisApplet.getGameState().getPolymetals();
    int j = GameApplet.thisApplet.getGameState().getEnergyUnits();
    Robot localRobot = GameApplet.thisApplet.getBuildPanel().getRobotOnRamp();
    if (localRobot != null)
    {
      i += 20;
      j += 45;
      RobotPart[] localObject = localRobot.getRobotParts();
      for (int k = 0; k < localObject.length; k++)
      {
        j += localObject[k].getEnergyCost();
        i += localObject[k].getSalvageCost();
      }
    }
    Object localObject = new GrabberArm();
    int k = 999; int m = 999;
    Enumeration localEnumeration = GameApplet.thisApplet.getGameState().getRobotPartPatterns().elements();
    while (localEnumeration.hasMoreElements())
    {
      RobotPart localRobotPart = (RobotPart)localEnumeration.nextElement();
      if ((!(localRobotPart instanceof Chassis)) || 
        (localRobotPart.getSecurityLevel() > GameApplet.thisApplet.getGameState().getSecurityLevel()))
        continue;
      if ((localRobotPart.getEnergyCost() > m) || (localRobotPart.getSalvageCost() > k))
        continue;
      m = localRobotPart.getEnergyCost();
      k = localRobotPart.getSalvageCost();
    }

    return (i < 20 + ((GrabberArm)localObject).getSalvageCost() + k) || 
      (j < 45 + ((GrabberArm)localObject).getEnergyCost() + m);
  }

  public static boolean robotContainsBeacon(Robot paramRobot) {
    RobotPart[] arrayOfRobotPart = paramRobot.getRobotParts();
    for (int i = 0; i < arrayOfRobotPart.length; i++)
    {
      if (!(arrayOfRobotPart[i] instanceof InventoryContainer))
        continue;
      Enumeration localEnumeration = ((InventoryContainer)arrayOfRobotPart[i]).getInventory().elements();
      while (localEnumeration.hasMoreElements()) {
        if ((localEnumeration.nextElement() instanceof Satellite))
          return true;
      }
    }
    return false;
  }

  public static boolean lostOnlyPart(Robot paramRobot, Class paramClass) {
    Enumeration localEnumeration = GameApplet.thisApplet.getGameState().getRobotPartPatterns().elements();
    while (localEnumeration.hasMoreElements())
      if (paramClass.isInstance(localEnumeration.nextElement()))
        return false;
    RobotPart[] arrayOfRobotPart = paramRobot.getRobotParts();
    for (int i = 0; i < arrayOfRobotPart.length; i++)
      if (paramClass.isInstance(arrayOfRobotPart[i]))
        return true;
    return false;
  }
}