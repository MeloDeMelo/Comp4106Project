package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private final int K = 5;    //number picked during tournament selection
    private final int MUTATION_THRESHOLD = 100;    // 1 in a 1000 to mutate
    private final Random random = new Random();
    private final Mutator mutator = new Mutator(random);

    private String directory;
    private BufferedImage initialPicture;
    private BufferedImage[] population;
    private int height, width;

    public guessThePicture(boolean randomData, String phtotoName){
        directory = System.getProperty("user.dir");
        directory = directory + "\\Pictures";
        try{
        this.initialPicture = ImageIO.read(new File(directory + "\\intialPictures\\" + phtotoName));
        }catch(IOException e){
            e.printStackTrace();
        }

        this.height = initialPicture.getHeight();
        this.width = initialPicture.getWidth();

        if(randomData)
            randomPopulationInit();
        //else
            //choosenPopulationInit();

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
        savePopulation();
    }

    private void savePopulation() {
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
                int mating = random.nextInt(MUTATION_THRESHOLD);
                int mutate = random.nextInt(MUTATION_THRESHOLD);
                if(mutate == mating)
                    child.setRGB(x, y, mutator.mutantPixel());
                else if(mating < MUTATION_THRESHOLD/2)
                    child.setRGB(x, y, father.getRGB(x,y));
                else
                    child.setRGB(x, y, mother.getRGB(x,y));
            }
        }

        return child;
    }

    public void newGenerations(int numberOfGenerations){
        for(int i = 0; i < numberOfGenerations; i ++) {
            newGeneration();
            System.out.println("Gen: " + (i+ 1));
        }
        savePopulation();
    }

    public void newGeneration(){
        BufferedImage[] newPopulation = new BufferedImage[POPULATION_COUNT];

        int start;
        int superMutation = random.nextInt(MUTATION_THRESHOLD) + 1;
        //runt mutated into new population
        if(superMutation > (MUTATION_THRESHOLD - 1)){
            start = 1;
            int runtIndex = findRunt();
            newPopulation[0] = mutator.mutate(population[runtIndex]);
        }//no full mutations
        else
            start = 0;

        for(int i = start; i < POPULATION_COUNT; i ++){
            int father = tournamentSelection();
            newPopulation[i] = mate(population[father], population[tournamentSelection(father)]);
        }

        population = newPopulation;
    }

    private int findRunt(){
        int runtIndex = 0;
        double runtStrength = percentDifferencePicture(population[0]);
        for(int i = 1; i < POPULATION_COUNT; i ++){
            if(runtStrength > percentDifferencePicture(population[i])){
                runtIndex = i;
                runtStrength = percentDifferencePicture(population[i]);
            }
        }
        return runtIndex;
    }

    private int tournamentSelection(int firstParent){
        int[] selected = new int[K];
        boolean cantSelect;
        do {
            selected[0] = random.nextInt(POPULATION_COUNT);
        }while(selected[0] == firstParent);

        for(int i = 0; i < selected.length; i++){
            do{
                cantSelect = false;
                selected[i] = random.nextInt(POPULATION_COUNT);
                for(int k = 1; k < i; k ++){
                    if((selected[i] == selected[k]) && (selected[i] == firstParent)){
                        cantSelect = true;
                        continue;
                    }
                }
            }while(cantSelect);
        }

        int alphaIndex = 0;
        for(int i = 1; i < selected.length; i ++){
            if(percentDifferencePicture(population[selected[alphaIndex]]) < percentDifferencePicture(population[selected[i]]))
                alphaIndex = i;
        }

        return selected[alphaIndex];
    }

    private int tournamentSelection(){
        int[] selected = new int[K];
        boolean cantSelect;
        selected[0] = random.nextInt(POPULATION_COUNT);
        for(int i = 0; i < selected.length; i++){
            do{
                cantSelect = false;
                selected[i] = random.nextInt(POPULATION_COUNT);
                for(int k = 1; k < i; k ++){
                    if(selected[i] == selected[k]) {
                        cantSelect = true;
                        continue;
                    }
                }
            }while(cantSelect);
        }

        int alphaIndex = 0;
        for(int i = 1; i < selected.length; i ++){
            if(percentDifferencePicture(population[selected[alphaIndex]]) < percentDifferencePicture(population[selected[i]]))
                alphaIndex = i;
        }

        return selected[alphaIndex];
    }

    public double percentDifferencePicture(BufferedImage picture1){
        return percentDifferencePicture(picture1, initialPicture);
    }

    public double percentDifferencePicture(BufferedImage picture1, BufferedImage picture2){
        double total = 0;
        for(int y  = 0; y < height; y ++){
            for(int x = 0; x < width; x ++){
                total += percentDifferencePixel(x, y, picture1, picture2);
            }
        }
        return total / (height*width);
    }

    private double percentDifferencePixel(int x, int y, BufferedImage picture1, BufferedImage picture2){
        Color c1 = new Color(picture1.getRGB(x,y)), c2 = new Color (picture2.getRGB(x,y));
        double pctDiffRed = ((Math.abs((double)c1.getRed() - (double)c2.getRed())) / 255);
        double pctDiffGreen = (Math.abs((double)c1.getGreen() - (double)c2.getGreen())) / 255;
        double pctDiffBlue = (Math.abs((double)c1.getBlue() - (double)c2.getBlue())) / 255;
        return (100 - ((pctDiffRed + pctDiffBlue + pctDiffGreen) / 3 * 100));
    }

    public int getRGB(int x, int y){
        return initialPicture.getRGB(x,y);
    }

    public int getPOPULATION_COUNT(){
        return POPULATION_COUNT;
    }

    public BufferedImage getInitialPicture(){
        return initialPicture;
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
