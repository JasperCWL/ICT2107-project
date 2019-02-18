import java.awt.*;

public class App {


    public static void main(String[] args) {
        View v = new View();
        Model m = new Model();
        Controller c = new Controller(v, m);
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                v.getChatFrame().setVisible(true);
            }
        });
    }
}