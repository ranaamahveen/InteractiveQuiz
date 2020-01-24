package cs533.assignment1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Assignment1Properties
{
    public static final String AUTHOR               = "CS533 student";
    public static final String VERSION              = "Version 1.0";
    public static final String DATE                 = "February 4, 2004";

    public static final String COMMENT              = "*";

    public static final String Q_START              = "@Q";
    public static final String A_START              = "@A";
    public static final String A_END                = "@E";

    public static final String MAC_EOL              = "\r";
    public static final String UNIX_EOL             = "\n";
    public static final String WINDOWS_EOL          = "\r\n";

    public static final int MAX_QUESTIONS           = 1000;
    public static final int MAX_QUESTION_LINES      = 10;
    public static final int MAX_CHARS_PER_LINE      = 75;

    public static final int DEFAULT_Q_COUNT       = 10;

    public static final boolean SHOW_ANSWERS        = true;

    public static String EOL                        = WINDOWS_EOL;

    public static String ASS1_DIR                   = "/home/somebody"
                                                    + File.separator + "cs533"
                                                    + File.separator + "assignment1";

    public static String DEBUG_FILE                 = ASS1_DIR + File.separator +
"debug.txt";

    public static String DEFAULT_Q_FILE             = ASS1_DIR
                                                    + File.separator +
"question_files"
                                                    + File.separator +
"student_questions.txt";
}