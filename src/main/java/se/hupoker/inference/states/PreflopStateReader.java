package se.hupoker.inference.states;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import se.hupoker.common.Street;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Alexander Nyberg
 */
public class PreflopStateReader {
    private final Collection<GenericState> states = new LinkedList<>();
    private PreflopStateReader() {}

    public static Collection<GenericState> getStates(StateConfigurationPath configuration) {
        System.out.println("PreflopStateReader loading");
        PreflopStateReader preflopStateReader = new PreflopStateReader();

        preflopStateReader.initialize(configuration);

        return preflopStateReader.states;
    }

    private void addState(GenericState state) {
        state.setStreet(Street.PREFLOP);
        states.add(state);
    }

    private void initialize(StateConfigurationPath configuration) {
        try {
//			YamlReader reader = new YamlReader(StatePath.getPreflop());
            YamlReader reader = new YamlReader(new FileReader(configuration.preflop()));

            while (true) {
                GenericState state = reader.read(GenericState.class);
                if (state == null) {
                    break;
                }

                addState(state);
            }
        } catch (YamlException | FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not load!");
        }
    }
}