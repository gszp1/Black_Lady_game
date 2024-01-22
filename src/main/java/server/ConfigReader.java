package server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.model.Play;
import utils.score.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Game configuration reader.
 */
public class ConfigReader {
    /**
     * Reads XML file and maps rule names with score computers.
     * @return List of mapped
     */
    public List<Function<Play, ScoreComputer>> readConfig() {
        try {
            String filePath = "src/main/resources/configurations/configuration.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            Element rootElement = document.getDocumentElement();
            NodeList gameNodes = rootElement.getElementsByTagName("game");

            List<String> gameTypes = new ArrayList<>();
            for (int i = 0; i < gameNodes.getLength(); i++) {
                final Element gameElement = (Element) gameNodes.item(i);
                gameTypes.add(gameElement.getTextContent());
            }
            return gameTypes.stream().map(this::getScoreComputer).collect(Collectors.toList());
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns score computer by name.
     * @param gameType Name of game type.
     * @return Score computer for given game type.
     */
    private Function<Play, ScoreComputer> getScoreComputer(String gameType) {
        if (gameType.equals("NoTrick")) {
            return (NoTrickScoreComputer::new);
        } else if (gameType.equals("NoKiers")) {
            return (NoKiersScoreComputer::new);
        } else if (gameType.equals("NoWoman")) {
            return (NoWomenScoreComputer::new);
        } else if (gameType.equals("NoMan")) {
            return (NoManScoreComputer::new);
        } else if (gameType.equals("NoHearthKiers")) {
            return (NoHeartsKingScoreComputer::new);
        } else if (gameType.equals("NoSevenLastTrick")) {
            return (NoSevenLastTrickScoreComputer::new);
        } else if (gameType.equals("Robber")) {
            return (RobberScoreComputer::new);
        }
        else {
            throw new RuntimeException("Invalid Config!");
        }
    }
}
