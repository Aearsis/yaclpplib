package showcase;


import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ArgumentParser parser = ArgumentParserFactory.create();
        final TimeOptions ta = parser.addOptions(new TimeOptions());
        final MakeOptions ma = parser.addOptions(new MakeOptions());

        final List<String> positional = parser.requestPositionalArguments();

        parser.parse(args);

        for (String s : positional)
            System.out.println(s);
    }
}
