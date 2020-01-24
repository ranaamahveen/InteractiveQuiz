package cs533.assignment1;

import java.util.Vector;

public class QuestionIndex
{
    private long    qStart;
    private long    qEnd;

    private Vector  answers;

    /**
     * Constructor
     * @param qStart the beginning offset of the quiz question
     * @param qEnd the ending offset of the quiz question
     */
    public QuestionIndex( long qStart, long qEnd )
    {
        this.qStart = qStart;
        this.qEnd   = qEnd;
    }

    /**
     * @return the beginning offset of the quiz question
     */
    public long getQuestionStart()
    {
        return qStart;
    }

    /**
     * @return the ending offset of the quiz question
     */
    public long getQuestionEnd()
    {
        return qEnd;
    }

    /**
     * @return A Vector containing the offsets for the answer choices to the quiz question.
The first
     * element is the correct answer.
     */
    public Vector getAnswerVector()
    {
        return answers;
    }

    /**
     * Adds the offset to an answer choice to the quiz question.
     * @param answerOffset an offset to an answer choice to the quiz question
     */
    public void addAnswerOffset( long answerOffset )
    {
        if( answers == null )
        {
            answers = new Vector();
        }

        answers.addElement( new Long( answerOffset ) );
    }
}
