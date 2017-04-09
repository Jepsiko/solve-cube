package Cube;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class Cuboid extends Frame implements Cube, WindowListener {

    private Cubi[][][] cubis;

    private final int x;
    private final int y;
    private final int z;
    public final int maxSide;
    
    private boolean graphic;
    private final float size;
    private final float margin;
    private SimpleUniverse world;
    private BranchGroup myScene;

    private final char[] colors;

    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Cuboid(int _x, int _y, int _z) {
        graphic = false;
        colors = new char[]{'o', 'r', 'y', 'w', 'g', 'b'};
        // Orange   Red      Yellow   White   Blue     Green
        // Left     Right    Down     Up      Back     Front
        
        if (_x < 2) {
            _x = 2;
        }
        if (_y < 2) {
            _y = 2;
        }
        if (_z < 2) {
            _z = 2;
        }
        x = _x;
        y = _y;
        z = _z;
        maxSide = Math.max(Math.max(_x, _y), _z);
        
        size = 0.6f/maxSide;
        margin = 0.03f/maxSide;
        
        cubis = new Cubi[x][y][z];
        char[] cubi_colors;
        int index;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    //System.out.println(Integer.toString(i) + "," + Integer.toString(j) + "," + Integer.toString(k)); 
                    if (i % (x - 1) == 0 && j % (y - 1) == 0 && k % (z - 1) == 0) {
                        cubi_colors = new char[3]; // Corner

                        for (int c = 0; c < cubi_colors.length; c++) {
                            index = c * 2;
                            if (c == 0 && i == (x - 1)) {
                                index += 1;
                            }
                            if (c == 1 && j == (y - 1)) {
                                index += 1;
                            }
                            if (c == 2 && k == (z - 1)) {
                                index += 1;
                            }
                            cubi_colors[c] = colors[index];
                        }
                    } else if (isEdge(i, j, k)) {
                        cubi_colors = new char[3]; // Edge

                        for (int c = 0; c < cubi_colors.length; c++) {
                            // ' ' mean that there is no color on this axis
                            if (c == 0 && i % (x - 1) != 0) {
                                cubi_colors[c] = ' ';
                            } else if (c == 1 && j % (y - 1) != 0) {
                                cubi_colors[c] = ' ';
                            } else if (c == 2 && k % (z - 1) != 0) {
                                cubi_colors[c] = ' ';
                            } else {
                                index = c * 2;
                                if (c == 0 && i == (x - 1)) {
                                    index += 1;
                                }
                                if (c == 1 && j == (y - 1)) {
                                    index += 1;
                                }
                                if (c == 2 && k == (z - 1)) {
                                    index += 1;
                                }
                                cubi_colors[c] = colors[index];
                            }
                        }
                    } else if (isFace(i, j, k)) {
                        cubi_colors = new char[3]; // Face

                        index = 0;
                        if (j % (y - 1) == 0) {
                            index = 2;
                        }
                        if (k % (z - 1) == 0) {
                            index = 4;
                        }
                        if (i == (x - 1) || j == (y - 1) || k == (z - 1)) {
                            index += 1;
                        }

                        for (int c = 0; c < cubi_colors.length; c++) {
                            // ' ' mean that there is no color on this axis
                            if (c != index / 2) {
                                cubi_colors[c] = ' ';
                            } else {
                                cubi_colors[c] = colors[index];
                            }
                        }
                    } else {
                        cubi_colors = null; // Hidden Piece
                    }

                    cubis[i][j][k] = new Cubi(cubi_colors);
                }
            }
        }
    }

    public Cuboid(int size) {
        // Create a cubic cuboid
        this(size, size, size);
    }
    
    public Cuboid(int _x, int _y, int _z, boolean graph) {
        this(_x, _y, _z);
        graphic = graph;
    }
    
    public Cuboid(int size, boolean graph) {
        this(size, size, size, graph);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Cube Methods">
    @Override
    public String toString() {
        String res = "[ ";
        for (Cubi[][] row : cubis) {
            for (Cubi[] line : row) {
                for (Cubi piece : line) {
                    if (piece.getColors() != null) {
                        res += piece + " ";
                    }
                }
            }
        }
        res += "]";
        return res;
    }

    @Override
    public void setup() {
        graphic = true;
        this.addWindowListener(this);
        this.setLayout(new BorderLayout());
        
        //creation de la scene java3d
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        world = new SimpleUniverse(canvas);
        myScene = createScene();
        world.addBranchGraph(myScene);
        world.getViewingPlatform().setNominalViewingTransform();
        //fin de creation

        this.add("Center", canvas);
        this.setSize(400, 400);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
    }

    @Override
    public void draw() {
        myScene.detach();
        myScene = createScene();
        world.addBranchGraph(myScene);
    }

    @Override
    public void scramble(int step, boolean verbose) {
        Random randint = new Random();
        int axis;
        int row;
        boolean clockwise;
        char axis_c;
        int maxRow;
        
        int previousAxis = 0;
        int previousRow = 0;
        boolean previousClockwise = true;

        for (int i = 0; i < step; i++) {
            axis = randint.nextInt(3);
            axis_c = (char) (axis += 120);
            switch (axis_c) {
                case 'x':
                    maxRow = x;
                    break;
                case 'y':
                    maxRow = y;
                    break;
                default:
                    maxRow = z;
                    break;
            }
            row = randint.nextInt(maxRow);

            clockwise = randint.nextInt(2) == 0;
            
            if (i != 0) {
                while (previousAxis == axis && previousRow == row && previousClockwise == !clockwise) {
                    axis = randint.nextInt(3);
                    axis_c = (char) (axis += 120);
                    switch (axis_c) {
                        case 'x':
                            maxRow = x;
                            break;
                        case 'y':
                            maxRow = y;
                            break;
                        default:
                            maxRow = z;
                            break;
                    }
                    row = randint.nextInt(maxRow);

                    clockwise = randint.nextInt(2) == 0;
                }
            }
            
            
            if (verbose) {
                System.out.print("Turn the row " + Integer.toString(row) + " around the axis " + axis_c);
                if (clockwise) System.out.println(" clockwise");
                else System.out.println(" counter-clockwise");
            }
            turnFace(axis_c, row, clockwise);
            previousAxis = axis;
            previousRow = row;
            previousClockwise = clockwise;
        }
    }
    
    @Override
    public int solve(boolean changeCube) {
        int step = 0;
        if (changeCube) {
            Cubi[][][] save_cube = cubis;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        save_cube[i][j][k] = new Cubi(cubis[i][j][k].getColors());
                    }
                }
            }
        }
        
        
        
        return step;
    }

    @Override
    public boolean isSolved() {
        for (int i = 0; i < 6; i++) {
            if (!faceSolved(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isGraphic() { return graphic; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Graphic Methods">
    private BranchGroup createScene() {
        
        BranchGroup scene = new BranchGroup();
        scene.setCapability(BranchGroup.ALLOW_DETACH);
        
        TransformGroup TG_Cube = new TransformGroup();
        TG_Cube.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    if (cubis[i][j][k].getColors() != null) drawCubi(TG_Cube, i, j, k);
                }
            }
        }
        
        RotationMouse rotation = new RotationMouse(TG_Cube);
        rotation.setSchedulingBounds(new BoundingSphere());
        TG_Cube.addChild(rotation);
        
        Appearance ap = new Appearance();
        ColoringAttributes black = new ColoringAttributes();
        black.setColor(0, 0, 0);
        black.setShadeModel(ColoringAttributes.NICEST);
        ap.setColoringAttributes(black);
        Box box = new Box(x*size/2-0.001f, y*size/2-0.001f, z*size/2-0.001f, ap);
        TG_Cube.addChild(box);

        scene.addChild(TG_Cube);
        scene.compile();
        return scene;
    }
    
    private void drawCubi(TransformGroup TG, int x, int y, int z) {
        
        QuadArray quad = new QuadArray(24, QuadArray.COORDINATES | QuadArray.COLOR_3);
        
        for (int i = 0; i < 6; i+=1) {
            drawSquare(quad, i, x, y, z);
        }
        
        TG.addChild(new Shape3D(quad));
    }
    
    private void drawSquare(QuadArray quad, int axis, float x, float y, float z) {
        char color = cubis[(int)x][(int)y][(int)z].getColors()[axis%3];
        
        x *= size;
        y *= size;
        z *= size;
        
        // Center the cube
        x -= this.x*size/2;
        y -= this.y*size/2;
        z -= this.z*size/2;
        
        if (axis%3 != 0) x += margin;
        if (axis%3 != 1) y += margin;
        if (axis%3 != 2) z += margin;
        
        float flag = 0;
        if (axis >= 3) {
            flag = size;
        }
        
        float copy_size = size - margin*2;
        
        switch(axis) {
            case 0:
                quad.setCoordinate(4*axis, new Point3f(x+flag, y, z));
                quad.setCoordinate(1+4*axis, new Point3f(x+flag, y, z+copy_size));
                quad.setCoordinate(2+4*axis, new Point3f(x+flag, y+copy_size, z+copy_size));
                quad.setCoordinate(3+4*axis, new Point3f(x+flag, y+copy_size, z));
                break;
            case 3:
                quad.setCoordinate(4*axis, new Point3f(x+flag, y, z));
                quad.setCoordinate(1+4*axis, new Point3f(x+flag, y+copy_size, z));
                quad.setCoordinate(2+4*axis, new Point3f(x+flag, y+copy_size, z+copy_size));
                quad.setCoordinate(3+4*axis, new Point3f(x+flag, y, z+copy_size));
                break;
            case 1:
                quad.setCoordinate(4*axis, new Point3f(x, y+flag, z));
                quad.setCoordinate(1+4*axis, new Point3f(x+copy_size, y+flag, z));
                quad.setCoordinate(2+4*axis, new Point3f(x+copy_size, y+flag, z+copy_size));
                quad.setCoordinate(3+4*axis, new Point3f(x, y+flag, z+copy_size));
                break;
            case 4:
                quad.setCoordinate(4*axis, new Point3f(x, y+flag, z));
                quad.setCoordinate(1+4*axis, new Point3f(x, y+flag, z+copy_size));
                quad.setCoordinate(2+4*axis, new Point3f(x+copy_size, y+flag, z+copy_size));
                quad.setCoordinate(3+4*axis, new Point3f(x+copy_size, y+flag, z));
                break;
            case 5:
                quad.setCoordinate(4*axis, new Point3f(x, y, z+flag));
                quad.setCoordinate(1+4*axis, new Point3f(x+copy_size, y, z+flag));
                quad.setCoordinate(2+4*axis, new Point3f(x+copy_size, y+copy_size, z+flag));
                quad.setCoordinate(3+4*axis, new Point3f(x, y+copy_size, z+flag));
                break;
            default :
                quad.setCoordinate(4*axis, new Point3f(x, y, z+flag));
                quad.setCoordinate(1+4*axis, new Point3f(x, y+copy_size, z+flag));
                quad.setCoordinate(2+4*axis, new Point3f(x+copy_size, y+copy_size, z+flag));
                quad.setCoordinate(3+4*axis, new Point3f(x+copy_size, y, z+flag));
                break;
        }

        for (int i = 0; i < 4; i++) {
            switch(color) {
                case 'o':
                    quad.setColor(i+4*axis, new Color3f(1f, 0.34f, 0f));
                    break;
                case 'r':
                    quad.setColor(i+4*axis, new Color3f(0.77f, 0.12f, 0.11f));
                    break;
                case 'b':
                    quad.setColor(i+4*axis, new Color3f(0f, 0.32f, 0.73f));
                    break;
                case 'g':
                    quad.setColor(i+4*axis, new Color3f(0f, 0.62f, 0.38f));
                    break;
                case 'y':
                    quad.setColor(i+4*axis, new Color3f(1f, 0.83f, 0f));
                    break;
                case 'w':
                    quad.setColor(i+4*axis, new Color3f(1f, 1f, 1f));
                    break;
                default:
                    quad.setColor(i+4*axis, new Color3f(0f, 0f, 0f));
                    break;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Basic Movements">
    public void turnFace(char axis_c, int row, boolean clockwise) {
        /*
        Turn a face at the given row around the given axis
         */
        if (axis_c != 'x' && axis_c != 'y' && axis_c != 'z') {
            return;
        }

        //System.out.println(axis_c + " " + Integer.toString(row) + " " + Boolean.toString(clockwise));
        int axis = (int) axis_c;
        axis -= 120; // int('x') = 120 and we want x = 0, y = 1, z = 2

        switch (axis) {
            case 0: {
                // Rotation around 'x' axis
                if (row < 0 || row >= x) {
                    return;
                }

                Cubi[][] old = new Cubi[y][z];
                for (int i = 0; i < y; i++) {
                    for (int j = 0; j < z; j++) {
                        old[i][j] = new Cubi(cubis[row][i][j].getColors());
                    }
                }
                for (int i = 0; i < y; i++) {
                    for (int j = 0; j < z; j++) {
                        if (clockwise) {
                            cubis[row][i][j] = old[x - j - 1][i];
                        } else {
                            cubis[row][i][j] = old[j][x - i - 1];
                        }
                        swapColor(axis, cubis[row][i][j]);
                    }
                }
                break;
            }
            case 1: {
                // Rotation around 'y' axis
                if (row < 0 || row >= y) {
                    return;
                }

                Cubi[][] old = new Cubi[x][z];
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < z; j++) {
                        old[i][j] = new Cubi(cubis[i][row][j].getColors());
                    }
                }
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < z; j++) {
                        if (clockwise) {
                            cubis[i][row][j] = old[j][y - i - 1];
                        } else {
                            cubis[i][row][j] = old[y - j - 1][i];
                        }
                        swapColor(axis, cubis[i][row][j]);
                    }
                }
                break;
            }
            default: {
                // Rotation around 'z' axis
                if (row < 0 || row >= z) {
                    return;
                }

                Cubi[][] old = new Cubi[x][y];
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < y; j++) {
                        old[i][j] = new Cubi(cubis[i][j][row].getColors());
                    }
                }
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < y; j++) {
                        if (clockwise) {
                            cubis[i][j][row] = old[z - j - 1][i];
                        } else {
                            cubis[i][j][row] = old[j][z - i - 1];
                        }
                        swapColor(axis, cubis[i][j][row]);
                    }
                }
                break;
            }
        }
        if (graphic) draw();
    }

    public void halfTurn(char axis_c, int row) {
        turnFace(axis_c, row, true);
        turnFace(axis_c, row, true);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Special Movements">
    public void L() {
        turnFace('x', 0, true);
    }

    public void LP() {
        turnFace('x', 0, false);
    }

    public void L2() {
        halfTurn('x', 0);
    }
    

    public void R() {
        turnFace('x', x - 1, true);
    }

    public void RP() {
        turnFace('x', x - 1, false);
    }

    public void R2() {
        halfTurn('x', x - 1);
    }
    
    
    public void B() {
        turnFace('z', 0, true);
    }

    public void BP() {
        turnFace('z', 0, false);
    }

    public void B2() {
        halfTurn('z', 0);
    }
    

    public void F() {
        turnFace('z', y - 1, true);
    }

    public void FP() {
        turnFace('z', y - 1, false);
    }

    public void F2() {
        halfTurn('z', y - 1);
    }
    

    public void D() {
        turnFace('y', 0, true);
    }

    public void DP() {
        turnFace('y', 0, false);
    }

    public void D2() {
        halfTurn('y', 0);
    }
    

    public void U() {
        turnFace('y', z - 1, true);
    }

    public void UP() {
        turnFace('y', z - 1, false);
    }

    public void U2() {
        halfTurn('y', z - 1);
    }
    

    public void X() {
        for (int i = 0; i < x; i++) {
            turnFace('x', i, true);
        }
    }

    public void XP() {
        for (int i = 0; i < x; i++) {
            turnFace('x', i, false);
        }
    }

    public void X2() {
        for (int i = 0; i < x; i++) {
            halfTurn('x', i);
        }
    }
    

    public void Y() {
        for (int i = 0; i < y; i++) {
            turnFace('y', i, true);
        }
    }

    public void YP() {
        for (int i = 0; i < y; i++) {
            turnFace('y', i, false);
        }
    }

    public void Y2() {
        for (int i = 0; i < y; i++) {
            halfTurn('y', i);
        }
    }
    

    public void Z() {
        for (int i = 0; i < z; i++) {
            turnFace('z', i, true);
        }
    }

    public void ZP() {
        for (int i = 0; i < z; i++) {
            turnFace('z', i, false);
        }
    }

    public void Z2() {
        for (int i = 0; i < z; i++) {
            halfTurn('z', i);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="WindowListener Methods">
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Other Methods">
    public int getAxisX() { return x; }
    public int getAxisY() { return y; }
    public int getAxisZ() { return z; }
    
    private boolean isEdge(int i, int j, int k) {
        if (i % (x - 1) == 0 && j % (y - 1) == 0) {
            return true;
        }
        if (j % (y - 1) == 0 && k % (z - 1) == 0) {
            return true;
        }
        return i % (x - 1) == 0 && k % (z - 1) == 0;
    }

    private boolean isFace(int i, int j, int k) {
        if (i % (x - 1) == 0 && j % (y - 1) != 0 && k % (z - 1) != 0) {
            return true;
        }
        if (i % (x - 1) != 0 && j % (y - 1) == 0 && k % (z - 1) != 0) {
            return true;
        }
        return i % (x - 1) != 0 && j % (y - 1) != 0 && k % (z - 1) == 0;
    }

    private void swapColor(int axis, Cubi cubi) {
        /*
        Swap the colors of a cubi
         */

        if (cubi.getColors() == null) {
            return;
        }
        int a, b;
        switch (axis) {
            case 0:
                a = 1;
                b = 2;
                break;
            case 1:
                a = 0;
                b = 2;
                break;
            default:
                a = 0;
                b = 1;
                break;
        }

        char[] cubi_colors = cubi.getColors();
        char tmp = cubi_colors[a];
        cubi_colors[a] = cubi_colors[b];
        cubi_colors[b] = tmp;
        cubi.setColors(cubi_colors);
    }

    private boolean faceSolved(int axis) {
        /*
        Return 'true' if the face along with the axis is solved
         */

        boolean otherFace = axis >= 3;
        if (axis >= 3) {
            axis -= 3;
        }
        int row;
        char faceColor;

        switch (axis) {
            case 0:
                if (otherFace) {
                    row = x - 1;
                } else {
                    row = 0;
                }
                faceColor = cubis[row][0][0].getColors()[axis];
                for (int i = 0; i < y; i++) {
                    for (int j = 1; j < z; j++) {
                        if (faceColor != cubis[row][i][j].getColors()[axis]) {
                            return false;
                        }
                    }
                }
                break;
            case 1:
                if (otherFace) {
                    row = y - 1;
                } else {
                    row = 0;
                }
                faceColor = cubis[0][row][0].getColors()[axis];
                for (int i = 0; i < x; i++) {
                    for (int j = 1; j < z; j++) {
                        if (faceColor != cubis[i][row][j].getColors()[axis]) {
                            return false;
                        }
                    }
                }
                break;
            default:
                if (otherFace) {
                    row = z - 1;
                } else {
                    row = 0;
                }
                faceColor = cubis[0][0][row].getColors()[axis];
                for (int i = 0; i < x; i++) {
                    for (int j = 1; j < y; j++) {
                        if (faceColor != cubis[i][j][row].getColors()[axis]) {
                            return false;
                        }
                    }
                }
                break;
        }

        return true;
    }
    
    public Cubi[][][] getCubis() { return cubis; }
    //</editor-fold>
}
