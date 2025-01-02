//incorporate sample size. PRIORITY
//then, change wavfilesaver to take Sound children as inputs



//two different freq notes can't be named the same
//can create custom notes if note of the same freq or name doesn't exist

//Noteset must have at least two different notes

public class prog{
    public static void main(String[] args){
        SoundNote note1 = new SoundNote(44100, 16, 2, 440, "A4");
        SoundNote note2 = new SoundNote(44100, 16, 2, 659.25, "E5");
        SoundNote note3 = new SoundNote(44100, 16, 2, 880, "A5");
        note1.prepareToPlay();
        note2.prepareToPlay();
        note3.prepareToPlay();

        SoundNoteSet chord1 = new SoundNoteSet(44100, 8, 2, "TrialChord");
        chord1.addNote(note1);
        chord1.addNote(note2);
        chord1.addNote(note3);
        chord1.prepareToPlay();
        WavFileSaver.saveWavFile(chord1.sampleArray, chord1.sampleRate, chord1.sampleSize);

        chord1.prepareToPlay();
        chord1.start();



        // WavFileSaver.saveWavFile(note1.sampleArray, note1.sampleRate, note1.sampleSize);
        // note1.prepareToPlay();
        // note1.start();
    }
}