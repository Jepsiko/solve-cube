package IA;

import java.util.Random;

public class Neuron {
    
    static float eta = 0.15f;
    static float alpha = 0.5f;
    
    double outputVal;
    Connection[] outputWeights;
    int index;
    double gradiant;
    
    class Connection {
        public double weight;
        public double deltaWeight;
        
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
    
    double sumDOW(Neuron[] nextLayer) {
        double sum = 0;
        
        for (int i = 0; i < nextLayer.length-1; i++) {
            sum += outputWeights[i].weight * nextLayer[i].gradiant;
        }
        
        return sum;
    }
    
    static double transferFunction(double x, boolean derive) {
        if (derive) { return 1/((1+Math.abs(x))*(1+Math.abs(x))); }
        else return x/(1+Math.abs(x));
    }
}
