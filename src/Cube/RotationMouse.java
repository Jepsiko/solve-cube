package Cube;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;

public class RotationMouse extends Behavior {

    // variable de travail
    private final TransformGroup targetTG;
    private final Transform3D rotationX = new Transform3D();
    private final Transform3D rotationY = new Transform3D();

    // constructeur
    RotationMouse(TransformGroup targetTG)
    {
        this.targetTG = targetTG;
    }

    // on définit les évenemenst à détecter
    @Override
    public void initialize()
    {
        this.wakeupOn(new WakeupOnElapsedFrames(0));
    }

    // on définit l'action de ces évenements
    @Override
    public void processStimulus(Enumeration criteria)
    {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        float scl = 0.01f;
        rotationX.rotY(mouse.x* scl);
        rotationY.rotX(mouse.y* scl);
        rotationX.mul(rotationY);
        targetTG.setTransform(rotationX);
        // on se remet en attente
        this.wakeupOn(new WakeupOnElapsedFrames(0));

    }
}
