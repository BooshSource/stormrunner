package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.gui.YesNoDialog;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.EventObject;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public class LoadSaveFrame extends Dialog
  implements WindowListener, ActionListener, ItemListener, TextListener
{
  public static final String SAVEGAMENAME = "SRSavegame.";
  public static final int LOADING = 0;
  public static final int SAVING = 1;
  Frame parentFrame;
  List savegamelist;
  TextField field;
  Button action;
  Button del;
  Button cancel;
  int mode;
  int index;
  Vector descs = new Vector(); Vector filenames = new Vector();
  File response;
  int newSaveNumber;
  String desc;
  String LastFieldContents;
  boolean skipTextEvent = false;

  public LoadSaveFrame(Frame paramFrame, int paramInt)
  {
    super(paramFrame, paramInt == 0 ? "Load..." : "Save...", true);

    this.parentFrame = paramFrame;
    this.mode = paramInt;

    setLayout(new BorderLayout());

    addWindowListener(
      new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramWindowEvent)
      {
        LoadSaveFrame.this.setVisible(false); LoadSaveFrame.this
          .dispose(); LoadSaveFrame.this.response = 
          null; LoadSaveFrame.this.desc = 
          null;
      }
    });
    Panel localPanel1 = new Panel(new BorderLayout()); Panel localPanel2 = new Panel();
    add(localPanel1, "Center");
    add(localPanel2, "South");

    this.savegamelist = new List();
    this.savegamelist.addItemListener(this);
    localPanel1.add(this.savegamelist, "Center");

    this.field = new TextField();
    this.field.addTextListener(this);
    this.field.addActionListener(this);
    localPanel1.add(this.field, "South");

    this.action = new Button(paramInt == 0 ? "Load" : "Save");
    localPanel2.add(this.action);
    this.action.addActionListener(this);
    this.action.setEnabled(false);

    this.del = new Button("Delete");
    localPanel2.add(this.del);
    this.del.addActionListener(this);

    this.cancel = new Button("Cancel");
    localPanel2.add(this.cancel);
    this.cancel.addActionListener(this);

    File localFile = new File(".");
    String[] arrayOfString = localFile.list(new SavegameFilter());
    this.newSaveNumber = arrayOfString.length;

    for (int i = 0; i < arrayOfString.length; i++)
    {
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(
          new BufferedInputStream(
          new GZIPInputStream(
          new FileInputStream(new File(arrayOfString[i])))));

        localObjectInputStream.readDouble();
        localObjectInputStream.readDouble();

        String localObject = (String)localObjectInputStream.readObject();
        long l = localObjectInputStream.readLong() / 5L;
        Debug.println(arrayOfString[i] + ":\n" + (String)localObject + "\n" + l + "-----");

        localObjectInputStream.close();

        StringBuffer localStringBuffer = new StringBuffer("[");

        int[] arrayOfInt = { 3600, 60, 1 };
        if (l / 86400L > 0L)
        {
          int j = (int)(l / 86400L);
          l %= 86400L;
          localStringBuffer.append(j);
          localStringBuffer.append("D ");
        }
        for (int j = 0; j < arrayOfInt.length; j++)
        {
          if (l / arrayOfInt[j] > 0L)
          {
            int k = (int)(l / arrayOfInt[j]);
            l %= arrayOfInt[j];
            String str = Integer.toString(k);
            if (str.length() == 1)
              localStringBuffer.append("0");
            localStringBuffer.append(str);
          }
          else {
            localStringBuffer.append("00");
          }if (j != arrayOfInt.length - 1)
            localStringBuffer.append(":");
        }
        localStringBuffer.append("] ");
        localStringBuffer.append((String)localObject);

        this.savegamelist.add(localStringBuffer.toString());

        this.descs.addElement(localObject);

        this.filenames.addElement(arrayOfString[i]);
      }
      catch (Exception localException)
      {
        Debug.println("Invalid savegame " + arrayOfString[i]);
        localException.printStackTrace();
      }

    }

    pack();
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    Object localObject = getSize();
    setLocation(localDimension.width / 2 - ((Dimension)localObject).width / 2, localDimension.height / 2 - ((Dimension)localObject).height / 2);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject;
    if ((paramActionEvent.getSource() == this.action) || (paramActionEvent.getSource() == this.field))
    {
      Debug.println(paramActionEvent);
      if (this.mode == 0)
      {
        Debug.println("loading");

        if (this.field.getText().compareTo("") == 0) {
          return;
        }
       int i = this.savegamelist.getSelectedIndex();

        if (i != -1)
        {
          this.response = new File((String)this.filenames.elementAt(i));
          setVisible(false);
          dispose();
        }

        return;
      }

      Debug.println("saving");

      if (this.field.getText().compareTo("") == 0) {
        this.field.setText("Savegame at " + new Date());
      }
      int i = this.descs.indexOf(this.field.getText());
      if (i != -1)
      {
        localObject = new YesNoDialog(this.parentFrame, "Confirm Overwrite", "Overwrite this savegame?");
        if (((YesNoDialog)localObject).getResponse().compareTo("Yes") == 0)
        {
          this.response = new File((String)this.filenames.elementAt(i));
          ((Window)localObject).dispose();
        }
      }
      else
      {
        this.response = new File("SRSavegame." + this.newSaveNumber);
      }
      this.desc = this.field.getText();
      setVisible(false);
      dispose();
    }

    if (paramActionEvent.getSource() == this.del)
    {
      if (this.savegamelist.getSelectedIndex() != -1)
      {
        YesNoDialog localYesNoDialog = new YesNoDialog(this.parentFrame, "Confirm Delete", 
          "Really delete this savegame?");
        if (localYesNoDialog.getResponse().compareTo("Yes") == 0)
        {
          try
          {
            localObject = new File((String)this.filenames.elementAt(this.savegamelist.getSelectedIndex()));
            Debug.println("Deleting " + localObject);

            this.savegamelist.remove(this.savegamelist.getSelectedIndex());

            if (((File)localObject).exists()) {
              ((File)localObject).delete();
            }

            String[] arrayOfString = new File(".").list(new SavegameFilter());
            for (int j = 0; j < arrayOfString.length; j++)
            {
              File localFile = new File("SRSavegame." + j);
              if (localFile.toString().compareTo(arrayOfString[j]) == 0)
                continue;
              Debug.println("renaming " + arrayOfString[j] + " to " + localFile);
              new File(arrayOfString[j]).renameTo(localFile);
              int k = this.filenames.indexOf(arrayOfString[j]);

              if (k == -1) {
                System.err.println("LoadSaveFrame: deletion rename: " + arrayOfString[j] + " is not in the filenames vector.");
              }
              this.filenames.setElementAt(localFile.toString(), k);
            }

            this.newSaveNumber -= 1;
          }
          catch (Exception localException)
          {
            localException.printStackTrace();
          }
        }
        localYesNoDialog.dispose();
      }
    }
    if (paramActionEvent.getSource() == this.cancel)
    {
      setVisible(false);
      dispose();
      this.response = null;
    }
  }

  public synchronized void itemStateChanged(ItemEvent paramItemEvent)
  {
    ((Integer)paramItemEvent.getItem()).intValue();
    String str = this.savegamelist.getSelectedItem();
    this.skipTextEvent = true;
    this.field.setText(str.substring(str.indexOf(']') + 2));
    this.action.setEnabled((this.field.getText().compareTo("") != 0) || 
      (this.savegamelist.getSelectedIndex() != -1));
  }

  public synchronized void textValueChanged(TextEvent paramTextEvent)
  {
    String str1 = this.field.getText();
    if (this.LastFieldContents == null) {
      this.LastFieldContents = str1;
    }
    if (this.skipTextEvent)
    {
      this.skipTextEvent = false;
    }
    else
    {
      int i = str1.compareTo("") != 0 ? 0 : 1;
      if (i != 0)
      {
        this.savegamelist.select(-1);
        this.action.setEnabled(false);
      }
      else
      {
        int j;
        String str2;
        if (str1.length() < this.LastFieldContents.length())
        {
          if (this.savegamelist.getSelectedIndex() != -1)
          {
            this.savegamelist.select(-1);

            j = this.field.getCaretPosition();
            str2 = str1.substring(0, j);
            this.skipTextEvent = true;
            this.field.setText(str2);
            this.field.setCaretPosition(j);
          }

        }
        else
        {
          this.action.setEnabled((i == 0) || (this.savegamelist.getSelectedIndex() != -1));

          j = this.field.getCaretPosition();

          str2 = str1.substring(0, j);

          String[] arrayOfString = this.savegamelist.getItems();
          int k = 0;
          for (int m = 0; (m < arrayOfString.length) && (k == 0); m++)
          {
            String str3 = arrayOfString[m].substring(arrayOfString[m].indexOf(']') + 2);
            if (!str3.startsWith(str2))
              continue;
            this.skipTextEvent = true;
            this.field.setText(str3);
            this.field.setCaretPosition(j);
            this.savegamelist.select(m);
            k = 1;
          }

          if (k == 0)
          {
            int n = this.savegamelist.getSelectedIndex();
            if (n != -1)
            {
              this.savegamelist.select(-1);

              this.skipTextEvent = true;
              this.field.setText(str2);
              this.field.setCaretPosition(j);
            }
          }
        }
      }

    }

    this.LastFieldContents = str1;
  }

  public void windowClosing(WindowEvent paramWindowEvent)
  {
    setVisible(false);
    dispose();
    this.response = null;
  }
  public void windowActivated(WindowEvent paramWindowEvent) {  }

  public void windowClosed(WindowEvent paramWindowEvent) {  }

  public void windowDeactivated(WindowEvent paramWindowEvent) {  }

  public void windowDeiconified(WindowEvent paramWindowEvent) {  }

  public void windowIconified(WindowEvent paramWindowEvent) {  }

  public void windowOpened(WindowEvent paramWindowEvent) {  }

  public File getResponse() { return this.response; } 
  public String getDescription() { return this.desc;
  }

  class SavegameFilter
    implements FilenameFilter
  {
    public boolean accept(File paramFile, String paramString)
    {
      return paramString.toLowerCase().startsWith("SRSavegame.".toLowerCase());
    }

    SavegameFilter()
    {
    }
  }
}