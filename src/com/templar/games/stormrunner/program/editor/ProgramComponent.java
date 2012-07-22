package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.program.Conditional;
import com.templar.games.stormrunner.program.Contextualized;
import com.templar.games.stormrunner.program.Copyable;
import com.templar.games.stormrunner.program.Instruction;
import com.templar.games.stormrunner.program.Linkable;
import com.templar.games.stormrunner.program.Loop;
import com.templar.games.stormrunner.program.Parameterized;
import com.templar.games.stormrunner.templarutil.gui.HighlightBox;
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

    super.setImage(paramImage1);

    this.CurrentFont = new Font("SansSerif", 0, 9);
    this.CurrentFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(this.CurrentFont);

    this.DropTargets = new Vector();
    this.next = new Vector(1, 1);

    this.mh = new MotionHandler(this);
    this.ch = new ClickHandler(this);
    addMouseMotionListener(this.mh);
    addMouseListener(this.ch);

    setSize(super.getSize());
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

    if ((this.ParameterYOffset >= 0) && (this.ProgramPart instanceof Parameterized))
    {
      paramGraphics.setColor(Color.white);
      paramGraphics.setFont(this.CurrentFont);

      String str = ((Parameterized)this.ProgramPart).getParameterString();
      char[] arrayOfChar = str.toCharArray();
      int i = this.CurrentFontMetrics.charsWidth(arrayOfChar, 0, arrayOfChar.length);
      int j = super.getSize().width / 2 - i / 2;
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
    if (this instanceof Linkable)
      localProgramComponent = new ProgramComponent((Linkable)this, getNormalImage(), getIconImage(), this.ParameterYOffset, this.OurPalette);
    else
      localProgramComponent = new ProgramComponent((Linkable)((Copyable)this.ProgramPart).copy(), getNormalImage(), getIconImage(), this.ParameterYOffset, this.OurPalette);

    localProgramComponent.DropTargets = this.DropTargets;
    localProgramComponent.setAncillaryImage(this.AncillaryImage, this.AncillaryDrop);
    if (this.BoundingComponent != null)
      localProgramComponent.setBoundingComponent(this.BoundingComponent.copy());
    return localProgramComponent;
  }

  public void delete()
  {
    Vector localVector = (Vector)this.next.clone();
    for (int i = 0; i < localVector.size(); ++i)
    {
      localObject = (ProgramComponent)localVector.elementAt(i);
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

    paramProgramComponent.setLocation(getLocation().x, getLocation().y + super.getSize().height + 15);
  }

  public boolean dropAgainst(ProgramComponent paramProgramComponent)
  {
    int i = 0;
    Enumeration localEnumeration = this.DropTargets.elements();
    while ((localEnumeration.hasMoreElements()) && (i == 0))
    {
      DropTargetInfo localDropTargetInfo = (DropTargetInfo)localEnumeration.nextElement();
      try
      {
        Class localClass = Class.forName(localDropTargetInfo.TargetPart);
        if (!(localClass.isAssignableFrom(paramProgramComponent.ProgramPart.getClass()))) {
          break label108;
        }

        int j = paramProgramComponent.getLocation().x + localDropTargetInfo.DropTarget.x;
        int k = paramProgramComponent.getLocation().y + localDropTargetInfo.DropTarget.y;
        setLocation(j, k);
        label108: i = 1;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
      }
    }

    return i;
  }

  public boolean attachPrevious(ProgramComponent paramProgramComponent)
  {
    Object localObject;
    boolean bool1 = false;

    Enumeration localEnumeration = paramProgramComponent.next.elements();
    while (localEnumeration.hasMoreElements())
    {
      localObject = (ProgramComponent)localEnumeration.nextElement();
      if (localObject instanceof BoundingComponent)
      {
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
    }

    if (paramProgramComponent.ProgramPart instanceof Loop)
    {
      localObject = (Loop)paramProgramComponent.ProgramPart;
      if (((Loop)localObject).getDestination() == null)
      {
        ((Loop)localObject).setDestination((Instruction)this.ProgramPart);

        if (((Instruction)this.ProgramPart).getPreviousInstruction() != null)
          System.out.println("ProgramComponent: Inconsistency detected: ProgramPart " + this.ProgramPart + " had a non-null prev while attempting a loop attachment.");
        else
          bool1 = true;
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
        else if ((localProgramComponent instanceof BoundingComponent) && (!(localVector.contains(((BoundingComponent)localProgramComponent).getBoundSource()))))
        {
          i = 1;

          localProgramComponent.prev.next.removeElement(localProgramComponent);

          this.prev.next.addElement(localProgramComponent);
          localProgramComponent.prev = this.prev;

          localProgramComponent.moveToProgramLayer();

          boolean bool2 = localProgramComponent.dropAgainst(this.prev);

          if (!(bool2))
          {
            System.out.println("ProgramComponent: BoundingComponent detatched but failed to find a match while attempting to reposition.");
          }
        }

      }

      if (this.prev.ProgramPart instanceof Loop)
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
      else {
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

    setLocation(paramInt1, paramInt2);

    if (this.AncillaryActive)
    {
      Point localPoint = this.Ancillary.getLocation();
      localPoint.translate(i, j);
      this.Ancillary.setLocation(localPoint);
    }

    for (int k = 0; k < this.next.size(); ++k)
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
    setLocation(getLocation().x + i, getLocation().y + j);
    this.programlayer.add(this, 0);

    for (int k = 0; k < this.next.size(); ++k)
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
    setLocation(getLocation().x + i, getLocation().y + j);
    this.display.add(this, 0);

    for (int k = 0; k < this.next.size(); ++k)
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
    if (this.PaletteMouse)
      this.StartPoint.translate(getLocation().x - paramInt1, getLocation().y - paramInt2);

    setLocation(paramInt1, paramInt2);
  }

  protected void editParameter()
  {
    if (this.ProgramPart instanceof Parameterized)
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
    if ((!(this.ProgramPart instanceof Conditional)) && 
      (this.prev != null))
    {
      Vector localVector = new Vector();
      ProgramComponent localProgramComponent1 = this;

      while (localProgramComponent1.next.size() > 0)
      {
        localProgramComponent1 = (ProgramComponent)localProgramComponent1.next.elementAt(0);

        if ((localProgramComponent1.ProgramPart != null) && (localProgramComponent1.ProgramPart instanceof Contextualized))
          localVector.addElement(localProgramComponent1);
      }

      if (localVector.size() > 0)
      {
        for (int i = 0; i < localVector.size(); ++i)
        {
          localProgramComponent1 = (ProgramComponent)localVector.elementAt(i);

          localProgramComponent2 = localProgramComponent1.prev;
          if (localProgramComponent1.next.size() > 0)
            localProgramComponent3 = (ProgramComponent)localProgramComponent1.next.elementAt(0);
          else {
            localProgramComponent3 = null;
          }

          localProgramComponent1.detachPrevious();

          if ((localProgramComponent3 != null) && ((
            (localProgramComponent2.next.size() <= 0) || (localProgramComponent2.next.elementAt(0) != localProgramComponent3))))
          {
            localProgramComponent3.detachPrevious();
            localProgramComponent3.attachPrevious(localProgramComponent2);

            localProgramComponent3.dropAgainst(localProgramComponent2);
          }

        }

        ProgramComponent localProgramComponent2 = null;
        for (int j = localVector.size() - 1; j > 0; --j)
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

  protected boolean verifyContext(ProgramComponent paramProgramComponent)
  {
    if (this.ProgramPart instanceof Contextualized)
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
        else if (localProgramComponent.getProgramPart() instanceof Conditional)
        {
          j = 1;
        }

        if (k == 0)
        {
          Linkable localLinkable = localProgramComponent.getProgramPart();
          if (localContextualized.checkContext(localLinkable))
          {
            i = 0;
            k = 1;
            this.LastContext = localLinkable;
          }
          else {
            localProgramComponent = localProgramComponent.prev; }
        }
      }
      return i;
    }

    return false;
  }

  protected void detacher(MouseEvent paramMouseEvent)
  {
    this.Detached = true;

    filterAndRepositionByContext();

    if (!(this.PaletteMouse))
    {
      moveToDisplayLayer();
    }

    if (this.prev != null)
    {
      detachPrevious();

      if (this.ProgramPart instanceof Contextualized) {
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

  static Point access$0(ProgramComponent paramProgramComponent)
  {
    return paramProgramComponent.StartPoint;
  }

  static ProgramComponent access$1(ProgramComponent paramProgramComponent)
  {
    return paramProgramComponent.realthis;
  }

  static void access$2(ProgramComponent paramProgramComponent, Point paramPoint)
  {
    paramProgramComponent.StartPoint = paramPoint;
  }
  
  
  class ClickHandler extends MouseAdapter
  {
    private final ProgramComponent this$0;
    private long LastClickTime;
    private Point PickupLocation;

    public void mousePressed(MouseEvent paramMouseEvent)
    {
      long l = System.currentTimeMillis();

      if (this.this$0.editor.getProgram().isExecuting())
        this.this$0.editor.stopProgram();

      if (l - this.LastClickTime <= 500L)
      {
        this.this$0.editParameter();

        this.this$0.DoubleClickPressed = true;
      }
      else
      {
        this.this$0.editor.cancelEditParameter();

        if (paramMouseEvent.getSource() instanceof PaletteComponent) {
          this.this$0.PaletteMouse = true;
        }
        else
        {
          this.this$0.PaletteMouse = false;
          this.PickupLocation = this.this$0.getLocation();
        }

        this.this$0.OurPalette.setCurrentActiveProgramComponent(ProgramComponent.access$1(this.this$0));

        ProgramComponent.access$2(this.this$0, paramMouseEvent.getPoint());

        this.this$0.MouseJustPressed = true;
      }

      this.LastClickTime = l;
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (this.this$0.MouseJustPressed)
        this.this$0.MouseJustPressed = false;

      if (this.this$0.DoubleClickPressed) {
        this.this$0.DoubleClickPressed = false;

        return;
      }
      if (this.this$0.Detached)
      {
        this.this$0.Detached = false;

        this.this$0.DragScrollDone = true;

        if (this.this$0.OurPalette.getDelete().getOn())
        {
          this.this$0.delete();
          this.this$0.OurPalette.getDelete().setOn(false);

          return;
        }

        Rectangle localRectangle = new Rectangle(Math.abs(this.this$0.display.getLocation().x) + Editor.EditorInsets.left, 
          Math.abs(this.this$0.display.getLocation().y) + Editor.EditorInsets.top, 
          this.this$0.editor.getSize().width - Editor.EditorInsets.left - Editor.EditorInsets.right, 
          this.this$0.editor.getSize().height - Editor.EditorInsets.top - Editor.EditorInsets.bottom);
        if (!(localRectangle.intersects(ProgramComponent.access$1(this.this$0).getBounds())))
        {
          if (this.this$0.PaletteMouse)
          {
            this.this$0.delete();

            return;
          }

          this.this$0.setLocation(this.PickupLocation);
        }

        this.this$0.moveToProgramLayer();

        this.this$0.OurPalette.setCurrentActiveProgramComponent(null);

        int i = this.this$0.getLocation().x;
        int j = this.this$0.getLocation().y;

        int i1 = 0;
        Vector localVector = new Vector();
        for (int i2 = 0; (i2 < this.this$0.DropTargets.size()) && (i1 == 0); ++i2)
        {
          Class localClass;
          DropTargetInfo localDropTargetInfo = (DropTargetInfo)this.this$0.DropTargets.elementAt(i2);

          int k = i + localDropTargetInfo.TargetResolveX + localDropTargetInfo.HalfTargetAreaX;
          int l = j + localDropTargetInfo.TargetResolveY + localDropTargetInfo.HalfTargetAreaY;
          try
          {
            localClass = Class.forName(localDropTargetInfo.TargetPart);
          }
          catch (ClassNotFoundException localClassNotFoundException)
          {
            localClass = null;
            System.err.println("ProgramComponent: Badly initialized: Can't find TargetPart Class " + localDropTargetInfo.TargetPart);
          }

          Component localComponent = ProgramComponent.access$1(this.this$0).getParent().getComponentAt(k, l);

          if ((localComponent != null) && (localComponent instanceof ProgramComponent))
          {
            int i3 = localComponent.getLocation().x;
            int i4 = localComponent.getLocation().y;
            int i5 = k - i3;
            int i6 = l - i4;

            if ((i5 <= localDropTargetInfo.DropAreaSize.width) && (i6 <= localDropTargetInfo.DropAreaSize.height))
            {
              ProgramComponent localProgramComponent = (ProgramComponent)localComponent;
              Linkable localLinkable = localProgramComponent.getProgramPart();

              if (localClass.isAssignableFrom(localLinkable.getClass()))
              {
                boolean bool1 = false;
                for (int i7 = 0; (i7 < localVector.size()) && (!(bool1)); ++i7)
                {
                  if (((Class)localVector.elementAt(i7)).isAssignableFrom(localLinkable.getClass())) {
                    bool1 = true;
                  }

                }

                if (!(bool1))
                  bool1 = this.this$0.verifyContext(localProgramComponent);

                if (!(bool1))
                {
                  boolean bool2 = this.this$0.attachPrevious(localProgramComponent);

                  if (bool2)
                  {
                    int i8 = localProgramComponent.getLocation().x + localDropTargetInfo.DropTarget.x;
                    int i9 = localProgramComponent.getLocation().y + localDropTargetInfo.DropTarget.y;
                    this.this$0.setLocation(i8, i9);

                    if (this.this$0.ProgramPart instanceof Contextualized) {
                      ((Contextualized)this.this$0.ProgramPart).setContext(this.this$0.LastContext);
                    }

                    this.this$0.addAncillaryImage();

                    ProgramComponent.access$1(this.this$0).getParent().repaint();

                    GameApplet.audio.play("ProgramClick");

                    i1 = 1;
                  }
                }

              }

            }

          }

          if (i1 == 0)
          {
            localVector.addElement(localClass);
          }
        }
      }
    }

    protected ClickHandler(ProgramComponent paramProgramComponent)
    {
      this.this$0 = paramProgramComponent;
    }
  }
  
  
  
  public class MotionHandler extends MouseMotionAdapter
  {
    private final ProgramComponent this$0;
    private UtilityThread Fred;
    private int xfactor;
    private int yfactor;
    private int tx;
    private int ty;
    private HighlightBox hb;
    private int ScrollThreadCounter;

    public boolean dragScroll(MouseEvent paramMouseEvent)
    {
      if (this.this$0.DragScrollDone == false)
      {
        int i = this.this$0.getLocation().x;
        int j = this.this$0.getLocation().y;

        int k = Math.max(Math.min(this.xfactor, 20), -20);
        int l = Math.max(Math.min(this.yfactor, 20), -20);

        if (this.xfactor != 0)
          this.tx = 0;
        if (this.yfactor != 0) {
          this.ty = 0;
        }

        if ((i < Editor.EditorInsets.left) && (i > 10) && (l > 0)) {
          l = 0;
        }

        int i1 = i + this.tx;
        int i2 = j + this.ty;

        this.tx = (this.ty = 0);

        i1 = Math.max(Math.min(i1, this.this$0.programlayer.getSize().width - this.this$0.getSize().width), 0);
        i2 = Math.max(Math.min(i2, this.this$0.programlayer.getSize().height - this.this$0.getSize().height - Editor.EditorInsets.bottom), Editor.EditorInsets.top);

        this.this$0.setLocation(i1, i2);

        int i3 = this.this$0.programlayer.getLocation().x;
        int i4 = this.this$0.programlayer.getLocation().y;

        int i5 = i3 - k;
        int i6 = i4 - l;

        Dimension localDimension1 = this.this$0.programlayer.getSize();
        Dimension localDimension2 = this.this$0.display.getSize();
        i5 = Math.max(Math.min(i5, 0), localDimension2.width - localDimension1.width - Editor.EditorInsets.right);
        i6 = Math.max(Math.min(i6, 0), localDimension2.height - localDimension1.height);

        if ((i5 == this.this$0.programlayer.getLocation().x) && (i6 == this.this$0.programlayer.getLocation().y)) {
          this.this$0.DragScrollDone = true;
        }
        else
        {
          this.this$0.programlayer.setLocation(i5, i6);
        }
      }

      if (this.this$0.DragScrollDone) {
        this.ScrollThreadCounter -= 1;
      }

      return (!(this.this$0.DragScrollDone));
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
      if (!(this.this$0.DoubleClickPressed))
      {
        if (this.this$0.MouseJustPressed)
        {
          this.this$0.MouseJustPressed = false;
          this.this$0.detacher(paramMouseEvent);
        }

        int i = this.this$0.getLocation().x;
        int j = this.this$0.getLocation().y;

        int k = paramMouseEvent.getPoint().x;
        int l = paramMouseEvent.getPoint().y;

        int i1 = k + i - ProgramComponent.access$0(this.this$0).x;
        int i2 = l + j - ProgramComponent.access$0(this.this$0).y;

        this.this$0.TrashBounds = this.this$0.OurPalette.getDeleteBounds();
        if (this.this$0.TrashBounds.intersects(this.this$0.getBounds()))
        {
          if (!(this.this$0.OurPalette.getDelete().getOn()))
          {
            this.this$0.OurPalette.getDelete().setOn(true);
            this.hb = new HighlightBox();
            this.hb.setTarget(ProgramComponent.access$1(this.this$0));
            this.this$0.getParent().add(this.hb, 0);
          }
        }
        else if (this.this$0.OurPalette.getDelete().getOn())
        {
          this.this$0.OurPalette.getDelete().setOn(false);
          this.this$0.getParent().remove(this.hb);
          this.this$0.getParent().repaint();
        }

        Dimension localDimension = this.this$0.getParent().getSize();
        int i3 = i1 + this.this$0.getSize().width - localDimension.width + Editor.EditorInsets.right;
        int i4 = i1;
        if (i3 > 0)
        {
          this.this$0.setLocation(localDimension.width - this.this$0.getSize().width - Editor.EditorInsets.right, this.this$0.getLocation().y);

          this.this$0.DragScrollDone = false;

          this.xfactor = i3;
          this.ty = (l - ProgramComponent.access$0(this.this$0).y);

          scrollStart();
        }
        else if (i4 < 0)
        {
          this.this$0.setLocation(0, this.this$0.getLocation().y);

          this.this$0.DragScrollDone = false;

          this.xfactor = i4;
          this.ty = (l - ProgramComponent.access$0(this.this$0).y);

          scrollStart();
        }
        else
        {
          this.xfactor = 0;
        }

        int i5 = i2 - Editor.EditorInsets.top;
        int i6 = i2 + this.this$0.getSize().height - localDimension.height + Editor.EditorInsets.bottom;
        if (i6 > 0)
        {
          this.this$0.setLocation(this.this$0.getLocation().x, localDimension.height - this.this$0.getSize().height - Editor.EditorInsets.bottom);

          this.this$0.DragScrollDone = false;

          this.yfactor = i6;
          this.tx = (k - ProgramComponent.access$0(this.this$0).x);

          scrollStart();
        }
        else if (i5 < 0)
        {
          this.this$0.setLocation(this.this$0.getLocation().x, Editor.EditorInsets.top);

          this.this$0.DragScrollDone = false;

          this.yfactor = i5;
          this.tx = (k - ProgramComponent.access$0(this.this$0).x);

          scrollStart();
        }
        else
        {
          this.yfactor = 0;
        }

        if ((this.xfactor == 0) && (this.yfactor == 0))
        {
          this.this$0.DragScrollDone = true;
          this.this$0.setLocation(i1, i2);
        }
      }
    }

    public MotionHandler(ProgramComponent paramProgramComponent)
    {
      this.this$0 = paramProgramComponent;

    }
  }
}