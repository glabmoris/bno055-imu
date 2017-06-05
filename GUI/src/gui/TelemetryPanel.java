package gui;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Glm
 */
public class TelemetryPanel extends JPanel implements Runnable {
    Thread thread;
    
    JLabel lblX;
    JLabel lblY;
    JLabel lblZ;
    
    JLabel lblSpeedX;
    JLabel lblSpeedY;
    JLabel lblSpeedZ;
    
    JLabel lblAccelX;
    JLabel lblAccelY;
    JLabel lblAccelZ;
    
    JLabel lblHeading;
    JLabel lblRoll;
    JLabel lblPitch;
    
    private double position_x;
    private double position_y;
    private double position_z;
    
    private double velocity_x;
    private double velocity_y;
    private double velocity_z;

    private double accel_x;
    private double accel_y;
    private double accel_z;
    
    private double heading;
    private double roll;
    private double pitch;    
    
    public TelemetryPanel(){
        initComponents();
        
        this.start();
    }
    
    void initComponents(){
        this.setBorder(BorderFactory.createCompoundBorder(   BorderFactory.createTitledBorder("Telemetry"),    BorderFactory.createEmptyBorder(5,5,5,5)));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        lblX = new JLabel("X: ");this.add(lblX);
        lblY = new JLabel("Y: ");this.add(lblY);
        lblZ = new JLabel("Z: ");this.add(lblZ);
        
        this.add(new JLabel(" "));
        //this.add(new JSeparator(SwingConstants.HORIZONTAL));

        lblSpeedX = new JLabel("X Velocity: ");this.add(lblSpeedX);
        lblSpeedY = new JLabel("Y Velocity: ");this.add(lblSpeedY);
        lblSpeedZ = new JLabel("Z Velocity: ");this.add(lblSpeedZ);
        
        //this.add(new JSeparator(SwingConstants.HORIZONTAL));        
        this.add(new JLabel(" "));
        
        lblAccelX = new JLabel("X Acceleration: ");this.add(lblAccelX);
        lblAccelY = new JLabel("Y Acceleration: ");this.add(lblAccelY);
        lblAccelZ = new JLabel("Z Acceleration: ");this.add(lblAccelZ);
        
        //this.add(new JSeparator(SwingConstants.HORIZONTAL));        
        this.add(new JLabel(" "));

        lblHeading = new JLabel("Heading: ");this.add(lblHeading);
        lblRoll    = new JLabel("Roll: ");this.add(lblRoll);
        lblPitch   = new JLabel("Pitch: ");this.add(lblPitch);
    }
    
    public void start(){
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }
    
    public void update(){
        lblX.setText(String.format("%-20s %10.5f","X:",position_x));
        lblY.setText(String.format("%-20s %10.5f","Y: ", position_y));
        lblZ.setText(String.format("%-20s %10.5f","Z: ", position_z));
        
        lblSpeedX.setText(String.format("%-20s %10.5f","X Velocity: " , velocity_x));
        lblSpeedY.setText(String.format("%-20s %10.5f","Y Velocity: " , velocity_y));
        lblSpeedZ.setText(String.format("%-20s %10.5f","Z Velocity: " , velocity_z));
        
        lblAccelX.setText(String.format("%-20s %10.5f","X Acceleration: " , accel_x));
        lblAccelY.setText(String.format("%-20s %10.5f","Y Acceleration: " , accel_y));
        lblAccelZ.setText(String.format("%-20s %10.5f","Z Acceleration: " , accel_z));
        
        lblHeading.setText(String.format("%-20s %10.5f","Heading: " , heading));
        lblRoll.setText(String.format("%-20s %10.5f","Roll: " , roll));
        lblPitch.setText(String.format("%-20s %10.5f","Pitch: " , pitch));
    }

    @Override
    public void run() {
        try{
            //TODO: get host info from GUI
            Socket s = new Socket(InetAddress.getByName("192.168.0.108"), 1024);
            Random rand = new Random(new Date().getTime());

            while(true){
                /*try {
                  Thread.sleep(100);
                } catch (InterruptedException e) {
                }       */     

                position_x = rand.nextInt(100);
                position_y = rand.nextInt(100);
                position_z = rand.nextInt(100);

                velocity_x = rand.nextInt(100);
                velocity_y = rand.nextInt(100);
                velocity_z = rand.nextInt(100);

                accel_x = rand.nextInt(100);
                accel_y = rand.nextInt(100);
                accel_z = rand.nextInt(100);

                heading = 0 + rand.nextInt(2) - 1;
                roll    = 0 + rand.nextInt(2) - 1;
                pitch   = 0 + rand.nextInt(2) - 1;

                update();
            }
        }
        catch(Exception e){
            System.err.println("Telemetry: " + e.getMessage());
            
            //disconnected...wait 5 seconds
            try{
                Thread.sleep(5000);
            }
            catch(Exception e2){
                //Oh well, we tried...
            }
        }
    }



    /**
     * @return the velocity_x
     */
    public double getVelocity_x() {
        return velocity_x;
    }

    /**
     * @return the velocity_y
     */
    public double getVelocity_y() {
        return velocity_y;
    }

    /**
     * @return the velocity_z
     */
    public double getVelocity_z() {
        return velocity_z;
    }

    /**
     * @return the accel_x
     */
    public double getAccel_x() {
        return accel_x;
    }

    /**
     * @return the accel_y
     */
    public double getAccel_y() {
        return accel_y;
    }

    /**
     * @return the accel_z
     */
    public double getAccel_z() {
        return accel_z;
    }

    /**
     * @return the heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     * @return the roll
     */
    public double getRoll() {
        return roll;
    }

    /**
     * @return the pitch
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * @return the position_x
     */
    public double getPosition_x() {
        return position_x;
    }

    /**
     * @return the position_y
     */
    public double getPosition_y() {
        return position_y;
    }

    /**
     * @return the position_z
     */
    public double getPosition_z() {
        return position_z;
    }
}
