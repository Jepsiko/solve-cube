package Cube;

import java.util.Arrays;

abstract public interface Cube {
    
    public class Cubi {
        char[] colors;
        // colors are in this order : [x, y, z [,...]]
        
        Cubi(char[] _colors) {
            colors = _colors;
        }
        
        final char[] getColors() { return colors; }
        void setColors(final char[] _colors) { colors = _colors; }
        @Override
        public String toString() { return Arrays.toString(colors); }
    }
    
    abstract void setup(); // Setup the windows
    abstract void draw(); // Draw the cube every frame
    abstract void scramble(int step, boolean verbose); // Scramble the cube
    abstract int solve(boolean changeCube); // Solve the cube and return the number of step
    abstract boolean isSolved();
    abstract boolean isGraphic();
}
