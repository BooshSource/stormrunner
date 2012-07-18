package com.templar.games.stormrunner;

import java.awt.Dimension;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Position
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public int x;
  public int y;
  public int dx;
  public int dy;

  public void readExternal(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    this.x = paramObjectInput.readInt();
    this.y = paramObjectInput.readInt();
    this.dx = paramObjectInput.readInt();
    this.dy = paramObjectInput.readInt();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.x);
    paramObjectOutput.writeInt(this.y);
    paramObjectOutput.writeInt(this.dx);
    paramObjectOutput.writeInt(this.dy);
  }

  public Position()
  {
    this.x = 0;
    this.y = 0;
    this.dx = 0;
    this.dy = 0;
  }

  public Position(Point paramPoint)
  {
    this.x = paramPoint.x;
    this.y = paramPoint.y;
    this.dx = 0;
    this.dy = 0;
  }

  public Position(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.x = paramInt1;
    this.y = paramInt2;
    this.dx = paramInt3;
    this.dy = paramInt4;
  }

  public Position(Position paramPosition)
  {
    this.x = paramPosition.x;
    this.y = paramPosition.y;
    this.dx = paramPosition.dx;
    this.dy = paramPosition.dy;
  }

  public static Point mapToScreen(int paramInt1, int paramInt2)
  {
    return mapToScreen(new Position(paramInt1, paramInt2, 0, 0));
  }

  public static Point mapToScreen(Point paramPoint)
  {
    return mapToScreen(new Position(paramPoint));
  }

  public static Point mapToScreen(Position paramPosition)
  {
    int i = paramPosition.x * 50 + paramPosition.dx;
    int j = paramPosition.y * 50 + paramPosition.dy;
    return new Point(i, j);
  }

  public Position translate(Position paramPosition)
  {
    this.x += paramPosition.x;
    this.y += paramPosition.y;
    this.dx += paramPosition.dx;
    this.dy += paramPosition.dy;
    return this;
  }

  public static Position screenToMap(Point paramPoint)
  {
    return screenToMap(paramPoint, new Position());
  }

  public static Position screenToMap(Point paramPoint, Position paramPosition)
  {
    Position localPosition = new Position();
    localPosition.x = (paramPosition.x + paramPoint.x / 50);
    localPosition.y = (paramPosition.y + paramPoint.y / 50);
    localPosition.dx = (paramPosition.dx + paramPoint.x % 50);
    localPosition.dy = (paramPosition.dy + paramPoint.y % 50);
    return localPosition;
  }

  public Point getMapPoint()
  {
    return new Point(this.x, this.y);
  }

  public Dimension getMapOffset()
  {
    return new Dimension(this.dx, this.dy);
  }

  public String toString()
  {
    return "Position[" + this.x + "." + this.dx + "," + this.y + "." + this.dy + "]";
  }
}