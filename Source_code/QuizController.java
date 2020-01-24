package cs533.assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Vector;

public class QuizController
{
    public static final String INTRO_MESSAGE =
            "CS 533 Assignment 1 - Quiz Program"
            + Assignment1Properties.EOL
            + Assignment1Properties.AUTHOR
            + Assignment1Properties.EOL
            + Assignment1Properties.VERSION
            + Assignment1Properties.EOL
            + Assignment1Properties.DATE;

    private boolean showAnswers = Assignment1Properties.SHOW_ANSWERS;
    private int     qCount      = Assignment1Properties.DEFAULT_Q_COUNT;
    private File    qFile;

    private int     asked       = 0;
    private int     correct     = 0;
    private long    startTime   = 0;

    private QuestionFileReader  qFileReader;

    /**
     * Constructor
     * @param qCount The number of questions to be asked in the quiz
     * @param qFilename The name of the quiz file containing quiz questions and
answers
     */
    public QuizController( int qCount, String qFilename )
    {
        this( qCount, new File( qFilename ) );
    }

    /**
     * Constructor
     * @param qCount The number of questions to be asked in the quiz
     * @param qFilename The name of a quiz file containing quiz questions
     * and answers
     * @param showAnswers Toggle to determine if answers are shown
     * for incorrect choices
     */
    public QuizController( int qCount, String qFilename, boolean showAnswers )
    {
        this( qCount, new File( qFilename ), showAnswers );
    }

    /**
     * Constructor
     * @param qCount The number of questions to be asked in the quiz
     * @param qFile A File object containing quiz questions and answers
     */
    public QuizController( int qCount, File qFile )
    {
        this( qCount, qFile, Assignment1Properties.SHOW_ANSWERS );
    }

    /**
     * Constructor
     * @param qCount The number of questions to be asked in the quiz
     * @param qFile A File object containing quiz questions and answers
     * @param showAnswers Toggle to determine if answers are shown
     * for incorrect selections
     */
    public QuizController( int qCount, File qFile, boolean showAnswers  )
    {
        this.qCount         = qCount;
        this.qFile          = qFile;
        this.showAnswers    = showAnswers;

        initialize();
    }

    /**
     * Initialized the QuizController by indexing the quiz file and creating a
QuizFileReader.
     *  Exits the program if there is a problem reading or indexing the file.
This normally means that
     *  the quiz file is an invalid format.
     */
    private void initialize()
    {
        if( qCount < 1 )
        {
            exit( "Invalid question count: " + qCount + ". You must ask atleast one question" );
        }

        if( qFile == null )
        {
            exit( "Invalid quiz file" );
        }

        if( !qFile.exists()
         || !qFile.isFile() )
        {
            exit( "Invalid quiz file: " + qFile );
        }

        QuestionFileIndexer qFileIndexer = new QuestionFileIndexer( qFile );

        boolean fileIndexed = qFileIndexer.indexFile();
        if( !fileIndexed )
        {
            exit( "Unable to read quiz file: " + qFile +". It may be in aninvalid format." );
        }

        Vector qFileIndex = qFileIndexer.getQuestionFileIndex();

        if( qCount > qFileIndex.size() )
        {
            exit( "The quiz file contained only " + qFileIndex.size() + "questions." + Assignment1Properties.EOL
                  + "Use a larger quiz file of you want to ask " + qCount + "questions." + Assignment1Properties.EOL );
        
        }

        qFileReader = new QuestionFileReader( qFile, qFileIndex );
    }

    /**
     * Method to start the quiz. The method begins the main quiz control loop.
It will ask <i>qCount</i>
     * questions or will end when there are no more questions available.
     */
    public void quiz()
    {

        QuestionRecord qRecord = null;

        intro();

        startTime = System.currentTimeMillis();

        while( true )
        {
            if( asked >= qCount )
            {
                results();
            }

            try
            {
                qRecord = qFileReader.getQuestionRecord();
            }
            catch( Exception e )
            {
                e.printStackTrace();
                output( "Error reading quiz file. Printing results and exiting"
);
                results();
            }

            if( qRecord == null )
            {
                //no more questions, show results
                results();
            }

            showQuestion( qRecord );
            asked++;

            int answer = getAnswer( qRecord.getChoices().length );
            if( answer == qRecord.getAnswerNumber() )
            {
                correct++;
                output( Assignment1Properties.EOL + "CORRECT!" );
            }
            else
            {
                if( showAnswers )
                {
                    output( Assignment1Properties.EOL + "Incorrect, the correct answer was " + qRecord.getAnswerNumber() + "." );
                }
                else
                {
                    output( Assignment1Properties.EOL + "Incorrect." );
                }
            }
        }
    }

    /**
     * Display an introductory message to standard output.
     */
    private void intro()
    {
        output( Assignment1Properties.EOL + INTRO_MESSAGE );
    }

    /**
     * Displays a questions to standard output.
     * @param qRecord An object containing the current question, choices and
correct answer
     */
    private void showQuestion( QuestionRecord qRecord )
    {
        output( Assignment1Properties.EOL );

        output( "Question " + (asked+1) );
        output( qRecord.getQuestion() );

        String [] choices = qRecord.getChoices();
        for( int i=0; i<choices.length; i++ )
        {
            output( (i+1) + ": " + choices[i] );
        }

        output( Assignment1Properties.EOL );
    }

    /**
     * Reads the user's answer choice from the standard input. Displays error
messages for invalid
     * choices and allows user to exit program.
     * @param the index of the highest answer.
     * @return The user's answer choice
     */
    private int getAnswer( int highest )
    {
        int answer = -1;

        try
        {
            BufferedReader reader = new BufferedReader( new InputStreamReader(
System.in ) );
            String line = null;
            while( true )
            {
                output( "Select your answer (Type 'Q' to exit): " );

                line = reader.readLine();
                line = line.trim().toLowerCase();

                if( line.equals( "q" )
                 || line.equals( "quit" ) )
                {
                    results();
                }

                try
                {
                    answer = Integer.parseInt( line );
                    if( answer > 0 && answer <= highest )
                    {
                        break;
                    }
                }
                catch( NumberFormatException nfe )
                {
                }

                output( "You must enter a number between 1-" + highest + "." );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return answer;
    }

    /**
     * Displays a message to the standard output. Uses println, so each message
ends with a NEWLINE
     * @param message The message to be displayed
     */
    private void output( String message )
    {
        System.out.println( message );
    }

    /**
     * Calculates and displays the results of the quiz.
     */
    private void results()
    {
        output( Assignment1Properties.EOL );

        float percentage = (float) correct / (float) asked;
        percentage = percentage * 100;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits( 1 );
        String formattedPercentage = numberFormat.format( percentage );

        String formattedElapsedTime = null;
        long elapsedTime = System.currentTimeMillis() - startTime;
        if( (elapsedTime / 1000) > 60 )
        {
            long minutes = (elapsedTime / 1000) / 60;
            long seconds = (elapsedTime / 1000) % 60;
            formattedElapsedTime = new String( minutes + " minutes " + seconds
+ " seconds " );
        }
        else
        {
            formattedElapsedTime = new String( (elapsedTime / 1000) + " seconds" );
        }

        output( "Results:" );
        output( "You correctly answered " + correct + " of " + asked + " questions." );
        output( "Percentage: " + formattedPercentage + "%" );
        output( "Elapsed Time: " + formattedElapsedTime + " (" + elapsedTime + " milliseconds)" );

        output( Assignment1Properties.EOL );

        exit( "Quiz Complete." );
    }

    /**
     * Displays and information message to standard output and exits the
program
     * This could also be where any cleanup would occur. Currently no cleanup
is needed.
     * @param exitMessage Information message displayed before exit.
     */
    private void exit( String exitMessage )
    {
        System.out.println( exitMessage + Assignment1Properties.EOL );
        System.out.flush();

        //cleanup??

        System.exit( 0 );
    }
}
   