package Kefel;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class FreeTTS {

    private static final String VOICENAME_kevin = "kevin";
    private String text; // string to speech

    public FreeTTS(String text) {
        this.text = text;
    }

    public void speak() {
        Voice voice;
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.allocate();
        voice.speak(text);
    }

//    public static void main(String[] args) {
//        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
//        String text = "Karrinn aaffaa  aalll oorrevv";
//               // + "Speech Team and is based on CMU's Flite engine.";
//        FreeTTS freeTTS = new FreeTTS(text);
//        freeTTS.speak();
//    }
}


//public class FreeTTS {
//
//    private static final String VOICENAME= "kevin16";
//
//    public static void main(String[] args) {
//
//        Voice voice;
//        VoiceManager voiceManager = VoiceManager.getInstance();
//
//        voice = voiceManager.getVoice(VOICENAME);
//        voice.allocate();
//        voice.speak("hello world");
//    }
//}