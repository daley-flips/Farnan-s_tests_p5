// javac cs1501_p5\*.java; if ($?) { java -cp . cs1501_p5.farnan_tests }
package cs1501_p5;

import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.math.BigDecimal;
import java.math.RoundingMode;



public class farnan_tests{

static DistanceMetric_Inter dm;
static ColorMapGenerator_Inter generator;
static ColorQuantizer cq;

    public static void main(String args[]){


        basic_euclidean_test();
        basic_circular_hue_test();
        basic_bucketing_palette_test();
        basic_quantize_2d_array();

        System.out.println();
    }


// helper functions--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------

    static Pixel[][] genStripedArr() {
        return new Pixel[][]{
            {new Pixel(5, 5, 5), new Pixel(5, 5, 5), new Pixel(5, 5, 5)},
            {new Pixel(50, 50, 50), new Pixel(50, 50, 50), new Pixel(50, 50, 50)},
            {new Pixel(100, 100, 100), new Pixel(100, 100, 100), new Pixel(100, 100, 100)},
            {new Pixel(150, 150, 150), new Pixel(150, 150, 150), new Pixel(150, 150, 150)},
            {new Pixel(200, 200, 200), new Pixel(200, 200, 200), new Pixel(200, 200, 200)},
            {new Pixel(250, 250, 250), new Pixel(250, 250, 250), new Pixel(250, 250, 250)}
        };
    }
//-----------------------------------------------------------------------------------------------------------------
    private static int pixelToInt(Pixel pix) {
        return ((pix.getRed() << 16) & 0xff0000) | ((pix.getGreen() << 8) & 0xff00) | ((pix.getBlue() & 0xff));
    }
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//Tests:


    private static void basic_euclidean_test(){
        System.out.println("\n----------Farnan's Basic Squared Euclidean Test----------");

        Pixel p1 = new Pixel(10, 15, 20);
        Pixel p2 = new Pixel(20, 25, 30);
        dm = new SquaredEuclideanMetric();

        double expectedDistance = 300;
        double actualDistance = dm.colorDistance(p1, p2);
        System.out.println("Expected Distance: " + expectedDistance + " | Actual Distance: " + actualDistance + " | " + (actualDistance == expectedDistance ? "SUCCESS!!\n" : "FAIL*********\n"));
        System.out.println("Flipping Pixel Inputs");
        actualDistance = dm.colorDistance(p2, p1);
        System.out.println("Expected Distance: " + expectedDistance + " | Actual Distance: " + actualDistance + " | " + (actualDistance == expectedDistance ? "SUCCESS!!\n" : "FAIL*********\n"));
        

    }
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
    private static void basic_circular_hue_test(){
        System.out.println("\n----------Farnan's Basic Circular hue Test----------");
        Pixel p1 = new Pixel(200, 68, 0);
        Pixel p2 = new Pixel(215, 0, 70);
        dm = new CircularHueMetric();

        double expectedDistance = 40;
        double actualDistance = dm.colorDistance(p1, p2);
        System.out.println("Expected Distance: " + expectedDistance + " | Actual Distance: " + actualDistance + " | " + (actualDistance == expectedDistance ? "SUCCESS!!\n" : "FAIL*********\n"));
        System.out.println("Flipping Pixel Inputs");
        actualDistance = dm.colorDistance(p2, p1);
        System.out.println("Expected Distance: " + expectedDistance + " | Actual Distance: " + actualDistance + " | " + (actualDistance == expectedDistance ? "SUCCESS!!\n" : "FAIL*********\n"));
    }
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------

    private static void basic_bucketing_palette_test(){
        System.out.println("\n----------Farnan's Basic Bucketing generateColorPalette Test----------");

        Pixel[][] stripedArr = genStripedArr();
        generator = new BucketingMapGenerator();

        // check 1 color
        Pixel[] result = generator.generateColorPalette(stripedArr, 1);
        Pixel expected = new Pixel(128, 0, 0);

        int expectedRes = 1;
        int actualRes = result.length;
        System.out.println("Expected: " + expectedRes + " | Actual: " + actualRes + " | " + (actualRes == expectedRes ? "SUCCESS!!\n" : "FAIL*********\nIncorrect number of colors returned from Basic Bucketing generateColorPalette\n"));
        
        Pixel actual_pixel = result[0];
        System.out.println("Expected: " + expected + " | Actual: " + actual_pixel + " | " + (actual_pixel.equals(expected) ? "SUCCESS!!\n" : "FAIL*********\nIncorrect color returned for palette of a single color\n"));
        
        // Check with 4 colors that evenly divide 2^24
        result = generator.generateColorPalette(stripedArr, 4);
        Pixel[] expectedCT = new Pixel[]{new Pixel(32, 0, 0), new Pixel(96, 0, 0), new Pixel(160, 0, 0), new Pixel(224, 0, 0)};

        // assertEquals(4, result.length, "Incorrect number of colors returned from Basic Bucketing generateColorPalette");
        expectedRes = 4;
        actualRes = result.length;
        System.out.println("Expected: " + expectedRes + " | Actual: " + actualRes + " | " + (actualRes == expectedRes ? "SUCCESS!!\n" : "FAIL*********\nIncorrect number of colors returned from Basic Bucketing generateColorPalette\n"));
        
        for (int i = 0; i < expectedCT.length; i++) {
            //assertEquals(expectedCT[i], result[i], "Incorrect color returned for palette of bucketing");
            actual_pixel = result[i];
            System.out.println("Expected: " + expectedCT[i] + " | Actual: " + actual_pixel + " | " + (actual_pixel.equals(expectedCT[i]) ? "SUCCESS!!\n" : "FAIL*********\nIncorrect color returned for palette of bucketing\n"));
        
        }

        // Check with 7 colors that do not evenly divide 2^24
        result = generator.generateColorPalette(stripedArr, 7);
        expectedCT = new Pixel[]{
            new Pixel(18, 73, 36),
            new Pixel(54, 219, 109),
            new Pixel(91, 109, 182),
            new Pixel(128, 0, 0),
            new Pixel(164, 146, 73),
            new Pixel(201, 36, 146),
            new Pixel(237, 182, 219)
        };

        // assertEquals(7, result.length, "Incorrect number of colors returned from Basic Bucketing generateColorPalette");
        expectedRes = 7;
        actualRes = result.length;
        System.out.println("Expected: " + expectedRes + " | Actual: " + actualRes + " | " + (actualRes == expectedRes ? "SUCCESS!!\n" : "FAIL*********\nIncorrect number of colors returned from Basic Bucketing generateColorPalette\n"));
        
        for (int i = 0; i < expectedCT.length; i++) {
            // assertEquals(expectedCT[i], result[i], "Incorrect color returned for palette of Bucketing");
            actual_pixel = result[i];
            System.out.println("Expected: " + expectedCT[i] + " | Actual: " + actual_pixel + " | " + (actual_pixel.equals(expectedCT[i]) ? "SUCCESS!!\n" : "FAIL*********\nIncorrect color returned for palette of bucketing\n"));
        
        }

    }
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------

    private static void basic_quantize_2d_array(){

        System.out.println("\n----------Farnan's Basic quantizeTo2DArray Test----------");

        Pixel[][] stripedArr = genStripedArr();
        dm = new SquaredEuclideanMetric();
        generator = new ClusteringMapGenerator(dm);
        cq = new ColorQuantizer(stripedArr, generator);

        // Check for 1 color
        Pixel[][] result = cq.quantizeTo2DArray(1);
        Pixel single_expected = new Pixel(125, 125, 125);


        int actualRes = result.length;
        int expectedRes = stripedArr.length;
        //assertEquals(stripedArr.length, result.length, "Incorrect number of rows in quantized pixels");
        System.out.println("Expected: " + expectedRes + " | Actual: " + actualRes + " | " + (actualRes == expectedRes ? "SUCCESS!!\n" : "FAIL*********\nIncorrect number of rows in quantized pixels\n"));
        

        actualRes = result[0].length;
        expectedRes = stripedArr[0].length;
        //assertEquals(stripedArr[0].length, result[0].length, "Incorrect number of columns in quantized pixels");
        System.out.println("Expected: " + expectedRes + " | Actual: " + actualRes + " | " + (actualRes == expectedRes ? "SUCCESS!!\n" : "FAIL*********\nIncorrect number of columns in quantized pixels\n"));
        
        for (int row = 0; row < stripedArr.length; row++) {
            for (int col = 0; col < stripedArr[0].length; col++) {
                //assertEquals(single_expected, result[row][col], "Incorrectly quantized pixel");

                Pixel actualpix = result[row][col];
                Pixel expectedpix = single_expected;
                System.out.println("Expected: " + expectedpix + " | Actual: " + actualpix + " | " + (actualpix.equals(expectedpix) ? "SUCCESS!!\n" : "FAIL*********\nIncorrectly quantized pixel\n"));
        
            }
        }

        // Check for 4 colors
        result = cq.quantizeTo2DArray(4);
        Pixel[] expectedMappings = new Pixel[]{
      			new Pixel(27, 27, 27),
      			new Pixel(125, 125, 125),
      			new Pixel(200, 200, 200),
      			new Pixel(250, 250, 250)
    		};

    		int expected = 0;
        for (int row = 0; row < stripedArr.length; row++) {
            for (int col = 0; col < stripedArr[0].length; col++) {
        				switch (row) {
          					case 0:
          					case 1:
            						expected = 0;
            						break;
          					case 2:
          					case 3:
            						expected = 1;
            						break;
          					case 4:
            						expected = 2;
            						break;
          					default:
            						expected = 3;
        				}
                        Pixel actualpix = result[row][col];
                        Pixel expectedpix = expectedMappings[expected];
                // assertEquals(expectedMappings[expected], result[row][col], "A pixel was mapped to the incorrect reduced color in Clustering");
                System.out.println("Expected: " + expectedpix + " | Actual: " + actualpix + " | " + (actualpix.equals(expectedpix) ? "SUCCESS!!\n" : "FAIL*********\nA pixel was mapped to the incorrect reduced color in Clustering\n"));
        
            }
        }

        
    }
    //----------------
    //----------------
    //----------------

}// end class