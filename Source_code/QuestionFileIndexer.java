package cs533.assignment1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.Vector;

public class QuestionFileIndexer
{
    private File    qFile;
    private Vector  qFileIndex;

    /**
     * Constructor
     * @param qFilename The name of the quiz file containing quiz questions
     * and answers
     */
    public QuestionFileIndexer( String qFilename )
    {
        this( new File( qFilename ) );
    }

    /**
     * Constructor
     * @param qFile A File object containing quiz questions and answers
     */
    public QuestionFileIndexer( File qFile )
    {
        this.qFile= qFile;
    }

    /**
     * Indexes the quiz file. Places the offsets of questions and
     * answer choices into QuestionIndex objects
     * @return whether or not the quiz file was successfully indexed.
     */
    public boolean indexFile()
    {
        try
        {
            RandomAccessFile raf = new RandomAccessFile( qFile, "r" );

            long fp                 = 0;
            String line             = null;

            long qStart             = 0;
            long qEnd               = 0;
            StringBuffer qBuffer    = null;

            QuestionIndex qIndex  = null;

            int status = 0; // 0 = default, 1 = question, 2 = answer

            while( ( line = raf.readLine() ) != null )
            {

                if( !( line.startsWith( Assignment1Properties.COMMENT ) )
                 && !( line.trim().length() == 0 ) )
                {
                    if( status == 0 )
                    {
                        // expecting a question

                        if( line.startsWith( Assignment1Properties.Q_START ) )
                        {
                            // question start
                            status = 1;

                            qStart = raf.getFilePointer();
                            qBuffer= new StringBuffer();
                        }
                        else
                        {
                            System.err.println
                            ("QuestionFileReader: unexpected input: " + line );
                        }
                    }
                    else if( status == 1 )
                    {

                        // reading question, checking for answer start

                        if( line.startsWith( Assignment1Properties.A_START ) )
                        {
                            // question end, answer start
                            status = 2;

                            qIndex = new QuestionIndex( qStart, qEnd );
                        }
                        else
                        {
                            // keep track of end of last part of question
                            qEnd = raf.getFilePointer();
                        }
                    }
                    else
                    {
                        //reading answer, checking for answer end
                        if( line.startsWith( Assignment1Properties.A_END ) )
                        {
                            // answer end
                            status = 0;

                            if( qFileIndex == null )
                            {
                                qFileIndex = new Vector();
                            }

                            qFileIndex.addElement( qIndex );
                        }
                        else
                        {
                            // add answer offset
                            qIndex.addAnswerOffset( fp );
                        }
                    }
                }

                fp = raf.getFilePointer();
            }

            raf.close();

            if( qFileIndex == null || qFileIndex.size() < 1 )
            {
                return false;
            }

            return true;
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @return a vector of QuestionIndex.
     */
    public Vector getQuestionFileIndex()
    {
        return qFileIndex;
    }

 }

