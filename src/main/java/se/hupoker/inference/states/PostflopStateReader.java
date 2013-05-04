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
public class PostflopStateReader {
    private final Collection<GenericState> states = new LinkedList<>();
    private final Street street;

    private PostflopStateReader(Street st) {
        street = st;
    }

    public static Collection<GenericState> getStates(Street st) {
        PostflopStateReader postflopStateReader = new PostflopStateReader(st);

        postflopStateReader.initialize();
        return postflopStateReader.states;
    }

    private void addState(GenericState state) {
        state.setStreet(street);
        states.add(state);
    }

    private void initialize() {
        try {
//			YamlReader reader = new YamlReader(StatePath.getPostFlop());
            YamlReader reader = new YamlReader(new FileReader(StatePath.getPostFlopPath()));

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