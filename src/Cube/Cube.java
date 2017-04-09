package Cube;

import java.util.Arrays;

public interface Cube {
    
    class Cubi {
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
    
    void setup(); // Setup the windows
    void draw(); // Draw the cube every frame
    void scramble(int step, boolean verbose); // Scramble the cube
    int solve(boolean changeCube); // Solve the cube and return the number of step
    boolean isSolved();
    boolean isGraphic();
}
