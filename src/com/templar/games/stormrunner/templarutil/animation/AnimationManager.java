package com.templar.games.stormrunner.templarutil.animation;

import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageContainer;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Hashtable;

public class AnimationManager extends ImageContainer
{
  protected Hashtable Sequences;
  protected Hashtable Actors;
  protected Hashtable Images;
  protected Hashtable Layers;
  protected AudioManager Audio;
  protected transient ThreadGroup Running;
  protected transient String LastActorName = "";
  protected transient ImageComponent LastActor;

  public AnimationManager()
  {
    this(null);
  }

  public AnimationManager(AudioManager paramAudioManager)
  {
    this.Sequences = new Hashtable();
    this.Actors = new Hashtable();
    this.Images = new Hashtable();
    this.Layers = new Hashtable();
    this.Audio = paramAudioManager;
  }

  public void run(String paramString)
  {
    ((Sequence)this.Sequences.get(paramString)).run();
  }

  public void start(String paramString)
  {
    ((Sequence)this.Sequences.get(paramString)).start();
  }

  public void start(String paramString, ActionListener paramActionListener)
  {
    Sequence localSequence = (Sequence)this.Sequences.get(paramString);
    localSequence.addActionListener(paramActionListener);
    localSequence.start();
  }

  public void stop(String paramString)
  {
    ((Sequence)this.Sequences.get(paramString)).stop();
  }

  public void stopAll()
  {
  }

  public void addImage(String paramString, Image paramImage)
  {
    this.Images.put(paramString, paramImage);
  }

  public void addSequence(String paramString, Sequence paramSequence)
  {
    paramSequence.setManager(this);

    this.Sequences.put(paramString, paramSequence);
  }

  public void createActor(String paramString)
  {
    this.Actors.put(paramString, new ImageComponent(null, true, false));
  }

  public void createActor(String paramString1, String paramString2)
  {
    this.Actors.put(paramString1, new ImageComponent((Image)this.Images.get(paramString2), true, false));
  }

  public void createActor(String paramString1, String paramString2, Point paramPoint)
  {
    ImageComponent localImageComponent = new ImageComponent((Image)this.Images.get(paramString2), true, false);
    localImageComponent.setLocation(paramPoint);
    this.Actors.put(paramString1, localImageComponent);
  }

  public void addActor(String paramString1, String paramString2)
  {
    ((Container)this.Layers.get(paramString2)).add(getActor(paramString1));
  }

  public void removeActor(String paramString)
  {
    ImageComponent localImageComponent = getActor(paramString);
    Container localContainer = localImageComponent.getParent();
    if (localContainer != null)
    {
      localContainer.remove(localImageComponent);
    }
  }

  public void moveActor(String paramString, Point paramPoint)
  {
    getActor(paramString).setLocation(paramPoint);
  }

  public synchronized void setActorImage(String paramString1, String paramString2)
  {
    if (paramString2 != null) {
      getActor(paramString1).setImage((Image)this.Images.get(paramString2));

      return;
    }

    getActor(paramString1).setImage(null);
  }

  public void takeActorImage(String paramString1, String paramString2)
  {
    ImageComponent localImageComponent1 = getActor(paramString1);
    ImageComponent localImageComponent2 = getActor(paramString2);
    localImageComponent1.setImage(localImageComponent2.getImage());
  }

  public void actorSwitchLayer(String paramString1, String paramString2)
  {
    ImageComponent localImageComponent = getActor(paramString1);
    Container localContainer = localImageComponent.getParent();
    if (localContainer != null)
    {
      localContainer.remove(localImageComponent);
      ((Container)this.Layers.get(paramString2)).add(localImageComponent);

      return;
    }

    System.out.println("AnimationManager: actorSwitchLayer(" + paramString1 + "): Attempt to switch an Actor which has no parent.");
  }

  public void playSound(String paramString)
  {
    if (this.Audio != null)
      this.Audio.play(paramString);
  }

  public void loopSound(String paramString)
  {
    if (this.Audio != null)
      this.Audio.loop(paramString);
  }

  public void stopSound(String paramString)
  {
    if (this.Audio != null)
      this.Audio.stop(paramString);
  }

  public void createLayer(String paramString)
  {
    SimpleContainer localSimpleContainer = new SimpleContainer();
    localSimpleContainer.setBounds(0, 0, getSize().width, getSize().height);
    localSimpleContainer.setLayout(null);
    this.Layers.put(paramString, localSimpleContainer);
  }

  public void addLayer(String paramString, int paramInt)
  {
    register((Component)this.Layers.get(paramString), paramInt);
  }

  public void addLayer(String paramString)
  {
    register((Component)this.Layers.get(paramString));
  }

  public void removeLayer(String paramString)
  {
    unregister((Component)this.Layers.get(paramString));
  }

  public void moveLayer(String paramString, Point paramPoint)
  {
    ((Component)this.Layers.get(paramString)).setLocation(paramPoint);
  }

  public void reorderLayer(String paramString1, String paramString2)
  {
    Component localComponent1 = (Component)this.Layers.get(paramString1);
    Component localComponent2 = (Component)this.Layers.get(paramString2);

    Component[] arrayOfComponent = getChildren();
    int i = -1;
    for (int j = 0; (j < arrayOfComponent.length) && (i < 0); j++)
    {
      if (arrayOfComponent[j] == localComponent2) {
        i = j;
      }
    }

    if (i >= 0)
    {
      unregister(localComponent1);
      register(localComponent1, i);
    }
  }

  public void reorderLayer(String paramString, int paramInt)
  {
    Component localComponent = (Component)this.Layers.get(paramString);
    unregister(localComponent);
    register(localComponent, paramInt);
  }

  public ImageComponent getActor(String paramString)
  {
    if (paramString == this.LastActorName) {
      return this.LastActor;
    }

    this.LastActorName = paramString;
    this.LastActor = ((ImageComponent)this.Actors.get(paramString));
    return this.LastActor;
  }
}