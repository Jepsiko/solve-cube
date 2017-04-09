package IA.SolvingCube;

import Cube.Cuboid;
import IA.NeuralNetwork;

public class IASolvingCube {
    NeuralNetwork brain;
    
    public IASolvingCube(Cuboid cube) {
        int[] topology = {cube.getAxisX()*cube.getAxisY()*cube.getAxisZ(), 20, 20, 3};
        brain = new NeuralNetwork(topology);
    }
}
