package nl.tudelft.cse1110.andy.writer.weblab;

import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.utils.ResourceUtils;

import java.io.File;
import java.util.Random;

public class RandomAsciiArtGenerator {

    /**
     * @return String - ASCII art read from .txt file
     */
    public String getRandomAsciiArt() {
        File randomAsciiFile = pickRandomAsciiArtFile();
        String asciiArt = FilesUtils.readFile(randomAsciiFile);
        return asciiArt;
    }

    /** Picks a random ASCII art .txt file from src/main/resources/congrats
     * @return - File containing ASCII art
     */
    private File pickRandomAsciiArtFile() {

        String asciiDirPath = ResourceUtils.resourceFolder("congrats");
        File asciiDir = new File(asciiDirPath);

        File[] listOfAscii = FilesUtils.getAllFiles(asciiDir);

        // Randomly pick one of the .txt files under resources/congrats
        Random random = new Random();
        File randomAsciiFile = listOfAscii[random.nextInt(listOfAscii.length)];

        return randomAsciiFile;
    }

}
