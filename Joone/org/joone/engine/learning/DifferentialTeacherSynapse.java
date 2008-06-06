package org.joone.engine.learning;

import org.joone.log.*;
import org.joone.engine.*;
import org.joone.engine.learning.AbstractTeacherSynapse;

import java.util.ArrayList;


/**
 * Final element of a neural network; it permits to calculate
 * both the error of the last training cycle and the vector
 * containing the error pattern to apply to the net to
 * calculate the backprop algorithm.
 */
public class DifferentialTeacherSynapse extends AbstractTeacherSynapse {

       /**
     * Logger
     **/
    protected static final ILogger log = LoggerFactory.getLogger(DifferentialTeacherSynapse.class);

    /** The error being calculated for the current epoch. */
    protected transient double GlobalError = 0;

    private static final long serialVersionUID = -1301682557631180066L;
    
    public DifferentialTeacherSynapse() {
        super();
    }

    private ArrayList<Double> outputs = new ArrayList<Double>();


    protected double calculateError(double aDesired, double anOutput, int anIndex) {

        double myError;
        if(outputs.size() == 0){
           double deltaY = anOutput;
            deltaY -= (2.0 * (0.5));
            myError = 0 - deltaY;
        }else{
            //Evaluate output
            double deltaY = anOutput - outputs.get(outputs.size()-1);
            deltaY -= (2.0 * ((((double)outputs.size() + ((double)outputs.size() - 1.0)) / 2.0)+1.0));
            myError = 0 - deltaY;
        }

        double output = (myError * myError) / 2.0;

        GlobalError += output;
        outputs.add(anOutput);

        return myError;
    }

    protected double calculateGlobalError() {
        double myError = GlobalError / getMonitor().getNumOfPatterns();
        if(getMonitor().isUseRMSE()) {
            myError = Math.sqrt(myError);
        }
        GlobalError = 0;
        return myError;
    }


    public void fwdPut(Pattern pattern) {
        super.fwdPut(pattern);

        if (pattern.getCount() == -1) {
            GlobalError = 0;
        }

        if(pattern.getCount() == getMonitor().getNumOfPatterns()){
            outputs.clear();
        }

    }

}
