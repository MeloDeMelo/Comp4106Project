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
        population = new BufferedImage[POPULATION_COUNT];
        for(int i = 0; i < POPULATION_COUNT; i ++){
            population[i] = new BufferedImage(width, height, TYPE_INT_ARGB);
            for(int x = 0; x < width; x ++){
                for(int y = 0; y < height; y ++){
                    population[i].setRGB(x, y, intialPicture.getRGB(x,y));
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
