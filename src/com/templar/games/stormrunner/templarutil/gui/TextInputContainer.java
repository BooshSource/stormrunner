package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

public class TextInputContainer extends Container
  implements KeyListener
{
  public static final Color DEFAULTCOLOR = Color.blue;
  public static final Font DEFAULTFONT = new Font("SansSerif", 0, 12);
  protected TextContainer Prompt;
  protected Color PromptColor;
  protected Color InputColor;
  protected Font TextFont;
  protected FontMetrics TextMetrics;
  protected Dimension CursorSize;
  protected int PromptPadding;
  protected Insets Padding = new Insets(3, 3, 3, 3);
  protected Color BackgroundColor;
  protected UtilityThread Blinker;
  protected boolean CursorOn = true;
  protected StringBuffer Response;
  protected int ResponseLength;
  protected int MaxResponseLength = -1;
  protected String AllowedChars;
  protected String DeniedChars;
  protected Vector ActionRecipients;
  protected Vector ImageListeners = new Vector();

  public TextInputContainer()
  {
    initialize();

    setColors(DEFAULTCOLOR, DEFAULTCOLOR);
    setFont(DEFAULTFONT);

    updateCursor();
    updateSize();
  }

  public TextInputContainer(String paramString)
  {
    initialize();

    setColors(DEFAULTCOLOR, DEFAULTCOLOR);
    setFont(DEFAULTFONT);
    setPrompt(paramString);

    updateCursor();
    updateSize();
  }

  public TextInputContainer(String paramString, Color paramColor1, Color paramColor2, Font paramFont)
  {
    initialize();

    setFont(paramFont);
    setColors(paramColor1, paramColor2);
    setPrompt(paramString);

    updateSize();
  }

  protected void initialize()
  {
    appearanceChanged();

    super.setLayout(null);
    this.Prompt = new TextContainer();
    this.ActionRecipients = new Vector();
    this.Prompt.setLocation(this.Padding.left, this.Padding.top);
    super.add(this.Prompt);
    this.Response = new StringBuffer();
    addKeyListener(this);
  }

  public void addNotify()
  {
    appearanceChanged();

    super.addNotify();
    try
    {
      this.Blinker = new UtilityThread(500, this, getClass().getMethod("blinkCursor", new Class[0]), false);
      this.Blinker.start();
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
      System.err.println("TextInputContainer - Blinker thread not instantiated properly.");
    }

    requestFocus();
  }

  public void removeNotify()
  {
    appearanceChanged();

    super.removeNotify();

    this.Blinker.politeStop();
  }

  public boolean blinkCursor()
  {
    appearanceChanged();

    if (this.CursorOn)
      this.CursorOn = false;
    else
      this.CursorOn = true;

    repaint();

    return true;
  }

  public int getMaxResponseLength()
  {
    return this.MaxResponseLength;
  }

  public void setMaxResponseLength(int paramInt)
  {
    this.MaxResponseLength = paramInt;
  }

  public void setAllowedChars(String paramString)
  {
    this.AllowedChars = paramString;
  }

  public void setDeniedChars(String paramString)
  {
    this.DeniedChars = paramString;
  }

  public Insets getPadding()
  {
    return this.Padding;
  }

  public void setPadding(Insets paramInsets)
  {
    this.Padding = paramInsets;

    this.Prompt.setLocation(paramInsets.left, paramInsets.top);

    updateSize();
  }

  public Font getFont()
  {
    return this.TextFont;
  }

  public void setFont(Font paramFont)
  {
    this.TextFont = paramFont;
    this.Prompt.setFont(paramFont);

    this.TextMetrics = getToolkit().getFontMetrics(this.TextFont);

    updateCursor();
    updateSize();
  }

  public String getPrompt()
  {
    return this.Prompt.getText();
  }

  public void setPrompt(String paramString)
  {
    this.Prompt.setText(paramString);

    updateSize();
  }

  public void clearResponse()
  {
    this.Response.setLength(0);
    updateSize();
  }

  public void setColors(Color paramColor1, Color paramColor2)
  {
    appearanceChanged();

    this.PromptColor = paramColor1;
    this.Prompt.setColor(paramColor1);
    this.InputColor = paramColor2;
  }

  public void setBackgroundColor(Color paramColor)
  {
    appearanceChanged();

    this.BackgroundColor = paramColor;

    repaint();
  }

  protected void updateSize()
  {
    appearanceChanged();

    if (this.Response != null)
    {
      char[] arrayOfChar = this.Response.toString().toCharArray();
      this.ResponseLength = this.TextMetrics.charsWidth(arrayOfChar, 0, arrayOfChar.length);
    }
    else {
      this.ResponseLength = 0;
    }
    int i = this.Padding.left + this.Prompt.getSize().width + this.PromptPadding + this.ResponseLength + this.CursorSize.width + this.Padding.right;
    int j = this.Padding.top + this.Prompt.getSize().height + this.Padding.bottom;

    setSize(i, j);
  }

  protected void updateCursor()
  {
    appearanceChanged();

    this.CursorSize = new Dimension(this.TextMetrics.charWidth('s'), this.TextMetrics.getAscent() + this.TextMetrics.getDescent());
    this.PromptPadding = this.TextMetrics.charWidth(' ');
  }

  protected void deliverResult()
  {
    ActionEvent localActionEvent = new ActionEvent(this, 1001, this.Response.toString());
    processActionEvent(localActionEvent);

    clearResponse();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    if (this.BackgroundColor != null)
    {
      paramGraphics.setColor(this.BackgroundColor);
      paramGraphics.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
    }

    super.paint(paramGraphics);

    paramGraphics.setColor(this.InputColor);
    paramGraphics.setFont(this.TextFont);

    int i = this.Prompt.getLocation().x + this.Prompt.getSize().width + this.PromptPadding;
    int j = this.Padding.top + this.TextMetrics.getMaxAscent();

    char[] arrayOfChar = this.Response.toString().toCharArray();
    paramGraphics.drawChars(arrayOfChar, 0, arrayOfChar.length, i, j);

    if (this.CursorOn)
      paramGraphics.fillRect(i + this.ResponseLength, this.Padding.top, this.CursorSize.width, this.CursorSize.height);
  }

  public void keyPressed(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 10)
    {
      deliverResult();

      return;
    }

    if ((paramKeyEvent.getKeyCode() == 8) || (paramKeyEvent.getKeyCode() == 127))
    {
      this.CursorOn = true;

      this.Response.setLength(Math.max(this.Response.length() - 1, 0));
      updateSize();

      return;
    }

    if ((paramKeyEvent.getKeyCode() != 16) && (((this.MaxResponseLength < 0) || (this.Response.length() < this.MaxResponseLength))))
    {
      this.CursorOn = true;

      char c = paramKeyEvent.getKeyChar();
      if ((((this.DeniedChars == null) || (this.DeniedChars.indexOf(c, 0) == -1))) && ((
        (this.AllowedChars == null) || (this.AllowedChars.indexOf(c, 0) != -1))))
      {
        this.Response.append(c);
      }

      updateSize(); }
  }

  public void keyReleased(KeyEvent paramKeyEvent) {
  }

  public void keyTyped(KeyEvent paramKeyEvent) {
  }

  protected synchronized void processActionEvent(ActionEvent paramActionEvent) {
    if (this.ActionRecipients.size() > 0)
    {
      Enumeration localEnumeration = this.ActionRecipients.elements();

      while (localEnumeration.hasMoreElements())
      {
        ActionListener localActionListener = (ActionListener)localEnumeration.nextElement();
        localActionListener.actionPerformed(paramActionEvent);
      }
    }
  }

  public synchronized void addActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.addElement(paramActionListener);
  }

  public synchronized void removeActionListener(ActionListener paramActionListener)
  {
    this.ActionRecipients.removeElement(paramActionListener);
  }

  public void addImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.addElement(paramImageListener);
  }

  public void removeImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.removeElement(paramImageListener);
  }

  public void appearanceChanged()
  {
    for (int i = 0; i < this.ImageListeners.size(); ++i)
    {
      ImageListener localImageListener = (ImageListener)this.ImageListeners.elementAt(i);
      localImageListener.imageChanged(new ImageEvent(this, 4));
    }
  }
}