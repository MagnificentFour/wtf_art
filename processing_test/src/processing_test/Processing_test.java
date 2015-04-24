package processing_test;

import java.io.IOException;


/**
 * Mainloop class for the application
 *
 * @author nikla_000
 */
public class Processing_test {

    /**
     * Main loop for the application
     *
     * @param args the command line arguments. Not really relevant when the application is not run from commandline
     */
    public static void main(String[] args) throws IOException {
        System.out.println("(!) Initializing Skynet");
        System.out.println("(!) Prepare to be assimilated");
        new DisplayFrame().setVisible(true);
    }
}
