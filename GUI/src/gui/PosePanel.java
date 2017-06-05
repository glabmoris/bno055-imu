package gui;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 *
 * @author Glm
 */
public class PosePanel extends GLCanvas implements GLEventListener{
    TelemetryPanel  telemetry;
    
    //Useless
    private double rotateX, rotateY, rotateZ = 0;   
    
    
    public PosePanel(GLCapabilities caps,TelemetryPanel telemetry){

        super(caps);
        this.telemetry = telemetry;
        this.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(this, 60);
        //animator.add(canvas);
        animator.start();        
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    private void update() {
        //update from telemetry data
        rotateZ = telemetry.getRoll();
        rotateX = telemetry.getPitch();
        rotateY = telemetry.getHeading();
        
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0,0,0,0);
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        gl.glMatrixMode(GL2.GL_PROJECTION);  // Set up the projection.
        gl.glLoadIdentity();
        gl.glOrtho(-1,1,-1,1,-2,2);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        
        gl.glLoadIdentity();             // Set up modelview transform. 
        gl.glRotated(rotateZ,0,0,1);
        gl.glRotated(rotateY,0,1,0);
        gl.glRotated(rotateX,1,0,0);
        
        cube(gl);
    }    
    
    private void square(GL2 gl, float r, float g, float b) {
            gl.glColor3f(r,g,b);         // The color for the square.
            gl.glTranslatef(0,0,0.5f);    // Move square 0.5 units forward.
            gl.glNormal3f(0,0,1);        // Normal vector to square (this is actually the default).
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            gl.glVertex2f(-0.5f,-0.5f);    // Draw the square (before the
            gl.glVertex2f(0.5f,-0.5f);     //   the translation is applied)
            gl.glVertex2f(0.5f,0.5f);      //   on the xy-plane, with its
            gl.glVertex2f(-0.5f,0.5f);     //   at (0,0,0).
            gl.glEnd();
    }

    private void cube(GL2 gl) {

            gl.glPushMatrix();
            square(gl,1,0,0);        // front face is red
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(180,0,1,0); // rotate square to back face
            square(gl,0,1,1);        // back face is cyan
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(-90,0,1,0); // rotate square to left face
            square(gl,0,1,0);        // left face is green
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(90,0,1,0); // rotate square to right face
            square(gl,1,0,1);       // right face is magenta
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(-90,1,0,0); // rotate square to top face
            square(gl,0,0,1);        // top face is blue
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(90,1,0,0); // rotate square to bottom face
            square(gl,1,1,0);        // bottom face is yellow
            gl.glPopMatrix();

    }    
    
 
}
