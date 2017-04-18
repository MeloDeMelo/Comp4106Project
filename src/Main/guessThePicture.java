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

    private final int POPULATION_COUNT = 20;
    private final Random random = new Random();

    private String directory;
    private BufferedImage intialPicture;
    private BufferedImage[] population;
    private int height, width;

    public guessThePicture(){
        directory = System.getProperty("user.dir");
        directory = directory + "\\Pictures";
        try{
        this.intialPicture = ImageIO.read(new File(directory + "\\intialPictures\\Blitz.png"));
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
        }
        savepopulation();
    }

    private void savepopulation() {
        for (int i = 0; i < POPULATION_COUNT; i++) {
            try {
                File outputfile = new File(directory + "\\population\\Population_" + (i + 1) + ".jpg");
                ImageIO.write(population[i], "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage mate(BufferedImage father, BufferedImage mother){
        BufferedImage child = new BufferedImage(width, height, TYPE_INT_ARGB);

        for(int y = 0; y < height; y ++){
            for(int x = 0; x < width; x++){
                int mating = random.nextInt(1000) + 1;
                if(mating <= 750){
                    if(precentDifferencePixel(x, y, intialPicture, father) > precentDifferencePixel(x, y, intialPicture, mother))
                        child.setRGB(x, y, father.getRGB(x,y));
                    else
                        child.setRGB(x, y, mother.getRGB(x,y));
                }
                else if(mating <= 870){
                    child.setRGB(x, y, father.getRGB(x,y));
                }
                else if(mating <= 999){
                    child.setRGB(x, y, mother.getRGB(x,y));
                }
                else{
                    child.setRGB(x, y, intialPicture.getRGB(x,y));
                }
            }
        }

        return child;
    }

    public void newGeneration(){
        BufferedImage[] prevPopulation = population;
        population = new BufferedImage[POPULATION_COUNT];

        int alphaIndex = 0;
        double alphaValue = precentDifferencePicture(intialPicture, prevPopulation[0]);
        for(int i = 1; i < POPULATION_COUNT; i ++){
            if (precentDifferencePicture(intialPicture, prevPopulation[i]) > alphaValue){
                alphaIndex = i;
                alphaValue = precentDifferencePicture(intialPicture, prevPopulation[i]);
            }
        }

        for(int i = 0; i < POPULATION_COUNT; i ++){
            int mateWithAlpha = random.nextInt(100);
            if ((i != alphaIndex) && (mateWithAlpha <= 30)) {
                    population[i] = mate(prevPopulation[alphaIndex], prevPopulation[i]);
            }
            else {
                int fatherIndex = random.nextInt(POPULATION_COUNT);
                int motherIndex;
                do {
                    motherIndex = random.nextInt(POPULATION_COUNT);
                } while (fatherIndex != motherIndex);

                population[i] = mate(prevPopulation[fatherIndex], prevPopulation[motherIndex]);
            }
        }
        savepopulation();
    }

    public double precentDifferencePicture(BufferedImage picture1, BufferedImage picture2){
        double total = 0;
        for(int y  = 0; y < height; y ++){
            for(int x = 0; x < width; x ++){
                total += precentDifferencePixel(x, y, picture1, picture2);
            }
        }
        return total / (height*width) * 100;
    }

    private double precentDifferencePixel(int x, int y, BufferedImage picture1, BufferedImage picture2){
        Color c1 = new Color(picture1.getRGB(x,y)), c2 = new Color (picture2.getRGB(x,y));
        double pctDiffRed = (Math.abs(c1.getRed() - c2.getRed())) / 255;
        double pctDiffGreen = (Math.abs(c1.getGreen() - c2.getGreen())) / 255;
        double pctDiffBlue = (Math.abs(c1.getBlue() - c2.getBlue())) / 255;
        return (pctDiffRed + pctDiffBlue + pctDiffGreen) / 3 * 100;
    }

    public int getRGB(int x, int y){
        return intialPicture.getRGB(x,y);
    }

    public int getPOPULATION_COUNT(){
        return POPULATION_COUNT;
    }

    public BufferedImage getIntialPicture(){
        return intialPicture;
    }

    public BufferedImage getPopulation(int index){
        return population[index];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth(){
        return width;
    }
}
