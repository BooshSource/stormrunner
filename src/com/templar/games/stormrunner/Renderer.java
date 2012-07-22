package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ImageContainer;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

public class Renderer extends ImageContainer
  implements FocusListener, ShroudListener
{
  public static final int TILEWIDTH = 50;
  public static final int TILEHEIGHT = 50;
  public static final int WINDOWWIDTH = 480;
  public static final int WINDOWHEIGHT = 360;
  public static final int BUFFERWIDTH = 500;
  public static final int BUFFERHEIGHT = 400;
  public static final int WINDOWTILEWIDTH = (int)Math.ceil(9.0D);
  public static final int WINDOWTILEHEIGHT = (int)Math.ceil(7.0D);
  protected Scene CurrentScene;
  protected Map map;
  protected Dimension MapSize;
  protected Shroud shroud;
  protected GameState State;
  protected Vector ObjectList;
  protected OrderedTable Layers = new OrderedTable();
  protected boolean Visible = false;
  protected Vector Players = new Vector();
  protected Vector Loopers = new Vector();
  protected Vector CurrentlyLooping = new Vector();
  protected Point Offset;
  protected Point NewOffset;
  protected PhysicalObject FollowingObject;
  protected ImageContainer Surface;
  protected GroundComponent ground;
  protected Image GroundBufferImage;
  protected Graphics GroundBufferGraphics;
  protected boolean Added = false;
  protected Vector VisibleObjects = new Vector();
  protected Component InternalFocus;
  protected KeyHandler kh;
  private Object BlitLock = new Object();
  ////
  Object localObject;

  public Renderer(GameState paramGameState)
  {
    super.setBufferSize(new Dimension(500, 400));

    this.Surface = new ImageContainer();
    this.Surface.setBufferSize(new Dimension(500, 400));
    this.Surface.setBounds(0, 0, 480, 360);

    this.State = paramGameState;
    this.Offset = new Point(0, 0);

    this.kh = new KeyHandler(this);
    addKeyListener(this.kh);

    addFocusListener(this);
  }

  public void invalidate()
  {
    super.invalidate();

    createBufferImage();
  }

  protected void createBufferImage()
  {
    if ((this.Added) && (getSize().width > 0) && (getSize().height > 0))
    {
      if (this.GroundBufferGraphics != null)
        this.GroundBufferGraphics.dispose();
      if (this.GroundBufferImage != null)
        this.GroundBufferImage.flush();

      this.GroundBufferImage = createImage(500, 400);
      this.GroundBufferGraphics = this.GroundBufferImage.getGraphics();
    }
  }

  public void removeNotify()
  {
    super.removeNotify();

    this.Added = false;
  }

  public void addNotify()
  {
    super.addNotify();

    this.Added = true;

    if (this.GroundBufferImage == null)
      createBufferImage();
  }

  public void setScene(Scene paramScene)
  {
    super.unregisterAll();

    if (this.shroud != null)
      this.shroud.removeShroudListener(this);

    this.Surface.unregisterAll();
    this.Layers.clear();
    this.CurrentScene = paramScene;

    this.map = paramScene.getMap();

    this.MapSize = this.map.getSize();

    ClickDelegator localClickDelegator = new ClickDelegator(this, this.State.getCurrentScene());
    localClickDelegator.setBounds(0, 0, 480, 360);
    add(localClickDelegator);

    this.shroud = this.State.getCurrentScene().getShroud();
    this.shroud.setSize(new Dimension(480, 360));
    this.shroud.setLocation(0, 0);
    this.shroud.addShroudListener(this);
    super.register(this.shroud);

    Enumeration localEnumeration1 = paramScene.getLayers().elements();
    while (localEnumeration1.hasMoreElements()) {
      addLayer((String)localEnumeration1.nextElement());
    }

    this.ObjectList = this.State.getCurrentScene().getObjects();
    Enumeration localEnumeration2 = this.ObjectList.elements();
    while (localEnumeration2.hasMoreElements())
    {
      PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration2.nextElement();
      addObject(localPhysicalObject);
    }

    this.ground = new GroundComponent(this);
    this.ground.setBounds(getBounds());
    this.Surface.register(this.ground);

    super.register(this.Surface);
  }

  public void addObject(PhysicalObject paramPhysicalObject)
  {
    if ((paramPhysicalObject.getLayer().compareTo("") != 0) && (paramPhysicalObject.getLayer() != null))
    {
      SimpleContainer localSimpleContainer = null;
      addLayer(paramPhysicalObject.getLayer());
      localSimpleContainer = (SimpleContainer)this.Layers.get(paramPhysicalObject.getLayer());
      paramPhysicalObject.setLocation(Position.mapToScreen(paramPhysicalObject.getPosition()));
      paramPhysicalObject.setSize(paramPhysicalObject.getShapeSize().width * 50, 
        paramPhysicalObject.getShapeSize().height * 50);
      paramPhysicalObject.setRenderer(this);
      if (isInWindow(paramPhysicalObject))
      {
        localSimpleContainer.add(paramPhysicalObject);

        if (isInWindow(paramPhysicalObject))
        {
          this.VisibleObjects.addElement(paramPhysicalObject);
        }
      }
    }
  }

  public void removeObject(PhysicalObject paramPhysicalObject)
  {
    Vector localVector;
    int i;
    if (this.Loopers.contains(paramPhysicalObject))
    {
      localVector = paramPhysicalObject.getLoopList();

      for (i = 0; i < localVector.size(); ++i)
      {
        String str = (String)localVector.elementAt(i);
        GameApplet.audio.stop(str, paramPhysicalObject);
      }

      this.Loopers.removeElement(paramPhysicalObject);
      localVector.removeAllElements();
      this.CurrentlyLooping.removeElement(paramPhysicalObject);
    }

    if (this.Players.contains(paramPhysicalObject))
    {
      localVector = paramPhysicalObject.getPlayList();

      for (i = 0; i < localVector.size(); ++i)
      {
        GameApplet.audio.stop((String)localVector.elementAt(i), paramPhysicalObject);
      }

      localVector.removeAllElements();
      this.Players.removeElement(paramPhysicalObject);
    }

    ((SimpleContainer)this.Layers.get(paramPhysicalObject.getLayer())).remove(paramPhysicalObject);
    this.VisibleObjects.removeElement(paramPhysicalObject);

    paramPhysicalObject.setRenderer(null);
  }

  private void addLayer(String paramString)
  {
    if (!(this.Layers.containsKey(paramString)))
    {
      if (paramString.compareTo("Robot") == 0)
        addLayer("Robot Effects");
      SimpleContainer localSimpleContainer = new SimpleContainer();
      localSimpleContainer.setSize(this.MapSize.width * 50, this.MapSize.height * 50);
      Point localPoint = Position.mapToScreen(getOffset());
      localSimpleContainer.setLocation(-localPoint.x, -localPoint.y);
      this.Layers.put(paramString, localSimpleContainer);
      this.Surface.register(localSimpleContainer);

      localSimpleContainer.removeReportingComponentListener(this.Surface);
      if (paramString.compareTo("Robot") == 0)
        addLayer("Ground Effects");
    }
  }

  private void removeLayer(String paramString)
  {
    if (this.Layers.containsKey(paramString))
    {
      SimpleContainer localSimpleContainer = (SimpleContainer)this.Layers.get(paramString);
      localSimpleContainer.removeAll();
      this.Surface.unregister(localSimpleContainer);
    }
  }

  public boolean isVisible()
  {
    return this.Visible;
  }

  public void setVisible(boolean paramBoolean)
  {
	  Enumeration localEnumeration;
    String str;
    PhysicalObject localPhysicalObject;
    int i;
    this.Visible = paramBoolean;

    if (paramBoolean)
    {
      localEnumeration = this.Loopers.elements();

      while (localEnumeration.hasMoreElements())
      {
        localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();

        if (isInWindow(localPhysicalObject))
        {
          this.CurrentlyLooping.addElement(localPhysicalObject);

          localObject = localPhysicalObject.getLoopList();
          for (i = 0; i < ((Vector)localObject).size(); ++i)
          {
            str = (String)((Vector)localObject).elementAt(i);
            GameApplet.audio.loop(str, localPhysicalObject);
          }
        }
      }
      return;
    }

    localEnumeration = this.Players.elements();

    while (localEnumeration.hasMoreElements())
    {
      localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
      localObject = localPhysicalObject.getPlayList();

      for (i = 0; i < ((Vector)localObject).size(); ++i)
      {
        GameApplet.audio.stop((String)((Vector)localObject).elementAt(i), localPhysicalObject);
      }

      ((Vector)localObject).removeAllElements();
    }
    this.Players.removeAllElements();

    Object localObject = this.CurrentlyLooping.elements();
    while (((Enumeration)localObject).hasMoreElements())
    {
      localPhysicalObject = (PhysicalObject)((Enumeration)localObject).nextElement();
      Vector localVector = localPhysicalObject.getLoopList();

      for (int j = 0; j < localVector.size(); ++j)
      {
        str = (String)localVector.elementAt(j);
        GameApplet.audio.stop(str, localPhysicalObject);
      }
    }
    this.CurrentlyLooping.removeAllElements();
  }

  public Point getOffset()
  {
    return new Point(this.Offset);
  }

  public void setOffsetToCenter(Point paramPoint)
  {
    int i = Math.round(WINDOWTILEWIDTH / 2F) - 1;
    int j = Math.round(WINDOWTILEHEIGHT / 2F) - 1;

    Point localPoint = new Point(paramPoint);
    localPoint.translate(-i, -j);

    softSetOffset(localPoint);
  }

  public void softSetOffset(Point paramPoint)
  {
    Point localPoint = new Point();
    localPoint.x = Math.max(Math.min(paramPoint.x, this.MapSize.width - WINDOWTILEWIDTH - 1), 0);
    localPoint.y = Math.max(Math.min(paramPoint.y, this.MapSize.height - WINDOWTILEHEIGHT - 1), 0);

    setOffset(localPoint);
  }

  public boolean setOffset(Point paramPoint)
  {
    if ((paramPoint.x < 0) || (paramPoint.y < 0) || (paramPoint.x >= this.MapSize.width - WINDOWTILEWIDTH) || (paramPoint.y >= this.MapSize.height - WINDOWTILEHEIGHT))
    {
      return false;
    }

    this.NewOffset = paramPoint;

    repaint();

    return true;
  }

  public void refreshBuffer()
  {
    if (this.NewOffset != null)
    {
      int i = this.Offset.x - this.NewOffset.x;
      int j = this.Offset.y - this.NewOffset.y;

      if ((i != 0) || (j != 0))
      {
        String str;
        PhysicalObject localPhysicalObject2;
        this.Offset = this.NewOffset;

        if ((this.BufferImage != null) && (!(super.isEntireScreenTainted())) && (super.getBlit() == null) && (((Math.abs(i) < WINDOWTILEWIDTH) || (Math.abs(j) < WINDOWTILEHEIGHT))))
        {
          int k;
          int i1;
          if (super.isBufferTainted())
          {
            super.translateTaintArea(i * 50, j * 50);
            this.Surface.translateTaintArea(i * 50, j * 50);
          }

          super.setBlit(i * 50, j * 50);
          this.Surface.setBlit(i * 50, j * 50);
          this.ground.setGroundBlit(i * 50, j * 50);

          if (i < 0)
            k = (WINDOWTILEWIDTH + i + 1) * 50;
          else
            k = 0;
          int l = Math.abs(i) * 50;
          localObject = new Rectangle(k, 0, l, 400);

          if (j < 0)
            i1 = (WINDOWTILEHEIGHT + j + 1) * 50;
          else
            i1 = 0;
          int i2 = Math.abs(j) * 50;
          Rectangle localRectangle = new Rectangle(0, i1, 500, i2);

          if ((i != 0) && (j != 0))
            ((Rectangle)localObject).add(localRectangle);
          else if (j != 0)
            localObject = localRectangle;

          this.ground.taintGround((Rectangle)localObject);
          this.Surface.taintBuffer((Rectangle)localObject);
        }
        else
        {
          this.ground.taintGround();
          this.Surface.taintBuffer();
        }

        Enumeration localEnumeration1 = this.Layers.elements();
        while (localEnumeration1.hasMoreElements()) {
          ((Container)localEnumeration1.nextElement()).setLocation(-this.Offset.x * 50, -this.Offset.y * 50);
        }

        this.shroud.setOffset(this.Offset);

        Vector localVector1 = new Vector();
        Object localObject = this.CurrentScene.getObjects();

        for (int i3 = 0; i3 < ((Vector)localObject).size(); ++i3)
        {
          PhysicalObject localPhysicalObject1 = (PhysicalObject)((Vector)localObject).elementAt(i3);
          SimpleContainer localSimpleContainer = (SimpleContainer)this.Layers.get(localPhysicalObject1.getLayer());
          if (isInWindow(localPhysicalObject1))
          {
            if (!(this.VisibleObjects.contains(localPhysicalObject1)))
              localSimpleContainer.add(localPhysicalObject1);

            localVector1.addElement(localPhysicalObject1);
          }
          else if (this.VisibleObjects.contains(localPhysicalObject1)) {
            localSimpleContainer.remove(localPhysicalObject1);
          }

        }

        this.VisibleObjects = localVector1;

        Enumeration localEnumeration2 = this.CurrentlyLooping.elements();
        Vector localVector2 = new Vector();

        while (localEnumeration2.hasMoreElements())
        {
          localPhysicalObject2 = (PhysicalObject)localEnumeration2.nextElement();

          if (!(isInWindow(localPhysicalObject2)))
          {
            Vector localVector3 = localPhysicalObject2.getLoopList();

            for (int i5 = 0; i5 < localVector3.size(); ++i5)
            {
              str = (String)localVector3.elementAt(i5);
              GameApplet.audio.stop(str, localPhysicalObject2);
            }

            localVector2.addElement(localPhysicalObject2);
          }

        }

        for (int i4 = 0; i4 < localVector2.size(); ++i4)
        {
          this.CurrentlyLooping.removeElement(localVector2.elementAt(i4));
        }

        localEnumeration2 = this.Loopers.elements();
        while (localEnumeration2.hasMoreElements())
        {
          localPhysicalObject2 = (PhysicalObject)localEnumeration2.nextElement();
          if ((isInWindow(localPhysicalObject2)) && (!(this.CurrentlyLooping.contains(localPhysicalObject2))))
          {
            this.CurrentlyLooping.addElement(localPhysicalObject2);

            Vector localVector4 = localPhysicalObject2.getLoopList();

            for (int i6 = 0; i6 < localVector4.size(); ++i6)
            {
              str = (String)localVector4.elementAt(i6);
              GameApplet.audio.loop(str, localPhysicalObject2);
            }
          }
        }
      }

      this.NewOffset = null;
    }

    super.refreshBuffer();
  }

  public void setObjectToFollow(PhysicalObject paramPhysicalObject)
  {
    this.FollowingObject = paramPhysicalObject;

    if (paramPhysicalObject != null)
    {
      Position localPosition = paramPhysicalObject.getPosition();
      if (localPosition != null)
      {
        Point localPoint = localPosition.getMapPoint();
        setOffsetToCenter(localPoint);
      }
    }
  }

  public PhysicalObject getObjectToFollow()
  {
    return this.FollowingObject;
  }

  public void reportNewPosition(PhysicalObject paramPhysicalObject)
  {
    if (this.Visible)
    {
      Vector localVector;
      String str;
      int i;
      if (this.FollowingObject == paramPhysicalObject)
      {
        setOffsetToCenter(paramPhysicalObject.getPosition().getMapPoint());
      }

      if (isInWindow(paramPhysicalObject))
      {
        if (this.VisibleObjects.contains(paramPhysicalObject)) { return;
        }

        localVector = paramPhysicalObject.getLoopList();
        if ((localVector.size() > 0) && (!(this.CurrentlyLooping.contains(paramPhysicalObject))))
        {
          this.CurrentlyLooping.addElement(paramPhysicalObject);

          for (i = 0; i < localVector.size(); ++i)
          {
            str = (String)localVector.elementAt(i);
            GameApplet.audio.loop(str, paramPhysicalObject);
          }

        }

        addObject(paramPhysicalObject);
        this.VisibleObjects.addElement(paramPhysicalObject);

        return;
      }

      if (this.VisibleObjects.contains(paramPhysicalObject))
      {
        if (this.CurrentlyLooping.contains(paramPhysicalObject))
        {
          localVector = paramPhysicalObject.getLoopList();

          for (i = 0; i < localVector.size(); ++i)
          {
            str = (String)localVector.elementAt(i);
            GameApplet.audio.stop(str, paramPhysicalObject);
          }

          this.CurrentlyLooping.removeElement(paramPhysicalObject);
        }

        ((SimpleContainer)this.Layers.get(paramPhysicalObject.getLayer())).remove(paramPhysicalObject);

        this.VisibleObjects.removeElement(paramPhysicalObject);
      }
    }
  }

  public void shroudChanged(ShroudEvent paramShroudEvent)
  {
    Rectangle localRectangle = paramShroudEvent.getAffectedArea();

    if (localRectangle.intersects(new Rectangle(this.Offset.x, this.Offset.y, WINDOWTILEWIDTH, WINDOWTILEHEIGHT)))
    {
      localRectangle.setLocation(localRectangle.x * 50 - this.Offset.x * 50, localRectangle.y * 50 - this.Offset.y * 50);
      localRectangle.setSize(localRectangle.width * 50, localRectangle.height * 50);
      super.taintBuffer(localRectangle);
      repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
    }
  }

  public void soundStopped(PhysicalObject paramPhysicalObject, int paramInt)
  {
    if (paramPhysicalObject.getPlayList().size() == 0)
    {
      this.Players.removeElement(paramPhysicalObject);
    }
  }

  public void playSound(PhysicalObject paramPhysicalObject, String paramString)
  {
    if ((isInWindow(paramPhysicalObject)) && (this.Visible))
    {
      this.Players.addElement(paramPhysicalObject);

      GameApplet.audio.play(paramString, paramPhysicalObject);
    }
  }

  public void loopSound(PhysicalObject paramPhysicalObject, String paramString)
  {
    this.Loopers.addElement(paramPhysicalObject);

    if ((isInWindow(paramPhysicalObject)) && (this.Visible))
    {
      this.CurrentlyLooping.addElement(paramPhysicalObject);

      GameApplet.audio.loop(paramString, paramPhysicalObject);
    }
  }

  public void stopSound(PhysicalObject paramPhysicalObject, String paramString)
  {
    if (paramPhysicalObject.getLoopList().size() == 0)
    {
      this.CurrentlyLooping.removeElement(paramPhysicalObject);
      this.Loopers.removeElement(paramPhysicalObject);
    }

    if (paramPhysicalObject.getPlayList().size() == 0)
    {
      this.Players.removeElement(paramPhysicalObject);
    }

    GameApplet.audio.stop(paramString, paramPhysicalObject);
  }

  public boolean isInWindow(PhysicalObject paramPhysicalObject)
  {
    boolean bool = false;

    Rectangle localRectangle1 = new Rectangle(this.Offset.x, this.Offset.y, WINDOWTILEWIDTH + 1, WINDOWTILEHEIGHT + 1);

    Position localPosition = paramPhysicalObject.getPosition();
    Dimension localDimension = paramPhysicalObject.getShapeSize();
    Rectangle localRectangle2 = new Rectangle(localPosition.x, localPosition.y, localDimension.width, localDimension.height);

    bool = localRectangle2.intersects(localRectangle1);

    return bool;
  }

  public Dimension getSize()
  {
    return new Dimension(480, 360); }

  public Dimension getMinimumSize() {
    return getSize(); } 
  public Dimension getPreferredSize() { return getSize(); } 
  public Dimension getMaximumSize() { return getSize();
  }

  public Component getInternalFocus()
  {
    return this.InternalFocus;
  }

  public void setInternalFocus(Component paramComponent)
  {
    this.InternalFocus = paramComponent;

    if (paramComponent != null)
    {
      paramComponent.requestFocus();

      return;
    }

    requestFocus();
  }

  public void focusGained(FocusEvent paramFocusEvent)
  {
    if (this.InternalFocus != null)
    {
      this.InternalFocus.requestFocus();
    }
  }

  public void focusLost(FocusEvent paramFocusEvent)
  {
  }

  public void setGameState(GameState paramGameState)
  {
    this.State = paramGameState;
  }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer(getClass().getName());
    localStringBuffer.append("@");
    localStringBuffer.append(hashCode());
    localStringBuffer.append("\n");
    localStringBuffer.append(toString());
    return localStringBuffer.toString(); }

  public OrderedTable getLayers() { return this.Layers;
  }
  
  
  class GroundComponent extends Component
  {
    private final Renderer this$0;
    private final Rectangle DEFAULT_GROUND_TAINT;
    private boolean GroundBufferTainted;
    private Rectangle GroundTaintArea;
    private Point GroundBlit;

    public void taintGround()
    {
      taintGround(this.DEFAULT_GROUND_TAINT);
    }

    public void taintGround(Rectangle paramRectangle)
    {
      Rectangle localRectangle = new Rectangle(paramRectangle);

      this.GroundBufferTainted = true;
      if (this.GroundTaintArea == null) {
        this.GroundTaintArea = localRectangle;

        return;
      }

      this.GroundTaintArea.add(localRectangle);
    }

    public void setGroundBlit(int paramInt1, int paramInt2)
    {
      this.GroundBlit = new Point(paramInt1, paramInt2);
    }

    public Point getGroundBlit()
    {
      return this.GroundBlit;
    }

    public void update(Graphics paramGraphics)
    {
      paint(paramGraphics);
    }

    public void paint(Graphics paramGraphics)
    {
      if (this.GroundBufferTainted)
      {
        if (this.GroundTaintArea == null) {
          this.GroundTaintArea = this.DEFAULT_GROUND_TAINT;
        }

        if (this.GroundBlit != null)
        {
          Rectangle localRectangle = this.this$0.GroundBufferGraphics.getClipBounds();
          this.this$0.GroundBufferGraphics.setClip(0, 0, 500, 400);
          this.this$0.GroundBufferGraphics.copyArea(0, 0, 500, 400, this.GroundBlit.x, this.GroundBlit.y);
          this.this$0.GroundBufferGraphics.setClip(localRectangle);

          this.GroundBlit = null;
        }

        int i = this.this$0.Offset.x + (int)Math.floor(this.GroundTaintArea.x / 50.0F);
        int j = i + (int)Math.ceil(this.GroundTaintArea.width / 50.0F);
        int k = this.this$0.Offset.y + (int)Math.floor(this.GroundTaintArea.y / 50.0F);
        int l = k + (int)Math.ceil(this.GroundTaintArea.height / 50.0F);

        j = Math.min(this.this$0.MapSize.width - 1, j);
        l = Math.min(this.this$0.MapSize.height - 1, l);

        int i1 = (i - this.this$0.Offset.x) * 50;
        int i2 = (k - this.this$0.Offset.y) * 50;
        for (int i3 = k; i3 <= l; )
        {
          for (int i4 = i; i4 <= j; )
          {
            Image localImage = this.this$0.map.getCell(i4, i3).getAppearance();
            GameApplet.thisApplet.hitCache(localImage);
            this.this$0.GroundBufferGraphics.drawImage(localImage, i1, i2, null);

            ++i4; i1 += 50;
          }

          i1 = (i - this.this$0.Offset.x) * 50;

          ++i3; i2 += 50;
        }

        this.GroundBufferTainted = false;
        this.GroundTaintArea = null;
      }

      paramGraphics.drawImage(this.this$0.GroundBufferImage, 0, 0, null);
    }

    protected GroundComponent(Renderer paramRenderer)
    {
      this.this$0 = paramRenderer;

      this.DEFAULT_GROUND_TAINT = new Rectangle(0, 0, 480, 360);
      this.GroundBufferTainted = true;
      this.GroundTaintArea = this.DEFAULT_GROUND_TAINT;
    }
  }
  
  
  
  class KeyHandler extends KeyAdapter
  {
    private final Renderer this$0;

    public void keyPressed(KeyEvent paramKeyEvent)
    {
      Point localPoint = new Point(this.this$0.getOffset());
      int i = 1;
      switch (paramKeyEvent.getKeyCode())
      {
      case 36:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(-i, -i);
        this.this$0.setOffset(localPoint);
        return;
      case 35:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(-i, i);
        this.this$0.setOffset(localPoint);
        return;
      case 33:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(i, -i);
        this.this$0.setOffset(localPoint);
        return;
      case 34:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(i, i);
        this.this$0.setOffset(localPoint);
        return;
      case 40:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(0, i);
        this.this$0.setOffset(localPoint);
        return;
      case 38:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(0, -i);
        this.this$0.setOffset(localPoint);
        return;
      case 37:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(-i, 0);
        this.this$0.setOffset(localPoint);
        return;
      case 39:
        this.this$0.setObjectToFollow(null);
        localPoint.translate(i, 0);
        this.this$0.setOffset(localPoint);
        return;
      case 32:
        if (this.this$0.State == null) return;
        this.this$0.setObjectToFollow(this.this$0.State.getCurrentRobot());
        return;
      case 71:
        GameApplet localGameApplet = GameApplet.thisApplet;
        if (localGameApplet.getGrid().isOn()) {
          localGameApplet.getGrid().burnGridOff();

          return;
        }

        localGameApplet.getGrid().burnGridOn();
        return;
      }
    }

    protected KeyHandler(Renderer paramRenderer)
    {
      this.this$0 = paramRenderer;
    }
  }
}