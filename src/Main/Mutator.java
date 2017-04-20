package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Max on 4/19/2017.
 */
public class Mutator {

    Random random;

    public Mutator(Random random){
        this.random = random;
    }

    public BufferedImage mutate(BufferedImage victim){
        mutations mutation = mutations.values()[random.nextInt(mutations.values().length)];
        switch(mutation){
            case redShift:
                victim = shiftColor(0, victim);
                break;
            case blueShift:
                victim = shiftColor(1, victim);
                break;
            case greenShift:
                victim = shiftColor(2, victim);
                break;
        }
        return victim;
    }

    private int shift50(int currValue){
        if((currValue + 50) <= 255)
            return currValue + 50;
        else
            return((currValue + 50) - 255);
    }

    public BufferedImage shiftColor(int shift, BufferedImage victim){
        Color currColor, newColor;
        for(int x = 0; x < victim.getWidth(); x ++){
            for(int y = 0; y < victim.getHeight(); y ++){
                currColor = new Color(victim.getRGB(x,y));
                switch(shift){
                    case 0:
                        int newRed = shift50(currColor.getRed());
                        newColor = new Color(newRed, currColor.getGreen(), currColor.getBlue());
                        victim.setRGB(x,y,newColor.getRGB());
                        break;
                    case 1:
                        int newGreen = shift50(currColor.getGreen());
                        newColor = new Color(currColor.getRed(), newGreen, currColor.getBlue());
                        victim.setRGB(x,y,newColor.getRGB());
                        break;
                    case 2:
                        int newBlue = shift50(currColor.getBlue());
                        newColor = new Color(currColor.getRed(), currColor.getGreen(), newBlue);
                        victim.setRGB(x,y,newColor.getRGB());
                        break;
                }
            }
        }
        return victim;
    }

    public int mutantPixel(){
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color.getRGB();
    }

    public enum mutations{
        redShift,
        blueShift,
        greenShift
    }
}
