package com.templar.games.stormrunner;

import com.templar.games.stormrunner.objects.Obstacle;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

public class Scene
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final double VERSION = 0.4D;
  protected Map sceneMap;
  protected transient Vector[][] objectMap;
  protected Vector actorsList;
  protected Vector objectList;
  protected String description;
  protected Shroud shroud;
  protected transient Renderer renderer;
  protected Vector layers;
  protected Point RobotStart;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeObject(this.RobotStart);
    MapBuilder.writeMap(this.sceneMap, (ObjectOutputStream)paramObjectOutput);
    paramObjectOutput.writeObject(this.objectList);
    paramObjectOutput.writeObject(this.description);
    paramObjectOutput.writeObject(this.shroud);
    paramObjectOutput.writeObject(this.layers);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    this.RobotStart = ((Point)paramObjectInput.readObject());
    try
    {
      Map localMap = MapBuilder.readMap(GameApplet.thisApplet, (ObjectInputStream)paramObjectInput);
      setMap(GameApplet.thisApplet, localMap);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }

    Vector localVector = (Vector)paramObjectInput.readObject();
    this.description = ((String)paramObjectInput.readObject());
    this.shroud = ((Shroud)paramObjectInput.readObject());
    setShroudImages(GameApplet.thisApplet, this.shroud);
    this.layers = ((Vector)paramObjectInput.readObject());

    Enumeration localEnumeration = localVector.elements();
    while (localEnumeration.hasMoreElements())
    {
      PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();

      localPhysicalObject.setEnvironment(this);
      addObject(localPhysicalObject);
    }
  }

  public Scene()
  {
    this.sceneMap = new Map(new Dimension(1, 1));
    this.description = "Empty Scene";
    this.actorsList = new Vector();
    this.objectList = new Vector();
  }

  public Scene(ImageRetriever paramImageRetriever, String paramString, Map paramMap, Vector paramVector)
  {
    this.description = paramString;
    Enumeration localEnumeration = paramVector.elements();
    setMap(paramImageRetriever, paramMap);
    while (localEnumeration.hasMoreElements())
    {
      PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
      localPhysicalObject.place(this.objectMap);
      if ((localPhysicalObject instanceof Actor))
        this.actorsList.addElement(localPhysicalObject);
    }
    this.objectList = paramVector;
  }

  public void setRenderer(Renderer paramRenderer)
  {
    this.renderer = paramRenderer;
    paramRenderer.setScene(this);
  }

  public Renderer getRenderer()
  {
    return this.renderer;
  }

  public Vector[][] getObjectMap()
  {
    return this.objectMap;
  }
  public Map getMap() { return this.sceneMap; }

  public Vector getObjectAt(int paramInt1, int paramInt2)
  {
    try {
      return this.objectMap[paramInt1][paramInt2];
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
    return null;
  }

  public Vector getObjectAt(Position paramPosition)
  {
    return getObjectAt(paramPosition.x, paramPosition.y);
  }

  public Vector getObjectAt(Point paramPoint) {
    return getObjectAt(paramPoint.x, paramPoint.y);
  }

  public Vector getObjectOfTypeAt(Point paramPoint, Class paramClass) {
    return getObjectOfTypeAt(paramPoint.x, paramPoint.y, paramClass);
  }

  public Vector getObjectOfTypeAt(Position paramPosition, Class paramClass) {
    return getObjectOfTypeAt(paramPosition.x, paramPosition.y, paramClass);
  }

  public Vector getObjectOfTypeAt(int paramInt1, int paramInt2, Class paramClass) {
    Vector localVector1 = getObjectAt(paramInt1, paramInt2); Vector localVector2 = new Vector(1, 1);
    if (localVector1 == null)
      return localVector1;
    Enumeration localEnumeration = localVector1.elements();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      if (paramClass.isInstance(localObject))
        localVector2.addElement(localObject);
    }
    if (localVector2.size() == 0)
      return null;
    return localVector2;
  }

  public boolean isObstructed(Point paramPoint) {
    Vector localVector = null;
    try
    {
      localVector = this.objectMap[paramPoint.x][paramPoint.y];
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      return true;
    }
    if (localVector != null)
    {
     boolean b = false;
      for (int j = 0; j < localVector.size(); j++)
      {
        if ((localVector.elementAt(j) instanceof Obstacle)) {
          b = true;
        }
      }
      return b;
    }

    return false;
  }
  public void setLayers(Vector paramVector) { this.layers = paramVector; } 
  public Vector getLayers() { return this.layers; } 
  public Vector getActors() { return this.actorsList; } 
  public Vector getObjects() { return this.objectList; } 
  public String getDescription() { return this.description; }

  public void setDescription(String paramString) {
    this.description = paramString;
  }

  public boolean mightBeDescribedAs(String paramString) {
    return this.description.toLowerCase().indexOf(paramString.toLowerCase()) != -1;
  }

  public Shroud getShroud() {
    return this.shroud;
  }

  public void addObject(PhysicalObject paramPhysicalObject) {
    paramPhysicalObject.place(this.objectMap);
    if ((paramPhysicalObject instanceof Actor))
      this.actorsList.addElement(paramPhysicalObject);
    this.objectList.addElement(paramPhysicalObject);

    if (this.renderer != null)
    {
      this.renderer.addObject(paramPhysicalObject);
    }
  }

  private void removeObject(PhysicalObject paramPhysicalObject, Point paramPoint)
  {
    for (int i = paramPoint.x; i < paramPoint.x + paramPhysicalObject.getShapeSize().width; i++)
      for (int j = paramPoint.y; j < paramPoint.y + paramPhysicalObject.getShapeSize().height; j++)
        try
        {
          if ((this.objectMap[i][j] == null) || 
            (!this.objectMap[i][j].contains(paramPhysicalObject)))
            continue;
          this.objectMap[i][j].removeElement(paramPhysicalObject);
          if (this.objectMap[i][j].size() == 0)
            this.objectMap[i][j] = null;
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
        }
  }

  public void removeObject(PhysicalObject paramPhysicalObject)
  {
    removeObject(paramPhysicalObject, paramPhysicalObject.getPosition().getMapPoint());
    this.objectList.removeElement(paramPhysicalObject);
    if ((paramPhysicalObject instanceof Actor))
      this.actorsList.removeElement(paramPhysicalObject);
    if (this.renderer != null)
      this.renderer.removeObject(paramPhysicalObject);
  }

  public void moveObject(PhysicalObject paramPhysicalObject, Point paramPoint) {
    removeObject(paramPhysicalObject, paramPoint);
    paramPhysicalObject.place(this.objectMap);
  }

  public void setMap(ImageRetriever paramImageRetriever, Map paramMap) {
    this.sceneMap = paramMap;
    this.objectMap = new Vector[paramMap.getSize().width][paramMap.getSize().height];
    this.shroud = new Shroud(paramMap.getSize(), 
      new Dimension(50, 50));
    setShroudImages(paramImageRetriever, this.shroud);
  }

  private void setShroudImages(ImageRetriever paramImageRetriever, Shroud paramShroud)
  {
    Image[] arrayOfImage = new Image[16];
    arrayOfImage[0] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_midmid.gif");
    arrayOfImage[1] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_topleft.gif");
    arrayOfImage[2] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_topmid.gif");
    arrayOfImage[3] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_topright.gif");
    arrayOfImage[4] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_midleft.gif");
    arrayOfImage[5] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_midright.gif");
    arrayOfImage[6] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_botleft.gif");
    arrayOfImage[7] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_botmid.gif");
    arrayOfImage[8] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_botright.gif");
    arrayOfImage[9] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/clear_topleft.gif");
    arrayOfImage[10] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/clear_topright.gif");
    arrayOfImage[11] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/clear_botleft.gif");
    arrayOfImage[12] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/clear_botright.gif");
    arrayOfImage[13] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_bottop.gif");
    arrayOfImage[14] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/solid_topbot.gif");
    arrayOfImage[15] = paramImageRetriever.getImage("com/templar/games/stormrunner/media/images/shroud/clear_midmid.gif");
    paramShroud.setTileImages(arrayOfImage);
  }

  public void setRobotStart(Point paramPoint) {
    this.RobotStart = paramPoint;
  }

  public Point getRobotStart() {
    return this.RobotStart;
  }
}