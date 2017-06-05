package gui;

import com.jogamp.opengl.GLProfile;
import javax.swing.JFrame;

/**
 *
 * @author Utilisateur
 */
public class GUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        //Init 3D
        GLProfile.initSingleton();
        
        
        MainWindow main = new MainWindow();
        main.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        main.setVisible(true);
    }
    
}
