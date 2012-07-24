package com.templar.games.stormrunner.templarutil.util;

import com.templar.games.stormrunner.templarutil.Debug;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Vector;

public class OrderedTable extends Dictionary
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected Vector Keys = new Vector();
  protected Vector Values = new Vector();

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.Keys = ((Vector)paramObjectInput.readObject());
    this.Values = ((Vector)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.Keys);
    paramObjectOutput.writeObject(this.Values);
  }

  public void clear()
  {
    this.Keys.removeAllElements();
    this.Values.removeAllElements();
  }

  public boolean contains(Object paramObject)
  {
    return this.Values.contains(paramObject);
  }

  public boolean containsKey(Object paramObject)
  {
    return this.Keys.contains(paramObject);
  }

  public Enumeration elements()
  {
    return this.Values.elements();
  }

  public Enumeration keys()
  {
    return this.Keys.elements();
  }

  public Object get(Object paramObject)
  {
    int i = this.Keys.indexOf(paramObject);

    if (i == -1) {
      return null;
    }
    return this.Values.elementAt(i);
  }

  public Object get(int paramInt)
  {
    return this.Values.elementAt(paramInt);
  }

  public Object getKey(Object paramObject)
  {
    int i = this.Values.indexOf(paramObject);

    if (i == -1) {
      return null;
    }
    return this.Keys.elementAt(i);
  }

  public Object getKey(int paramInt)
  {
    return this.Keys.elementAt(paramInt);
  }

  public int indexOf(Object paramObject)
  {
    return this.Values.indexOf(paramObject);
  }

  public int indexOfKey(Object paramObject)
  {
    return this.Keys.indexOf(paramObject);
  }

  public boolean isEmpty()
  {
    return this.Keys.size() == 0;
  }

  public Object put(Object paramObject1, Object paramObject2)
  {
    int i = this.Keys.indexOf(paramObject1);

    if (i == -1)
    {
      this.Keys.addElement(paramObject1);
      this.Values.addElement(paramObject2);
      return null;
    }

    Object localObject = this.Values.elementAt(i);
    this.Values.setElementAt(paramObject2, i);
    return localObject;
  }

  public Object put(Object paramObject1, Object paramObject2, int paramInt)
  {
    if ((paramInt < 0) || (paramInt > size())) {
      return put(paramObject1, paramObject2);
    }

    this.Keys.insertElementAt(paramObject1, paramInt);
    this.Values.insertElementAt(paramObject2, paramInt);
    return this.Values.elementAt(paramInt + 1);
  }

  public void put(Object paramObject, int paramInt)
  {
    this.Values.setElementAt(paramObject, paramInt);
  }

  public Object remove(Object paramObject)
  {
    int i = this.Keys.indexOf(paramObject);

    if (i != -1)
    {
      Object localObject = this.Values.elementAt(i);
      this.Keys.removeElementAt(i);
      this.Values.removeElementAt(i);
      return localObject;
    }

    return null;
  }

  public Object remove(int paramInt)
  {
    Object localObject = this.Values.elementAt(paramInt);
    this.Keys.removeElementAt(paramInt);
    this.Values.removeElementAt(paramInt);
    return localObject;
  }

  public int size()
  {
    return this.Keys.size();
  }

  public String toString()
  {
    if (this.Keys.size() == 0)
      return "OrderedTable[]\n";
    Enumeration localEnumeration1 = this.Keys.elements();
    Enumeration localEnumeration2 = this.Values.elements();
    StringBuffer localStringBuffer = new StringBuffer("OrderedTable[\n");
    while (localEnumeration1.hasMoreElements())
    {
      Object localObject = localEnumeration1.nextElement();
      if (localObject.getClass().isArray())
        localStringBuffer.append(Debug.arrayPrint(localObject));
      else
        localStringBuffer.append(localObject);
      localStringBuffer.append("  :  ");
      localObject = localEnumeration2.nextElement();
      if (localObject.getClass().isArray())
        localStringBuffer.append(Debug.arrayPrint(localObject));
      else
        localStringBuffer.append(localObject);
      localStringBuffer.append("\n");
    }
    localStringBuffer.append("]\n");
    return localStringBuffer.toString();
  }
}