package IA;

import java.util.Random;

class Neuron {
    private static float eta = 0.15f;
    private static float alpha = 0.5f;

    private double outputVal;
    private Connection[] outputWeights;
    private int index;
    private double gradiant;
    
    class Connection {
        double weight;
        double deltaWeight;
        
        Connection() {
            Random rand = new Random();
            weight = rand.nextDouble();
            deltaWeight = 0;
        }
    }
    
    Neuron(int nbOutputs, int _index) {
        index = _index;
        
        outputWeights = new Connection[nbOutputs];
        for (int i = 0; i < nbOutputs; i++) {
            outputWeights[i] = new Connection();
        }
    }
    
    void setOutputVal(double _outputVal) { outputVal = _outputVal; }
    double getOutputVal() { return outputVal; }
    
    void feedForward(Neuron[] prevLayer) {
        double sum = 0;
        
        for (Neuron prevNeuron : prevLayer) {
            sum += prevNeuron.getOutputVal() * prevNeuron.outputWeights[index].weight;
        }
        
        outputVal = transferFunction(sum, false);
    }
    
    void calcOutputGradients(double targetVal) {
        double delta = targetVal-outputVal;
        gradiant = delta*transferFunction(outputVal, true);
    }
    
    void calcHiddenGradients(Neuron[] nextLayer) {
        double dow = sumDOW(nextLayer);
        gradiant = dow*transferFunction(outputVal, true);
    }
    
    void updateInputWeights(Neuron[] prevLayer) {
        for (Neuron neuron : prevLayer) {
            double oldDeltaWeight = neuron.outputWeights[index].deltaWeight;
            double newDeltaWeight = eta*neuron.getOutputVal()*gradiant + alpha*oldDeltaWeight;
            
            neuron.outputWeights[index].deltaWeight = newDeltaWeight;
            neuron.outputWeights[index].weight += newDeltaWeight;
        }
    }
    
    private double sumDOW(Neuron[] nextLayer) {
        double sum = 0;
        
        for (int i = 0; i < nextLayer.length-1; i++) {
            sum += outputWeights[i].weight * nextLayer[i].gradiant;
        }
        
        return sum;
    }
    
    private static double transferFunction(double x, boolean derive) {
        if (derive) { return 1/((1+Math.abs(x))*(1+Math.abs(x))); }
        else return x/(1+Math.abs(x));
    }
}
