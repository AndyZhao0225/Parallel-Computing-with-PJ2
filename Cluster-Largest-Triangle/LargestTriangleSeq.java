import edu.rit.pj2.Task;
import edu.rit.util.Instance;

/**
 * Given a scatter random two-dimensional points, Class LargestTriangleSeq aims at
 * finding a triangle with the largest area, in which the triangle's vertices are 
 * from the given points. 
 * This is the sequential version
 *
 * @author  Junan Zhao
 * @version 20-Oct-2018
 */
public class LargestTriangleSeq extends Task 
{ 
   PointSpec pointSpec;   //the PoinSpec instance to generate points  
   int N;                       //the number of points
   Point[] points;              //it stores points' coordinates
   double[][] distance;         //it stores disntances between points
   Answer answer;               //the answer we found, the triangle with largest area
   
   //main method
   public void main(String[] args) throws Exception
   {
      // Validate command line arguments. 
      if(args.length!=1) usage();
  
      initialize(args[0]);                              //Initialization.   
      TriangleAgent.recordPoints(pointSpec,points);     //Record points coordinates.      
      TriangleAgent.recordDistance(distance,points,N);  //Record distances between points.
      
      //Loop through all possible triangles to find the one with largest area
      for( int i=0; i<=N-3; i++)
         {
            for( int j=i+1; j<=N-2; j++)
               {
                  for( int k=j+1; k<=N-1; k++)
                      TriangleAgent.compareTwoTriangles(answer,distance,i,j,k);
               }
         }
      TriangleAgent.printTheAnswer(answer,points);
   }
   
   
   /**
    * Initialization 
    *
    * @param  args  a constructor expression for a PointSpec object 
    *
    */
   private void initialize(String arg) 
   {
      try
        {
           pointSpec = (PointSpec)Instance.newInstance(arg);
           N = pointSpec.size();
           answer = new Answer(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1);
           points = new Point[N];
           for( int i=0; i<=N-1; i++)
               points[i] = new Point();
           distance = new double[N][N];
        }
      catch(Exception ex)
        {
           System.err.println("LargestTriangleSeq: Cannot construct point spec \"" + arg + "\"");
           usage();
           terminate(1);
        }
   }
   
   
   // Print a usage message and exit.
   private static void usage()
   {
      System.err.println ("Usage: java pj2 LargestTriangleSeq <ctor>");
      terminate(1);
   }
   
   
   // Specify that this task requires one core.
   protected static int coresRequired()
   {
      return 1;
   }
}
