package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.program.Conditional;
import com.templar.games.stormrunner.program.Contextualized;
import com.templar.games.stormrunner.program.Copyable;
import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.Linkable;
import com.templar.games.stormrunner.program.Loop;
import com.templar.games.stormrunner.program.Parameterized;
import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.HighlightBox;
import com.templar.games.stormrunner.templarutil.gui.ImageButton;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

public class ProgramComponent extends ImageComponent
  implements ActionListener
{
  public static final int DOUBLE_CLICK_THRESHOLD = 500;
  public static final int BOUNDING_COMPONENT_DROP = 15;
  protected Insets EditorInsets;
  protected Linkable ProgramPart;
  protected Vector DropTargets;
  protected Image NormalImage;
  protected Image IconImage;
  protected Image AncillaryImage;
  protected ImageComponent Ancillary;
  protected int AncillaryDrop;
  protected boolean AncillaryActive = false;
  protected ProgramComponent BoundingComponent;
  protected int ParameterYOffset;
  protected EditorPalette OurPalette;
  protected Editor editor;
  protected EditorDisplay display;
  protected ProgramContainer programlayer;
  protected Vector next;
  protected ProgramComponent prev;
  protected MotionHandler mh;
  protected ClickHandler ch;
  private Point StartPoint;
  protected boolean DragScrollDone = true;
  protected Rectangle TrashBounds;
  protected Font CurrentFont;
  protected FontMetrics CurrentFontMetrics;
  protected Object LastContext;
  protected boolean PaletteMouse;
  protected boolean DoubleClickPressed = false;
  protected boolean MouseJustPressed = false;
  protected boolean Detached = false;
  private ProgramComponent realthis;

  public ProgramComponent(Linkable paramLinkable, Image paramImage1, Image paramImage2, int paramInt, EditorPalette paramEditorPalette)
  {
    this.realthis = this;

    setContext(paramEditorPalette);

    this.NormalImage = paramImage1;
    this.IconImage = paramImage2;
    this.ProgramPart = paramLinkable;

    this.ParameterYOffset = paramInt;

    setImage(paramImage1);

    this.CurrentFont = new Font("SansSerif", 0, 9);
    this.CurrentFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(this.CurrentFont);

    this.DropTargets = new Vector();
    this.next = new Vector(1, 1);

    this.mh = new MotionHandler();
    this.ch = new ClickHandler();
    addMouseMotionListener(this.mh);
    addMouseListener(this.ch);

    setSize(getSize());
  }

  public void setAncillaryImage(Image paramImage, int paramInt)
  {
    this.AncillaryImage = paramImage;
    this.AncillaryDrop = paramInt;

    if (paramImage != null)
      this.Ancillary = new ImageComponent(paramImage);
  }

  public void setContext(EditorPalette paramEditorPalette)
  {
    this.OurPalette = paramEditorPalette;
    this.display = paramEditorPalette.getEditorDisplay();
    this.editor = this.display.getEditor();
    this.programlayer = this.editor.getProgramLayer();
  }

  public void setContext(ProgramContainer paramProgramContainer)
  {
    this.programlayer = paramProgramContainer;
    this.editor = paramProgramContainer.getEditor();
    this.OurPalette = this.editor.getPalette();
    this.display = this.editor.getEditorDisplay();
  }

  public void paint(Graphics paramGraphics)
  {
    super.paint(paramGraphics);

    if ((this.ParameterYOffset >= 0) && ((this.ProgramPart instanceof Parameterized)))
    {
      paramGraphics.setColor(Color.white);
      paramGraphics.setFont(this.CurrentFont);

      String str = ((Parameterized)this.ProgramPart).getParameterString();
      char[] arrayOfChar = str.toCharArray();
      int i = this.CurrentFontMetrics.charsWidth(arrayOfChar, 0, arrayOfChar.length);
      int j = getSize().width / 2 - i / 2;
      paramGraphics.drawString(str, j, this.ParameterYOffset);
    }
  }

  protected Vector getNextProgramComponents()
  {
    return this.next;
  }

  public ProgramComponent copy()
  {
    ProgramComponent localProgramComponent;
    if ((this instanceof Linkable))
      localProgramComponent = new ProgramComponent((Linkable)this, getNormalImage(), getIconImage(), this.ParameterYOffset, this.OurPalette);
    else {
      localProgramComponent = new ProgramComponent((Linkable)((Copyable)this.ProgramPart).copy(), getNormalImage(), getIconImage(), this.ParameterYOffset, this.OurPalette);
    }
    localProgramComponent.DropTargets = this.DropTargets;
    localProgramComponent.setAncillaryImage(this.AncillaryImage, this.AncillaryDrop);
    if (this.BoundingComponent != null)
      localProgramComponent.setBoundingComponent(this.BoundingComponent.copy());
    return localProgramComponent;
  }

  public void delete()
  {
    Vector localVector = (Vector)this.next.clone();
    for (int i = 0; i < localVector.size(); i++)
    {
      ProgramComponent localObject = (ProgramComponent)localVector.elementAt(i);
      ((ProgramComponent)localObject).delete();
    }

    if (this.AncillaryActive)
    {
      getParent().remove(this.Ancillary);
      this.AncillaryActive = false;
    }

    Object localObject = getParent();
    ((Container)localObject).remove(this);

    ((Component)localObject).repaint();
  }

  public Linkable getProgramPart()
  {
    return this.ProgramPart;
  }

  public Image getNormalImage()
  {
    return this.NormalImage;
  }

  public Image getIconImage()
  {
    return this.IconImage;
  }

  public ProgramComponent getBoundingComponent()
  {
    return this.BoundingComponent;
  }

  public void setBoundingComponent(ProgramComponent paramProgramComponent)
  {
    this.BoundingComponent = paramProgramComponent;

    ((BoundingComponent)paramProgramComponent).setBoundSource(this);

    this.next.addElement(paramProgramComponent);

    paramProgramComponent.setLocation(getLocation().x, getLocation().y + getSize().height + 15);
  }

  public boolean dropAgainst(ProgramComponent paramProgramComponent)
  {
    boolean b = false;
    Enumeration localEnumeration = this.DropTargets.elements();
    while ((localEnumeration.hasMoreElements()) && (b == false))
    {
      DropTargetInfo localDropTargetInfo = (DropTargetInfo)localEnumeration.nextElement();
      try
      {
        Class localClass = Class.forName(localDropTargetInfo.TargetPart);
        if (!localClass.isAssignableFrom(paramProgramComponent.ProgramPart.getClass()))
        {
          continue;
        }
        int j = paramProgramComponent.getLocation().x + localDropTargetInfo.DropTarget.x;
        int k = paramProgramComponent.getLocation().y + localDropTargetInfo.DropTarget.y;
        setLocation(j, k);
        b = true;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
      }
    }

    return b;
  }

  public boolean attachPrevious(ProgramComponent paramProgramComponent)
  {
    boolean bool1 = false;

    Enumeration localEnumeration = paramProgramComponent.next.elements();
    Object localObject;
    while (localEnumeration.hasMoreElements())
    {
      localObject = (ProgramComponent)localEnumeration.nextElement();
      if (!(localObject instanceof BoundingComponent))
      {
        continue;
      }

      ProgramComponent localProgramComponent = this;
      while (localProgramComponent.next.size() > 0) {
        localProgramComponent = (ProgramComponent)localProgramComponent.next.elementAt(0);
      }

      boolean bool2 = ((ProgramComponent)localObject).dropAgainst(localProgramComponent);

      if (bool2)
      {
        paramProgramComponent.next.removeElement(localObject);
        localProgramComponent.next.addElement(localObject);
        ((ProgramComponent)localObject).prev = localProgramComponent;
      }
      else {
        return false;
      }

    }

    if ((paramProgramComponent.ProgramPart instanceof Loop))
    {
      localObject = (Loop)paramProgramComponent.ProgramPart;
      if (((Loop)localObject).getDestination() == null)
      {
        ((Loop)localObject).setDestination((Instruction)this.ProgramPart);

        if (((Instruction)this.ProgramPart).getPreviousInstruction() != null)
          System.out.println("ProgramComponent: Inconsistency detected: ProgramPart " + this.ProgramPart + " had a non-null prev while attempting a loop attachment.");
        else {
          bool1 = true;
        }
      }
    }
    else
    {
      bool1 = this.ProgramPart.attachPrevious(paramProgramComponent.ProgramPart);
    }

    if (bool1)
    {
      this.prev = paramProgramComponent;
      paramProgramComponent.next.addElement(this.realthis);
    }

    return bool1;
  }

  public void manualAttachPrevious(ProgramComponent paramProgramComponent)
  {
    this.prev = paramProgramComponent;
    paramProgramComponent.next.addElement(this.realthis);
  }

  public boolean detachPrevious()
  {
    boolean bool1 = false;

    if (this.prev != null)
    {
      int i = 0;
      ProgramComponent localProgramComponent = this;
      Vector localVector = new Vector();
      if (this.BoundingComponent != null)
        localVector.addElement(this);
      while ((localProgramComponent.next.size() > 0) && (i == 0))
      {
        localProgramComponent = (ProgramComponent)localProgramComponent.next.elementAt(0);

        if (localProgramComponent.BoundingComponent != null)
        {
          localVector.addElement(localProgramComponent);
        }
        else {
          if ((!(localProgramComponent instanceof BoundingComponent)) || (localVector.contains(((BoundingComponent)localProgramComponent).getBoundSource()))) {
            continue;
          }
          i = 1;

          localProgramComponent.prev.next.removeElement(localProgramComponent);

          this.prev.next.addElement(localProgramComponent);
          localProgramComponent.prev = this.prev;

          localProgramComponent.moveToProgramLayer();

          boolean bool2 = localProgramComponent.dropAgainst(this.prev);

          if (bool2)
            continue;
          System.out.println("ProgramComponent: BoundingComponent detatched but failed to find a match while attempting to reposition.");
        }

      }

      if ((this.prev.ProgramPart instanceof Loop))
      {
        if ((((Loop)this.prev.ProgramPart).getDestination() == this.ProgramPart) && (((Instruction)this.ProgramPart).getPreviousInstruction() == null))
        {
          ((Loop)this.prev.ProgramPart).setDestination(null);
          ((Instruction)this.ProgramPart).setPreviousInstruction(null);
          bool1 = true;
        }
        else {
          System.out.println("ProgramComponent: Inconsistent list state while trying to detach from a Loop.");
        }
      }
      else
      {
        bool1 = this.ProgramPart.detachPrevious(this.prev.ProgramPart);
      }

      if (bool1)
      {
        this.prev.next.removeElement(this);
        this.prev = null;
      }
    }

    return bool1;
  }

  public void addDropTargetInfo(DropTargetInfo paramDropTargetInfo)
  {
    this.DropTargets.addElement(paramDropTargetInfo);
  }

  public synchronized void setLocation(int paramInt1, int paramInt2)
  {
    int i = paramInt1 - getLocation().x;
    int j = paramInt2 - getLocation().y;

    super.setLocation(paramInt1, paramInt2);

    if (this.AncillaryActive)
    {
      Point localPoint = this.Ancillary.getLocation();
      localPoint.translate(i, j);
      this.Ancillary.setLocation(localPoint);
    }

    for (int k = 0; k < this.next.size(); k++)
    {
      ProgramComponent localProgramComponent = (ProgramComponent)this.next.elementAt(k);
      localProgramComponent.setLocation(localProgramComponent.getLocation().x + i, localProgramComponent.getLocation().y + j);
    }
  }

  protected void moveToProgramLayer()
  {
    int i = this.programlayer.getLocation().x * -1;
    int j = this.programlayer.getLocation().y * -1;

    if (this.AncillaryActive)
    {
      getParent().remove(this.Ancillary);
      this.Ancillary.setLocation(this.Ancillary.getLocation().x + i, this.Ancillary.getLocation().y + j);
      this.programlayer.add(this.Ancillary);
    }
    getParent().remove(this);
    super.setLocation(getLocation().x + i, getLocation().y + j);
    this.programlayer.add(this, 0);

    for (int k = 0; k < this.next.size(); k++)
    {
      ProgramComponent localProgramComponent = (ProgramComponent)this.next.elementAt(k);
      localProgramComponent.moveToProgramLayer();
    }
  }

  protected void moveToDisplayLayer()
  {
    int i = this.programlayer.getLocation().x;
    int j = this.programlayer.getLocation().y;

    if (this.AncillaryActive)
    {
      getParent().remove(this.Ancillary);
      this.Ancillary.setLocation(this.Ancillary.getLocation().x + i, this.Ancillary.getLocation().y + j);
      this.display.add(this.Ancillary, 0);
    }
    getParent().remove(this);
    super.setLocation(getLocation().x + i, getLocation().y + j);
    this.display.add(this, 0);

    for (int k = 0; k < this.next.size(); k++)
    {
      ProgramComponent localProgramComponent = (ProgramComponent)this.next.elementAt(k);
      localProgramComponent.moveToDisplayLayer();
    }
  }

  public void addAncillaryImage()
  {
    if (this.AncillaryImage != null)
    {
      int i = getLocation().x - this.Ancillary.getSize().width;
      int j = getLocation().y + this.AncillaryDrop;
      this.Ancillary.setLocation(i, j);
      this.Ancillary.setSize(this.Ancillary.getSize());
      this.realthis.getParent().add(this.Ancillary);
      this.AncillaryActive = true;
    }
  }

  public void setLocationWhileDragging(int paramInt1, int paramInt2)
  {
    if (this.PaletteMouse) {
      this.StartPoint.translate(getLocation().x - paramInt1, getLocation().y - paramInt2);
    }
    setLocation(paramInt1, paramInt2);
  }

  protected void editParameter()
  {
    if ((this.ProgramPart instanceof Parameterized))
    {
      Parameterized localParameterized = (Parameterized)this.ProgramPart;

      this.editor.editParameter(localParameterized.getPrompt(), this, localParameterized.getMaxResponseLength(), localParameterized.getAllowedCharacters());
    }
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    ((Parameterized)this.ProgramPart).setParameterString(paramActionEvent.getActionCommand());

    this.editor.cancelEditParameter();

    this.editor.repaint();
  }

  protected void filterAndRepositionByContext()
  {
    if (!(this.ProgramPart instanceof Conditional))
    {
      if (this.prev != null)
      {
        Vector localVector = new Vector();
        ProgramComponent localProgramComponent1 = this;

        while (localProgramComponent1.next.size() > 0)
        {
          localProgramComponent1 = (ProgramComponent)localProgramComponent1.next.elementAt(0);

          if ((localProgramComponent1.ProgramPart != null) && ((localProgramComponent1.ProgramPart instanceof Contextualized))) {
            localVector.addElement(localProgramComponent1);
          }
        }
        if (localVector.size() > 0)
        {
          for (int i = 0; i < localVector.size(); i++)
          {
            localProgramComponent1 = (ProgramComponent)localVector.elementAt(i);
            
            ProgramComponent localProgramComponent2 = localProgramComponent1.prev;
            
            ProgramComponent localProgramComponent3;
            if (localProgramComponent1.next.size() > 0)
            	localProgramComponent3 = (ProgramComponent)localProgramComponent1.next.elementAt(0);
            else {
            	localProgramComponent3 = null;
            }

            localProgramComponent1.detachPrevious();

            if (localProgramComponent3 == null)
              continue;
            if ((localProgramComponent2.next.size() > 0) && (localProgramComponent2.next.elementAt(0) == localProgramComponent3))
            {
              continue;
            }

            localProgramComponent3.detachPrevious();
            localProgramComponent3.attachPrevious(localProgramComponent2);

            localProgramComponent3.dropAgainst(localProgramComponent2);
          }

          ProgramComponent localProgramComponent2 = null;
          for (int j = localVector.size() - 1; j > 0; j--)
          {
            localProgramComponent2 = (ProgramComponent)localVector.elementAt(j - 1);
            localProgramComponent1 = (ProgramComponent)localVector.elementAt(j);

            localProgramComponent1.attachPrevious(localProgramComponent2);

            localProgramComponent1.dropAgainst(localProgramComponent2);
          }

          localProgramComponent1 = (ProgramComponent)localVector.elementAt(0);
          ProgramComponent localProgramComponent3 = (ProgramComponent)localVector.elementAt(localVector.size() - 1);

          localProgramComponent2 = this.prev;
          detachPrevious();
          localProgramComponent1.attachPrevious(localProgramComponent2);
          attachPrevious(localProgramComponent3);

          localProgramComponent1.dropAgainst(localProgramComponent2);
          dropAgainst(localProgramComponent3);
        }
      }
    }
  }

  protected boolean verifyContext(ProgramComponent paramProgramComponent)
  {
    if ((this.ProgramPart instanceof Contextualized))
    {
      Contextualized localContextualized = (Contextualized)this.ProgramPart;
      ProgramComponent localProgramComponent = paramProgramComponent;
      int i = 1;
      int j = 0;
      int k = 0;
      while (k == 0)
      {
        if (localProgramComponent == null) {
          k = 1;
        }
        else if (j != 0)
          k = 1;
        else if ((localProgramComponent.getProgramPart() instanceof Conditional))
        {
          j = 1;
        }

        if (k != 0)
          continue;
        Linkable localLinkable = localProgramComponent.getProgramPart();
        if (localContextualized.checkContext(localLinkable))
        {
          i = 0;
          k = 1;
          this.LastContext = localLinkable;
        }
        else {
          localProgramComponent = localProgramComponent.prev;
        }
      }
      if(i==1) return true; else return false;
    }

    return false;
  }

  protected void detacher(MouseEvent paramMouseEvent)
  {
    this.Detached = true;

    filterAndRepositionByContext();

    if (!this.PaletteMouse)
    {
      moveToDisplayLayer();
    }

    if (this.prev != null)
    {
      detachPrevious();

      if ((this.ProgramPart instanceof Contextualized)) {
        ((Contextualized)this.ProgramPart).setContext(null);
      }

      if (this.AncillaryImage != null)
      {
        getParent().remove(this.Ancillary);

        Rectangle localRectangle = this.Ancillary.getBounds();
        getParent().repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);

        this.AncillaryActive = false;
      }
    }
  }

  protected class ClickHandler extends MouseAdapter
  {
    private long LastClickTime;
    private Point PickupLocation;

    public void mousePressed(MouseEvent paramMouseEvent) {
      long l = System.currentTimeMillis();

      if (ProgramComponent.this.editor.getProgram().isExecuting()) {
        ProgramComponent.this.editor.stopProgram();
      }
      if (l - this.LastClickTime <= 500L)
      {
        ProgramComponent.this.editParameter();

        ProgramComponent.this.DoubleClickPressed = true;
      }
      else
      {
        ProgramComponent.this.editor.cancelEditParameter();

        if ((paramMouseEvent.getSource() instanceof PaletteComponent)) {
          ProgramComponent.this.PaletteMouse = true;
        }
        else
        {
          ProgramComponent.this.PaletteMouse = false;
          this.PickupLocation = ProgramComponent.this.getLocation();
        }

        ProgramComponent.this.OurPalette.setCurrentActiveProgramComponent(ProgramComponent.this.realthis);

        ProgramComponent.this.StartPoint = paramMouseEvent.getPoint();

        ProgramComponent.this.MouseJustPressed = true;
      }

      this.LastClickTime = l;
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (ProgramComponent.this.MouseJustPressed) {
        ProgramComponent.this.MouseJustPressed = false;
      }
      if (ProgramComponent.this.DoubleClickPressed) {
        ProgramComponent.this.DoubleClickPressed = false;

        return;
      }
      if (ProgramComponent.this.Detached)
      {
        ProgramComponent.this.Detached = false;

        ProgramComponent.this.DragScrollDone = true;

        if (ProgramComponent.this.OurPalette.getDelete().getOn())
        {
          ProgramComponent.this.delete();
          ProgramComponent.this.OurPalette.getDelete().setOn(false);

          return;
        }

        Rectangle localRectangle = new Rectangle(Math.abs(ProgramComponent.this.display.getLocation().x) + Editor.EditorInsets.left, 
          Math.abs(ProgramComponent.this.display.getLocation().y) + Editor.EditorInsets.top, 
          ProgramComponent.this.editor.getSize().width - Editor.EditorInsets.left - Editor.EditorInsets.right, 
          ProgramComponent.this.editor.getSize().height - Editor.EditorInsets.top - Editor.EditorInsets.bottom);
        if (!localRectangle.intersects(ProgramComponent.this.realthis.getBounds()))
        {
          if (ProgramComponent.this.PaletteMouse)
          {
            ProgramComponent.this.delete();

            return;
          }

          ProgramComponent.this.setLocation(this.PickupLocation);
        }

        ProgramComponent.this.moveToProgramLayer();

        ProgramComponent.this.OurPalette.setCurrentActiveProgramComponent(null);

        int i = ProgramComponent.this.getLocation().x;
        int j = ProgramComponent.this.getLocation().y;

        int n = 0;
        Vector localVector = new Vector();
        for (int i1 = 0; (i1 < ProgramComponent.this.DropTargets.size()) && (n == 0); i1++) {
          DropTargetInfo localDropTargetInfo = (DropTargetInfo)ProgramComponent.this.DropTargets.elementAt(i1);

          int k = i + localDropTargetInfo.TargetResolveX + localDropTargetInfo.HalfTargetAreaX;
          int m = j + localDropTargetInfo.TargetResolveY + localDropTargetInfo.HalfTargetAreaY;
          Class localClass;
          try { localClass = Class.forName(localDropTargetInfo.TargetPart);
          }
          catch (ClassNotFoundException localClassNotFoundException)
          {
            localClass = null;
            System.err.println("ProgramComponent: Badly initialized: Can't find TargetPart Class " + localDropTargetInfo.TargetPart);
          }

          Component localComponent = ProgramComponent.this.realthis.getParent().getComponentAt(k, m);

          if ((localComponent != null) && ((localComponent instanceof ProgramComponent)))
          {
            int i2 = localComponent.getLocation().x;
            int i3 = localComponent.getLocation().y;
            int i4 = k - i2;
            int i5 = m - i3;

            if ((i4 <= localDropTargetInfo.DropAreaSize.width) && (i5 <= localDropTargetInfo.DropAreaSize.height))
            {
              ProgramComponent localProgramComponent = (ProgramComponent)localComponent;
              Linkable localLinkable = localProgramComponent.getProgramPart();

              if (localClass.isAssignableFrom(localLinkable.getClass()))
              {
                boolean bool1 = false;
                for (int i6 = 0; (i6 < localVector.size()) && (!bool1); i6++)
                {
                  if (((Class)localVector.elementAt(i6)).isAssignableFrom(localLinkable.getClass())) {
                    bool1 = true;
                  }

                }

                if (!bool1) {
                  bool1 = ProgramComponent.this.verifyContext(localProgramComponent);
                }
                if (!bool1)
                {
                  boolean bool2 = ProgramComponent.this.attachPrevious(localProgramComponent);

                  if (bool2)
                  {
                    int i7 = localProgramComponent.getLocation().x + localDropTargetInfo.DropTarget.x;
                    int i8 = localProgramComponent.getLocation().y + localDropTargetInfo.DropTarget.y;
                    ProgramComponent.this.setLocation(i7, i8);

                    if ((ProgramComponent.this.ProgramPart instanceof Contextualized)) {
                      ((Contextualized)ProgramComponent.this.ProgramPart).setContext(ProgramComponent.this.LastContext);
                    }

                    ProgramComponent.this.addAncillaryImage();

                    ProgramComponent.this.realthis.getParent().repaint();

                    GameApplet.audio.play("ProgramClick");

                    n = 1;
                  }
                }

              }

            }

          }

          if (n != 0)
          {
            continue;
          }
          localVector.addElement(localClass);
        }
      }
    }
    protected ClickHandler() {
    }
  }
  public class MotionHandler extends MouseMotionAdapter { private UtilityThread Fred;
    private int xfactor;
    private int yfactor;
    private int tx;
    private int ty;
    private HighlightBox hb;
    private int ScrollThreadCounter;

    public boolean dragScroll() { if (ProgramComponent.this.DragScrollDone == false)
      {
        int i = ProgramComponent.this.getLocation().x;
        int j = ProgramComponent.this.getLocation().y;

        int k = Math.max(Math.min(this.xfactor, 20), -20);
        int m = Math.max(Math.min(this.yfactor, 20), -20);

        if (this.xfactor != 0)
          this.tx = 0;
        if (this.yfactor != 0) {
          this.ty = 0;
        }

        if ((i < Editor.EditorInsets.left) && (i > 10) && (m > 0)) {
          m = 0;
        }

        int n = i + this.tx;
        int i1 = j + this.ty;

        this.tx = (this.ty = 0);

        n = Math.max(Math.min(n, ProgramComponent.this.programlayer.getSize().width - ProgramComponent.this.getSize().width), 0);
        i1 = Math.max(Math.min(i1, ProgramComponent.this.programlayer.getSize().height - ProgramComponent.this.getSize().height - Editor.EditorInsets.bottom), Editor.EditorInsets.top);

        ProgramComponent.this.setLocation(n, i1);

        int i2 = ProgramComponent.this.programlayer.getLocation().x;
        int i3 = ProgramComponent.this.programlayer.getLocation().y;

        int i4 = i2 - k;
        int i5 = i3 - m;

        Dimension localDimension1 = ProgramComponent.this.programlayer.getSize();
        Dimension localDimension2 = ProgramComponent.this.display.getSize();
        i4 = Math.max(Math.min(i4, 0), localDimension2.width - localDimension1.width - Editor.EditorInsets.right);
        i5 = Math.max(Math.min(i5, 0), localDimension2.height - localDimension1.height);

        if ((i4 == ProgramComponent.this.programlayer.getLocation().x) && (i5 == ProgramComponent.this.programlayer.getLocation().y)) {
          ProgramComponent.this.DragScrollDone = true;
        }
        else
        {
          ProgramComponent.this.programlayer.setLocation(i4, i5);
        }
      }

      if (ProgramComponent.this.DragScrollDone) {
        this.ScrollThreadCounter -= 1;
      }

      return !ProgramComponent.this.DragScrollDone;
    }

    private void scrollStart()
    {
      if (this.ScrollThreadCounter == 0)
      {
        this.ScrollThreadCounter += 1;
        try
        {
          this.Fred = new UtilityThread(100, this, getClass().getMethod("dragScroll", null), false);
          this.Fred.start();

          return;
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
          localNoSuchMethodException.printStackTrace();
          System.err.println("BONEHEAD! Fix ProgramComponent MotionHandler code!");

          return;
        }
      }
    }

    public void mouseDragged(MouseEvent paramMouseEvent)
    {
      if (!ProgramComponent.this.DoubleClickPressed)
      {
        if (ProgramComponent.this.MouseJustPressed)
        {
          ProgramComponent.this.MouseJustPressed = false;
          ProgramComponent.this.detacher(paramMouseEvent);
        }

        int i = ProgramComponent.this.getLocation().x;
        int j = ProgramComponent.this.getLocation().y;

        int k = paramMouseEvent.getPoint().x;
        int m = paramMouseEvent.getPoint().y;

        int n = k + i - ProgramComponent.this.StartPoint.x;
        int i1 = m + j - ProgramComponent.this.StartPoint.y;

        ProgramComponent.this.TrashBounds = ProgramComponent.this.OurPalette.getDeleteBounds();
        if (ProgramComponent.this.TrashBounds.intersects(ProgramComponent.this.getBounds()))
        {
          if (!ProgramComponent.this.OurPalette.getDelete().getOn())
          {
            ProgramComponent.this.OurPalette.getDelete().setOn(true);
            this.hb = new HighlightBox();
            this.hb.setTarget(ProgramComponent.this.realthis);
            ProgramComponent.this.getParent().add(this.hb, 0);
          }
        }
        else if (ProgramComponent.this.OurPalette.getDelete().getOn())
        {
          ProgramComponent.this.OurPalette.getDelete().setOn(false);
          ProgramComponent.this.getParent().remove(this.hb);
          ProgramComponent.this.getParent().repaint();
        }

        Dimension localDimension = ProgramComponent.this.getParent().getSize();
        int i2 = n + ProgramComponent.this.getSize().width - localDimension.width + Editor.EditorInsets.right;
        int i3 = n;
        if (i2 > 0)
        {
          ProgramComponent.this.setLocation(localDimension.width - ProgramComponent.this.getSize().width - Editor.EditorInsets.right, ProgramComponent.this.getLocation().y);

          ProgramComponent.this.DragScrollDone = false;

          this.xfactor = i2;
          this.ty = (m - ProgramComponent.this.StartPoint.y);

          scrollStart();
        }
        else if (i3 < 0)
        {
          ProgramComponent.this.setLocation(0, ProgramComponent.this.getLocation().y);

          ProgramComponent.this.DragScrollDone = false;

          this.xfactor = i3;
          this.ty = (m - ProgramComponent.this.StartPoint.y);

          scrollStart();
        }
        else
        {
          this.xfactor = 0;
        }

        int i4 = i1 - Editor.EditorInsets.top;
        int i5 = i1 + ProgramComponent.this.getSize().height - localDimension.height + Editor.EditorInsets.bottom;
        if (i5 > 0)
        {
          ProgramComponent.this.setLocation(ProgramComponent.this.getLocation().x, localDimension.height - ProgramComponent.this.getSize().height - Editor.EditorInsets.bottom);

          ProgramComponent.this.DragScrollDone = false;

          this.yfactor = i5;
          this.tx = (k - ProgramComponent.this.StartPoint.x);

          scrollStart();
        }
        else if (i4 < 0)
        {
          ProgramComponent.this.setLocation(ProgramComponent.this.getLocation().x, Editor.EditorInsets.top);

          ProgramComponent.this.DragScrollDone = false;

          this.yfactor = i4;
          this.tx = (k - ProgramComponent.this.StartPoint.x);

          scrollStart();
        }
        else
        {
          this.yfactor = 0;
        }

        if ((this.xfactor == 0) && (this.yfactor == 0))
        {
          ProgramComponent.this.DragScrollDone = true;
          ProgramComponent.this.setLocation(n, i1);
        }
      }
    }

    public MotionHandler()
    {
    }
  }
}