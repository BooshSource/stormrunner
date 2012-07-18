package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;

public class TextContainer extends Component
  implements Highlightable, Paddable
{
  public static final Color DEFAULT_COLOR = Color.blue;
  public static final Font DEFAULT_FONT = new Font("SansSerif", 0, 12);
  public static final int DEFAULT_VERTICAL_LINE_SPACING = 0;
  public static final int SCROLL_STEPS = 5;
  public static final int DEFAULT_STREAK_DELAY = 25;
  public static final int DEFAULT_STREAK_LENGTH = 5;
  public static final int DEFAULT_STREAK_BRIGHTENING_WEIGHT = 50;
  public static final int DEFAULT_BURN_RATE = 25;
  public static final int DEFAULT_BURN_STEPS = 5;
  public static final int DEFAULT_BURN_BRIGHTENING_WEIGHT = 50;
  public static final Color DEFAULT_BURN_COLOR = Color.green;
  public static final int DEFAULT_BURN_LINE_THICKNESS = 2;
  protected String Text;
  protected Color TextColor;
  protected Font TextFont;
  protected boolean CenterAligned = false;
  protected FontMetrics TextFontMetrics;
  protected int VerticalLineSpacing = 0;
  protected int TextAscent;
  protected int MaxLineHeight;
  protected Color BackgroundColor;
  protected Insets Padding = new Insets(0, 0, 0, 0);
  protected int HardWidth = -1;
  protected int HardHeight = -1;
  protected boolean WidthPacked = false;
  protected boolean HeightPacked = false;
  protected int ActualWidth;
  protected int ActualHeight;
  protected int LinesDisplayable;
  protected int ScrollStepHeight;
  protected int ScrollStep;
  protected Point ScrollTranslation = new Point(0, 0);
  protected String[] Lines;
  protected Color[] LineColors;
  protected int[] LineDelays;
  public boolean Added = false;
  protected boolean StreakEffect = false;
  protected int StreakCharacterDelay = 25;
  protected int StreakLength = 5;
  protected int StreakBrighteningWeight = 50;
  protected int LastStreakedCharacter;
  protected int CurrentStreakLine;
  protected int LastXOffset;
  protected boolean StreakComplete;
  protected UtilityThread StreakThread;
  protected boolean BurnLineEffect = false;
  protected int BurnRate = 25;
  protected int BurnSteps = 5;
  protected int BurnBrighteningWeight = 50;
  protected int BurnLineLength = -1;
  protected Color BurnColor = DEFAULT_BURN_COLOR;
  protected int BurnLineThickness = 2;
  protected int BurnStep;
  protected int BurnVerticalPadding;
  protected int BurnYOffset;
  protected boolean BurnComplete;
  protected UtilityThread BurnThread;
  protected Color SwappedBackgroundColor;
  protected Color SwappedTextColor;
  protected Vector ImageListeners = new Vector();
  protected Vector ActionListeners = new Vector();

  public TextContainer()
  {
    setFont(DEFAULT_FONT);
    setColor(DEFAULT_COLOR);
  }

  public TextContainer(String paramString)
  {
    setFont(DEFAULT_FONT);
    setColor(DEFAULT_COLOR);
    setText(paramString);
  }

  public TextContainer(String paramString, Color paramColor, Font paramFont)
  {
    setColor(paramColor);
    setFont(paramFont);
    setText(paramString);
  }

  public String getText()
  {
    return this.Text;
  }

  public void setText(String paramString)
  {
    if (this.StreakThread != null)
      this.StreakThread.politeStop();

    if (this.BurnThread != null) {
      this.BurnThread.politeStop();
    }

    if (this.HardHeight > 0)
    {
      this.ScrollTranslation = new Point(0, 0);
    }

    if (paramString == null)
      this.Text = "";
    else {
      this.Text = paramString;
    }

    updateSize();

    if (this.Added) {
      textEffects();
    }

    super.repaint();
  }

  public int getHardWidth()
  {
    return this.HardWidth;
  }

  public void setHardWidth(int paramInt)
  {
    this.HardWidth = paramInt;

    updateSize();
  }

  public Dimension getHardSize()
  {
    return new Dimension(this.HardWidth, this.HardHeight);
  }

  public void setHardSize(int paramInt1, int paramInt2)
  {
    this.HardWidth = paramInt1;
    this.HardHeight = paramInt2;

    updateSize();
  }

  public void setPackedWidth(boolean paramBoolean)
  {
    this.WidthPacked = paramBoolean;
  }

  public void setPackedHeight(boolean paramBoolean)
  {
    this.HeightPacked = paramBoolean;
  }

  public boolean getCenterAligned()
  {
    return this.CenterAligned;
  }

  public void setCenterAligned(boolean paramBoolean)
  {
    appearanceChanged();

    this.CenterAligned = paramBoolean;
  }

  public int getVerticalLineSpacing()
  {
    return this.VerticalLineSpacing;
  }

  public void setVerticalLineSpacing(int paramInt)
  {
    this.VerticalLineSpacing = paramInt;

    updateSize();
  }

  public Color getColor()
  {
    return this.TextColor;
  }

  public void setColor(Color paramColor)
  {
    appearanceChanged();

    this.TextColor = paramColor;

    super.repaint();
  }

  public Color getBackgroundColor()
  {
    return this.BackgroundColor;
  }

  public void setBackground(Color paramColor)
  {
    setBackgroundColor(paramColor);
  }

  public void setBackgroundColor(Color paramColor)
  {
    appearanceChanged();

    this.BackgroundColor = paramColor;

    super.repaint();
  }

  public Font getFont()
  {
    return this.TextFont;
  }

  public void setFont(Font paramFont)
  {
    this.TextFont = paramFont;

    this.TextFontMetrics = super.getToolkit().getFontMetrics(this.TextFont);

    updateSize();
  }

  public Insets getInsets()
  {
    return this.Padding;
  }

  public void setPadding(Insets paramInsets)
  {
    this.Padding = paramInsets;

    updateSize();
  }

  public void setStreak(boolean paramBoolean)
  {
    this.StreakEffect = paramBoolean;
  }

  public void setStreak(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    this.StreakEffect = paramBoolean;
    setStreakCharacteristics(paramInt1, paramInt2, paramInt3);
  }

  public void setStreakCharacteristics(int paramInt1, int paramInt2, int paramInt3)
  {
    this.StreakCharacterDelay = paramInt1;
    this.StreakLength = paramInt2;
    this.StreakBrighteningWeight = paramInt3;
  }

  public void setBurnLine(boolean paramBoolean)
  {
    this.BurnLineEffect = paramBoolean;

    updateSize();
  }

  public void setBurnLine(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor, int paramInt6)
  {
    this.BurnLineEffect = paramBoolean;
    setBurnLineCharacteristics(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramColor, paramInt6);
  }

  public void setBurnLineCharacteristics(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor, int paramInt6)
  {
    this.BurnLineLength = paramInt1;
    this.BurnLineThickness = paramInt2;
    this.BurnVerticalPadding = paramInt3;
    this.BurnRate = paramInt4;
    this.BurnSteps = paramInt5;
    this.BurnColor = paramColor;
    this.BurnBrighteningWeight = paramInt6;

    updateSize();
  }

  public void highlight(Color paramColor1, Color paramColor2)
  {
    if ((paramColor1 != null) && (paramColor2 != null))
    {
      appearanceChanged();

      this.SwappedBackgroundColor = this.BackgroundColor;
      this.SwappedTextColor = this.TextColor;

      setBackgroundColor(paramColor1);
      setColor(paramColor2);
    }
  }

  public void unHighlight()
  {
    appearanceChanged();

    setBackgroundColor(this.SwappedBackgroundColor);
    if (this.SwappedTextColor != null)
      setColor(this.SwappedTextColor);
  }

  public void resetEffects()
  {
    if (this.StreakThread != null)
      this.StreakThread.politeStop();

    if (this.BurnThread != null) {
      this.BurnThread.politeStop();
    }

    this.ScrollStep = 0;
    this.ScrollTranslation = new Point(0, 0);

    this.LastStreakedCharacter = (-this.StreakLength);
    this.CurrentStreakLine = 0;
    this.StreakComplete = false;

    this.BurnStep = -1;
    this.BurnComplete = false;

    appearanceChanged();
  }

  protected void updateSize()
  {
    int i;
    int j;
    int k;
    appearanceChanged();

    this.ActualWidth = ((this.Text == null) ? 0 : this.TextFontMetrics.stringWidth(this.Text));

    if (this.HardWidth < 0)
      i = this.ActualWidth;
    else {
      i = this.HardWidth;
    }

    if (this.BurnLineEffect)
    {
      if (this.BurnLineLength > i)
        i = this.BurnLineLength;

      if (this.BurnLineLength < 0)
      {
        this.BurnLineLength = i;
      }

    }

    if (this.HardWidth < 0)
      this.ActualWidth = (j = i + this.Padding.left + this.Padding.right);
    else {
      j = this.HardWidth;
    }

    if ((this.HardWidth > 0) && (this.Text != null))
    {
      calculateLines();
      k = this.Lines.length;
    }
    else {
      k = 1;
    }
    this.TextAscent = (this.TextFontMetrics.getMaxAscent() + this.VerticalLineSpacing);
    this.MaxLineHeight = (this.TextAscent + this.TextFontMetrics.getMaxDescent() + this.TextFontMetrics.getLeading());
    int l = this.TextAscent * k + this.TextFontMetrics.getMaxDescent() + this.TextFontMetrics.getLeading();
    this.BurnYOffset = l;
    this.ActualHeight = l;

    if (this.HardHeight < 0)
    {
      this.ActualHeight = (l += this.Padding.top + this.Padding.bottom);

      if (this.BurnLineEffect)
        this.ActualHeight = (l += this.BurnLineThickness + this.BurnVerticalPadding);
    }
    else
    {
      l = this.HardHeight;

      this.ScrollStepHeight = (int)Math.floor(this.TextAscent / 5.0F);

      this.LinesDisplayable = (int)Math.floor((this.HardHeight - this.Padding.top - this.Padding.bottom) / this.TextAscent);
    }

    super.setSize(j, l);
  }

  protected void calculateLines()
  {
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    Vector localVector3 = new Vector();
    Vector localVector4 = new Vector();
    Vector localVector5 = new Vector();
    StringTokenizer localStringTokenizer = new StringTokenizer(this.Text, " \n\t]", true);
    int i = this.HardWidth;
    if (this.Padding != null)
      i -= this.Padding.left + this.Padding.right;

    int j = 0;
    Object localObject = new String();
    int l = 0;
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();

      if (str.compareTo(" ") == 0)
      {
        localObject = localObject + " ";
        label583: j += this.TextFontMetrics.charWidth(' ');
      }
      else if (str.compareTo("\n") == 0)
      {
        localVector1.addElement(localObject);
        localObject = new String();
        j = 0;
      }
      else if (str.compareTo("]") == 0)
      {
        if (l != 0)
        {
          l = 0;
        }
        else
        {
          localObject = localObject + "]";
          j += this.TextFontMetrics.charWidth(']');
        }
      }
      else if (str.compareTo("\t") == 0)
      {
        l = 1;
      }
      else if (l != 0)
      {
        try
        {
          switch (str.charAt(0))
          {
          case 'd':
            localVector2.addElement(new Integer(localVector1.size()));
            localVector3.addElement(new Integer(str.substring(1, str.length())));
            break;
          case 'c':
            i1 = str.indexOf(44) + 1;
            i2 = str.indexOf(44, i1) + 1;
            if ((i1 <= 0) || (i2 <= 0))
              break label583;
            int i3 = Integer.parseInt(str.substring(1, i1 - 1));
            int i4 = Integer.parseInt(str.substring(i1, i2 - 1));
            int i5 = Integer.parseInt(str.substring(i2, str.length()));
            localVector4.addElement(new Integer(localVector1.size()));
            localVector5.addElement(new Color(i3, i4, i5));

            break;
          default:
            System.out.println("TextContainer: bad control sequence in Text (unrecognized control): " + str);
          }

        }
        catch (NumberFormatException localNumberFormatException)
        {
          System.out.println("TextContainer: bad control sequence in Text (number did not parse): " + str);
        }

      }
      else
      {
        int k = this.TextFontMetrics.stringWidth(str);

        if (j + k > i)
        {
          localVector1.addElement(localObject);
          j = k;
          localObject = str;
        }
        else
        {
          j += k;
          localObject = localObject + str;
        }
      }

    }

    if (((String)localObject).length() > 0)
      localVector1.addElement(localObject);

    this.Lines = new String[Math.max(1, localVector1.size())];
    this.LineDelays = new int[this.Lines.length];
    this.LineColors = new Color[this.Lines.length];

    if (localVector1.size() < 1)
      this.Lines[0] = "";
    else
      for (i1 = 0; i1 < this.Lines.length; ++i1)
        this.Lines[i1] = ((String)localVector1.elementAt(i1));


    for (int i1 = 0; i1 < localVector2.size(); ++i1)
    {
      this.LineDelays[((Integer)localVector2.elementAt(i1)).intValue()] = ((Integer)localVector3.elementAt(i1)).intValue();
    }

    for (int i2 = 0; i2 < localVector4.size(); ++i2)
    {
      this.LineColors[((Integer)localVector4.elementAt(i2)).intValue()] = ((Color)localVector5.elementAt(i2));
    }
  }

  public void addNotify()
  {
    appearanceChanged();

    super.addNotify();

    this.Added = true;

    textEffects();
  }

  public void removeNotify()
  {
    appearanceChanged();

    super.removeNotify();

    this.Added = false;

    resetEffects();
  }

  protected void textEffects()
  {
    if ((this.Text != null) && (this.Text.length() > 0))
    {
      if ((this.BurnLineEffect) && (!(this.StreakEffect)))
      {
        startBurn();
      }

      if (this.StreakEffect)
      {
        try
        {
          this.StreakThread = new UtilityThread(this.StreakCharacterDelay, this, getClass().getMethod("streakStep", null), false);
          this.StreakThread.setInclusiveDelay(true);

          this.LastStreakedCharacter = (-this.StreakLength);
          this.CurrentStreakLine = 0;
          this.StreakComplete = false;

          this.StreakThread.start();

          return;
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
          localNoSuchMethodException.printStackTrace();

          return;
        }
      }
    }
  }

  public void startBurn()
  {
    try
    {
      this.BurnThread = new UtilityThread(this.BurnRate, this, getClass().getMethod("burnStep", null), false);
      this.BurnThread.setInclusiveDelay(true);

      this.BurnStep = -1;
      this.BurnComplete = false;

      this.BurnThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public boolean streakStep()
  {
    appearanceChanged();

    int i = 0;

    this.LastStreakedCharacter += 1;

    if (this.HardWidth < 0)
    {
      if (this.LastStreakedCharacter >= this.Text.length() + this.StreakLength - 1)
      {
        this.StreakComplete = true;
      }
    }
    else {
      if (this.LastStreakedCharacter >= this.Lines[this.CurrentStreakLine].length() + this.StreakLength - 1)
      {
        if (this.LineDelays[this.CurrentStreakLine] > 0)
          try
          {
            Thread.currentThread(); Thread.sleep(this.LineDelays[this.CurrentStreakLine]);
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }


        this.LastStreakedCharacter = (-this.StreakLength);
        i = 1;
      }

      if (i != 0)
      {
        this.CurrentStreakLine += 1;
        i = 0;
      }
      if (this.CurrentStreakLine >= this.Lines.length)
      {
        this.StreakComplete = true;
      }

      if ((this.CurrentStreakLine >= this.LinesDisplayable) && (this.LastStreakedCharacter == 0))
      {
        if (this.ScrollStep < 5)
        {
          this.LastStreakedCharacter -= 1;
          this.ScrollStep += 1;

          this.ScrollTranslation.translate(0, -this.ScrollStepHeight);
        }
        else
        {
          this.ScrollStep = 0;
          int j = this.CurrentStreakLine - this.LinesDisplayable + 1;
          this.ScrollTranslation = new Point(0, -(j * this.TextAscent));
        }
      }
    }

    if (this.StreakComplete)
    {
      this.StreakThread = null;

      if (this.BurnLineEffect)
        startBurn();
      else
        notifyEffectsComplete();

      return false;
    }

    super.repaint(this.StreakCharacterDelay);

    return true;
  }

  public boolean burnStep()
  {
    appearanceChanged();

    this.BurnStep += 1;

    if (this.BurnStep >= this.BurnSteps)
    {
      this.BurnThread = null;

      this.BurnComplete = true;

      notifyEffectsComplete();

      return false;
    }

    super.repaint(this.BurnRate);
    return true;
  }

  public static Color streakBright(Color paramColor, int paramInt)
  {
    int i = Math.min(paramColor.getRed() + paramInt, 255);
    int j = Math.min(paramColor.getGreen() + paramInt, 255);
    int k = Math.min(paramColor.getBlue() + paramInt, 255);
    Color localColor = new Color(i, j, k);

    return localColor;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    if ((this.Text != null) && (this.Text.length() > 0))
    {
      int k;
      int l;
      paramGraphics.setFont(this.TextFont);

      if (this.BackgroundColor != null)
      {
        paramGraphics.setColor(this.BackgroundColor);
        paramGraphics.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
      }

      if ((this.StreakEffect) && (!(this.StreakComplete)))
      {
        String str1;
        int i1;
        if (this.HardWidth < 0)
          str1 = this.Text;
        else
          str1 = this.Lines[this.CurrentStreakLine];

        if (this.HardHeight > 0)
        {
          paramGraphics.translate(this.ScrollTranslation.x, this.ScrollTranslation.y);
        }

        if (this.CurrentStreakLine > 0)
        {
          int j = this.TextAscent + this.Padding.top; l = 0;
          for (i1 = 0; i1 < this.CurrentStreakLine; ++i1)
          {
            String str2 = this.Lines[i1];

            if (this.CenterAligned)
              l = centeringOffset(str2);

            if (this.LineColors[i1] != null)
              paramGraphics.setColor(this.LineColors[i1]);
            else
              paramGraphics.setColor(this.TextColor);

            paramGraphics.drawString(str2, this.Padding.left + l, j);
            j += this.TextAscent;
          }

        }

        if ((this.Lines == null) || ((this.Lines != null) && (this.CurrentStreakLine < this.Lines.length)))
        {
          Color localColor1;
          if ((this.LineColors != null) && (this.LineColors[this.CurrentStreakLine] != null))
            paramGraphics.setColor(localColor1 = this.LineColors[this.CurrentStreakLine]);
          else
            paramGraphics.setColor(localColor1 = this.TextColor);

          l = 0;
          i1 = 0;
          if (this.CenterAligned)
            l = i1 = centeringOffset(str1);
          int i2 = Math.max(this.LastStreakedCharacter - this.StreakLength + 1, -this.StreakLength);

          if (this.LastStreakedCharacter >= this.StreakLength)
          {
            localObject = str1.substring(0, i2);
            l += this.TextFontMetrics.stringWidth((String)localObject);

            paramGraphics.drawString((String)localObject, this.Padding.left + i1, this.TextAscent * (this.CurrentStreakLine + 1) + this.Padding.top);
          }

          this.LastXOffset = l;

          Object localObject = localColor1;
          char[] arrayOfChar = new char[1];
          int i3 = Math.min(i2 + this.StreakLength, str1.length());
          for (int i4 = i2; i4 < i3; ++i4)
          {
            localObject = streakBright((Color)localObject, this.StreakBrighteningWeight);
            if (i4 >= 0)
            {
              paramGraphics.setColor((Color)localObject);
              arrayOfChar[0] = str1.charAt(i4);
              paramGraphics.drawChars(arrayOfChar, 0, 1, this.Padding.left + l, this.TextAscent * (this.CurrentStreakLine + 1) + this.Padding.top);
              l += this.TextFontMetrics.charsWidth(arrayOfChar, 0, 1);
            }
          }
        }
        return;
      }

      paramGraphics.setColor(this.TextColor);

      if (this.HardHeight > 0)
      {
        paramGraphics.translate(this.ScrollTranslation.x, this.ScrollTranslation.y);
      }

      int i = this.TextAscent + this.Padding.top;
      if (this.HardWidth < 0)
      {
        k = 0;
        if (this.CenterAligned)
          k = centeringOffset(this.Text);
        paramGraphics.drawString(this.Text, this.Padding.left + k, i);
      }
      else
      {
        for (k = 0; k < this.Lines.length; ++k)
        {
          l = 0;

          if (this.CenterAligned)
            l = centeringOffset(this.Lines[k]);

          if (this.LineColors[k] != null)
            paramGraphics.setColor(this.LineColors[k]);
          else
            paramGraphics.setColor(this.TextColor);

          paramGraphics.drawString(this.Lines[k], this.Padding.left + l, i);
          i += this.TextAscent;
        }
      }

      if (this.BurnLineEffect)
      {
        Color localColor2 = this.BurnColor;
        if (!(this.BurnComplete))
          localColor2 = streakBright(localColor2, this.BurnBrighteningWeight * (this.BurnSteps - this.BurnStep));

        paramGraphics.setColor(localColor2);
        paramGraphics.fillRect(this.Padding.left, this.BurnYOffset + this.BurnVerticalPadding + this.Padding.top, this.BurnLineLength, this.BurnLineThickness);
      }
    }
  }

  private int centeringOffset(String paramString)
  {
    return (this.HardWidth / 2 - this.TextFontMetrics.stringWidth(paramString) / 2); }

  public boolean getStreakComplete() { return this.StreakComplete;
  }

  public void addImageListener(ImageListener paramImageListener) {
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

  public void addEffectsCompletionListener(ActionListener paramActionListener)
  {
    this.ActionListeners.addElement(paramActionListener);
  }

  public void removeEffectsCompletionListener(ActionListener paramActionListener)
  {
    this.ActionListeners.removeElement(paramActionListener);
  }

  public void notifyEffectsComplete()
  {
    for (int i = 0; i < this.ActionListeners.size(); ++i)
    {
      ActionListener localActionListener = (ActionListener)this.ActionListeners.elementAt(i);
      localActionListener.actionPerformed(new ActionEvent(this, 1001, "Text Effects Complete")); }
  }

  public Dimension getMaximumSize() {
    return getMinimumSize(); } 
  public Dimension getPreferredSize() { return getMinimumSize(); } 
  public Dimension getSize() { return getMinimumSize();
  }

  public Dimension getMinimumSize()
  {
    int i;
    int j;
    Dimension localDimension = super.getSize();

    if (this.HeightPacked)
    {
      j = ((this.HardHeight > -1) && (this.HardHeight < this.ActualHeight)) ? this.HardHeight : this.ActualHeight;
    } else if (this.HardHeight > -1)
      j = this.HardHeight;
    else
      j = localDimension.height;

    if (this.WidthPacked)
    {
      i = ((this.HardWidth > -1) && (this.HardWidth < this.ActualWidth)) ? this.HardWidth : this.ActualWidth;
    } else if (this.HardWidth > -1)
      i = this.HardWidth;
    else
      i = localDimension.width;

    return new Dimension(i, j);
  }
}