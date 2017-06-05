package gui;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import java.awt.BorderLayout;
import javax.swing.JFrame;


/**
 *
 * @author Utilisateur
 */
public class MainWindow extends JFrame{
    TelemetryPanel  telemetryPanel;
    PosePanel       posePanel;
    
    public MainWindow(){
        this.setTitle("PiRobot");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        
        this.setLocationRelativeTo(null);        
    }
    
    void initComponents(){
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);        
        
        this.getContentPane().setLayout(new BorderLayout());
        
        telemetryPanel = new TelemetryPanel();
        posePanel = new PosePanel(caps,telemetryPanel);
        
        this.add(telemetryPanel , BorderLayout.WEST);
        this.add(posePanel , BorderLayout.CENTER);
        
    }
}
