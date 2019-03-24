import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.util.Instance;
import edu.rit.pj2.Loop;

/**
 * Given a scatter random two-dimensional points, Class LargestTriangleSeq aims at
 * finding a triangle with the largest area, in which the triangle's vertices are 
 * from the given points. 
 * This is the cluster parallel version
 *
 * @author  Junan Zhao
 * @version 20-Oct-2018
 */
public class LargestTriangleClu extends Job
{
   PointSpec pointSpec;       //the PoinSpec instance to generate points.
   int N;                     //the number of points
    
   //main method
   public void main(String[] args) throws Exception
   {
      // Validate command line arguments. 
      if(args.length!=1) usage();
      
      //Initialization. See initializeJob(String[] args) below for detail
      initializeJob(args[0]);

      // Set up a task group of K worker tasks.
      masterFor(0,N-3,WorkerTask.class).args("" + args[0]);
      
      // Set up reduction task.
      rule().atFinish().task(ReduceTask.class).runInJobProcess();
   }    
   
   
   /**
    * Initialization at the Job level
    *
    * @param  args  a constructor expression for a PointSpec object 
    * 
    */
   private void initializeJob(String arg) 
   {
      try
        {
           pointSpec = (PointSpec)Instance.newInstance(arg);
           N = pointSpec.size();
        }
      catch(Exception ex)
        {
           System.err.println("LargestTriangleClu: Cannot construct point spec \"" + arg + "\"");
           usage();
           terminate(1);
        }
   }
   
   
   // Print a usage message and exit.
   private static void usage()
   {
      System.err.println("Usage: java pj2 [workers=<K>] LargestTriangleClu <ctor>");
      terminate(1);
   }
   
   
   // Task subclasses.

   /**
    * Class LargestTriangleClu.WorkerTask performs part of the computation for the
    * LargestTriangle program.
    *
    * @author  Junan Zhao
    * @version 20-Oct-2018
    */
   private static class WorkerTask extends Task
   {
      //global variables at the task level 
      PointSpec pointSpec;       //the PoinSpec instance to generate points
      Point[] points;               //it stores points' coordinates
      double[][] distance;          //it stores disntances between points
      AnswerVbl ansVbl;             //the answer we found, the triangle with largest area
      int N;                        //the number of points
      
      //main method
      public void main(String[] args) throws Exception
      {
         initialize(args[0]);                                //Initialization.          
         TriangleAgent.recordPoints(pointSpec,points);    //Record points coordinates.  
         TriangleAgent.recordDistance(distance,points,N);    //Record distances between points.
         
         //Loop through all possible triangles to find the one with largest area
         workerFor().schedule(dynamic).exec(new Loop() 
         { 
             AnswerVbl thrAnsVbl;    
             
             public void start() 
             { 
                thrAnsVbl = threadLocal(ansVbl); 
             }
             
             public void run(int i) 
             { 
                for( int j=i+1; j<=N-2; j++)
                   {
                      for( int k=j+1; k<=N-1; k++)
                          TriangleAgent.compareTwoTriangles(thrAnsVbl.ans,distance,i,j,k);
                   } 
                TriangleAgent.recordCoordinates(points,thrAnsVbl);
             } 
         }); 
         
         putTuple(ansVbl);   // Report results.  
      }
      
      
      /**
      * Initialization at the task level 
      *
      * @param  args  a constructor expression for a PointSpec object 
      * @exception  Exception
      *     Thrown if an error occurred.
      */
      private void initialize(String arg) throws Exception
      {
         pointSpec = (PointSpec)Instance.newInstance(arg);
         N = pointSpec.size();
         points = new Point[N];
         for( int i=0; i<=N-1; i++)
             points[i] = new Point();
         distance = new double[N][N];
         ansVbl = new AnswerVbl();
      }
   }
   
   
   /**
    * Class LargestTriangleClu.ReduceTask combines the worker tasks' results and 
    * print overall result for the LargestTriangleClu program.
    *
    * @author  Junan Zhao
    * @version 20-Oct-2018
    */
   private static class ReduceTask extends Task
   {
      //main method 
      public void main(String[] args) throws Exception
      {
         AnswerVbl answerVbl = new AnswerVbl(); 
         AnswerVbl template = new AnswerVbl(); 
         AnswerVbl taskAnswerVbl; 
         while((taskAnswerVbl=tryToTakeTuple(template))!=null) 
               answerVbl.reduce(taskAnswerVbl);
         TriangleAgent.printTheAnswer(answerVbl);
      }
   }
}
