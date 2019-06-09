package Kefel;
import java.awt.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
// speech
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
//swing
import javax.swing.*;
import java.awt.event.*;
// messages
import javax.swing.JOptionPane;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.application.Platform;

import java.awt.image.*;
import javax.swing.ImageIcon.*;

//-----------------------------------------------------------------------------
// פרויקט לוח הכפל של הדרי
// Speeching using FreeTTS

// Download FreeTTS from here: https://sourceforge.net/projects/freetts/files/
// More information here: https://freetts.sourceforge.io/
// You need to add 2 libraries to make the speech works:
//  \freetts-1.2.2-bin\freetts-1.2\lib\cmu_us_kal.jar
//  \freetts-1.2.2-bin\freetts-1.2\lib\freetts.jar
//-----------------------------------------------------------------------------
public class Main {

	private static final int MAXTESTS = 10; // מקסימום מבחנים
	private static final int MAXQUESTIONS = 10; // מקסימום שאלות למבחן
            ; // מקסמום תרגילים למבחן
	private static final int MAXTRIES = 2;  // מקסימום ניסיונות להשיב נכון
	private static final int SHOWTABLE = 0; // האם להציג את טבלת לוח הכפל: 1 כן; 2 לא
	// מספר שמאלי
	private static final int MINNUMBER1 = 2; // מספר אקראי מינימלי בתרגיל
	private static final int MAXNUMBER1 = 9; // מספר אקראי מקסימלי בתרגיל
	// מספר ימני
	private static final int MINNUMBER2 = 2; // מספר אקראי מינימלי בתרגיל
	private static final int MAXNUMBER2 = 9; // מספר אקראי מקסימלי בתרגיל

	private static final int SHUFFLE = 5;   // מספר הפעמים לערבב תרגילים: [1..] כן;0 לא לערבב

    private static final int TIMETOANSWERFORREPORT = 5; // תרגילים שלקחו יותר ממספר השניות שצוין יוספו לדוח סיכום לשיפור
    private static final int TIMETOANSWERFORGOODFEEDBACK = 3; // זמן לפתרון תרגיל לשם ציון בהודעת עידוד מיד עם קבלת תשובה

	private static final int WITHSPEECH = 0; // 1 - עם דיבור ; 0 - בלי דיבור

	private static final String VOICENAME_kevin = "kevin"; // Speech type

    private static final boolean CONSOLEVISIBLE = false; // האם gui ייראה או יוסתר

    private static final Color BGFRAMECOLOR = Color.PINK; // צבע רקע למסך gui

    private static JFrame frame;

    private static JLabel labelTitle;
    private static JLabel labelHeader;
    private static JLabel labelTargil;
    private static JLabel labelRemark1;
    private static JLabel labelRemark2;
    private static JLabel labelbottom;
    private static JLabel labelSummary;

    private static JButton button;

    private static JTextField textFieldAnswer;
    private static JTextField textFieldLog;
    private static Font font1,font2,font3;
    private static Dimension preferredSize;

	// צובר תרגילים כללי - כל סיבוב יעורבב בתוכו ויתווסף
	private static ArrayList<Integer[]> targil = new ArrayList<Integer[]>();

	// צובר תרגילים זמני - בסיום כל סיבוב כל האפשרויות - יתאפס לקבלת סדרת תרגילים חדשה
	private static ArrayList<Integer[]> targilTemp = new ArrayList<Integer[]>();

    private static Scanner console = new Scanner(System.in);

    // משחק הכפולות
    private static int k = 0, // משחק הכפולות - מונה 10 תרגילים
                num1 = 0,num2 = 0, // קבלת שני מספרים אקראיים למכפלה
                input = 0, // משחק הכפולות - קבלת קלט מהמשתמש
                grade = 0, // צובר ציון ברמת מבחן אחד
                g = 0, // מונה 10 מבחנים
                gradeAll = 0, //  צובר ציון לכל המבחנים שנעשו
                i = 0, // מונה מחזוריות הזדמנויות להשיב ולתקן תשובה לא נכונה
                secondsDiff = 0, // הפרש שניות עד מתן תשובה כלשהי או תשובה נכונה
				secondsTest = 0,  // משך הזמן המצטבר שלקח להשיב על מבחן אחד
                secondsTestAll = 0; // משך הזמן המצטבר שלקח להשיב על כל המבחנים

    private static Date now1 = null; /*now2 = null;*/		// מדידת זמן בשניות לתשובה - לפני פתרון ועד הפיתרון
    //AnswerDetails answerDetails = new AnswerDetails();
    // אתחול אינדיקציה לבדוק אם היתה תשובה לא נכונה
    private static boolean falseAnswer = false;

    // constructor
	Main(){
	    // GUI designing
	    String iconPath = "./src/Kefel/hadar_slime.jpg";
		ImageIcon picIcon = new ImageIcon(iconPath);
		frame=new JFrame("משחקי המספרים של הדר");//creating instance of JFrame

        Image icon = new javax.swing.ImageIcon(iconPath).getImage();
        frame.setIconImage(icon);
        //frame.setIconImage(ImageIcon.getImage());
		labelTargil=new JLabel();

        labelTitle = new JLabel("",SwingConstants.RIGHT);
        labelHeader = new JLabel("",SwingConstants.RIGHT);
        labelRemark1 = new JLabel("",SwingConstants.RIGHT);
        labelRemark2 = new JLabel("",SwingConstants.RIGHT);
        labelbottom = new JLabel("",SwingConstants.RIGHT);
        labelSummary = new JLabel("",SwingConstants.RIGHT);

        textFieldAnswer = new JTextField();

        font1 = new Font("SansSerif", Font.BOLD, 30);
        font2 = new Font("SansSerif", Font.BOLD, 48);

        labelTitle.setFont(font1);
        labelHeader.setFont(font1);
        labelTargil.setFont(font2);
        labelRemark1.setFont(font1);
        labelRemark2.setFont(font1);
        labelbottom.setFont(font1);
        labelSummary.setFont(font1);



        textFieldAnswer.setFont(font2);
        textFieldAnswer.setBackground(Color.PINK);

		button=new JButton();//creating instance of JButton

        frame.setSize(800,800);//400 width and 500 height

        labelTitle.setBounds(50,50,400, 50);
        labelHeader.setBounds(50,150,400, 50);
        labelTargil.setBounds(50,250,400, 50);
        labelRemark1.setBounds(50,350,500, 50);
        labelRemark2.setBounds(50,400,500, 50);
        labelbottom.setBounds(50,500,500, 50);
        labelSummary.setBounds(50,550,500, 50);

        textFieldAnswer.setBounds(10,250, 40,50);
        labelTargil.setBorder(BorderFactory.createLoweredBevelBorder());
		button.setBounds(450,250,100, 100);

        // Set image to size of JButton...
        int offset = button.getInsets().left;
        button.setIcon(resizeIcon(picIcon, button.getWidth() - offset, button.getHeight() - offset));

		// אירוע לחיצת כפתור
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                input = Integer.parseInt(textFieldAnswer.getText());
                System.out.println(input);
                checkAnswer();

                if(falseAnswer) {
                    i++;
                } //else {
                  //  k++;
                  //  i=1;
                //}
                postAnswerCheck();
                if (!falseAnswer) {
                    i=1;
                }
                textFieldAnswer.setText("");

				if(MAXQUESTIONS >= k && !falseAnswer && i>MAXTRIES) {
                    getNextTargil();
                    showTargil();
                }
                if (MAXQUESTIONS >= k) { //==
                    endOfTest();
                    if (MAXTESTS >= g) {
                       testsSetSummary();
                    }
                }
			}
		});

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(button);//adding button in JFrame
		frame.add(textFieldAnswer);
        frame.add(labelTitle);
        frame.add(labelHeader);
        frame.add(labelTargil);
        frame.add(labelRemark1);
        frame.add(labelRemark2);
        frame.add(labelbottom);
        frame.add(labelSummary);
        frame.getContentPane().setBackground(BGFRAMECOLOR);
		frame.setLayout(null);//using no layout managers
		frame.setVisible(CONSOLEVISIBLE);//making the frame visible
	}

    //////////////////////////////////////////////////////////////////////////////////
    // התאמת גודל אובייקט תמונה לתמונה
    //////////////////////////////////////////////////////////////////////////////////
    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    //////////////////////////////////////////////////////////////////////////////////
    // לוגיקה ראשית
    //////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {

		// ציור לוח הכפל
		int y, //  מונה שורות
			x, // מונה עמודות

//		// משחק הכפולות
//			k = 0, // משחק הכפולות - מונה 10 תרגילים
//			num1 = 0,num2 = 0, // קבלת שני מספרים אקראיים למכפלה
//			input = 0, // משחק הכפולות - קבלת קלט מהמשתמש
//			grade = 0, // צובר ציון ברמת מבחן אחד
//			g = 0, // מונה 10 מבחנים
//			gradeAll = 0, //  צובר ציון לכל המבחנים שנעשו
//			i = 0, // מונה מחזוריות הזדמנויות להשיב ולתקן תשובה לא נכונה
			minNumberLeft = 0, // סדר המספרים האמיתי שירוץ על לולאת אתחול התרגילים
			maxNumberLeft = 0, // ההחלפה נועדה למקרה שמטריצת לוח הכפל שנבחרה אינה
			minNumberRight = 0,// שווה ויש טווח רוחב מספרים גדול מטווח אורך
			maxNumberRight = 0,// במקרה זה, כדי לתמוך בהעדר חזרות על תרגילים, נעמיד את הטבלה בהחלפת סדר המספרים בתרגיל
			numOfTargilim = 0,  // מונה כמות התרגילים באתחול תרגילים (מקסימום מספר תרגילים למבחן * מספר המבחנים)
			numOfTargilimTemp = 0,  // מונה כמות התרגילים באתחול תרגילים (מתאפס בכל סיבוב שהסתיימו כל אפשרויות התרגילים ללא חזרות)
			regularLoop = 0;  // באתחול תרגילים אינדיקציה להחלפת מקום המספרים בתרגיל
//		    secondsDiff = 0, // הפרש שניות עד מתן תשובה כלשהי או תשובה נכונה
//			secondsTest = 0,  // משך הזמן המצטבר שלקח להשיב על מבחן אחד
//			secondsTestAll = 0; // משך הזמן המצטבר שלקח להשיב על כל המבחנים
        //long secondsDiffLong; // הפרש שניות עד מתן תשובה לפני העברה לטיפוס int
//		Date now1 = null; /*now2 = null;*/		// מדידת זמן בשניות לתשובה - לפני פתרון ועד הפיתרון
		//FreeTTS freeTTS; // speach to text object

//


		//AnswerDetails answerDetails = new AnswerDetails();

		new Main();

		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		//=========== ציור לוח הכפל ===========
		// הצגת כותרת
        String messageTitle = "תרגילי לוח הכפל של הדר";
		System.out.println(messageTitle) ;
		System.out.println("----------------------") ;
        labelTitle.setText(messageTitle);

		// לולאה חיצונית לציור 10 שורות

		//  צייר את לוח הכפל רק אם אינדיקציה היא 1
		if(SHOWTABLE==1) {
			System.out.println("───────────────────────────────");
			for (y = 1; y <= 10; y++) {
				// לולאה פנימית להצגת 10 עמודות
				// כלומר, בכל שורה תשרשר 10 תוצאות ל 10 עמודות
				for (x = 1; x <= 10; x++) {
					// טיפול בקו מפריד ראשון בעמודת הטבלה
					if (x == 1) System.out.print("│");
					// טיפול רווח או שניים בתצוגת מכפלות שתוצאתם קטנה מ 10 או בעמודה אחרונה 100
					if (x * y < 10) {
						// הדפסת רווח לעמודות שהתוצאה בהן קטנה מ 10
						System.out.print(" ");
					} else if (x == 10 && x * y < 100) {
						// הדפת רווח נוסף לעמודה אחרונה לפני 100
						System.out.print(" ");
					}
					// הצגת תוצאת מכפלה + קו מפריד
					System.out.print(x * y + "│");
					// המשך לערך הבא בשורה
				}
				// הדפסת ירידת שורה להתחלת שורה חדשה
				System.out.println();
				System.out.println("───────────────────────────────");
				// המשך לשורה הבאה
			}
		}

		//=========== משחק תרגילים ===========
		// ----  רמת כל המשחק ואתחול ----
		// החלף את צדדי המספרים לצורך אתחול מחסנית התרגילים
		// הדבר נועד לתמוך בלוגיקת מניעת חזרות של תרגילים במחסנית
		// לשם כך נבדוק אם המטריצה של לוח הכפל הנבחר ע"י המשתמש
		// באמצעות בחירת טווחי המספרים ימין ושמאל מהווים מטריצה סימטרית
		// כלומר שטווח מספרים באורך שווה לטווח המספרים ברוחב
		// אם לא שווה ויש רוחב גדול מאורך, נהפוך את הטבלה,
		// כלומר נעמיד את מלבן המטריצה כדי לכסות את הטווח הארוך בין הרוחב והאורך
		// וכך ניצור כיסוי מלא של תרגילים תוך שמירת עיקרון מניעת חזרות
		// ובהתאם לבחירת המשתמש בכמות מבחנים, מספר תריגילים במבחן והטווחים של המספרים
		if(MAXNUMBER1 - MINNUMBER1 >= MAXNUMBER2 - MINNUMBER2) {
			// המספרים משמאל יעמדו לאורך ציר Y
			minNumberLeft = MINNUMBER1;
			maxNumberLeft = MAXNUMBER1;
			minNumberRight = MINNUMBER2;
			maxNumberRight = MAXNUMBER2;
		} else {
			// המספרים מימין יעמדו לאורך ציר Y
			minNumberLeft = MINNUMBER2;
			maxNumberLeft = MAXNUMBER2;
			minNumberRight = MINNUMBER1;
			maxNumberRight = MAXNUMBER1;
		}
        // קבל מספר התרגילים האפשריים ללא חזרות על מנת לערבב כל סיבוב כזה בנפרד
        // ולמנוע חזרה על תרגילים קרוב מדי ככל שיש יותר תרגילים מקומבינציות
        int NumberOfTargilimWithoutDuplicates =
                getNumberOfTargilimWithoutDuplicates(maxNumberLeft - minNumberLeft + 1);
		// המטרה של אתחול התרגילים מראש הוא לבצע בקרה על כל התרגילים במכל המבחנים
		// כדי למנוע חזרה על תרגילים שהיו באותו מבחן ובמבחנים אחרים באותו סט
		// מלא מחסנית בתרגילים כמספר כמות תרגילים בכל מבחן ומספר המבחנים
		do {
			// רוץ על טווחי המספרים שהוגדרו בתרגיל
			// לולאה חיצונית למספר שמאלי (שורות)
			for (y = minNumberLeft; y <= maxNumberLeft ; y++){
				// לולאה פנימית למספר ימני (עמודות)
				for (x = minNumberRight; x <= maxNumberRight ; x++){
					// שליטה בתרגילים חוזרים - אם אינדיקציה לסיבוב ראשון של אתחול
					// אז דחוף מספר ראשון לשמאל ומספר שני לימין (אחרת החלף עבור חזרות במידת האפשר)
					if (regularLoop==0) {
						// הצב במחסנית את הכפולה (שני המספרים) ואת התוצאה
						targilTemp.add(new Integer[] {y, x, x*y});

						// אם אינדיקציה לסיבוב do חדש (שני וכו) של אתחול
						// אז החלף בין המספרים ודחוף מספר ראשון לימין ומספר שני לשמאל
					} else if (regularLoop==1) {
						// הצב במחסנית את הכפולה (שני המספרים) ואת התוצאה
						targilTemp.add(new Integer[] {x, y, x*y});
					}
					// קדם/עדכן את מספר התרגילים שהוכנסו למחסניות
					numOfTargilim++ ;
					numOfTargilimTemp++ ;
					// טיפול בסיום צבירת כל אפשרויות התרגילים - ערבוב, העברה לצובר כללי ואיפוס להמשך קליטה
					if(numOfTargilimTemp == NumberOfTargilimWithoutDuplicates ||
                            (numOfTargilim > ((MAXNUMBER1 - MINNUMBER1+1) * (MAXNUMBER2 - MINNUMBER2+1)) &&
                                    numOfTargilim%(MAXNUMBER1 - MINNUMBER1+1) * (MAXNUMBER2 - MINNUMBER2+1) == 0)) {
 					    // מאחר שהגענו לסוף הקומבינציות או לסוף מילוי מכסת התרגילים למבחן ומעבר אולי,
                        // תתערבב המחסנית הזמנית ותרוקן את התוצאה למחסנית הראשית והזמנית תתאפס
                        for(int p = 1;p<=SHUFFLE;p++) {
                            Collections.shuffle(targilTemp);
                        }
						// שפוך הכל ממחסנית זמנית למחסנית קבועה
                        // בסיבוב אחרון יתכן שיישפכו למחסנית יותר תרגילים ממה שיוצגו
                        // ובעתיד אפשר להגביל למספר התרגילים שנותרו למילוי המכסה בלבד
                        // בכל מקרה, המערכת בהמשך לא תציג יותר תרגילים מהמכסה שקבע המשתמש
						targil.addAll(targilTemp);
						// נקה מחסנית זמנית
						targilTemp.clear();
						// אפס צובר למחסנית זמנית
						numOfTargilimTemp = 0;
					}
					// אם תוצאת הכפולה שווה לכפולת מספר עמודה באותו מספר עמודה אז סיים את השורה
					// כדי למנוע חזרה על תרגילים בהחלפת צדדי מספרי הכפולה
					if(x*y==y*y) break;
				}
			}
			// במידה והסתיים אתחול כל התרגילים האפשריים לטווח הנתון ונשארו מקומות ריקים במכסה,
			// כלומר אם טרם מולאה מכסת המחסנית, בצע חזרות על הכפולות תוך החלפת מקומות המספרים
			if (regularLoop==0) {
				regularLoop = 1;
			} else if (regularLoop==1) {
				regularLoop = 0;
			}
			// המשך באתחול מחסנית התרגילים כל עוד לא הגעת למספר כולל מקסימלי של תרגילים לכל המבחנים
		} while(numOfTargilim < MAXQUESTIONS*MAXTESTS);

//		for (int e = 0; e < targil.size();e++) {
//			//int bbb = targil.getFirst()[0] ;// + " * " + targil.getFirst() + " = " + targil.getFirst());
//			System.out.println("(" + e + ")   " + targil.get(e)[0] + " * " + targil.get(e)[1] + " = " + targil.get(e)[2]);
//		}

		// --- רוץ ברמת כל המבחנים (יש מספר מבחנים לסט אחד)  ----
		do {
			// אפס משתנים
			grade = 0;
			// כותרת מבחן מספר __
			// רד 2 שורות ליצירת רווח
			System.out.println();
			System.out.println();
			String messageHeader = "מבחן מספר " + (g+1) + " מתוך " + MAXTESTS;
			System.out.println(messageHeader);
			System.out.println("__________________");
            labelHeader.setText(messageHeader);


			// --- רוץ ברמת מבחן אחד (כמות תרגילים למבחן אחד) ----
			for (k = 1; k <= MAXQUESTIONS; k++){
				getNextTargil();
//				// לולאת מחזוריות הזדמנויות להשיב במקרה ותשובה אינה נכונה
//				// אם התשובה נכונה, הלולאה תפסיק ונעבור לתרגיל הבא
				// --- רמת שאלה במבחן ----
				for (i = 1; i <= MAXTRIES; i++){
					showTargil();
					// קבל קלט תשובה מהמשתמש
					input = getAnswer(console);
                    checkAnswer();
                    if(!falseAnswer) break;
				}
				postAnswerCheck();
			}
            endOfTest();
			// בצע לולאה של 10 מבחנים או הקשת 0 ליציאה
		} while(g < MAXTESTS && !(input==0));
        testsSetSummary();
	}
    //////////////////////////////////////////////////////////////////////////////////
	// קבלת קלט חוזר מהמשתמש עד מתן תשובה מספרית
    //////////////////////////////////////////////////////////////////////////////////
	public static int getAnswer(Scanner console) {
		boolean isInputCorrect = true;
		int input = 0;
		do{
			// בדיקה לוודא שיוקלד רק מספר
			if(console.hasNextInt()){
				input = console.nextInt();
				isInputCorrect = true;
			} else{
				System.out.print("  הקלדה שגויה, הקלידי שוב ");
				isInputCorrect = false;
				console.next();
			}
		} while(isInputCorrect == false);
        textFieldAnswer.setText(Integer.toString(input));
        textFieldAnswer.setText("");
		return input;
	}

    //////////////////////////////////////////////////////////////////////////////////
	// חישוב מספר התרגילים ללא חזרות
    //////////////////////////////////////////////////////////////////////////////////
   	public static int getNumberOfTargilimWithoutDuplicates(int n) {
		int sum = 0;
	    for (int i=1;i<=n;i++) {
		    sum=sum+i;
	    }
	    return sum;
	}

    //////////////////////////////////////////////////////////////////////////////////
	// לגרום למחשב לומר מה שכתוב
    //////////////////////////////////////////////////////////////////////////////////
	public static void speak(String text) {
		Voice voice;
		VoiceManager voiceManager = VoiceManager.getInstance();
		voice = voiceManager.getVoice(VOICENAME_kevin);
		voice.allocate();
		voice.speak(text);
	}

    //////////////////////////////////////////////////////////////////////////////////
    // שליפת תרגיל אחד מהמחסנית
    //////////////////////////////////////////////////////////////////////////////////
	public static void getNextTargil() {
		// הבא מהמחסנית שני מספרים אקראיים
		// מספר שמאלי
		num1 = targil.get(0)[0];

		// מספר ימני
		num2 = targil.get(0)[1] ;

		// הסר תרגיל מהרשימה למניעת חזרתו
		targil.remove(0);

		// אתחול אינדיקציה לבדוק אם היתה תשובה לא נכונה
		//falseAnswer = false;
	}

    //////////////////////////////////////////////////////////////////////////////////
    // הצגת תרגיל אחד
    //////////////////////////////////////////////////////////////////////////////////
	public static void showTargil() {
		// הדפס תרגיל
		String targilText = "(" + k + ")   " + num1 + " x " + num2 + " = ";
		System.out.print(targilText);

		// set GUI
		if (i == 1) {
			labelTargil.setText(targilText);
			preferredSize = labelTargil.getPreferredSize();
			preferredSize.width = (int) (preferredSize.width * 1.1);
//            labelRemark1.setText("");
//            labelRemark2.setText("");
			labelTargil.setPreferredSize(preferredSize);
            int labelTargilx = labelTargil.getX() + preferredSize.width - 10;
            textFieldAnswer.setBounds(labelTargilx, 250, 90, 50);
            button.setBounds(labelTargilx + 100,250,100, 100);
		}

		// השמע תרגיל
		if(WITHSPEECH > 0) {
			speak(num1 + "kkkkafful" + num2);
		}

		if (i == 1) {
			// מדוד זמן התחלה
			now1 = new Date();
		}
	}

    //////////////////////////////////////////////////////////////////////////////////
    // טיפול בתשובה נכונה ולא נכונה לאחר תרגיל אחד
    //////////////////////////////////////////////////////////////////////////////////
    public static void checkAnswer() {
        // בצע בדיקת נכונות התשובה והצג מסקנה
        // אם התשובה נכונה - צא מהלולאה והמשך לתרגיל הבא

//        AnswerDetails answerDetails = new AnswerDetails();
        String messgaeRemark1, messgaeRemark2;
//        boolean falseAnswer=false;
        long secondsDiffLong; // initial 0
//        int secondsDiff=0;
        Date now2=null;

        if (input==num1*num2){
            // create the return object that will contain the answer details

            // טיפול בתשובה נכונה
            // עצור מדידת זמן
            now2 = new Date();

            // אתחול אינדיקציה שהיתה תשובה לא נכונה
            falseAnswer = false;
            // חשב משך הזמן שלקח להשיב תשובה נכונה בשניות
            secondsDiffLong = (now2.getTime() - now1.getTime()) / 1000;
            secondsDiff = (int)secondsDiffLong;

            // אם התשובה נכונה ציין זאת ואת מספר השניות שלקח
            messgaeRemark1 = "--- תשובה נכונה בתוך " + secondsDiff + " שניות ---";
            System.out.print(messgaeRemark1) ;
            labelRemark1.setText(messgaeRemark1);
            labelRemark2.setText("");

            if(WITHSPEECH > 0) {
                speak("correct");
            }

            // תנאי פרגון אם נתנה תשובה נכונה תוך 3 שניות
            if (secondsDiff <= TIMETOANSWERFORGOODFEEDBACK ) {
                messgaeRemark2 = "   הדרי מהירה - כל הכבוד!!!";
                System.out.print(messgaeRemark2);
                labelRemark2.setText(messgaeRemark2);
            }
            System.out.println(); // רד שורה

            // אם התשובה נכונה בניסיון ראשון, הוסף נקודה לצובר תשובות נכונות
            if (i == 1) {
                grade++;
                gradeAll++;
            }
            // בנכונות התשובה, נצא מלולאת אפשרויות מתן תשובות נוספות לאותו תרגיל
            //break;
        } else {
            // טיפול בתשובה לא נכונה
            Toolkit.getDefaultToolkit().beep();
            // אתחול אינדיקציה שהיתה תשובה לא נכונה
            falseAnswer = true;
            // אם התשובה אינה נכונה ציין זאת, תאפשר הזדמנות נוספת ובסוף הצג תשובה נכונה
            // אם התשובה האחרונה אינה נכונה - יתאפשר לחזור ולהקליד אותה לאחר הצגת התשובה הנכונה
            if (i < MAXTRIES) {
                // עד 4 ניסיונות כושלים הצג הודעה זו
                messgaeRemark1 = "תשובה לא נכונה - נסה שוב בניסיון " + (i + 1);
                System.out.println(messgaeRemark1);
                labelRemark1.setText(messgaeRemark1);
                labelRemark2.setText("");

                if(WITHSPEECH > 0) {
                    speak("wrong answer");
                }
            } else if (i==MAXTRIES) {
                // בניסיון הרביעי והאחרון הצג הודעה זו
                messgaeRemark1 = "תשובה לא נכונה";
                System.out.println(messgaeRemark1);
                labelRemark1.setText(messgaeRemark1);
                labelRemark2.setText("");
            }
            if (MAXTRIES==1 || i == MAXTRIES -1) {
                //  גלה תשובה בניסיון שלישי (לפני אחרון)
                messgaeRemark1 = "התשובה הנכונה היא: " + num1*num2;
                System.out.println(messgaeRemark1);
                labelRemark1.setText(messgaeRemark1);
                labelRemark2.setText("");
                // עצור מדידת זמן
                now2 = new Date();
                // חשב משך הזמן שלקח להשיב תשובה נכונה בשניות
                secondsDiffLong = (now2.getTime() - now1.getTime()) / 1000;
                secondsDiff = (int)secondsDiffLong;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    // עדכון דוח סטטיסטיקות (שגיאה או משך זמן מתן תשובה חריג) לאחר סיום תרגיל אחד
    //////////////////////////////////////////////////////////////////////////////////
    public static void postAnswerCheck() {
		if (i>1&&i<=MAXTRIES) {
			// אם באחד הניסיונות התשובה אינה נכונה
			// תאגור תרגיל שלא הצליח לצורך הצגתו בסיכום
			// האיבר השלישי במארך אינו התשובה הנכונה אלא מספר הניסיונות
			targilTemp.add(new Integer[] {num1, num2, i});
		} else if (i > MAXTRIES) {
			// אם אף תשובה אינה נכונה
			targilTemp.add(new Integer[] {num1, num2, (secondsDiff - 1) * (-1) });
		} else if (secondsDiff > TIMETOANSWERFORREPORT) {
			// אם לקח לענות יותר מ 6 שניות התרגיל יצטבר לדוח התרגילים לשיפור
			targilTemp.add(new Integer[] {num1, num2, secondsDiff * (-1) });
		}
		// צבור את שניות מתן התשובה לצובר המבחן
		secondsTest += secondsDiff ;
	}

    //////////////////////////////////////////////////////////////////////////////////
    // הצגת סטטיסטיקות בסיום מבחן אחד
    //////////////////////////////////////////////////////////////////////////////////
	public static void endOfTest() {

        String messageBottom, messageBottom2 = "";
        // צבור את שניות מתן התשובות של המבחן לצובר כל המבחנים
        secondsTestAll += secondsTest ;

        // הצג ציון סופי של בחינה אחת
        messageBottom = "___________________________________________\n" +
                "הציון שלך הוא: " + grade * 100 / MAXQUESTIONS + "    \n" +
                "הממוצע לתרגיל: " + secondsTest/MAXQUESTIONS + " שניות\n" +
                "לקח לך להשיב: " + secondsTest + " שניות\n";

        // תנאים לפרגון או המלצה לשיפור
        if (grade * 100 / MAXQUESTIONS > 90){
            // אם הציון מעל 90 ציין זאת
            messageBottom += "___________________________________________\n" +
                             "כל הכבוד הדרי - קיבלת ציון גבוה מאד !!!\n";

            if (secondsTest/MAXQUESTIONS < 9) {
                // אם זמן תרגיל ממוצע פחות מ 10 שניות ציין זאת
                messageBottom += "כל הכבוד הדרי - המלכה  !!! פחות מ 9 שניות לתרגיל\n";
            }
        } else if (grade * 100 / MAXQUESTIONS < 60){
            // אם הציון מתחת 70 ציין זאת
            messageBottom += "\nהדרי, מומלץ להוסיף ולתרגל";
        }

        // מונה מבחנים
        g++ ;

        // הצג נתונים סופיים של כל הבחינות עד עתה

        messageBottom += "__________________________________________\n";
        if (g == MAXTESTS) { // להדפיס דוח סיכום כללי רק למבחן האחרון
            messageBottom += "\n_________________ סיכום __________________";
            messageBottom += "\n__________________________________________";
        }
        if(g > 1) { // מקרה קצה: הצג ציון ממוצע בכל המבחנים רק אם זה לא המבחן הראשון
            messageBottom += "\nציונך הממוצע בכל המבחנים עד עתה הוא: " + gradeAll * 100 / MAXQUESTIONS / g +
                               "\nזמנך הממוצע לפתירת תרגיל: " + secondsTestAll / MAXQUESTIONS / g + " שניות" + "\n";
        }
        if(g < MAXTESTS) { // מקרה קצה: שאל אם להמשיך למבחן הבא ככל שטרם הגיע המבחן האחרון
            // שאלה למשתמש האם ברצונו להמשיך למבחן נוסף
            messageBottom += "\nהאם ברצונך להמשיך למבחן הבא?";
            messageBottom2 = "\nלהמשך הקש 1; לסיום הקש 0";
            // אפס צובר כל המבחנים, את צובר כל זמן במחינה ואת צובר כל זמן 10 הבחינות,
            secondsTest = 0;
            //gradeAll = 0;
            //secondsTestAll = 0; // לא חובה לאפס כי זה נתון סופי
        }
        // הדפס צבר הודעות
        System.out.println(messageBottom + messageBottom2);
        infoBox(messageBottom,  "סיום מבחן " + g);
    }

    //////////////////////////////////////////////////////////////////////////////////
    // הצגת סיכום וסטטיסטיקות ממוצע ציונים, זמנים, הערכות והצגת דוח לשיפור בסיום סט כל המבחנים
    //////////////////////////////////////////////////////////////////////////////////
    public static void testsSetSummary() {
        String messageBottom = "";
        // תנאי פירגון בסוף הסט
        if (gradeAll * 100 / MAXQUESTIONS / g == 100) {
            messageBottom += "===============================================";
            messageBottom += "אבא, קיבלתי 100!!!\n" +
                    "תקנה לי פלאפון עכשיו!!! תודה אבא, יש, יש, יש";
            messageBottom +="===============================================";
        }
        if (targilTemp.size() > 0) {
            messageBottom += "רשימת תרגילים לשיפור (" + targilTemp.size() + ")" + "\n" +
                             "-------------------------\n";
            // רשימת תרגילים שהמבצע התקשה בהם
            for (int s = 0; s < targilTemp.size(); s++) {
                num1 = targilTemp.get(s)[0]; // מספר שמאלי
                num2 = targilTemp.get(s)[1]; // מספר ימני
                int num3 = targilTemp.get(s)[2]; // אם חיובי: אוגר מספר ניסיונות לאחר תשובה שגויה ; אם שלילי: אוגר תרגילים שלקח זמן רב לפתור
                if (num3 > 0) {
                    // הצגת תרגיל עם בעיית פיתרון שגוי
                    messageBottom += num1 + " x " + num2 + " = " + num1 * num2 + " | ניסיון " + num3 + " |* " + "\n";
                } else {
                    // הצגת תרגיל עם בעיית זמן הפיתרון
                    messageBottom += num1 + " x " + num2 + " = " + num1 * num2 + " | שניות " + num3 * (-1) + " | " + "\n" ;
                }
            }
        }
        System.out.println(messageBottom);
        infoBox(messageBottom,  "סיום סט מבחנים " + g);
        console.close();
        if (WITHSPEECH > 0) {
            speak("end of test");
        }

    }

    //////////////////////////////////////////////////////////////////////////////////
    // message box
    // example: infoBox("YOUR INFORMATION HERE", "TITLE BAR MESSAGE", "HEADER MESSAGE");
    //////////////////////////////////////////////////////////////////////////////////
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}