package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.util.ProgressListener;
import com.templar.games.stormrunner.util.SceneObject;
import java.applet.Applet;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class SceneBuilder
{
  public static final String OBJECT_URL_TARGET = "subnav";
  public static Vector objectlist;

  public static Scene readScene(ImageRetriever paramImageRetriever, InputStream paramInputStream)
    throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, StreamCorruptedException, OptionalDataException, InvocationTargetException, MalformedURLException
  {
    return readScene(paramImageRetriever, paramInputStream, null);
  }

  public static Scene readScene(ImageRetriever paramImageRetriever, InputStream paramInputStream, ProgressListener paramProgressListener)
    throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, StreamCorruptedException, OptionalDataException, InvocationTargetException, MalformedURLException
  {
    ObjectInputStream localObjectInputStream;
    Scene localScene = new Scene();

    if (paramInputStream instanceof ObjectInputStream)
      localObjectInputStream = (ObjectInputStream)paramInputStream;
    else
      localObjectInputStream = new ObjectInputStream(paramInputStream);

    double d = localObjectInputStream.readDouble();
    if (d == 0.40000000000000002D)
      d = 0.29999999999999999D;
    if (d == 0.29999999999999999D)
    {
      Debug.println("reading start point");
      localScene.setRobotStart((Point)localObjectInputStream.readObject());
      d = 0.20000000000000001D;
    }
    else
    {
      localScene.setRobotStart(new Point(172, 141));
      d = 0.20000000000000001D;
    }
    if (d == 0.20000000000000001D)
    {
      Debug.println("reading layers");
      localScene.setLayers((Vector)localObjectInputStream.readObject());
      d = 0.10000000000000001D;
    }
    if (d == 0.10000000000000001D)
    {
      Map localMap = MapBuilder.readMap(paramImageRetriever, localObjectInputStream);
      objectlist = (Vector)localObjectInputStream.readObject();
      String str = (String)localObjectInputStream.readObject();
      if (((String)localObjectInputStream.readObject()).compareTo("EOS") != 0)
      {
        return null;
      }
      localScene.setDescription(str);
      localScene.setMap(paramImageRetriever, localMap);

      Class localClass1 = Class.forName("com.templar.games.stormrunner.Scene");
      Class localClass2 = Class.forName("com.templar.games.stormrunner.Position");
      Hashtable localHashtable = new Hashtable(50, 50.0F);

      Enumeration localEnumeration = objectlist.elements();
      while (localEnumeration.hasMoreElements())
      {
        SceneObject localSceneObject = (SceneObject)localEnumeration.nextElement();
        Class localClass3 = null;

        localClass3 = (Class)localHashtable.get(localSceneObject.classname);

        if (localClass3 == null)
        {
          try
          {
            localClass3 = Class.forName("com.templar.games.stormrunner.objects." + localSceneObject.classname);
          }
          catch (ClassNotFoundException localClassNotFoundException2)
          {
            try
            {
              localClass3 = Class.forName("com.templar.games.stormrunner.actors." + localSceneObject.classname);
            }
            catch (ClassNotFoundException localClassNotFoundException1)
            {
              localClassNotFoundException1.printStackTrace();
            }

          }

          localHashtable.put(localSceneObject.classname, localClass3);
        }

        Class[] arrayOfClass = new Class[5];
        arrayOfClass[0] = localClass1;
        arrayOfClass[1] = localClass2;
        arrayOfClass[2] = new Image[1].getClass();
        arrayOfClass[3] = new boolean[1][1].getClass();
        arrayOfClass[4] = Boolean.TYPE;
        Constructor localConstructor = localClass3.getConstructor(arrayOfClass);
        Image[] arrayOfImage = { paramImageRetriever.getImage(localSceneObject.filename) };
        Object[] arrayOfObject = { 
          localScene, 
          new Position(localSceneObject.coord), 
          arrayOfImage, 
          localSceneObject.shape, 
          new Boolean(false) };

        PhysicalObject localPhysicalObject = null;
        try
        {
          localPhysicalObject = (PhysicalObject)localConstructor.newInstance(arrayOfObject);
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          if (localInvocationTargetException.getTargetException() instanceof ThreadDeath)
            throw ((ThreadDeath)localInvocationTargetException.getTargetException());

          throw localInvocationTargetException;
        }

        localPhysicalObject.setLayer(localSceneObject.layer);
        localPhysicalObject.setID(localSceneObject.name);
        if ((localSceneObject.urltarget != null) && (localSceneObject.urltarget.compareTo("") != 0) && 
          (GameApplet.thisApplet != null))
        {
          localPhysicalObject.setClickTarget(new URL(GameApplet.thisApplet.getDocumentBase(), localSceneObject.urltarget), "subnav");
        }
        localScene.addObject(localPhysicalObject);
        if (paramProgressListener != null)
          paramProgressListener.notifyProgress(20);
      }
      return localScene;
    }

    return null;
  }
}