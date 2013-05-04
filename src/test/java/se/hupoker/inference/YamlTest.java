package se.hupoker.inference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.hupoker.inference.holebucket.HoleTuple;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;


public class YamlTest {
    public static String filename = "preflophole111.yml";

    public static class Contact {
        public String name;
        public List<String> list = new ArrayList<>();
    }

    public static void writeContract() {
        Contact c1 = new Contact();
        c1.name = "qk";
        c1.list.add("a");
        c1.list.add("b");

        Contact c2 = new Contact();
        c2.name = "qk2";
        c2.list.add("c");
        c2.list.add("d");

        try {
            YamlWriter writer = new YamlWriter(new FileWriter(filename));
            //writer.getConfig().setClassTag("contact", Contact.class);
            writer.write(c1);
            writer.write(c2);
            writer.close();
        } catch (YamlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readContract() {
        try {
            YamlReader reader = new YamlReader(new FileReader(filename));

            while (true) {
                Contact contact = reader.read(Contact.class);
                if (contact == null) {
                    break;
                }

                System.out.println("Found:" + contact.name);
                for (String s : contact.list) {
                    System.out.println("list:" + s);
                }

            }
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not load.");
        }
    }

    private static final List<HoleTuple> tupleList = new ArrayList<>();
    public static String filenamee = "flophole.yml";

    private static void readList() {
        try {
            YamlReader reader = new YamlReader(new FileReader(filenamee));

            while (true) {
                HoleTuple tuple = reader.read(HoleTuple.class);
                if (tuple == null) {
                    break;
                }

                tupleList.add(tuple);
            }
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not load.");
        }

        for (HoleTuple tuple : tupleList) {
            System.out.println(tuple);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //writeContract();
        //readContract();

        //writeList();
        readList();

    }
}