package com.templar.games.stormrunner.templarutil.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YesNoDialog extends Dialog
  implements ActionListener
{
  public static final String YES = "Yes";
  public static final String NO = "No";
  Button yes;
  Button no;
  String response;

  public YesNoDialog(Frame paramFrame, String paramString1, String paramString2)
  {
    super(paramFrame, paramString1, true);
    Panel localPanel1 = new Panel(); Panel localPanel2 = new Panel();
    setLayout(new BorderLayout());
    this.yes = new Button("Yes");
    this.no = new Button("No");
    add(localPanel1, "Center");
    add(localPanel2, "South");
    localPanel2.add(this.yes);
    localPanel2.add(this.no);
    this.yes.addActionListener(this);
    this.no.addActionListener(this);
    Label localLabel = new Label(paramString2, 1);
    localPanel1.add(localLabel);
    pack();
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(localDimension.width / 2 - getSize().width / 2, localDimension.height / 2 - getSize().height / 2);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent paramActionEvent) {
    this.response = paramActionEvent.getActionCommand();
    setVisible(false);
  }
  public String getResponse() { return this.response;
  }
}