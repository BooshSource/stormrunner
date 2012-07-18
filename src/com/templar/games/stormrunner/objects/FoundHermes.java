package com.templar.games.stormrunner.objects;

import [Z;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FoundHermes extends FoundRobot
  implements HairTrigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final String[] imageDirName = { 
    "g_stuck_u", "g_stuck_r", "g_stuck_d", "g_stuck_l" };
  int whichAnimationComponent;
  Point endingPoint;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.whichAnimationComponent = paramObjectInput.readInt();
    this.endingPoint = ((Point)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.whichAnimationComponent);
    paramObjectOutput.writeObject(this.endingPoint);
  }

  public FoundHermes()
  {
  }

  public FoundHermes(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public FoundHermes(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public FoundHermes(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  protected void initialize() {
    super.initialize();
    if (GameApplet.thisApplet == null)
      return;
    this.initialOrientation = 90;
    this.newName = "Hermes Prototype";
    this.chassis = "Hermes";
    String[] arrayOfString = { "sensor.VidSensor" };
    this.parts = arrayOfString;
    this.clearAnimations = new AnimationComponent[4];
    for (int i = 0; i < 4; ++i)
    {
      Image[] arrayOfImage = new Image[9];
      int[] arrayOfInt = new int[9];
      for (int j = 0; j < 9; ++j)
      {
        arrayOfInt[j] = j;
        arrayOfImage[j] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/mudstuck/" + imageDirName[i] + "/" + 
          imageDirName[i] + "_0" + (j + 1) + ".gif");
      }
      this.clearAnimations[i] = new AnimationComponent();
      this.clearAnimations[i].setCells(arrayOfImage);
      this.clearAnimations[i].setSequence(arrayOfInt, null, false);
    }
  }

  public void activate(Robot paramRobot, int paramInt)
  {
    if (this.clearAnimations != null)
    {
      if ((this.clear) || (this.active)) return;

      this.active = true;

      return;
    }

    this.clear = true;
  }

  public void tick() {
    if ((this.active) && 
      (!(this.clearAnimations[this.whichAnimationComponent].nextImage())))
    {
      [Z[] arrayOf[Z = { { true } };
      getEnvironment().removeObject(this);
      setShape(arrayOf[Z);
      ImageComponent[] arrayOfImageComponent = new ImageComponent[2];
      arrayOfImageComponent[1] = new ImageComponent(
        GameApplet.thisApplet.getImage(
        "com/templar/games/stormrunner/media/images/robot/chassis/Hermes/walk0_90C.gif"), true, false);
      arrayOfImageComponent[0] = this.clearAnimations[this.whichAnimationComponent];
      setImages(arrayOfImageComponent);
      this.clear = true;
      this.active = false;
      setPosition(new Position(this.endingPoint.x, this.endingPoint.y, 0, 0));
      getEnvironment().addObject(this);
      getEnvironment().getRenderer().repaint();
    }
  }

  public boolean isObstructing(Robot paramRobot)
  {
    if (this.clear)
    {
      return true;
    }
    if (this.endingPoint != null)
    {
      return true;
    }

    if (paramRobot.getChassis().getID().compareTo("Achilles") != 0) {
      return true;
    }

    int i = paramRobot.getOrientation();
    Point localPoint1 = paramRobot.getPosition().getMapPoint();
    Point localPoint2 = new Point(localPoint1.x, localPoint1.y);
    switch (i)
    {
    case 0:
      localPoint1.y -= 2;
      localPoint2.y = localPoint1.y;
      break;
    case 90:
      localPoint1.x += 2;
      localPoint2.x += 1;
      break;
    case 180:
      localPoint1.y += 2;
      localPoint2.y += 1;
      break;
    case 270:
      localPoint1.x -= 2;
      localPoint2.x = localPoint1.x;
    }

    if (paramRobot.getEnvironment().isObstructed(localPoint1)) {
      return true;
    }

    this.whichAnimationComponent = (i / 90);

    Image[] arrayOfImage = this.clearAnimations[this.whichAnimationComponent].getCells();
    for (int j = 0; j < arrayOfImage.length; ++j)
      GameApplet.thisApplet.hitCache(arrayOfImage[j]);
    AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
    arrayOfAnimationComponent[0] = this.clearAnimations[this.whichAnimationComponent];
    arrayOfAnimationComponent[0].reset();
    setImages(arrayOfAnimationComponent);
    paramRobot.getEnvironment().removeObject(this);
    boolean[][] arrayOfBoolean = new boolean[(i % 180 == 0) ? 1 : 2][(i % 180 == 0) ? 2 : 1];
    for (int k = 0; k < ((i % 180 == 0) ? 1 : 2); ++k)
      for (int l = 0; l < ((i % 180 == 0) ? 2 : 1); ++l)
        arrayOfBoolean[k][l] = 1;

    setPosition(new Position(localPoint2.x, localPoint2.y, 0, 0));
    setShape(arrayOfBoolean);
    this.endingPoint = localPoint1;
    paramRobot.getEnvironment().addObject(this);
    paramRobot.getEnvironment().getRenderer().repaint();
    return false; }

  public int activateOnEvent() {
    return 0; } 
  public int getThreshold() { return 45;
  }
}