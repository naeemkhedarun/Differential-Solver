package org.joone.engine.learning;

import org.joone.log.*;
import org.joone.engine.*;
import org.joone.io.*;
import org.joone.net.NetCheck;

import java.io.IOException;
import java.util.TreeSet;
import java.util.ArrayList;


/**
 * Final element of a neural network; it permits to calculate
 * both the error of the last training cycle and the vector
 * containing the error pattern to apply to the net to
 * calculate the backprop algorithm.
 */
public class TeacherSynapse extends AbstractTeacherSynapse {
    
    /**
     * Logger
     **/
    protected static final ILogger log = LoggerFactory.getLogger(TeacherSynapse.class);
    
    /** The error being calculated for the current epoch. */
    protected transient double GlobalError = 0;
    
    private static final long serialVersionUID = -1301682557631180066L;
    private double epsilon = 1.0;

    public TeacherSynapse() {
        super();
    }

    private ArrayList<Double> outputs = new ArrayList<Double>();


    protected double calculateError(double aDesired, double anOutput, int anIndex) {

//        double myError = aDesired - anOutput;
//

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

//        System.out.println("Hmm:" +  (((double)outputs.size() + ((double)outputs.size() - 1)) / 2) + ", Desired: " + aDesired + ", Output: " + anOutput + ", outputError:" + output);

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
            // reset error
            GlobalError = 0;
        }

        if(pattern.getCount() == 10){
            outputs.clear();
//            System.out.println("Outputs Cleared.");
        }

    }
    
}

           //Evaluate output
//            double deltaY = anOutput - outputs.get(outputs.size()-1);
//            deltaY -= (2.0 * outputs.size());
//            myError = 0 - deltaY;