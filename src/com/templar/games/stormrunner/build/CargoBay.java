package com.templar.games.stormrunner.build;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.templarutil.animation.AnimationManager;
import com.templar.games.stormrunner.templarutil.animation.Sequence;
import com.templar.games.stormrunner.templarutil.animation.Step;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Vector;

public class CargoBay extends SimpleContainer
{
  public static final Dimension SIZE = new Dimension(480, 360);
  protected AnimationManager Animator;
  protected Image BackgroundImage;
  protected ImageRetriever ir;
  private boolean FirstAdded = false;

  public CargoBay(ImageRetriever paramImageRetriever)
  {
    this.ir = paramImageRetriever;

    setLayout(null);

    setSize(SIZE);

    initAnimator();
    add(this.Animator);
  }

  public void addNotify()
  {
    addNotify();

    if (!(this.FirstAdded))
    {
      Image localImage = this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_background.jpg");
      this.ir.hitCache(localImage);
      this.BackgroundImage = createImage(localImage.getWidth(this), localImage.getHeight(this));
      Graphics localGraphics = this.BackgroundImage.getGraphics();
      localGraphics.drawImage(localImage, 0, 0, this);
      localGraphics.dispose();

      this.FirstAdded = true;
    }
  }

  private void initAnimator()
  {
    this.Animator = new AnimationManager(GameApplet.audio);
    this.Animator.setLocation(0, 0);
    this.Animator.setSize(SIZE);

    this.Animator.createLayer("Foreground");
    this.Animator.createLayer("ToolFront");
    this.Animator.createLayer("RCXFront");
    this.Animator.createLayer("Sensors");
    this.Animator.createLayer("RCXCenter");
    this.Animator.createLayer("AssemblyFront");
    this.Animator.createLayer("AssemblyArmFront");
    this.Animator.createLayer("RCXRear");
    this.Animator.createLayer("ToolTopFront");
    this.Animator.createLayer("AssemblyTop");
    this.Animator.createLayer("ToolTop");
    this.Animator.createLayer("AssemblyRear");
    this.Animator.createLayer("ToolRear");
    this.Animator.createLayer("Ramp");
    this.Animator.createLayer("Background");

    this.Animator.addImage("Background", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_background.jpg"));
    this.Animator.addImage("RCX", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/rcx/B_RCX.gif"));
    this.Animator.addImage("FrontArmNeutral", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_00.gif"));
    this.Animator.addImage("FrontArmGrasp-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start02.gif"));
    this.Animator.addImage("FrontArmGrasp-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start03.gif"));
    this.Animator.addImage("FrontArmGrasp-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start04.gif"));
    this.Animator.addImage("FrontArmGrasp-4", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start05.gif"));
    this.Animator.addImage("FrontArmGrasp-5", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start06.gif"));
    this.Animator.addImage("FrontArmGrasp-6", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start07.gif"));
    this.Animator.addImage("FrontArmGrasp-7", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm/B_RCXarm_start08.gif"));
    this.Animator.addImage("SpindleNeutral", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_arm_00.gif"));
    this.Animator.addImage("SpindleExtended-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_arm_01.gif"));
    this.Animator.addImage("SpindleExtended-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_arm_02.gif"));
    this.Animator.addImage("SpindleExtended-Fingerless", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_arm_x.gif"));
    this.Animator.addImage("SpindleFingers-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_fingers_01.gif"));
    this.Animator.addImage("SpindleFingers-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_fingers_02.gif"));
    this.Animator.addImage("SpindleFingers-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_fingers_03.gif"));
    this.Animator.addImage("SpindleFingers-4", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/spindle/B_RCXstand_fingers_04.gif"));
    this.Animator.addImage("SensorArm-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_01.gif"));
    this.Animator.addImage("SensorArm-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_02.gif"));
    this.Animator.addImage("SensorArm-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_03.gif"));
    this.Animator.addImage("SensorArm-4", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_04.gif"));
    this.Animator.addImage("SensorArm-5", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_05.gif"));
    this.Animator.addImage("SensorArm-6", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_06.gif"));
    this.Animator.addImage("SensorArm-7", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_07.gif"));
    this.Animator.addImage("SensorArm-8", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_08.gif"));
    this.Animator.addImage("SensorArmLights-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_lights_01.gif"));
    this.Animator.addImage("SensorArmLights-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_lights_02.gif"));
    this.Animator.addImage("SensorArmLights-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/sensorarm/B_sensorarm_lights_03.gif"));
    this.Animator.addImage("BackArm", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_backarm.gif"));
    this.Animator.addImage("TopArm-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_01.gif"));
    this.Animator.addImage("TopArm-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_02.gif"));
    this.Animator.addImage("TopArm-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_03.gif"));
    this.Animator.addImage("TopArm-4", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_04.gif"));
    this.Animator.addImage("TopArm-Finger-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_finger_01.gif"));
    this.Animator.addImage("TopArm-Finger-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_finger_02.gif"));
    this.Animator.addImage("TopArm-Finger-3", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_finger_03.gif"));
    this.Animator.addImage("TopArm-Finger-4", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/toparm/b_buildarm_top_finger_04.gif"));
    this.Animator.addImage("BayForeground", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_foreground.gif"));
    this.Animator.addImage("Girder", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_girder.gif"));
    this.Animator.addImage("Ramp", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/B_ramp.gif"));
    this.Animator.addImage("ArachnaeFront", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_spider1.gif"));
    this.Animator.addImage("ArachnaeRear", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_spider2.gif"));
    this.Animator.addImage("AchillesFront", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_tracks1.gif"));
    this.Animator.addImage("AchillesRear", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_tracks2.gif"));
    this.Animator.addImage("HermesFront", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_wheels1.gif"));
    this.Animator.addImage("HermesRear", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/chassis/B_wheels2.gif"));
    this.Animator.addImage("HeatSensor", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/sensor/B_sensor_heat.gif"));
    this.Animator.addImage("ObstacleSensor", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/sensor/B_sensor_obstacle.gif"));
    this.Animator.addImage("VidSensor", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/sensor/B_sensor_video.gif"));
    this.Animator.addImage("EnergySensor", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/sensor/B_sensor_energy.gif"));
    this.Animator.addImage("GeolabSensor", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/sensor/b_sensor_spectracom.gif"));
    this.Animator.addImage("CargoPod", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/assembly/B_cargopod.gif"));
    this.Animator.addImage("Launcher", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/assembly/B_launcher.gif"));
    this.Animator.addImage("GrabberArm", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/assembly/B_arm.gif"));
    this.Animator.addImage("FrontAssemblyArm-1", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm2/B_buildarm_front00.gif"));
    this.Animator.addImage("FrontAssemblyArm-2", this.ir.getImage("com/templar/games/stormrunner/media/images/build/scene/frontarm2/B_buildarm_front01.gif"));
    this.Animator.addImage("Piledriver", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/assembly/B_piledriver.gif"));
    this.Animator.addImage("Launcher", this.ir.getImage("com/templar/games/stormrunner/media/images/build/robotparts/assembly/B_launcher.gif"));

    this.Animator.createActor("Background", "Background", new Point(0, 0));
    this.Animator.addActor("Background", "Background");

    this.Animator.createActor("Foreground", "BayForeground", new Point(190, 296));
    this.Animator.addActor("Foreground", "Foreground");
    this.Animator.createActor("Girder", "Girder", new Point(190, 0));
    this.Animator.addActor("Girder", "Foreground");

    this.Animator.createActor("FrontArm", "FrontArmNeutral", new Point(300, 0));
    this.Animator.addActor("FrontArm", "ToolFront");

    this.Animator.createActor("SensorArm", "SensorArm-1", new Point(346, 161));
    this.Animator.addActor("SensorArm", "ToolFront");

    this.Animator.createActor("SpindleFingers", "SpindleFingers-4", new Point(244, 110));

    this.Animator.createActor("SpindleArm", "SpindleExtended-Fingerless", new Point(244, 110));

    this.Animator.createActor("ChassisFront");
    this.Animator.moveActor("ChassisFront", new Point(190, 150));
    this.Animator.addActor("ChassisFront", "RCXFront");

    this.Animator.createActor("Sensor-1");
    this.Animator.moveActor("Sensor-1", new Point(348, 196));
    this.Animator.addActor("Sensor-1", "Sensors");

    this.Animator.createActor("Sensor-2");
    this.Animator.moveActor("Sensor-2", new Point(361, 196));
    this.Animator.addActor("Sensor-2", "Sensors");

    this.Animator.createActor("Sensor-3");
    this.Animator.moveActor("Sensor-3", new Point(374, 196));
    this.Animator.addActor("Sensor-3", "Sensors");

    this.Animator.createActor("RCX", "RCX", new Point(262, 96));
    this.Animator.addActor("RCX", "RCXCenter");

    this.Animator.createActor("ChassisRear");
    this.Animator.moveActor("ChassisRear", new Point(190, 150));
    this.Animator.addActor("ChassisRear", "RCXRear");

    this.Animator.createActor("TopArm", "TopArm-4", new Point(257, -15));
    this.Animator.addActor("TopArm", "ToolTop");

    this.Animator.createActor("TopArmFinger", "TopArm-Finger-4", new Point(257, -15));
    this.Animator.addActor("TopArmFinger", "ToolTopFront");

    this.Animator.createActor("TopAssembly");
    this.Animator.moveActor("TopAssembly", new Point(279, 28));
    this.Animator.addActor("TopAssembly", "AssemblyTop");

    this.Animator.createActor("BackArm", "BackArm", new Point(190, 0));
    this.Animator.addActor("BackArm", "ToolRear");

    this.Animator.createActor("RearAssembly-1");
    this.Animator.moveActor("RearAssembly-1", new Point(190, 0));
    this.Animator.addActor("RearAssembly-1", "AssemblyRear");

    this.Animator.createActor("Ramp", "Ramp", new Point(214, 261));
    this.Animator.addActor("Ramp", "Ramp");

    this.Animator.createActor("FrontAssembly");
    this.Animator.moveActor("FrontAssembly", new Point(320, 202));
    this.Animator.addActor("FrontAssembly", "AssemblyFront");

    this.Animator.createActor("FrontAssemblyArm");
    this.Animator.moveActor("FrontAssemblyArm", new Point(320, 202));
    this.Animator.addActor("FrontAssemblyArm", "AssemblyArmFront");

    clearRamp();

    this.Animator.addLayer("Foreground");
    this.Animator.addLayer("ToolFront");
    this.Animator.addLayer("RCXFront");
    this.Animator.addLayer("Sensors");
    this.Animator.addLayer("RCXCenter");
    this.Animator.addLayer("AssemblyFront");
    this.Animator.addLayer("AssemblyArmFront");
    this.Animator.addLayer("RCXRear");
    this.Animator.addLayer("ToolTopFront");
    this.Animator.addLayer("AssemblyTop");
    this.Animator.addLayer("ToolTop");
    this.Animator.addLayer("AssemblyRear");
    this.Animator.addLayer("ToolRear");
    this.Animator.addLayer("Ramp");
    this.Animator.addLayer("Background");

    Vector localVector1 = new Vector();
    localVector1.addElement("RCXFront");
    localVector1.addElement("RCXRear");
    localVector1.addElement("Ramp");

    Vector localVector2 = new Vector();
    localVector2.addElement("RCXFront");
    localVector2.addElement("Sensors");
    localVector2.addElement("RCXCenter");
    localVector2.addElement("RCXRear");
    localVector2.addElement("AssemblyRear");
    localVector2.addElement("AssemblyTop");
    localVector2.addElement("AssemblyFront");
    localVector2.addElement("Ramp");

    Vector localVector3 = new Vector();
    localVector3.addElement("Ramp");

    Vector localVector4 = new Vector();
    localVector4.addElement("RCXFront");
    localVector4.addElement("Sensors");
    localVector4.addElement("RCXCenter");
    localVector4.addElement("RCXRear");

    Vector localVector5 = new Vector();
    localVector5.addElement("RCXFront");
    localVector5.addElement("Sensors");
    localVector5.addElement("RCXCenter");
    localVector5.addElement("RCXRear");
    localVector5.addElement("AssemblyRear");
    localVector5.addElement("AssemblyTop");
    localVector5.addElement("AssemblyFront");

    Vector localVector6 = new Vector();
    localVector6.addElement("RCXFront");
    localVector6.addElement("RCXRear");

    Vector localVector7 = new Vector();
    localVector7.addElement("ToolRear");
    localVector7.addElement("AssemblyRear");

    Vector localVector8 = new Vector();
    localVector8.addElement("ToolRear");

    Vector localVector9 = new Vector();
    localVector9.addElement("ToolTop");
    localVector9.addElement("ToolTopFront");
    localVector9.addElement("AssemblyTop");

    Vector localVector10 = new Vector();
    localVector10.addElement("ToolTop");
    localVector10.addElement("ToolTopFront");

    Vector localVector11 = new Vector();
    localVector11.addElement("AssemblyFront");
    localVector11.addElement("AssemblyArmFront");

    Vector localVector12 = new Vector();
    localVector12.addElement("AssemblyArmFront");

    Sequence localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector2, new Point(0, 305), new Point(0, 0), 2600, 100);

    this.Animator.addSequence("RaiseRCX", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector2, new Point(0, 0), new Point(0, 305), 2600, 100);

    this.Animator.addSequence("LowerRCX", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector1, new Point(0, 0), new Point(0, 165), 2000, 100);

    this.Animator.addSequence("LowerChassis", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector1, new Point(0, 165), new Point(0, -4), 2000, 100);
    localSequence.moveLayers(localVector1, new Point(0, -4), new Point(0, 0), 100, 100);

    this.Animator.addSequence("RaiseChassis", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.moveActor("FrontArm", new Point(300, 0), new Point(280, 0), 500, 100);
    localSequence.addStep(new Step(500, 1000, null));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-1"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-2"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-3"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-4"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-5"));
    localSequence.addStep(new Step(140, 1000, "FrontArm", "FrontArmGrasp-6"));
    localSequence.addStep(new Step(140, 600, "FrontArm", "FrontArmGrasp-7"));

    this.Animator.addSequence("EngageFrontArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.addStep(new Step(500, 600, null));
    localSequence.addStep(new Step(140, 1000, "FrontArm", "FrontArmGrasp-6"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-5"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-4"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-3"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-2"));
    localSequence.addStep(new Step(140, 150, "FrontArm", "FrontArmGrasp-1"));
    localSequence.addStep(new Step(140, 1000, "FrontArm", "FrontArmNeutral"));
    localSequence.moveActor("FrontArm", new Point(280, 0), new Point(300, 0), 500, 100);

    this.Animator.addSequence("DisengageFrontArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector8, new Point(0, -186), new Point(0, 0), 1000, 100);
    localSequence.addStep(new Step(200, 500, "Build-RearTopArm-End"));

    this.Animator.addSequence("LowerBackArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector8, new Point(0, 0), new Point(0, -186), 1000, 100);
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-End"));

    this.Animator.addSequence("RaiseBackArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector7, new Point(0, -186), new Point(0, 0), 2000, 100);
    localSequence.addStep(new Step(200, 500, "Build-RearTopArm-End"));

    this.Animator.addSequence("LowerBackAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector7, new Point(0, 0), new Point(0, -186), 2000, 100);
    Step localStep = new Step(140, 0, "RearAssembly-1");
    localStep.Argument2 = null;
    localSequence.addStep(localStep);
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-End"));

    this.Animator.addSequence("RaiseBackAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.moveLayers(localVector7, new Point(0, -186), null, 0, 0);

    this.Animator.addSequence("ResetBackAssemblyLayers", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-1"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-1"));
    localSequence.moveLayers(localVector10, new Point(0, -110), new Point(0, 0), 1000, 100);
    localSequence.addStep(new Step(200, 0, "Build-ArmWrist"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-2"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-2"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-3"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-3"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-4"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-4"));
    localSequence.addStep(new Step(500, 500, null));

    this.Animator.addSequence("LowerTopArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector10, new Point(0, 0), new Point(0, -110), 1000, 100);

    this.Animator.addSequence("RaiseTopArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-4"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-4"));
    localSequence.moveLayers(localVector9, new Point(0, -110), new Point(0, 0), 2000, 100);
    localSequence.addStep(new Step(200, 500, "Build-RearTopArm-End"));
    localSequence.addStep(new Step(200, 0, "Build-ArmWrist"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-3"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-3"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-2"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-2"));
    localSequence.addStep(new Step(140, 0, "TopArm", "TopArm-1"));
    localSequence.addStep(new Step(140, 100, "TopArmFinger", "TopArm-Finger-1"));
    localSequence.addStep(new Step(500, 500, null));

    this.Animator.addSequence("LowerTopAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(500, 500, null));
    localSequence.addStep(new Step(200, 0, "Build-RearTopArm-Move"));
    localSequence.moveLayers(localVector9, new Point(0, 0), new Point(0, -110), 2000, 100);
    localStep = new Step(140, 0, "TopAssembly");
    localStep.Argument2 = null;
    localSequence.addStep(localStep);

    this.Animator.addSequence("RaiseTopAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.moveLayers(localVector9, new Point(0, -110), null, 0, 0);

    this.Animator.addSequence("ResetTopAssemblyLayers", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(1000, 0, "EngageFrontArm"));
    localSequence.addStep(new Step(1000, 0, "LowerChassis"));

    this.Animator.addSequence("RemoveChassis", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(1000, 0, "LowerChassis"));

    this.Animator.addSequence("RemoveSuspendedChassis", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(1000, 0, "RaiseChassis"));
    localSequence.addStep(new Step(1000, 0, "DisengageFrontArm"));

    this.Animator.addSequence("AddChassis", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector3, new Point(0, 0), new Point(0, 60), 800, 100);

    this.Animator.addSequence("LowerRamp", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-Elevator"));
    localSequence.moveLayers(localVector3, new Point(0, 60), new Point(0, 0), 800, 100);

    this.Animator.addSequence("RaiseRamp", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-SensorArm-Beat"));
    localSequence.addStep(new Step(200, 0, "Build-SensorArm-Move-Fadeout"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-2"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-3"));
    localSequence.addStep(new Step(140, 500, "SensorArm", "SensorArm-4"));
    localSequence.addStep(new Step(200, 100, "Build-SensorArm-Beat"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-5"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-6"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-7"));
    localSequence.addStep(new Step(140, 0, "SensorArm", "SensorArm-8"));
    localSequence.addStep(new Step(200, 100, "Build-SensorArm-End"));
    localSequence.addStep(new Step(220, 500, "Build-SensorArm-Move-Fadeout"));

    this.Animator.addSequence("ExtendSensorArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-SensorArm-Beat"));
    localSequence.addStep(new Step(200, 0, "Build-SensorArm-Move-Fadeout"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-7"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-6"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-5"));
    localSequence.addStep(new Step(140, 600, "SensorArm", "SensorArm-4"));
    localSequence.addStep(new Step(200, 0, "Build-SensorArm-Beat"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-3"));
    localSequence.addStep(new Step(140, 100, "SensorArm", "SensorArm-2"));
    localSequence.addStep(new Step(140, 0, "SensorArm", "SensorArm-1"));
    localSequence.addStep(new Step(200, 100, "Build-SensorArm-End"));
    localSequence.addStep(new Step(220, 0, "Build-SensorArm-Move-Fadeout"));

    this.Animator.addSequence("RetractSensorArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(120, 0, "ChassisFront"));
    localSequence.addStep(new Step(120, 0, "ChassisRear"));
    localSequence.addStep(new Step(140, 0, "SpindleArm", "SpindleExtended-Fingerless"));
    localSequence.addStep(new Step(140, 0, "SpindleFingers", "SpindleFingers-4"));
    localSequence.addStep(new Step(110, 0, "SpindleFingers", "RCXFront"));
    localSequence.addStep(new Step(110, 0, "SpindleArm", "RCXRear"));
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.moveLayers(localVector4, new Point(0, 305), new Point(0, 0), 2600, 100);
    localSequence.addStep(new Step(1000, 600, "EngageFrontArm"));
    localSequence.addStep(new Step(200, 0, "Robot-Happy"));
    localSequence.addStep(new Step(140, 100, "SpindleFingers", "SpindleFingers-3"));
    localSequence.addStep(new Step(140, 100, "SpindleFingers", "SpindleFingers-2"));
    localSequence.addStep(new Step(140, 1200, "SpindleFingers", "SpindleFingers-1"));
    localSequence.addStep(new Step(120, 0, "SpindleFingers"));
    localSequence.addStep(new Step(140, 100, "SpindleArm", "SpindleExtended-1"));
    localSequence.addStep(new Step(140, 600, "SpindleArm", "SpindleNeutral"));
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.moveLayers(localVector6, new Point(0, 0), new Point(0, 305), 2600, 100);
    localSequence.addStep(new Step(120, 0, "SpindleArm"));
    localSequence.addStep(new Step(110, 0, "ChassisFront", "RCXFront"));
    localSequence.addStep(new Step(110, 0, "ChassisRear", "RCXRear"));

    this.Animator.addSequence("NewRCX", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 1100, "Robot-Scared"));
    localSequence.addStep(new Step(1000, 0, "LowerRCX"));
    localSequence.addStep(new Step(200, 0, "Build-Dismantle"));

    this.Animator.addSequence("DismantleRCX", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 1100, "Robot-Scared"));
    localSequence.addStep(new Step(120, 0, "ChassisFront", "RCXFront"));
    localSequence.addStep(new Step(120, 0, "ChassisRear", "RCXRear"));
    localSequence.addStep(new Step(140, 0, "SpindleArm", "SpindleNeutral"));
    localSequence.addStep(new Step(140, 0, "SpindleFingers", "SpindleFingers-1"));
    localSequence.addStep(new Step(110, 0, "SpindleArm", "RCXRear"));
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.moveLayers(localVector6, new Point(0, 305), new Point(0, 0), 2600, 100);
    localSequence.addStep(new Step(140, 100, "SpindleArm", "SpindleExtended-1"));
    localSequence.addStep(new Step(140, 600, "SpindleArm", "SpindleExtended-2"));
    localSequence.addStep(new Step(140, 0, "SpindleArm", "SpindleExtended-Fingerless"));
    localSequence.addStep(new Step(110, 600, "SpindleFingers", "RCXFront"));
    localSequence.addStep(new Step(140, 100, "SpindleFingers", "SpindleFingers-2"));
    localSequence.addStep(new Step(140, 100, "SpindleFingers", "SpindleFingers-3"));
    localSequence.addStep(new Step(140, 600, "SpindleFingers", "SpindleFingers-4"));
    localSequence.addStep(new Step(1000, 600, "DisengageFrontArm"));
    localSequence.addStep(new Step(200, 700, "Robot-ReplyScared"));
    localSequence.addStep(new Step(200, 0, "Build-Arm-Tools"));
    localSequence.moveLayers(localVector5, new Point(0, 0), new Point(0, 305), 2600, 100);
    localSequence.addStep(new Step(120, 0, "SpindleArm"));
    localSequence.addStep(new Step(120, 0, "SpindleFingers"));
    localSequence.addStep(new Step(110, 0, "ChassisFront", "RCXFront"));
    localSequence.addStep(new Step(110, 0, "ChassisRear", "RCXRear"));
    localSequence.addStep(new Step(200, 0, "Build-Dismantle"));

    this.Animator.addSequence("DismantleSuspendedRCX", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(140, 0, "FrontAssemblyArm", "FrontAssemblyArm-2"));
    localSequence.moveLayers(localVector11, new Point(165, 16), new Point(0, 16), 2000, 100);
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.moveLayers(localVector11, new Point(0, 16), new Point(0, 0), 500, 100);
    localSequence.addStep(new Step(200, 100, "Build-RearTopArm-End"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));

    this.Animator.addSequence("ExtendFrontAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(140, 500, "FrontAssemblyArm", "FrontAssemblyArm-2"));
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.moveLayers(localVector11, new Point(0, 0), new Point(0, 16), 500, 100);
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.moveLayers(localVector11, new Point(0, 16), new Point(165, 16), 2000, 100);
    localSequence.addStep(new Step(200, 100, "Build-RearTopArm-End"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));

    this.Animator.addSequence("RetractFrontAssembly", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(140, 0, "FrontAssemblyArm", "FrontAssemblyArm-1"));
    localSequence.moveLayers(localVector12, new Point(165, 16), new Point(0, 16), 2000, 100);
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.moveLayers(localVector12, new Point(0, 16), new Point(0, 0), 500, 100);
    localSequence.addStep(new Step(200, 100, "Build-RearTopArm-End"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));

    this.Animator.addSequence("ExtendFrontAssemblyArm", localSequence);

    localSequence = new Sequence();
    localSequence.addStep(new Step(500, 500, null));
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(140, 0, "FrontAssemblyArm", "FrontAssemblyArm-1"));
    localSequence.moveLayers(localVector12, new Point(0, 0), new Point(0, 16), 500, 100);
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));
    localSequence.addStep(new Step(200, 0, "Build-FrontArm"));
    localSequence.moveLayers(localVector12, new Point(0, 16), new Point(165, 16), 2000, 100);
    localSequence.addStep(new Step(200, 100, "Build-RearTopArm-End"));
    localSequence.addStep(new Step(220, 0, "Build-FrontArm"));

    this.Animator.addSequence("RetractFrontAssemblyArm", localSequence);

    localSequence = new Sequence();
    localSequence.moveLayers(localVector11, new Point(165, 16), null, 0, 0);

    this.Animator.addSequence("ResetFrontAssemblyLayers", localSequence);
  }

  public void start(String paramString)
  {
    this.Animator.start(paramString);
  }

  public void start(String paramString, ActionListener paramActionListener) {
    this.Animator.start(paramString, paramActionListener);
  }

  public void setChassisImages(String paramString1, String paramString2)
  {
    this.Animator.setActorImage("ChassisFront", paramString1);
    this.Animator.setActorImage("ChassisRear", paramString2);
  }

  public void setSensorImage(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    case 1:
      if (paramString == null)
      {
        this.Animator.takeActorImage("Sensor-1", "Sensor-2");
        this.Animator.takeActorImage("Sensor-2", "Sensor-3");
        this.Animator.setActorImage("Sensor-3", null);

        return;
      }

      this.Animator.setActorImage("Sensor-1", paramString);
      return;
    case 2:
      if (paramString == null)
      {
        this.Animator.takeActorImage("Sensor-2", "Sensor-3");
        this.Animator.setActorImage("Sensor-3", null);

        return;
      }

      this.Animator.setActorImage("Sensor-2", paramString);
      return;
    case 3:
      this.Animator.setActorImage("Sensor-3", paramString);
      return;
    }
  }

  public void setSensorImages(String paramString1, String paramString2, String paramString3)
  {
    this.Animator.setActorImage("Sensor-1", paramString1);
    this.Animator.setActorImage("Sensor-2", paramString2);
    this.Animator.setActorImage("Sensor-3", paramString3);
  }

  public void resetRearAssemblyLayers()
  {
    this.Animator.run("ResetBackAssemblyLayers");
  }

  public void setRearAssemblyImage(String paramString)
  {
    this.Animator.setActorImage("RearAssembly-1", paramString);
  }

  public void resetTopAssemblyLayers()
  {
    this.Animator.run("ResetTopAssemblyLayers");
  }

  public void setTopAssemblyImage(String paramString)
  {
    this.Animator.setActorImage("TopAssembly", paramString);
  }

  public void resetFrontAssemblyLayers()
  {
    this.Animator.run("ResetFrontAssemblyLayers");
  }

  public void setFrontAssemblyImage(String paramString)
  {
    this.Animator.setActorImage("FrontAssembly", paramString);
  }

  public void clearRamp()
  {
    this.Animator.moveLayer("RCXFront", new Point(0, 305));
    this.Animator.moveLayer("Sensors", new Point(0, 305));
    this.Animator.moveLayer("RCXCenter", new Point(0, 305));
    this.Animator.moveLayer("RCXRear", new Point(0, 305));
    this.Animator.moveLayer("AssemblyRear", new Point(0, -186));
    this.Animator.moveLayer("ToolRear", new Point(0, -186));
    this.Animator.moveLayer("AssemblyTop", new Point(0, -110));
    this.Animator.moveLayer("ToolTop", new Point(0, -110));
    this.Animator.moveLayer("ToolTopFront", new Point(0, -110));
    this.Animator.moveLayer("AssemblyFront", new Point(165, 16));
    this.Animator.moveLayer("AssemblyArmFront", new Point(165, 16));
    this.Animator.moveLayer("Ramp", new Point(0, 305));
    this.Animator.setActorImage("Sensor-1", null);
    this.Animator.setActorImage("Sensor-2", null);
    this.Animator.setActorImage("Sensor-3", null);
    this.Animator.setActorImage("RearAssembly-1", null);
    this.Animator.setActorImage("TopAssembly", null);
    this.Animator.setActorImage("FrontAssembly", null);
  }
}