package IA.SolvingCube;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import Cube.Cuboid;

public class testIA {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Side of the cube : ");
        while (!sc.hasNextInt()) {
            System.out.println("The side must be an integer");
            System.out.print("Side of the cube : ");
            sc.next();
        }
        int n = sc.nextInt();
        
        Cuboid myCube = new Cuboid(n, true);
        
        if ((myCube.isGraphic() || args.length != 1) || !"-c".equals(args[0])) {
            myCube.setup();
        }

        //<editor-fold defaultstate="collapsed" desc="Input Loop">
        while (true) {
            System.out.print("Command : ");
            switch (sc.next()) {
                //<editor-fold defaultstate="collapsed" desc="Movements">
                case "move":
                    char axis = '\0';
                    while (axis != 'x' && axis != 'y' && axis != 'z') {
                        System.out.print("Axis (x, y or z) : ");
                        axis = sc.next().charAt(0);
                    }

                    int row = -1;
                    while (row <= 0 || row > myCube.maxSide) {
                        System.out.print("Row : ");
                        row = sc.nextInt();
                    }

                    String clockwize = "";
                    while (!Objects.equals(clockwize, "yes") && !Objects.equals(clockwize, "no")) {
                        System.out.print("Turn clockwize (yes/no) ? ");
                        clockwize = sc.next();
                    }

                    myCube.turnFace(axis, row-1, Objects.equals(clockwize, "yes"));
                    break;
                case "u":
                    myCube.U();
                    break;
                case "u'":
                    myCube.UP();
                    break;
                case "u2":
                    myCube.U2();
                    break;
                case "d":
                    myCube.D();
                    break;
                case "d'":
                    myCube.DP();
                    break;
                case "d2":
                    myCube.D2();
                    break;
                case "f":
                    myCube.F();
                    break;
                case "f'":
                    myCube.FP();
                    break;
                case "f2":
                    myCube.F2();
                    break;
                case "b":
                    myCube.B();
                    break;
                case "b'":
                    myCube.BP();
                    break;
                case "b2":
                    myCube.B2();
                    break;
                case "l":
                    myCube.L();
                    break;
                case "l'":
                    myCube.LP();
                    break;
                case "l2":
                    myCube.L2();
                    break;
                case "r":
                    myCube.R();
                    break;
                case "r'":
                    myCube.RP();
                    break;
                case "r2":
                    myCube.R2();
                    break;
                case "x":
                    myCube.X();
                    break;
                case "x'":
                    myCube.XP();
                    break;
                case "x2":
                    myCube.X2();
                    break;
                case "y":
                    myCube.Y();
                    break;
                case "y'":
                    myCube.YP();
                    break;
                case "y2":
                    myCube.Y2();
                    break;
                case "z":
                    myCube.Z();
                    break;
                case "z'":
                    myCube.ZP();
                    break;
                case "z2":
                    myCube.Z2();
                    break;
                //</editor-fold>
                
                case "scramble":
                    myCube.scramble(100, false);
                    break;
                case "scrambleV":
                    myCube.scramble(10, true);
                    break;
                case "reset":
                    myCube = new Cuboid(n, true);
                    myCube.setup();
                    break;
                case "isSolved":
                    System.out.println(myCube.isSolved());
                    break;
                case "print":
                    System.out.println(myCube);
                    break;
                case "solveRandom":
                    int step = 0;
                    Random randint = new Random();
                    int axisRandom;
                    int rowRandom;
                    char axis_c;
                    long startTime = System.currentTimeMillis();
                    
                    while(!myCube.isSolved()) {
                        axisRandom = randint.nextInt(3);
                        axis_c = (char) (axisRandom + 120);
                        rowRandom = randint.nextInt(myCube.maxSide);

                        myCube.turnFace(axis_c, rowRandom, randint.nextInt(2) == 0);
                        step++;
                    }
                    
                    long stopTime = System.currentTimeMillis();
                    
                    System.out.println("Solved in " + Integer.toString(step) + " steps");
                    long elapsedTime = (stopTime-startTime)/1000;
                    System.out.println("Elapsed time : "
                            + elapsedTime/3600 + " h "
                            + (elapsedTime%3600)/60 + " m "
                            + elapsedTime%60 + " s");
                    break;
                case "solve":
                    IASolvingCube ia = new IASolvingCube(myCube);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Command");
                    break;
            }
        }
        //</editor-fold>
    }
}