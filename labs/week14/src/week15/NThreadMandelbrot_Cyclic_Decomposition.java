/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package week3;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;

public class NThreadMandelbrot_Cyclic_Decomposition extends Thread {

    final static int N = 4096; // height and width 
    final static int CUTOFF = 100;
    final static int P = 8; // number of threads

    static int[][] set = new int[N][N];

    public static void main(String[] args) throws Exception {

        // Calculate set
        long startTime = System.currentTimeMillis();

        NThreadMandelbrot_Cyclic_Decomposition[] threads = new NThreadMandelbrot_Cyclic_Decomposition[P];
        for (int me = 0; me < P; me++) {
            threads[me] = new NThreadMandelbrot_Cyclic_Decomposition(me);
            threads[me].start();
//            threads[me].join();

        }

        for (int me = 0; me < P; me++) {
            threads[me].join();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Calculation completed in "
                + (endTime - startTime) + " milliseconds");

        // Plot image
        BufferedImage img = new BufferedImage(N, N,
                BufferedImage.TYPE_INT_ARGB);

        // Draw pixels
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                int k = set[i][j];

                float level;
                if (k < CUTOFF) {
                    level = (float) k / CUTOFF;
                } else {
                    level = 0;
                }
                Color c = new Color(level, level, level);  // White
                img.setRGB(i, j, c.getRGB());
            }
        }

        // Print file
        ImageIO.write(img, "PNG", new File("NThreadMandelbrot_Decomp.png"));
    }

    int me;

    public NThreadMandelbrot_Cyclic_Decomposition(int me) {
        this.me = me;
    }

    public void run() {

        for (int i = me; i < N; i += P) {
            for (int j = 0; j < N; j++) {

                double cr = (4.0 * i - 2 * N) / N;
                double ci = (4.0 * j - 2 * N) / N;

                double zr = cr, zi = ci;

                int k = 0;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) {

                    // z = c + z * z
                    double newr = cr + zr * zr - zi * zi;
                    double newi = ci + 2 * zr * zi;

                    zr = newr;
                    zi = newi;

                    k++;
                }

                set[i][j] = k;
            }
        }
    }

}
