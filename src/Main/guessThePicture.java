package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Created by Max on 4/17/2017.
 */
public class guessThePicture {

    private final int POPULATION_COUNT = 5;
    private final Random random = new Random();

    private String directory;
    private BufferedImage intialPicture;
    private BufferedImage[] population;
    private int height, width;

    public guessThePicture(){
        directory = System.getProperty("user.dir");
        directory = directory + "\\Pictures";
        try{
        this.intialPicture = ImageIO.read(new File(directory + "\\intialPictures\\draven.jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }

        this.height = intialPicture.getHeight();
        this.width = intialPicture.getWidth();

        randomPopulationInit();

    }

    public void randomPopulationInit(){
        Color color;
        population = new BufferedImage[POPULATION_COUNT];
        for(int i = 0; i < POPULATION_COUNT; i ++){
            population[i] = new BufferedImage(width, height, TYPE_INT_ARGB);
            for(int x = 0; x < width; x ++){
                for(int y = 0; y < height; y ++){
                    color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    population[i].setRGB(x, y, color.getRGB());
                }
            }
            try{
                File outputfile = new File(directory + "\\population\\Population_" + (i+1) + ".jpg");
                ImageIO.write(population[i], "png", outputfile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public double precentDifferencePicture(int populationNum){
        double total = 0;
        for(int y  = 0; y < height; y ++){
            for(int x = 0; x < width; x ++){
                total += precentDifferencePixel(x, y, populationNum);
            }
        }
        return total / (height*width) * 100;
    }

    private double precentDifferencePixel(int x, int y, int populationNum){
        Color c1 = new Color(intialPicture.getRGB(x,y)), c2 = new Color (population[populationNum].getRGB(x,y));
        double pctDiffRed = (Math.abs(c1.getRed() - c2.getRed())) / 255;
        double pctDiffGreen = (Math.abs(c1.getGreen() - c2.getGreen())) / 255;
        double pctDiffBlue = (Math.abs(c1.getBlue() - c2.getBlue())) / 255;
        return (pctDiffRed + pctDiffBlue + pctDiffGreen) / 3 * 100;
    }

    public int getRGB(int x, int y){
        return intialPicture.getRGB(x,y);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth(){
        return width;
    }
}
