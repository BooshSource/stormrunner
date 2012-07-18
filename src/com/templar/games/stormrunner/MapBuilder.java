package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.util.Vector;

public abstract class MapBuilder
{
  public static Map readMap(ImageRetriever paramImageRetriever, InputStream paramInputStream)
    throws IOException, StreamCorruptedException, ClassNotFoundException
  {
    ObjectInputStream localObjectInputStream;
    int i;
    int j;
    int k;
    if (paramInputStream instanceof ObjectInputStream)
      localObjectInputStream = (ObjectInputStream)paramInputStream;
    else
      localObjectInputStream = new ObjectInputStream(paramInputStream);

    double d = localObjectInputStream.readDouble();

    Dimension localDimension = (Dimension)localObjectInputStream.readObject();
    Vector localVector1 = (Vector)localObjectInputStream.readObject();

    Map localMap = new Map(localDimension);

    if (d == 0.20000000000000001D)
    {
      Vector localVector2 = (Vector)localObjectInputStream.readObject();
      for (i = 0; i < localDimension.width; ++i)
        for (j = 0; j < localDimension.height; ++j)
        {
          k = localObjectInputStream.readInt();
          int l = localObjectInputStream.readInt();
          boolean bool = localObjectInputStream.readBoolean();
          int i1 = localObjectInputStream.readInt();
          if (k == -1)
          {
            Debug.println("Cell at " + i + "," + j + " has no url");
            k = 0;
          }
          if (i1 == -1)
          {
            Debug.println("Cell at " + i + "," + j + " has no category");
            i1 = 0;
          }
          localMap.setCell(i, j, 
            new MapCell(paramImageRetriever, (String)localVector1.elementAt(k), l, bool, 
            (String)localVector2.elementAt(i1)));
        }
      if (!(((String)localObjectInputStream.readObject()).equals("EOF"))) {
        System.err.println("MapBuilder Error: End of file marker not found.\n                  Could be wrong version?");
      }

    }
    else if (d == 0.10000000000000001D)
    {
      for (i = 0; i < localDimension.width; ++i)
        for (j = 0; j < localDimension.height; ++j)
        {
          k = localObjectInputStream.readInt();
          Image localImage = paramImageRetriever.getImage((String)localVector1.elementAt(k));
          localMap.setCell(i, j, new MapCell(localImage, localObjectInputStream.readInt()));
        }

      if (!(((String)localObjectInputStream.readObject()).equals("EOF")))
        System.err.println("MapBuilder Error: End of file marker not found.\n                  Could be wrong version?");

    }
    else
    {
      System.err.println("MapBuilder Error: Map file version: " + d + 
        " not supported.");
      return null;
    }

    return localMap;
  }

  public static void writeMap(Map paramMap, OutputStream paramOutputStream) throws IOException
  {
    ObjectOutputStream localObjectOutputStream;
    Dimension localDimension = paramMap.getSize();
    if (paramOutputStream instanceof ObjectOutput)
      localObjectOutputStream = (ObjectOutputStream)paramOutputStream;
    else
      localObjectOutputStream = new ObjectOutputStream(paramOutputStream);
    localObjectOutputStream.writeDouble(0.20000000000000001D);
    localObjectOutputStream.writeObject(localDimension);
    Vector localVector1 = new Vector(); Vector localVector2 = new Vector();
    for (int i = 0; i < localDimension.width; ++i)
      for (j = 0; j < localDimension.height; ++j)
      {
        MapCell localMapCell1 = paramMap.getCell(i, j);
        if (!(localVector1.contains(localMapCell1.getFilename())))
          localVector1.addElement(localMapCell1.getFilename());
        if (!(localVector2.contains(localMapCell1.getCategory())))
          localVector2.addElement(localMapCell1.getCategory());
      }
    localObjectOutputStream.writeObject(localVector1);
    localObjectOutputStream.writeObject(localVector2);
    for (int j = 0; j < localDimension.width; ++j)
      for (int k = 0; k < localDimension.height; ++k)
      {
        MapCell localMapCell2 = paramMap.getCell(j, k);
        localObjectOutputStream.writeInt(localVector1.indexOf(localMapCell2.getFilename()));
        localObjectOutputStream.writeInt(localMapCell2.getImpassibility());
        localObjectOutputStream.writeBoolean(localMapCell2.getStorm());
        localObjectOutputStream.writeInt(localVector2.indexOf(localMapCell2.getCategory()));
      }
    localObjectOutputStream.writeObject("EOF");
  }
}