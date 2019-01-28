/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

  import java.awt.Color ;
  import java.awt.image.BufferedImage ;
  
  import javax.imageio.ImageIO;
  
  import java.io.File ;
  
  public class MandelbrotQuadrant extends Thread {
  
      final static int N = 4096 ;
      final static int CUTOFF = 100 ; 
  
      static int [] [] set = new int [N] [N] ;
  
      public static void main(String [] args) throws Exception {
  
          // Calculate set
  
          long startTime = System.currentTimeMillis();
  
          MandelbrotQuadrant thread0 = new MandelbrotQuadrant(0,N/2,0,N/2) ;
          MandelbrotQuadrant thread1 = new MandelbrotQuadrant(0,N/2,N/2,N) ;
          MandelbrotQuadrant thread2 = new MandelbrotQuadrant(N/2, N, 0, N/2) ;
          MandelbrotQuadrant thread3 = new MandelbrotQuadrant(N/2,N,N/2,N) ;
  
          thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
  
          thread0.join();
        thread1.join();
        thread2.join();
        thread3.join();
  
          long endTime = System.currentTimeMillis();
  
          System.out.println("Calculation completed in " +
                             (endTime - startTime) + " milliseconds");
  
          // Plot image
  
          BufferedImage img = new BufferedImage(N, N,
                                                BufferedImage.TYPE_INT_ARGB) ;
  
          // Draw pixels
  
          for (int i = 0 ; i < N ; i++) {
              for (int j = 0 ; j < N ; j++) {
  
                  int k = set [i] [j] ;
  
                  float level ;
                  if(k < CUTOFF) {
                      level = (float) k / CUTOFF ;
                  }
                  else {
                      level = 0 ;
                  }
                  Color c = new Color(level, 0, level) ;  // magenta
                  img.setRGB(i, j, c.getRGB()) ;
              }
          }
      
  
          // Print file
  
          ImageIO.write(img, "PNG", new File("Mandelbrot-Parallel-Quadrant.png"));
      }
  
      int beginX;
      int beginY;
      int endX;
      int endY;
  
      public MandelbrotQuadrant(int beginX, int endX, int beginY, int endY) {
       
              this.beginX = beginX;
              this.beginY = beginY;
              this.endY = endY;
              this.endX = endX;
      }
  
      @Override
    public void run() {

        for(int i = this.beginX ; i < this.endX ; i++) {
            for(int j = this.beginY; j < this.endY ; j++) {
                double cr = (4.0 * i - 2 * N) / N ;
                double ci = (4.0 * j - 2 * N) / N ;

                double zr = cr, zi = ci ;

                int k = 0 ;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) {

                    // z = c + z * z

                    double newr = cr + zr * zr - zi * zi ;
                    double newi = ci + 2 * zr * zi ;

                    zr = newr ;
                    zi = newi ;

                    k++ ;
                }

                set [i] [j] = k ;
            }
        }
    }
 }