package IA;

public class NeuralNetwork {
    
    static int recentAverageSmoothingFactor = 100;
    
    Neuron[][] layers;
    double error;
    double recentAverageError;
    
    public NeuralNetwork(int[] topology) {
        recentAverageError = 100;
        
        int nbOutputs;
        layers = new Neuron[topology.length][];
        
        for (int layerNum = 0; layerNum < topology.length; layerNum++) {
            if (layerNum == topology.length-1) { nbOutputs = 0; }
            else { nbOutputs = topology[layerNum+1]; }
            
            layers[layerNum] = new Neuron[topology[layerNum]+1];
            for (int neuronNum = 0; neuronNum <= topology[layerNum]; neuronNum++) {
                layers[layerNum][neuronNum] = new Neuron(nbOutputs, neuronNum);
            }
            
            layers[layerNum][topology[layerNum]].setOutputVal(1);
        }
    }
    
    public void feedForward(double[] inputVals) {
        if (inputVals.length == layers[0].length-1) {
            for (int i = 0; i < inputVals.length; i++) {
                layers[0][i].setOutputVal(inputVals[i]);
            }
            
            for (int layerNum = 1; layerNum < layers.length; layerNum++) {
                Neuron[] prevLayer = layers[layerNum-1];
                for (int n = 0; n < layers[layerNum].length-1; n++) {
                    layers[layerNum][n].feedForward(prevLayer);
                }
            }
        }
    }
    
    public void backProp(double[] targetVals) {
        Neuron[] outputLayer = layers[layers.length-1];
        error = 0;
        
        for (int i = 0; i < outputLayer.length-1; i++) {
            double delta = targetVals[i] - outputLayer[i].getOutputVal();
            error += delta*delta;
        }
        
        error /= outputLayer.length-1;
        error = Math.sqrt(error);
        
        recentAverageError = recentAverageError*recentAverageSmoothingFactor + error;
        recentAverageError /= recentAverageSmoothingFactor+1;
        
        // Calculate output layer gradients
        
        for (int i = 0; i < outputLayer.length-1; i++) {
            outputLayer[i].calcOutputGradients(targetVals[i]);
        }
        
        // Calculate hidden layer gradients
        
        for (int layerNum = layers.length-2; layerNum > 0; layerNum--) {
            Neuron[] hiddenLayer = layers[layerNum];
            Neuron[] nextLayer = layers[layerNum+1];
            
            for (Neuron hiddenNeuron : hiddenLayer) {
                hiddenNeuron.calcHiddenGradients(nextLayer);
            }
        }
        
        // For all layers from outputs to first hidden layer,
        // update connection weights
        
        for (int layerNum = layers.length-1; layerNum > 0; layerNum--) {
            Neuron[] layer = layers[layerNum];
            Neuron[] prevLayer = layers[layerNum-1];
            
            for (int i = 0; i < layer.length-1; i++) {
                layer[i].updateInputWeights(prevLayer);
            }
        }
    }
    
    public double[] getResults() {
        double[] resultVals = new double[layers[layers.length-1].length-1];
        
        for (int i = 0; i < layers[layers.length-1].length-1; i++) {
            resultVals[i] = layers[layers.length-1][i].getOutputVal();
        }
        
        return resultVals;
    }
    
    public double getRecentAverageError() { return recentAverageError; }
}
