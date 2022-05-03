package nl.tudelft.cse1110.andy.writer.standard;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomAsciiArtGenerator {

    /**
     * @return String - ASCII art read from .txt file
     */
    public String getRandomAsciiArt() {
        List<String> congratsFiles = listOfCongratsFiles();

        // Randomly pick one of the .txt files
        Random random = new Random();
        String randomAsciiFile = congratsFiles.get(random.nextInt(congratsFiles.size()));
        try {
            return readCongratsFile(randomAsciiFile);
        } catch (RuntimeException exception) {
            return "";
        }
    }


    private List<String> listOfCongratsFiles() {
        try {
            InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream("/congrats/0list.txt");
            String list = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);

            List<String> allFiles = Arrays.asList(list.split("\n"));

            if(allFiles.isEmpty())
                throw new RuntimeException("No congrats files!");

            return allFiles;

        } catch(Exception e) {
            return List.of();
        }
    }

    private String readCongratsFile(String randomAsciiFile) throws RuntimeException {
        try {
            InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream("/congrats/" + randomAsciiFile);
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }


}
