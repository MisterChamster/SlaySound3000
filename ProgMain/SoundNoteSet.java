import javax.sound.sampled.*;
//cant have two same freq notes

class SoundNoteSet extends Sound{
    SoundNote[] noteArray;
    int noteArrayCLen;

    SoundNoteSet(float sampleRate, int sampleSize, double durationInSeconds, String soundName){
        super(sampleRate, sampleSize, durationInSeconds, soundName);
        this.noteArray = new SoundNote[15];
        this.noteArrayCLen = 0;
    }

    //testing function
    SoundNoteSet(SoundNote[] noteArray){
        super();
        for (int i=0; i<noteArray.length; i++){
            stripNote(noteArray[i]);
        }
        this.noteArray = noteArray;
        this.noteArrayCLen = noteArray.length;
    }


    private boolean isNoteInNoteArray(SoundNote note){
        for (int i=0; i<noteArrayCLen-1; i++){
            if (noteArray[i].frequency == note.frequency) return true;
        }
        return false;
    }

    //This class needs just note frequency and name, rest can be erased with that function
    private void stripNote(SoundNote note){
        note.sampleRate = 0;
        note.sampleSize = 0;
        note.durationInSeconds = 0;
        note.sampleArray = null;
        note.format = null;
        note.line = null;
    }

    void widenNoteArray(int value){
        SoundNote[] resizeArray = new SoundNote[noteArray.length + value];
        for (int i=0; i<noteArray.length; i++){
            resizeArray[i] = noteArray[i];
        }
        noteArray = resizeArray;
    }

    void addNote(SoundNote note){
        if (!isNoteInNoteArray(note)){
            stripNote(note);
            //Resizes the array if the user is stupid enough to use more than 15 notes in a noteset
            if (noteArrayCLen >= noteArray.length - 1){
                widenNoteArray(1);
            }
            noteArray[noteArrayCLen] = note;
            noteArrayCLen++;
        }
    }

    void delNote(int index){
        noteArrayCLen--;
        for (int i=index+1; i<=noteArrayCLen; i++){
            noteArray[i-1] = noteArray[i];
        }
        noteArray[noteArrayCLen] = null;
    }

    //SAMPLESIZE not implemented
    void computeSampleArray(){
        int sampleArrayLength = (int) (Math.ceil(sampleRate * durationInSeconds) * (sampleSize/8));
        sampleArray = new byte[sampleArrayLength];

        for (int i=0; i<noteArrayCLen; i++){
            noteArray[i].setSampleRate(this.sampleRate);
            noteArray[i].setSampleSize(this.sampleSize);
            noteArray[i].setDurationInSec(this.durationInSeconds);
            noteArray[i].computeSampleArray();
        }

        int temp = 0;
        //for all values in byte array...
        for (int i=0; i<sampleArrayLength; i++){
            //for all notes in note array...
            for (int j=0; j<noteArrayCLen; j++){
                temp += (int) noteArray[j].sampleArray[i];
            }
            sampleArray[i] = (byte) ((int)(temp/noteArrayCLen));
            // System.out.println(sampleArray[i]);
            temp = 0;
        }

        //Notes in noteArray no longer need to take so much space
        for (int i=0; i<noteArrayCLen; i++){
            stripNote(noteArray[i]);
        }
    }

    @Override
    void prepareToPlay(){
        computeSampleArray();
        super.prepareToPlay();
    }

    @Override
    void playSound(){
        if (noteArrayCLen < 2) {
            System.out.println("Noteset can be played if it has 2 or more different notes");
            System.exit(0);
        }
        super.playSound();
    }

    public void run(){
        this.playSound();
    }

    //debug function
    void printNoteArray(){
        for(int i=0; i<noteArrayCLen; i++){
            System.out.println((i+1) + ". " + noteArray[i].frequency);
        }
    }
}