package Kefel;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

//import java.util.InputMismatchException;
//import java.util.Random;
import java.time.Duration;
import java.time.LocalDateTime;
//import java.time.LocalDate;

/**
 * פרויקט לוח הכפל של הדרי  
 */
public class Main {
	
	private static final int MAXTESTS = 5; // מקסימום מבחנים
	private static final int MAXQUESTIONS = 8; // מקסמום תרגילים למבחן
	private static final int MAXTRIES = 4;  // מקסימום ניסיונות להשיב נכון
	private static final int SHOWTABLE = 1; // האם להציג את טבלת לוח הכפל: 1 כן; 2 לא
												// מספר שמאלי
	private static final int MINNUMBERLEFT = 2; // מספר אקראי מינימלי בתרגיל
	private static final int MAXNUMBERLEFT = 9; // מספר אקראי מקסימלי בתרגיל
												// מספר ימני
	private static final int MINNUMBERRIGHT = 2; // מספר אקראי מינימלי בתרגיל
	private static final int MAXNUMBERRIGHT = 9; // מספר אקראי מקסימלי בתרגיל
	
	private static final int SHUFFLE = 1;   // לערבב תרגילים: 1 כן;0 לא
						// ציור לוח הכפל
	public static int y, //  מונה שורות
					  x, // מונה עמודות
					  
					  	// משחק הכפולות
					  k, // משחק הכפולות - מונה 10 תרגילים
					  num1,num2, // קבלת שני מספרים אקראיים למכפלה
					  input, // משחק הכפולות - קבלת קלט מהמשתמש
					  tziun, // צובר ציון ברמת מבחן אחד
					  g, // מונה 10 מבחנים
					  tziunAll, //  צובר ציון לכל המבחנים שנעשו
					  l, // מונה השהייה בין הגרלת מספרים לכפולות
					  i, // מונה מחזוריות הזדמנויות להשיב ולתקן תשובה לא נכונה
					  s, // מונה מספר תרגילים עבור אתחול המשחק בתרגילים
					  numOfTargilim,  // מונה כמות התרגילים באתחול תרגילים (מקסימום מספר תרגילים למבחן * מספר המבחנים)
					  regularLoop;  // באתחול תרגילים אינדיקציה להחלפת מקום המספרים בתרגיל 
	public static long secondsDiff, // הפרש שניות עד מתן תשובה כלשהי או תשובה נכונה
					  secondsTest,  // משך הזמן המצטבר שלקח להשיב על מבחן אחד
					  secondsTestAll; // משך הזמן המצטבר שלקח להשיב על כל המבחנים

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
//		Random rand = new Random();
		
//		Integer[] targilItems = new Integer[3]; 

		ArrayList<Integer[]> targil = new ArrayList<Integer[]>();
		
		
		//=========== ציור לוח הכפל ===========
		// הצגת כותרת
		System.out.println("       לוח הכפל של הדר") ;
		System.out.println("       ---------------") ;

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
		// המטרה של אתחול התרגילים מראש הוא לבצע בקרה על כל התרגילים במכל המבחנים
		// כדי למנוע חזרה על תרגילים שהיו באותו מבחן ובמבחנים אחרים באותו סט
		// מלא מחסנית בתרגילים כמספר כמות תרגילים בכל מבחן ומספר המבחנים
		do {
			// רוץ על טווחי המספרים שהוגדרו בתרגיל
			// לולאה חיצונית למספר שמאלי (שורות)
			for (y = MINNUMBERLEFT; y <= MAXNUMBERLEFT ; y++){  
				// לולאה פנימית למספר ימני (עמודות)
				for (x = MINNUMBERRIGHT; x <= MAXNUMBERRIGHT ; x++){
					// אם אינדיקציה לסיבוב ראשון של אתחול
					// אז דחוף מספר ראשון לשמאל ומספר שני לימין (אחרת החלף עבור חזרות במידת האפשר)
					if (regularLoop==0) {
						// הצב במחסנית את הכפולה (שני המספרים) ואת התוצאה
						targil.add(new Integer[] {y, x, x*y});
						
						// אם אינדיקציה לסיבוב חדש (שני וכו) של אתחול
						// אז החלף בין המספרים ודחוף מספר ראשון לימין ומספר שני לשמאל 
					} else if (regularLoop==1) {
						// הצב במחסנית את הכפולה (שני המספרים) ואת התוצאה
						targil.add(new Integer[] {x, y, x*y});
					}
					// קדם/עדכן את מספר התרגילים שהוכנסו למחסנית
					numOfTargilim++ ;
					// אם תוצאת הכפולה שווה לכפולת מספר עמודה באותו מספר עמודה אז סיים את השורה 
					// כדי למנוע חזרה על תרגילים בהחלפת צדדי מספרי הכפולה
					if(x*y==y*y) break;
					if(numOfTargilim == MAXTESTS*MAXQUESTIONS) break; 
				}
				if(numOfTargilim == MAXTESTS*MAXQUESTIONS) break;
			}
			// במידה והסתיים אתחול כל התרגילים האפשריים לטווח הנתון ונשאר מקומות ריקים במכסה,
			// כלומר אם טרם מולאה מכסת המחסנית) החל חזרות תוך החלפת מקומות המספרים
			if (regularLoop==0) {
				regularLoop = 1;
			} else if (regularLoop==1) {
				regularLoop = 0;
			}
			// המשך באתחול מחסנית התרגילים כל עוד לא הגעת למספר כולל מקסימלי של תרגילים לכל המבחנים
		} while(numOfTargilim < MAXTESTS*MAXQUESTIONS );
		
//		for (int e = 0; e<= MAXTESTS*MAXQUESTIONS;e++) {
//			//int bbb = targil.getFirst()[0] ;// + " * " + targil.getFirst() + " = " + targil.getFirst());
//			System.out.println("(" + e + ")   " + targil.get(e)[0] + " * " + targil.get(e)[1] + " = " + targil.get(e)[2]);
//		}
		// תערבב תרגילים אם ההגדרה היא 1
		if(SHUFFLE == 1) Collections.shuffle(targil);
	
		// --- רוץ ברמת כל המבחנים (מספר מבחנים לסט אחד)  ----
		do {               
			// אפס משתנים
			tziun = 0;
			// כותרת מבחן מספר __
			// רד 2 שורות ליצירת רווח
			System.out.println();
			System.out.println();
			System.out.println("מבחן מספר " + (g+1));
			System.out.println("____________");

			// --- רוץ ברמת מבחן אחד (כמות תרגילים למבחן אחד) ---- 
			for (k = 1; k <= MAXQUESTIONS; k++){
				// הבא מהמחסנית שני מספרים אקראיים
				// מספר שמאלי
				num1 = targil.get(0)[0];
				//num1 = rand.nextInt((MAXNUMBERLEFT - MINNUMBERLEFT) + 1) + MINNUMBERLEFT;
				
				// מספר ימני
				num2 = targil.get(0)[1] ;
				targil.remove(0);	
				// השהייה בין הגרלת מספר השני בכפולה
				//for(l=1;l<=1000;l++);
				//num2 = rand.nextInt((MAXNUMBERRIGHT - MINNUMBERRIGHT) + 1) + MINNUMBERRIGHT;
				//num1 = (int) ((Math.random()*(MAXNUMBER-MINNUMBER+1))+MINNUMBER); // נבחר לא להשתמש ב Math בשל כך שלוקח Double 
			    
				// מדוד זמן התחלה
				LocalDateTime now1 = LocalDateTime.now();
				//Date now1.getTime();
				// לולאת מחזוריות הזדמנויות להשיב במקרה ותשובה אינה נכונה
				// אם התשובה נכונה, הלולאה תפסיק ונעבור לתרגיל הבא
			    // --- רמת שאלה במבחן ---- 
				for (i = 1; i <= MAXTRIES; i++){
					// הדפס תרגיל
					System.out.print("(" + k + ")   " + num1 + " x " + num2 + " = ");
					
					// קבל קלט תשובה מהמשתמש
					boolean isInputCorrect = true;
				    do{
				    	// בדיקה לוודא שיוקלד רק מספר
				        if(console.hasNextInt()){ 
				        	input = console.nextInt();
				        	isInputCorrect = true;
				        }
				        else{
				            System.out.print("  הקלדה שגויה, הקלידי שוב ");
				            isInputCorrect = false;
				            console.next();
				        }
				    } while(isInputCorrect == false);
					
					// בצע בדיקת נכונות התשובה והצג מסקנה
					// אם התשובה נכונה - צא מהלולאה והמשך לתרגיל הבא
					if (input==num1*num2){
						// טיפול בתשובה נכונה
						// עצור מדידת זמן
						LocalDateTime now2 = LocalDateTime.now();
						// חשב משך הזמן שלקח להשיב תשובה נכונה בשניות
					    Duration duration = Duration.between(now1, now2);
					    secondsDiff = Math.abs(duration.getSeconds());
						
						// אם התשובה נכונה ציין זאת ואת מספר השניות שלקח
						System.out.print("--- תשובה נכונה בתוך " + secondsDiff + " שניות ---") ;
						// תנאי פרגון אם נתנה תשובה נכונה תוך 3 שניות
						if (secondsDiff < 4 ) {
							System.out.print("   הדרי מהירה - כל הכבוד!!!");
						}
						System.out.println(); // רד שורה

						// אם התשובה נכונה בניסיון ראשון, הוסף 10 נקודת לצובר תשובות נכונות
						if (i == 1) {
							tziun = tziun + 1 ;
							tziunAll = tziunAll + 1;
						}
						// בנכונות התשובה, נצא מלולאת אפשרויות מתן תשובות נוספות לאותו תרגיל
						break;
					} else {
						// טיפול בתשובה לא נכונה
						// אם התשובה אינה נכונה ציין זאת, תאפשר הזדמנות נוספת ובסוף הצג תשובה נכונה
						// אם התשובה האחרונה אינה נכונה - יתאפשר לחזור ולהקליד אותה לאחר הצגת התשובה הנכונה
						if (i < MAXTRIES) {
							// עד 4 ניסיונות כושלים הצג הודעה זו
							System.out.println("תשובה לא נכונה - נסה שוב בניסיון " + (i + 1));
						} else if (i==MAXTRIES) {
							// בניסיון הרביעי והאחרון הצג הודעה זו
							System.out.println("תשובה לא נכונה");
						}
						if (MAXTRIES==1 || i == MAXTRIES -1) {
							// גלה תשובה בניסיון שלישי
							System.out.println("התשובה הנכונה היא: " + num1*num2);
							// עצור מדידת זמן
							LocalDateTime now2 = LocalDateTime.now();
							// חשב משך הזמן שלקח להשיב תשובה נכונה בשניות
						    Duration duration = Duration.between(now1, now2);
						    secondsDiff = Math.abs(duration.getSeconds());
						}
					}
				}
				// צבור את שניות מתן התשובה לצובר המבחן
				secondsTest += secondsDiff ;
			}
			// צבור את שניות מתן התשובות של המבחן לצובר כל המבחנים
			secondsTestAll += secondsTest ;			
			
			// הצג ציון סופי של בחינה אחת
			
			System.out.println("___________________________________________");
			System.out.println("הציון שלך הוא: " + tziun * 100 / MAXQUESTIONS + "      ");
			System.out.println("הממוצע לתרגיל: " + secondsTest/MAXQUESTIONS + " שניות");
			System.out.println("לקח לך להשיב: " + secondsTest + " שניות");			
			// תנאים לפרגון או המלצה לשיפור
			if (tziun * 100 / MAXQUESTIONS > 90){
				// אם הציון מעל 90 ציין זאת
				System.out.println("כל הכבוד הדרי - קיבלת ציון גבוה מאד !!!") ;
			} else if (tziun * 100 / MAXQUESTIONS < 60){ 
				// אם הציון מתחת 70 ציין זאת
				System.out.println("הדרי, מומלץ להוסיף ולתרגל") ;			
			}
			if (secondsTest/MAXQUESTIONS < 10) {
				// אם זמן תרגיל ממוצע פחות מ 10 שניות ציין זאת
				System.out.println("כל הכבוד הדרי - המלכה  !!!") ;
			}
			// מונה מבחנים
			g++ ;
			
			// הצג נתונים סופיים של כל הבחינות עד עתה
			
			System.out.println("___________________________________________");
			if(g > 1) { // מקרה קצה: הצג ציון ממוצע בכל המבחנים רק אם זה לא המבחן הראשון
				System.out.println("ציונך הממוצע בכל המבחנים עד עתה הוא: " + tziunAll * 100 / MAXQUESTIONS / g ) ; 
				System.out.println("זמנך הממוצע לפתירת תרגיל: " + secondsTestAll / MAXQUESTIONS / g + " שניות") ; 
				System.out.println();
			}
			if(g < MAXTESTS) { // מקרה קצה: שאל אם להמשיך למבחן הבא ככל שטרם הגיע המבחן האחרון
				// שאלה למשתמש האם ברצונו להמשיך למבחן נוסף
				System.out.println("האם ברצונך להמשיך למבחן הבא?");
				System.out.println("להמשך הקש 1; לסיום הקש 0") ;
				
				// קבל קלט תשובה מהמשתמש (כפילות לפונקציה)
				boolean isInputCorrect = true;
			    do{
			    	// בדיקה לוודא שיוקלד רק מספר
			        if(console.hasNextInt()){ 
			        	input = console.nextInt();
			        	isInputCorrect = true;
			        }
			        else{
			            System.out.print("  הקלדה שגויה, הקלידי שוב ");
			            isInputCorrect = false;
			            console.next();
			        }
			    } while(isInputCorrect == false);
				// אפס צובר כל המבחנים, את צובר כל זמן במחינה ואת צובר כל זמן 10 הבחינות,  
				secondsTest = 0;
				//tziunAll = 0;
				//secondsTestAll = 0; // לא חובה לאפס כי זה נתון סופי
			}
			// תנאי פירגון בסוף הסט
			if (g == MAXTESTS && tziunAll * 100 / MAXQUESTIONS / g == 100) {
				System.out.println("===============================================") ;
				System.out.println("אבא, קיבלתי 100!!!") ;
				System.out.println("תקנה לי פלאפון עכשיו!!! תודה אבא, יש, יש, יש") ;
				System.out.println("===============================================") ;
			}
		// בצע לולאה של 10 מבחנים או הקשת 0 ליציאה	
		} while(g < MAXTESTS && !(input==0));
		console.close();
	}
}