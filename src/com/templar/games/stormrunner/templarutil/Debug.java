package com.templar.games.stormrunner.templarutil;

import com.templar.games.stormrunner.GameApplet;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionAdapter;
import java.io.PrintStream;
import java.lang.reflect.Array;

public class Debug
{
  public static boolean AWT_DEBUG;
  public static ActionAdapter handler;
  public static Frame debugFrame;
  public static TextArea text;
  public static final String nullString = "(null)";
  public static final String debugString = "Debug> ";
  public static boolean debugFlag;
  public static Debug selfReference;

  public Debug()
  {
    if (GameApplet.thisApplet != null)
    {
      debugFlag = Debug.AWT_DEBUG = (GameApplet.thisApplet.getParameter("AWT_DEBUG") == null) ? 0 : 1;
      if (!(debugFlag))
        debugFlag = GameApplet.thisApplet.getParameter("DEBUG") != null;
    }
    else
    {
      try {
        debugFlag = Debug.AWT_DEBUG = (System.getProperty("AWT_DEBUG") == null) ? 0 : 1;
        if (debugFlag) break label114;
        debugFlag = System.getProperty("DEBUG") != null;
      }
      catch (Exception localException)
      {
        debugFlag = false; }
    }
    if (AWT_DEBUG)
    {
      label114: debugFrame = new Frame("Debug Output");
      if (debugFlag)
      {
        handler = new ActionAdapter(this);
        text = new TextArea();
        debugFrame.setLayout(new BorderLayout());
        debugFrame.add(text, "Center");
        Panel localPanel = new Panel();
        debugFrame.add(localPanel, "South");
        Button localButton1 = new Button("Clear"); Button localButton2 = new Button("Close");
        localButton1.addActionListener(handler);
        localButton2.addActionListener(handler);
        localPanel.add(localButton1);
        localPanel.add(localButton2);
        debugFrame.pack();
        debugFrame.setVisible(true);
        Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
        debugFrame.setLocation(0, localDimension.height - debugFrame.getSize().height - 12);
        debugFrame.addWindowListener(new 1());
      }
    }
  }

  private static boolean debugCheck() {
    if (selfReference == null)
      selfReference = new Debug();
    return debugFlag;
  }

  public static void println(String paramString) {
    if (debugCheck())
    {
      if (AWT_DEBUG)
      {
        if (!(debugFrame.isVisible())) return;

        String str = text.getText();
        if (str.length() > 8192)
        {
          text.setText(str.substring(paramString.indexOf("\n") + 1));
          text.setCaretPosition(text.getText().length());
        }
        text.append(paramString + "\n");

        return;
      }

      System.out.println("Debug> " + paramString);
    }
  }

  public static void println(Object paramObject)
  {
    if (paramObject == null) {
      println("(null)");

      return;
    }

    if (paramObject.getClass().isArray())
    {
      Object[] arrayOfObject = new Object[Array.getLength(paramObject)];
      for (int i = 0; i < arrayOfObject.length; ++i)
        arrayOfObject[i] = Array.get(paramObject, i);
      println(arrayPrint(arrayOfObject));

      return;
    }

    println(paramObject.toString());
  }

  public static void print(String paramString) {
    if (debugCheck())
    {
      if (AWT_DEBUG)
      {
        if (!(debugFrame.isVisible())) return;
        text.append(paramString + "\n");

        return;
      }

      System.out.print(paramString);
    }
  }

  public static void print(Object paramObject) {
    if (paramObject == null) {
      print("(null)");

      return;
    }

    print(paramObject.toString());
  }

  public static String arrayPrint(Object paramObject) {
    if (paramObject.getClass().isArray())
    {
      Object[] arrayOfObject = new Object[Array.getLength(paramObject)];
      for (int i = 0; i < arrayOfObject.length; ++i)
        arrayOfObject[i] = Array.get(paramObject, i);
      return arrayPrint(arrayOfObject);
    }
    return "";
  }

  public static String arrayPrint(Object[] paramArrayOfObject) {
    StringBuffer localStringBuffer = new StringBuffer("[");
    if (paramArrayOfObject == null) {
      localStringBuffer.append("(null)");
    }
    else {
      localStringBuffer.append(paramArrayOfObject.length);
      localStringBuffer.append(":");
      for (int i = 0; i < paramArrayOfObject.length - 1; ++i)
      {
        if (paramArrayOfObject[i] == null) {
          localStringBuffer.append("(null)");
        }
        else if (paramArrayOfObject[i].getClass().isArray())
        {
          Object[] arrayOfObject1 = new Object[Array.getLength(paramArrayOfObject[i])];
          for (int k = 0; k < arrayOfObject1.length; ++k)
            arrayOfObject1[k] = Array.get(paramArrayOfObject[i], k);
          localStringBuffer.append(arrayPrint(arrayOfObject1));
        }
        else {
          localStringBuffer.append(paramArrayOfObject[i]); }
        localStringBuffer.append(",");
      }
      int j = paramArrayOfObject.length;
      if (j != 0)
      {
        if (paramArrayOfObject[(j - 1)] == null) {
          localStringBuffer.append("(null)");
        }
        else if (paramArrayOfObject[(j - 1)].getClass().isArray())
        {
          Object[] arrayOfObject2 = new Object[Array.getLength(paramArrayOfObject[(j - 1)])];
          for (int l = 0; l < arrayOfObject2.length; ++l)
            arrayOfObject2[l] = Array.get(paramArrayOfObject[(j - 1)], l);
          localStringBuffer.append(arrayPrint(arrayOfObject2));
        }
        else {
          localStringBuffer.append(paramArrayOfObject[(j - 1)]); }
      }
    }
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }
}