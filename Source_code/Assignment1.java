package cs533.assignment1;

import java.util.Scanner;

public class Assignment1
{
    public static final String USAGE = Assignment1Properties.EOL
            + "Usage: cs533.assignment1.Assignment1 [-n count]"
            + " [-a show_answers] quiz_file"
            + Assignment1Properties.EOL
            + Assignment1Properties.EOL + "\t" 
            + "count = number of questions to be asked"
            + Assignment1Properties.EOL + "\t"
            + "show_answers = (y|n|yes|no) show answers to incorrect choices"
            + Assignment1Properties.EOL;

    private boolean showAnswers = Assignment1Properties.SHOW_ANSWERS;
    
    private int     qCount      = Assignment1Properties.DEFAULT_Q_COUNT;

    private String  qFilename   = null;

    /**
     * Constructor, main class for Assignment1. Parses command line options
     * and  starts a QuizController
     * or exits if the command line arguments were invalid.
     * Parsing will fail if the quiz filename is not the last argument.
     * @param args Command line arguments
     */
    public Assignment1( String [] args )
    {
    	if( !parseArguments( args ) )
        {
            System.out.println( USAGE );
            return;
        }
        
    	
        QuizController quizController =
              new QuizController( qCount, qFilename, showAnswers );
        quizController.quiz();
    }

    /**
     * Parses the command line arguments. Any unexpected or invalid
     * argument will cause the parsing to fail.
     * @param args Command line arguments
     * @return whether or not the command line arguments were valid
     * and successfully parsed.
     */
     
    private boolean parseArguments( String [] args )
    {
    	if(args.length==2)
    	{
    		
    			if(!args[0].matches("\\d+"))
    				return false;
    		
    	    qCount= Integer.parseInt(args[0]);
    		qFilename= args[1];
    	}
    
	return true;
       
    }

    /**
     * The main method for invoking Assignment1.
     * @param args Command line arguments
     */
    public static void main( String [] args )
    {
        try
        {
            Assignment1 assignment1 = new Assignment1( args );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}

