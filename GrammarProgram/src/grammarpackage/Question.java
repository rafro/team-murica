package grammarpackage;

import java.awt.Color;
import java.awt.MouseInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Question extends JFrame {
    
    //To change the first letter of the first word in the sentence to lowercase if chosen
    final int TO_LOWERCASE = 32;
    Random gen = new Random();
    //Number of lines in each file
    int numWords =25;
    int numLines = 57*2;
    int numLBLs = 4;
    //Label for correct answer
    JLabel l;
    // Variables for split up sentence and tags (mark as noun ect.).
    String[] sens = new String[numLines / 2];
    String[] tags = new String[numLines / 2];
    //Where to split it
    String splitSenAt = " ";
    //Labels with wrong answers
    JLabel[] LBL = new JLabel[numLBLs];
    //Variables to hold values of answers
    String noun;
    String verb;
    String adjective;
    String adverb;
    // Tag that correspond with word that shouldn't be a answer
    String noTag = "N";
    //Random word in sentence
    int whatWord;
    //Labels and variables for sentence
    JLabel startLBL;
    String startOfSen = "";
    JLabel endLBL;
    String endOfSen = "";
    //Where to skip the word and leave a space
    boolean switchs = false;
    // Box to place labels in 
    JLabel BOX;
    //When adding labels to array so keep track of where at so do it in order
    int currentIndexOfLBL = 0;
    //What is in the box
    boolean redIn = false;
    boolean greenIn = false;
    //Palette
    JButton CheckBTTN = new JButton();
    JButton resultBTTN = new JButton();
    JLabel errorLBL = new JLabel();
    
    

    public Question() {
       
        //Don't let player check answer before screen loads
        CheckBTTN.setVisible(false);
        CheckBTTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkBTTNClicked(evt);
            }
        });
        // Show start button
        resultBTTN.setVisible(true);
        resultBTTN.setBackground(Color.yellow);
        resultBTTN.setForeground(Color.black);
        resultBTTN.setText("Start");
        //No error at beginning
        errorLBL.setVisible(false);
        
        this.setBounds(300, 200, 320, 250);
        this.setBackground(Color.orange);
    }
    
    public void checkBTTNClicked(java.awt.event.MouseEvent evt) {
        // if something goes wrong
        errorLBL.setVisible(false);
        int numInBOX = 0;
        boolean error = false;
        if (l.getBackground() == Color.yellow) {
            numInBOX = numInBOX + 1;
        }
        int i = 0;
        while (LBL[i] != null) {
            if (LBL[i].getBackground() == Color.yellow) {
                numInBOX = numInBOX + 1;
            }
            i++;
        }
        if (numInBOX > 1) {
            errorLBL.setVisible(true);
            errorLBL.setText("Only one answer should be in the box");
            error = true;
        }
        //nothing wrong check answers
        if (error == false) {
            //set visible so can see it
            resultBTTN.setVisible(true);
            // If they check and they got the correct answer. Let them know and add to score
            if (greenIn == true) {
                resultBTTN.setBackground(Color.green);
                resultBTTN.setForeground(Color.black);
                resultBTTN.setText("CORRECT: Click to continue");
            } // If they check and they got the incorrect answer, let them know
            else {
                resultBTTN.setBackground(Color.red);
                resultBTTN.setForeground(Color.black);
                resultBTTN.setText("INCORRECT: Click to continue");
            }
            // reset for new sentence so can go again
            reset();
        }
    }                                         

    private void resultBTTNActionPerformed(java.awt.event.ActionEvent evt) {                                           
        resultBTTN.setVisible(false);

        try {
            // read random line from file to pick random sentence
            File f = new File("sentencesTagged.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            // must be odd so next line is even and the sentence and tags go with correct array
            int ranSen = gen.nextInt(numLines);
            while (ranSen % 2 == 1) {
                ranSen = gen.nextInt(numLines);
            }
            for (int i = 0; i < ranSen; i++) {
                br.readLine();
            }
            String sen = br.readLine();
            sens = sen.split(splitSenAt);
            String tag = br.readLine();
            tags = tag.split(splitSenAt);
            //Pick random word in array and if this word is a word we want save where it is
            whatWord = gen.nextInt(sens.length);
            while (tags[whatWord].contains(noTag)) {
                whatWord = gen.nextInt(tags.length);
            }
            // Make label for correct answer
            l = new JLabel();
            l.setOpaque(true);
            this.getContentPane().add(l);
            l.setBounds(0, 0, 100, 50);
            l.setForeground(Color.white);
            l.setBorder(new LineBorder(Color.BLACK, 1));
            l.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            l.setBackground(Color.blue);
            l.addMouseMotionListener(new java.awt.event.MouseAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    lMouseDragged(evt);

                }
            });
            l.setText(sens[whatWord]);

            // If correct answer isn't noun then pick a random noun and put it in a label
            if (!tags[whatWord].contains("(n)")) {
                generateWord("nounList.txt");
            }

            // If correct answer isn't verb then pick a random verb and put it in a label
            if (!tags[whatWord].contains("(v)")) {
                generateWord("verbList.txt");
            }

            // If correct answer isn't adjective then pick a random adjective and put it in a label  
            if (!tags[whatWord].contains("(adj)")) {
                generateWord("adjectiveList.txt");
            }

            // If correct answer isn't adverb then pick a random adverb and put it in a label
            if (!tags[whatWord].contains("(adv)")) {
                generateWord("adverbList.txt");
            }

            // variables to hold part of sentence before missing word and part after
            for (int i = 0; i < sens.length; i++) {
                if (switchs == false) {
                    if (i != whatWord) {
                        startOfSen = startOfSen + (sens[i] + " ");
                    } else {
                        switchs = true;
                    }
                } else {
                    if (i != whatWord) {
                        endOfSen = endOfSen + (sens[i] + " ");
                    }
                }
            }
            if((!sens[sens.length-1].contains("?"))||(!sens[sens.length-1].contains("!"))){
                endOfSen = endOfSen + ".";
            }
            //label for first half of sentence
            startLBL = new JLabel();
            this.getContentPane().add(startLBL);
            startLBL.setBounds(50, 50, startOfSen.length() * 7, 50);
            startLBL.setBorder(new LineBorder(Color.BLACK, 1));
            startLBL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            startLBL.setText(startOfSen);

            //label for last half of sentence
            endLBL = new JLabel();
            this.getContentPane().add(endLBL);
            endLBL.setBounds(startLBL.getWidth() + 200, 50, endOfSen.length() *7, 50);
            endLBL.setBorder(new LineBorder(Color.BLACK, 1));
            endLBL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            endLBL.setText(sens[whatWord]);
            endLBL.setText(endOfSen);

            // label to drag correct answers into
            BOX = new JLabel();
            this.getContentPane().add(BOX);
            BOX.setBounds(startLBL.getX() + startLBL.getWidth() + 10, 50, 130, 90);
            BOX.setBorder(new LineBorder(Color.BLACK, 2));
            BOX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            if (tags[whatWord].contains("n")) {
                BOX.setText("NOUN");
            }
            if (tags[whatWord].contains("v")) {
                BOX.setText("VERB");
            }
            if (tags[whatWord].contains("adj")) {
                BOX.setText("ADJECTIVE");
            }
            if (tags[whatWord].contains("adv")) {
                BOX.setText("ADERB");
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        // shuffle up where the labels are
        int[] pointX = new int[numLBLs];
        pointX[0] = 150;
        int x = 0;
        while (LBL[x] != null) {
            pointX[x + 1] = pointX[x] + 170;
            x++;
        }

        shuffle(pointX);
        l.setLocation(pointX[0], 200);
        int i = 0;
        while (LBL[i] != null) {
            LBL[i].setLocation(pointX[i + 1], 200);
            i++;
        }
    }
    public void lMouseDragged(java.awt.event.MouseEvent evt) {
        if (l != null) {
            l.setLocation((MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x - 7) - (l.getWidth() / 2), (MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y - 53) - (l.getHeight() / 2));
            if ((l.getX() > BOX.getX()) && (l.getX() < BOX.getX() + BOX.getWidth() - l.getWidth()) && (l.getY() > BOX.getY()) && (l.getY() < BOX.getY() + BOX.getHeight() - l.getHeight())) {
                l.setBackground(Color.yellow);
                l.setForeground(Color.black);
                greenIn = true;
                CheckBTTN.setVisible(true);
            } else {
                l.setBackground(Color.blue);
                l.setForeground(Color.white);
                greenIn = false;
                CheckBTTN.setVisible(false);
                errorLBL.setVisible(false);
            }
        }

    }
    //drag LBL and check if is in BOX...

    public void LBLMouseDragged(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setLocation((MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x - 7) - (((JLabel) evt.getSource()).getWidth() / 2), (MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y - 53) - (((JLabel) evt.getSource()).getHeight() / 2));
        if ((((JLabel) evt.getSource()).getX() > BOX.getX()) && (((JLabel) evt.getSource()).getX() < BOX.getX() + BOX.getWidth() - ((JLabel) evt.getSource()).getWidth()) && (((JLabel) evt.getSource()).getY() > BOX.getY()) && (((JLabel) evt.getSource()).getY() < BOX.getY() + BOX.getHeight() - ((JLabel) evt.getSource()).getHeight())) {
            ((JLabel) evt.getSource()).setBackground(Color.yellow);
            ((JLabel) evt.getSource()).setForeground(Color.black);
            CheckBTTN.setVisible(true);
            redIn = true;
        } else {
            ((JLabel) evt.getSource()).setBackground(Color.blue);
            ((JLabel) evt.getSource()).setForeground(Color.white);
            redIn = false;
            CheckBTTN.setVisible(false);
            errorLBL.setVisible(false);
        }
    }

    public void shuffle(int[] array) {
        int index;
        int temp;
        for (int i = array.length - 1; i > 0; i--) {
            index = gen.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public void reset() {
        if (l != null) {
            switchs = false;
            startOfSen = "";
            endOfSen = "";
            greenIn = false;
            redIn = false;
            currentIndexOfLBL = 0;
            CheckBTTN.setVisible(false);
            l.setVisible(false);
            l = null;
            startLBL.setVisible(false);
            startLBL = null;
            endLBL.setVisible(false);
            endLBL = null;
            BOX.setVisible(false);
            BOX = null;
            int i = 0;
            while (LBL[i] != null) {
                LBL[i].setVisible(false);
                LBL[i] = null;
                i++;
            }
        }
    }
//Change the first letter to capital of incorrect answers if removed word is in the first spot so they all match

    public String capFirst(String s) {
        int ascii = s.charAt(0);
        String text = "";
        ascii = ascii - TO_LOWERCASE;
        char c = (char) ascii;
        text = text + c;
        for (int i = 1; i < s.length(); i++) {
            text = text + s.charAt(i);
        }
        System.out.println(text);
        return text;
    }
//adding a label with the wrong answer

    public int addLBL() {
        LBL[currentIndexOfLBL] = new JLabel();
        this.getContentPane().add(LBL[currentIndexOfLBL]);
        LBL[currentIndexOfLBL].setOpaque(true);
        LBL[currentIndexOfLBL].setVisible(true);
        LBL[currentIndexOfLBL].setForeground(Color.white);
        LBL[currentIndexOfLBL].setBackground(Color.BLUE);
        LBL[currentIndexOfLBL].setBounds(0, 0, 100, 50);
        LBL[currentIndexOfLBL].setBorder(new LineBorder(Color.BLACK, 1));
        LBL[currentIndexOfLBL].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBL[currentIndexOfLBL].addMouseMotionListener(new java.awt.event.MouseAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                LBLMouseDragged(evt);
            }
        });
        return currentIndexOfLBL;
    }

    public void generateWord(String fileName) {
        try {
            int index = addLBL();
            currentIndexOfLBL++;
            File f2 = new File(fileName);
            FileReader fr = new FileReader(f2);
            BufferedReader br = new BufferedReader(fr);

            int ranWord = gen.nextInt(numWords);
            for (int i = 0; i < ranWord; i++) {
                br.readLine();
            }
            String word = br.readLine();
            if (whatWord == 0) {
                word = capFirst(word);
            }
            LBL[index].setText(word);
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
