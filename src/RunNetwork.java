import org.joone.helpers.factory.JooneTools;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.engine.learning.TeacherSynapse;

/**
 * Created by IntelliJ IDEA.
 * User: father
 * Date: 27-May-2008
 * Time: 02:08:43
 */
public class RunNetwork {

    public static void main(String[] args) {

        // Create an MLP network with 3 layers [2,2,1 nodes] with a logistic output layer
        org.joone.net.NeuralNet nnet = org.joone.helpers.factory.JooneTools.create_standard(new int[]{1, 30, 1},
                org.joone.helpers.factory.JooneTools.LINEAR);

        TeachingSynapse teacher = new TeachingSynapse();
        teacher.setTheTeacherSynapse(new TeacherSynapse());
        nnet.setTeacher(teacher);

        nnet.getMonitor().setLearningRate(0.01);
        nnet.getMonitor().setMomentum(0.01);

        int size = 10;

        double[][] desiredArray = new double[size][1];

        double[][] inputArray = new double[size][1];
        for (int i = 1; i < size + 1; i++)
            for (int j = 0; j < 1; j++)
                inputArray[i - 1][j] = ((double) i / (double) size);

        for (int i = 1; i < size + 1; i++)
            for (int j = 0; j < 1; j++)
                desiredArray[i - 1][j] = ((double) (i * i) / (double) (size * size));

        // Train the network for 5000 epochs, or until the rmse < 0.01
        //        double rmse = JooneTools.train(nnet, desiredArray, inputArray,
        org.joone.helpers.factory.JooneTools.train(nnet, inputArray, desiredArray,
                500000,       // Max epochs
                0.0001,       // Min RMSE
                1000,          // Epochs between ouput reports
                System.out,       // Std Output
                true       // Asynchronous mode
        );

        // Interrogate the network
        while (nnet.isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < size; i++) {
            System.out.print((desiredArray[i][0] * (double)(size*size) + " : "));
            double[] output = JooneTools.interrogate(nnet, new double[]{inputArray[i][0]});
            System.out.println(output[0]);// * (double) (size * size));
        }

    }

}
