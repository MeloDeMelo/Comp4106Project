package Test;

import Main.guessThePicture;

import org.junit.Before;
import org.junit.Test;

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
        System.out.println("The RGB value at " + x + ", " + y + " is " + gtp.getRGB(x,y));
    }
}
