import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

class Flicker extends Frame {

    public static void main(String arg[]) {
        new Flicker();
    }

    Flicker() {
        super("Flicker");

        // if the window closes the prog will exit
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setLocation(150, 150);

        Scanner sc = new Scanner(System.in);

        int dis, peg;

        FlickerCanvas fc = new FlickerCanvas();

        System.out.println("enter number of discs(<10) and pegs(<10):");
        dis = sc.nextInt();
        peg = sc.nextInt();

        fc.init(dis, peg);
        fc.start();
        add(fc);
        pack();
        setVisible(true);
    }
}
