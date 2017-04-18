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
        gtp = new guessThePicture();
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
            System.out.println("Population " + (i + 1) + " is " + gtp.precentDifferencePicture(i) + "% the same");
        }
    }
}
