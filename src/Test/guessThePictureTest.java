package Test;

import Main.guessThePicture;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Max on 4/17/2017.
 */
public class guessThePictureTest {

    guessThePicture gtp;

    @Before
    public void setUp() throws Exception{
        gtp = new guessThePicture(true, "puppy.jpg");
    }

    @Test
    public void intialTest(){
        int x = 450, y = 450;
        int rgbValue = gtp.getRGB(x,y);
        Color color = new Color(rgbValue);
        System.out.println("The RGB value at " + x + ", " + y + " is " + color);
        assertEquals(color.getRGB(), rgbValue);
    }

    @Test
    public void compareTest(){
        for(int i = 0; i < 5; i ++){
            System.out.println("Population " + (i + 1) + " is " + gtp.percentDifferencePicture(gtp.getInitialPicture(), gtp.getPopulation(i)) + "% the same");
        }

        System.out.println("" + gtp.percentDifferencePicture(gtp.getInitialPicture(), gtp.getInitialPicture()));
    }

    @Test
    public void newGenerationTest(){
        int numberOfGenerations = 10;
        gtp.newGenerations(numberOfGenerations);
        int alphaIndex = 0;
        double alphaValue = gtp.percentDifferencePicture(gtp.getInitialPicture(), gtp.getPopulation(0));
        for(int q = 1; q < gtp.getPOPULATION_COUNT(); q ++){
            if (gtp.percentDifferencePicture(gtp.getInitialPicture(), gtp.getPopulation(q)) > alphaValue){
                alphaIndex = q;
                alphaValue = gtp.percentDifferencePicture(gtp.getInitialPicture(), gtp.getPopulation(q));
            }
        }

        System.out.println("The alpha of generation " + numberOfGenerations + " is " + (alphaIndex + 1) + " with a percentage of " + alphaValue + "%\n");
    }
}
