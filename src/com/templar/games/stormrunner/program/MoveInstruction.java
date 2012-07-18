package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.Map;
import com.templar.games.stormrunner.MapCell;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.Shroud;
import com.templar.games.stormrunner.chassis.Chassis;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class MoveInstruction extends Instruction
  implements Parameterized, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int DEFAULT_SQUARES = 1;
  private int squares;
  private int square_counter;
  private boolean onstorm = false;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.squares = paramObjectInput.readInt();
    this.square_counter = paramObjectInput.readInt();

    if (GameApplet.thisApplet.getLoadingVersion() > 0.40000000000000002D)
      this.onstorm = paramObjectInput.readBoolean();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.squares);
    paramObjectOutput.writeInt(this.square_counter);
    paramObjectOutput.writeBoolean(this.onstorm);
  }

  public MoveInstruction()
  {
    setSquares(1);
  }

  public MoveInstruction(int paramInt)
  {
    setSquares(paramInt);
  }

  public void setSquares(int paramInt) {
    this.squares = (paramInt + 1);
    this.square_counter = this.squares;
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return true;
  }

  public String getParameterString()
  {
    return Integer.toString(this.squares - 1);
  }

  public boolean setParameterString(String paramString)
  {
    int i;
    try {
      i = Integer.parseInt(paramString);
      if (i < 1) {
        return false;
      }

      setSquares(i);
      return true;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return false;
  }

  public int getMaxResponseLength()
  {
    return 3;
  }

  public String getAllowedCharacters()
  {
    return "1234567890";
  }

  public String getPrompt()
  {
    return "Number of squares to move:";
  }

  public boolean execute(Robot paramRobot)
  {
    Position localPosition1 = new Position(paramRobot.getPosition());
    Position localPosition2 = new Position(localPosition1);
    boolean bool1 = boundaryCheck(paramRobot);
    if (bool1)
    {
      this.square_counter -= 1;
      if (this.square_counter == 0)
      {
        terminate(paramRobot);
        return true;
      }

      if (paramRobot.checkObstacles())
      {
        paramRobot.getProgram().restart(paramRobot);
        paramRobot.playSound("Robot-NotCompute");
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + " is obstructed.\n");
        return true;
      }

      switch (paramRobot.getOrientation())
      {
      case 0:
        localPosition2.y -= 1;
        localPosition2.dy = 50;
        break;
      case 90:
        localPosition2.x += 1;
        localPosition2.dx = -50;
        break;
      case 180:
        localPosition2.y += 1;
        localPosition2.dy = -50;
        break;
      case 270:
        localPosition2.x -= 1;
        localPosition2.dx = 50;
      }

      paramRobot.setPosition(localPosition2);
      Shroud localShroud = paramRobot.getEnvironment().getShroud();
      localObject = paramRobot.getPosition().getMapPoint();

      boolean bool2 = paramRobot.getEnvironment().getMap().getCell((Point)localObject).getStorm();

      if ((!(this.onstorm)) && (bool2))
      {
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + ": extreme weather conditions - VidSensor failure.\n");
        this.onstorm = bool2;
      }
      else if ((this.onstorm) && (!(bool2)))
      {
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + ": condition normal - VidSensor operating normally.\n");
        this.onstorm = bool2;
      }
      boolean bool3 = paramRobot.getSensor("VidSensor") != null;
      localShroud.setVisible((Point)localObject, 2, true, (bool2) ? false : bool3);

      localShroud.setVisible(localPosition1.getMapPoint(), 2, false, false);
    }

    int i = paramRobot.getChassis().getSpeed();
    switch (paramRobot.getOrientation())
    {
    case 0:
      localPosition2.dy -= i;
      break;
    case 90:
      localPosition2.dx += i;
      break;
    case 180:
      localPosition2.dy += i;
      break;
    case 270:
      localPosition2.dx -= i;
    }

    paramRobot.setPosition(localPosition2);
    paramRobot.setState(1, -1);
    Object localObject = paramRobot.getChassis().getID();
    AnimationComponent localAnimationComponent = paramRobot.getAnimationComponent((String)localObject);

    if ((int)(Math.random() * 40.0D) == 1)
    {
      int j = (int)(Math.random() * 5.0D + 1D);
      paramRobot.playSound("Robot-Nonsense-" + String.valueOf(j));
    }

    localAnimationComponent.nextImage();
    return false;
  }

  public static String getDescription()
  {
    return "This instruction makes the RCX move in the direction it is facing by a specified number of squares.";
  }

  public boolean boundaryCheck(Robot paramRobot)
  {
    int i = paramRobot.getChassis().getSpeed();
    Position localPosition = paramRobot.getPosition();
    int j = Math.abs(localPosition.dx); int k = Math.abs(localPosition.dy);
    return ((j <= i) && (k <= i));
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    int i = paramRobot.getChassis().getSpeed();
    Position localPosition = paramRobot.getPosition();
    switch (paramRobot.getOrientation())
    {
    case 90:
    case 270:
      return (Math.abs(localPosition.dx) - i <= paramInt);
    case 0:
    case 180:
      return (Math.abs(localPosition.dy) - i <= paramInt);
    }
    return false;
  }

  public void terminate(Robot paramRobot)
  {
    Position localPosition = paramRobot.getPosition();

    localPosition.dx = (localPosition.dy = 0);

    paramRobot.setPosition(localPosition);

    this.square_counter = this.squares;

    paramRobot.setState(0, -1);
    paramRobot.setOrientation(paramRobot.getOrientation());
  }

  public String toString()
  {
    return "Forward " + this.square_counter;
  }
}