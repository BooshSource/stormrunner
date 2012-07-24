package com.templar.games.stormrunner.actors;

import com.templar.games.stormrunner.Actor;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.deaths.Eaten;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Hashtable;
import java.util.Vector;

public class Plant extends Actor
  implements Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int EMPTY = 0;
  public static final int FULL = 1;
  public static final int EATING = 2;
  public static final int DIGESTION_RATE = 75;
  public static final int LOOKING = 0;
  public static final int STOP_LOOKING = 1;
  public static final int LUNGING = 2;
  public static final int GRABBING = 3;
  public static final int RETRACTING = 4;
  public static final int DIGESTING = 5;
  public static final int[][] animationFrames = { 
    { 0, 1, 2, 3 }, 
    { 3, 2, 1 }, 
    { 4, 5, 6, 7 }, 
    { 8, 9, 10 }, 
    { 11, 12, 13, 14, 15, 16 }, 
    { 16, 2, 1 } };
  int state;
  int frame;
  Robot victim;
  Position victimPos;
  int emptyCountDown;
  Image[] oldImages;
  AnimationComponent comp = new AnimationComponent();
  Hashtable cellCache = new Hashtable();
  ImageRetriever ir;

  public void readExternal(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    super.readExternalWithoutImages(paramObjectInput);
    this.state = paramObjectInput.readInt();
    this.frame = paramObjectInput.readInt();
    this.victim = ((Robot)paramObjectInput.readObject());
    this.victimPos = ((Position)paramObjectInput.readObject());
    this.emptyCountDown = paramObjectInput.readInt();
    Vector localVector = (Vector)paramObjectInput.readObject();
    if (localVector != null)
    {
      this.oldImages = new Image[localVector.size()];
      for (int i = 0; i < localVector.size(); i++)
        this.oldImages[i] = GameApplet.thisApplet.getImage((String)localVector.elementAt(i));
    }
    this.comp = ((AnimationComponent)paramObjectInput.readObject());
    if (this.state == 0) {
      setImages(this.oldImages);

      return;
    }

    AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
    arrayOfAnimationComponent[0] = this.comp;
    setImages(arrayOfAnimationComponent);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    super.writeExternalWithoutImages(paramObjectOutput);
    paramObjectOutput.writeInt(this.state);
    paramObjectOutput.writeInt(this.frame);
    paramObjectOutput.writeObject(this.victim);
    paramObjectOutput.writeObject(this.victimPos);
    paramObjectOutput.writeInt(this.emptyCountDown);
    if (this.oldImages == null) {
      paramObjectOutput.writeObject(null);
    }
    else {
      Vector localVector = new Vector();
      for (int i = 0; i < this.oldImages.length; i++)
        localVector.addElement(GameApplet.thisApplet.getImageFilename(this.oldImages[i]));
      paramObjectOutput.writeObject(localVector);
    }
    paramObjectOutput.writeObject(this.comp);
  }

  public Plant()
  {
  }

  public Plant(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public Plant(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public Plant(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  void initialize()
  {
    this.state = 0;
    this.frame = 0;
    this.emptyCountDown = -1;
    this.victim = null;
    this.victimPos = null;
    this.oldImages = getImages();
  }

  public void setImageRetriever(ImageRetriever paramImageRetriever)
  {
    this.ir = paramImageRetriever;
  }

  public boolean checkVictimPosition()
  {
    Position localPosition1 = this.victim.getPosition();
    Position localPosition2 = this.victimPos;

    return ((localPosition1.x == localPosition2.x) && (localPosition1.y == localPosition2.y) && (Math.abs(localPosition1.dx) < 25) && (Math.abs(localPosition1.dy) < 25)) || 
      ((localPosition1.x == localPosition2.x) && (localPosition1.y == localPosition2.y - 1) && (Math.abs(localPosition1.dy) < 25)) || 
      ((localPosition1.x == localPosition2.x - 1) && (localPosition1.y == localPosition2.y) && (Math.abs(localPosition1.dx) < 25)) || 
      ((localPosition1.x == localPosition2.x + 1) && (localPosition1.y == localPosition2.y) && (Math.abs(localPosition1.dx) < 25)) || (
      (localPosition1.x == localPosition2.x) && (localPosition1.y == localPosition2.y + 1) && (Math.abs(localPosition1.dy) < 25));
  }

  public void tick()
  {
    Image[] arrayOfImage;
    switch (this.state)
    {
    case 1:
      this.emptyCountDown -= 1;
      if (this.emptyCountDown == 0)
      {
        this.comp.setSequence(animationFrames[5], null, false);
      }
      if ((this.emptyCountDown >= 0) || 
        (this.comp.nextImage()))
        break;
      arrayOfImage = this.oldImages;
      initialize();
      this.oldImages = arrayOfImage;
      Debug.println("Setting plant images to: " + Debug.arrayPrint(this.oldImages));
      setImages(this.oldImages);
      this.comp.reset();

      return;
    case 2:
      switch (this.frame)
      {
      case 0:
        if (this.comp.nextImage()) {
          break;
        }
        if (checkVictimPosition())
        {
          playSound("Robot-Scream");
          this.frame = 2;
        }
        else {
          this.frame = 1;
        }this.comp.setSequence(animationFrames[this.frame], null, false);

        return;
      case 1:
        if (this.comp.nextImage())
          break;
        arrayOfImage = this.oldImages;
        initialize();
        this.oldImages = arrayOfImage;
        Debug.println("Setting plant images to: " + Debug.arrayPrint(this.oldImages));
        setImages(this.oldImages);

        return;
      case 2:
        if (this.comp.nextImage()) break;
        this.comp.setSequence(animationFrames[(++this.frame)], null, false);
        return;
      case 3:
        if (this.comp.nextImage())
          break;
        this.comp.setSequence(animationFrames[(++this.frame)], null, false);
        if (checkVictimPosition())
        {
          playSound("PlantDeath");

          this.victim.setVisible(false);
          this.victim.setDeath(new Eaten(animationFrames[this.frame].length));
          this.victim.stop();
          this.emptyCountDown = 75;

          return;
        }

        this.emptyCountDown = 0;

        return;
      case 4:
        if (this.comp.nextImage())
          break;
        this.frame = 0;
        this.state = 1;

        return;
      }
    case 0:
    }
  }

  public int activateOnEvent()
  {
    return 4;
  }
  public void activate(Robot paramRobot, int paramInt) {
    if (this.state == 0)
    {
      playSound("PlantNotice");

      this.state = 2;
      this.victim = paramRobot;
      this.victimPos = paramRobot.getPosition();
      this.frame = 0;
      int i = 0;
      Point localPoint1 = paramRobot.getPosition().getMapPoint(); Point localPoint2 = getPosition().getMapPoint();
      localPoint2.translate(1, 1);
      if ((localPoint2.x > localPoint1.x) && (localPoint2.y > localPoint1.y))
        i = 315;
      if ((localPoint2.x == localPoint1.x) && (localPoint2.y > localPoint1.y))
        i = 0;
      if ((localPoint2.x < localPoint1.x) && (localPoint2.y > localPoint1.y))
        i = 45;
      if ((localPoint2.x > localPoint1.x) && (localPoint2.y == localPoint1.y))
        i = 270;
      if ((localPoint2.x < localPoint1.x) && (localPoint2.y == localPoint1.y))
        i = 90;
      if ((localPoint2.x > localPoint1.x) && (localPoint2.y < localPoint1.y))
        i = 225;
      if ((localPoint2.x == localPoint1.x) && (localPoint2.y < localPoint1.y))
        i = 180;
      if ((localPoint2.x < localPoint1.x) && (localPoint2.y < localPoint1.y))
        i = 135;
      Image[] arrayOfImage1 = getCells(i);

      this.comp.setCells(arrayOfImage1);
      this.comp.setSequence(animationFrames[0], null, false);
      AnimationComponent[] arrayOfAnimationComponent = { this.comp };
      Image[] arrayOfImage2 = getImages();
      if (arrayOfImage2 != null)
        this.oldImages = arrayOfImage2;
      Debug.println("******\nOldImages:" + Debug.arrayPrint(this.oldImages) + ",\nnewImages:" + Debug.arrayPrint(arrayOfAnimationComponent) + "\n******");
      setImages(arrayOfAnimationComponent);
    }
  }

  public Image[] getCells(int paramInt)
  {
    Integer localInteger = new Integer(paramInt);
    Object localObject = this.cellCache.get(localInteger);
    if (localObject != null) {
      return (Image[])localObject;
    }

    Image[] arrayOfImage = new Image[getAnimationFrames()];
    String str = "com/templar/games/stormrunner/media/images/scenery/plants/eater/eater_";
    for (int i = 1; i < getAnimationFrames(); i++)
    {
      StringBuffer localStringBuffer = new StringBuffer(str);
      localStringBuffer.append(paramInt);
      localStringBuffer.append("/");
      localStringBuffer.append(i);
      localStringBuffer.append(".gif");

      arrayOfImage[(i - 1)] = GameApplet.thisApplet.getImage(localStringBuffer.toString());
    }
    this.cellCache.put(localInteger, arrayOfImage);
    return arrayOfImage;
  }
  public int getAnimationFrames() {
    return 18;
  }

  public void setGameState(GameState paramGameState)
  {
  }
}