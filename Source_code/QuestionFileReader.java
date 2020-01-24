package cs533.assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class QuestionFileReader
{
    private File    qFile;
    private Vector  qFileIndex;

    private Random  random;
    Set q= new HashSet();
    /**
     * Constructor
     * @param qFilename The name of the quiz file containing quiz questions and answers
     * @param qFileIndex A vector of QuestionIndex, containing the offsets to quiz
questions and answer choices.
     */
    public QuestionFileReader( String qFilename, Vector qFileIndex )
    {
        this( new File( qFilename ), qFileIndex );
    }

    /**
     * Constructor
     * @param qFile A File object containing quiz questions and answers
     * @param qFileIndex A vector of QuestionIndex, containing the offsets to quiz
questions and answer choices.
     */
    public QuestionFileReader( File qFile, Vector qFileIndex )
    {
        this.qFile      = qFile;
        this.qFileIndex = qFileIndex;

        random = new Random();
        
    }

    /**
     * Randomly selects a QuestionIndex from the vector of indexes and reads from the
     * question and answer choices from the quiz file using a RandomAccessFile.
     * @return a random QuestionRecord object or null if there are no more questions
     */
    public QuestionRecord getQuestionRecord()
        throws Exception
    {
    	BufferedReader br = new BufferedReader(new FileReader(qFile));
    	String readLine = "";
    	int count = 0;
    	readLine = br.readLine();
    	while(readLine != null) {
    		if(readLine.startsWith("@Q"))
    		count++;
    		readLine = br.readLine();
    	}
    	int q_number = random.nextInt(count);
    	while(q.contains(q_number))
    	{
    		q_number= random.nextInt(count);
    	}
    	q.add(q_number);
    	
    	QuestionIndex qIndex = (QuestionIndex) qFileIndex.get(q_number);
    	Vector options= qIndex.getAnswerVector();
    
    	RandomAccessFile raf = new RandomAccessFile(qFile, "r");
    	
    	String[] choices =  new String[options.size()-1];
    	raf.seek((long) options.get(0));
    	int answerNumber = Integer.parseInt(raf.readLine());
    	
    	for(int i=1; i<options.size(); i++)
    	{
    	raf.seek((long) options.get(i));
    	choices[i-1] = raf.readLine();
    	}
    
    	
    	long start = qIndex.getQuestionStart();
    	long end = qIndex.getQuestionEnd();
    	
    	raf.seek(start);
    	String a= "";
    	while(start<end-8)
    	{
           String  s= raf.readLine();
            int n = s.getBytes().length;
            a = a+s+"\n";
            start = start + n ;
    	}
    	
    	String question =a;
    	raf.close();
    	
        return new QuestionRecord( question, choices, answerNumber );
    }
}
