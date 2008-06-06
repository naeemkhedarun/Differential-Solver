import com.panayotis.gnuplot.GNUPlotParameters;
import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by IntelliJ IDEA.
 * User: father
 * Date: 27-May-2008
 * Time: 02:08:43
 */
public class RunNetwork {
    private ArrayList<NeuralNet> neuralNet;

    double[][] desiredArray;
    double[][] inputArray;


    public RunNetwork() {

        long startTime = Calendar.getInstance().getTimeInMillis();

        int size = 10;
        neuralNet = new ArrayList<NeuralNet>();

        SetupInput(size);

        neuralNet.add(JooneTools.create_standard(new int[]{1, 30, 1}, JooneTools.LINEAR));
        neuralNet.get(0).getMonitor().setLearningRate(0.01);
        neuralNet.get(0).getMonitor().setMomentum(0.01);

        neuralNet.add(JooneTools.create_standard(new int[]{1, 30, 1}, JooneTools.LINEAR));
        neuralNet.get(1).getMonitor().setLearningRate(0.01);
        neuralNet.get(1).getMonitor().setMomentum(0.01);

        neuralNet.add(JooneTools.create_standard(new int[]{1, 30, 1}, JooneTools.LINEAR));
        neuralNet.get(2).getMonitor().setLearningRate(0.01);
        neuralNet.get(2).getMonitor().setMomentum(0.01);

        neuralNet.add(JooneTools.create_standard(new int[]{1, 30, 1}, JooneTools.LINEAR));
        neuralNet.get(3).getMonitor().setLearningRate(0.01);
        neuralNet.get(3).getMonitor().setMomentum(0.01);

        for (NeuralNet net : neuralNet) {
            org.joone.helpers.factory.JooneTools.train(net, inputArray, desiredArray,
                    500000,       // Max epochs
                    0.0001,       // Min RMSE
                    1000,          // Epochs between ouput reports
                    System.out,       // Std Output
                    true       // Asynchronous mode
            );
        }


        WaitForNetworks();

        long endTime = Calendar.getInstance().getTimeInMillis();
        long duration = endTime - startTime;
        duration = duration / 1000;
        System.out.println("Training Time: " + duration);
    }

    private void WaitForNetworks() {


        int noNetworksRunning = neuralNet.size();
        while (noNetworksRunning != 0) {
            noNetworksRunning = neuralNet.size();
            for (NeuralNet net : neuralNet) {
                if (!net.isRunning())
                    noNetworksRunning--;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void PlotOutput(int size) {
        com.panayotis.gnuplot.JavaPlot p = new com.panayotis.gnuplot.JavaPlot();

        double[][] output = new double[size][size];
        for (int i = 0; i < size; i++) {
            output[i] = JooneTools.interrogate(neuralNet.get(0), new double[]{inputArray[i][0]});
        }

        GNUPlotParameters plotParameters = new GNUPlotParameters();
        plotParameters.set("data style lines");

        p.setParameters(plotParameters);
        p.addPlot(output);
        p.plot();
    }

    private void SetupInput(int size) {
        desiredArray = new double[size][1];
        inputArray = new double[size][1];


        for (int i = 1; i < size + 1; i++)
            for (int j = 0; j < 1; j++)
                inputArray[i - 1][j] = ((double) i / (double) size);

        for (int i = 1; i < size + 1; i++)
            for (int j = 0; j < 1; j++)
                desiredArray[i - 1][j] = ((double) (i * i) / (double) (size * size));
    }

    public static void main(String[] args) {

        new RunNetwork();
    }


    public static double[][] JoinVectors(double[] yAxis) {
        double[][] plot = new double[yAxis.length][yAxis.length];

        for (int y = 0; y < yAxis.length; y++) {

            plot[y][y] = yAxis[y];

        }

        return plot;
    }

}
