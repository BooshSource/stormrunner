package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

public class ScrollMenu extends Container
  implements ItemSelectable
{
  public static final int DEFAULT_STEP_LENGTH = 10;
  public static final String SELECTED_COMMAND = "Item Selected";
  protected OrderedTable List;
  protected SimpleContainer ScrollPane;
  protected Component CurrentSelection;
  protected Color Background;
  protected Color Border;
  protected Insets BorderPadding;
  protected Insets ItemPadding;
  protected int ScrollStepLength;
  protected boolean Horizontal;
  protected ListLayout layout;
  protected Color PrimaryHighlightColor;
  protected Color SecondaryHighlightColor;
  protected int ListDelay;
  protected Enumeration ListEnum;
  protected MouseHandler mh;
  protected ComponentHandler ch;
  protected Vector ActionRecipients;
  protected Vector ItemRecipients;
  protected Vector ImageListeners;

  public ScrollMenu()
  {
    this(false);
  }

  public ScrollMenu(boolean paramBoolean)
  {
    this.BorderPadding = new Insets(0, 0, 0, 0);

    this.ScrollStepLength = 10;

    this.ActionRecipients = new Vector();
    this.ItemRecipients = new Vector();
    this.ImageListeners = new Vector();

    this.ScrollPane = new SimpleContainer();

    this.layout = new ListLayout(paramBoolean);
    this.ScrollPane.setLayout(this.layout);

    this.ScrollPane.setLocation(0, 0);

    super.add(this.ScrollPane);

    this.mh = new MouseHandler(this);
    this.ch = new ComponentHandler(this);
    this.ScrollPane.addMouseListener(this.mh);
    this.List = new OrderedTable();
  }

  public void resetScroll()
  {
    appearanceChanged();

    this.ScrollPane.setLocation(this.BorderPadding.left, this.BorderPadding.top);
  }

  public int getScrollStepLength()
  {
    return this.ScrollStepLength;
  }

  public void setScrollStepLength(int paramInt)
  {
    this.ScrollStepLength = paramInt;
  }

  public void put(Component paramComponent, Object paramObject)
  {
    this.List.put(paramComponent, paramObject);
    setContents(this.List);
  }

  public void put(Component paramComponent, int paramInt)
  {
    this.List.put(paramComponent, paramInt);
  }

  public void add(Component paramComponent, Object paramObject, int paramInt)
  {
    this.List.put(paramComponent, paramObject, paramInt);
    setContents(this.List);
  }

  public void removeKey(Object paramObject)
  {
    this.List.remove(paramObject);
    setContents(this.List);
  }

  public void removeIndex(int paramInt)
  {
    this.List.remove(paramInt);
    setContents(this.List);
  }

  public void removeAll()
  {
    if (this.List != null)
      this.List.clear();
    setContents(null);
  }

  public void setContents(OrderedTable paramOrderedTable)
  {
    appearanceChanged();

    this.List = paramOrderedTable;

    Component[] arrayOfComponent = super.getComponents();
    for (int i = 0; i < arrayOfComponent.length; ++i)
    {
      arrayOfComponent[i].removeComponentListener(this.ch);
    }

    this.ScrollPane.removeAll();

    if (paramOrderedTable != null)
    {
      if (this.ListDelay > 0) {
        startDelayedList();
      }
      else {
        this.ListEnum = this.List.keys();
        while (this.ListEnum.hasMoreElements())
        {
          Component localComponent = (Component)this.ListEnum.nextElement();

          if ((this.ItemPadding != null) && (localComponent instanceof Paddable))
            ((Paddable)localComponent).setPadding(this.ItemPadding);

          this.ScrollPane.add(localComponent);

          localComponent.addComponentListener(this.ch);
        }
      }

      this.ScrollPane.doLayout();
    }

    this.ScrollPane.setSize(this.layout.preferredLayoutSize(this.ScrollPane));
  }

  public void refreshScrollSize()
  {
    this.ScrollPane.doLayout();
    this.ScrollPane.setSize(this.layout.preferredLayoutSize(this.ScrollPane));
  }

  public OrderedTable getContents()
  {
    return this.List;
  }

  public int getItemCount()
  {
    return this.List.size();
  }

  public Object getItemVisual(int paramInt)
  {
    return this.List.getKey(paramInt);
  }

  public Object getItemLogical(int paramInt)
  {
    return this.List.get(paramInt);
  }

  public Object getSelection()
  {
    if (this.CurrentSelection != null)
      return this.List.get(this.CurrentSelection);

    return null;
  }

  public Object[] getSelectedObjects()
  {
    if (this.CurrentSelection == null) {
      return null;
    }

    Object[] arrayOfObject = { this.CurrentSelection };
    return arrayOfObject;
  }

  public boolean isItemSelected(int paramInt)
  {
    if (this.CurrentSelection == null) {
      return false;
    }

    int i = this.List.indexOf(this.CurrentSelection);

    return (i == paramInt);
  }

  public int getSelectedIndex()
  {
    if (this.CurrentSelection == null)
      return -1;

    return this.List.indexOf(this.CurrentSelection);
  }

  public void selectByLogical(Object paramObject)
  {
    Component localComponent = (Component)this.List.getKey(paramObject);

    if (localComponent != null)
    {
      select(localComponent);

      return;
    }

    System.out.println("ScrollMenu: Object " + paramObject + " not in List.");
  }

  public void select(int paramInt)
  {
    select((Component)this.List.getKey(paramInt));
  }

  public void select(Component paramComponent)
  {
    appearanceChanged();

    if ((this.PrimaryHighlightColor != null) && (paramComponent instanceof Highlightable)) {
      ((Highlightable)paramComponent).highlight(this.PrimaryHighlightColor, this.SecondaryHighlightColor);
    }

    this.CurrentSelection = paramComponent;

    ActionEvent localActionEvent = new ActionEvent(this, 1001, "Item Selected");
    processActionEvent(localActionEvent);

    processItemEvent(new ItemEvent(this, 701, this.CurrentSelection, 1));
  }

  public void deselect()
  {
    if (this.CurrentSelection != null)
    {
      if (this.CurrentSelection instanceof Highlightable)
      {
        appearanceChanged();

        ((Highlightable)this.CurrentSelection).unHighlight();
      }

      processItemEvent(new ItemEvent(this, 701, this.CurrentSelection, 2));

      this.CurrentSelection = null;
    }
  }

  public void makeVisible(int paramInt)
  {
    makeVisible((Component)this.List.getKey(paramInt));
  }

  public void makeVisible(Component paramComponent)
  {
    int i;
    int j;
    int k;
    appearanceChanged();

    if (this.Horizontal)
    {
      if (getSize().width >= this.ScrollPane.getSize().width) { return;
      }

      i = 0;
      j = getSize().width - this.ScrollPane.getSize().width - this.BorderPadding.left - this.BorderPadding.right;
      k = Math.min(Math.max(this.ScrollPane.getLocation().x - paramComponent.getLocation().x, j), i);
      this.ScrollPane.setLocation(k, this.ScrollPane.getLocation().y);

      return;
    }

    if (getSize().height < this.ScrollPane.getSize().height)
    {
      i = 0;
      j = getSize().height - this.ScrollPane.getSize().height - this.BorderPadding.top - this.BorderPadding.bottom;
      k = Math.min(Math.max(this.ScrollPane.getLocation().y - paramComponent.getLocation().y, j), i);
      this.ScrollPane.setLocation(this.ScrollPane.getLocation().x, k);
    }
  }

  public void setItemPadding(Insets paramInsets)
  {
    appearanceChanged();

    this.ItemPadding = paramInsets;
  }

  public void setListDelay(int paramInt)
  {
    this.ListDelay = paramInt;
  }

  public void setHighlightColors(Color paramColor1, Color paramColor2)
  {
    appearanceChanged();

    this.PrimaryHighlightColor = paramColor1;
    this.SecondaryHighlightColor = paramColor2;
  }

  public void setBackground(Color paramColor)
  {
    appearanceChanged();

    this.Background = paramColor;
  }

  public void setBorder(Color paramColor)
  {
    appearanceChanged();

    this.Border = paramColor;
  }

  public void setItemGap(int paramInt)
  {
    appearanceChanged();

    this.layout.setGap(paramInt);

    this.ScrollPane.invalidate();
    this.ScrollPane.validate();
  }

  public void setBorderPadding(Insets paramInsets)
  {
    appearanceChanged();

    this.BorderPadding = paramInsets;

    this.ScrollPane.setLocation(paramInsets.left, paramInsets.top);

    repaint();
  }

  protected void startDelayedList()
  {
    this.ListEnum = this.List.keys();
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(this.ListDelay, this, getClass().getMethod("listDisplayStep", null), false);
      localUtilityThread.setInitialDelay(true);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public boolean listDisplayStep()
  {
    appearanceChanged();

    if (this.ListEnum.hasMoreElements())
    {
      Component localComponent = (Component)this.ListEnum.nextElement();

      if ((this.ItemPadding != null) && (localComponent instanceof Paddable))
        ((Paddable)localComponent).setPadding(this.ItemPadding);

      this.ScrollPane.add(localComponent);

      this.ScrollPane.doLayout();

      this.ScrollPane.setSize(this.layout.preferredLayoutSize(this.ScrollPane));

      return true;
    }

    return false;
  }

  public boolean stepUp()
  {
    return stepUp(this.ScrollStepLength);
  }

  public boolean stepUp(int paramInt)
  {
    appearanceChanged();

    if (getSize().height < this.ScrollPane.getSize().height)
    {
      int i = Math.min(this.ScrollPane.getLocation().y + paramInt, 0);
      this.ScrollPane.setLocation(this.ScrollPane.getLocation().x, i);

      return (i != 0);
    }

    return false;
  }

  public boolean stepDown()
  {
    return stepDown(this.ScrollStepLength);
  }

  public boolean stepDown(int paramInt)
  {
    appearanceChanged();

    if (getSize().height < this.ScrollPane.getSize().height)
    {
      int i = getSize().height - this.ScrollPane.getSize().height - this.BorderPadding.top - this.BorderPadding.bottom;
      int j = Math.max(this.ScrollPane.getLocation().y - paramInt, i);
      this.ScrollPane.setLocation(this.ScrollPane.getLocation().x, j);

      return (j != i);
    }

    return false;
  }

  public boolean stepLeft()
  {
    return stepLeft(this.ScrollStepLength);
  }

  public boolean stepLeft(int paramInt)
  {
    appearanceChanged();

    if (getSize().width < this.ScrollPane.getSize().width)
    {
      int i = Math.min(this.ScrollPane.getLocation().x + paramInt, 0);
      this.ScrollPane.setLocation(i, this.ScrollPane.getLocation().y);

      return (i != 0);
    }

    return false;
  }

  public boolean stepRight()
  {
    return stepRight(this.ScrollStepLength);
  }

  public boolean stepRight(int paramInt)
  {
    appearanceChanged();

    if (getSize().width < this.ScrollPane.getSize().width)
    {
      int i = getSize().width - this.ScrollPane.getSize().width - this.BorderPadding.left - this.BorderPadding.right;
      int j = Math.max(this.ScrollPane.getLocation().x - paramInt, i);
      this.ScrollPane.setLocation(j, this.ScrollPane.getLocation().y);

      return (j != i);
    }

    return false;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    int i = getSize().width;
    int j = getSize().height;

    if (this.Background != null)
    {
      paramGraphics.setColor(this.Background);
      paramGraphics.fillRect(0, 0, i - 1, j - 1);
    }

    if (this.Border != null)
    {
      paramGraphics.setColor(this.Border);
      paramGraphics.drawRect(0, 0, i - 1, j - 1);
    }

    paramGraphics.clipRect(this.BorderPadding.left, this.BorderPadding.top, i - this.BorderPadding.left - this.BorderPadding.right, j - this.BorderPadding.top - this.BorderPadding.bottom);
    super.paint(paramGraphics);
  }

  protected synchronized void processActionEvent(ActionEvent paramActionEvent)
  {
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

  protected synchronized void processItemEvent(ItemEvent paramItemEvent)
  {
    if (this.ItemRecipients.size() > 0)
    {
      Enumeration localEnumeration = this.ItemRecipients.elements();

      while (localEnumeration.hasMoreElements())
      {
        ItemListener localItemListener = (ItemListener)localEnumeration.nextElement();
        localItemListener.itemStateChanged(paramItemEvent);
      }
    }
  }

  public synchronized void addItemListener(ItemListener paramItemListener)
  {
    this.ItemRecipients.addElement(paramItemListener);
  }

  public synchronized void removeItemListener(ItemListener paramItemListener)
  {
    this.ItemRecipients.removeElement(paramItemListener);
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
      localImageListener.imageChanged(new ImageEvent(this, 6));
    }
  }
  
  
  class ComponentHandler extends ComponentAdapter
  {
    private final ScrollMenu this$0;

    public void componentResized()
    {
      this.this$0.refreshScrollSize();
    }

    protected ComponentHandler(ScrollMenu paramScrollMenu)
    {
      this.this$0 = paramScrollMenu;
    }
  }
  
  class MouseHandler extends MouseAdapter
  {
    private final ScrollMenu this$0;

    public void mousePressed(MouseEvent paramMouseEvent)
    {
      Component localComponent = this.this$0.ScrollPane.getComponentAt(paramMouseEvent.getX(), paramMouseEvent.getY());

      if (localComponent != null)
      {
        this.this$0.deselect();

        if (localComponent != this.this$0.ScrollPane)
        {
          this.this$0.select(localComponent);
        }
      }
    }

    protected MouseHandler(ScrollMenu paramScrollMenu)
    {
      this.this$0 = paramScrollMenu;
    }
  }
}