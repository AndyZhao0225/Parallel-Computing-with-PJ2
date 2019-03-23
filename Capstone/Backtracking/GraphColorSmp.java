import edu.rit.pj2.ObjectLoop;
import edu.rit.pj2.Task;
import static edu.rit.pj2.Task.terminate;
import edu.rit.pj2.WorkQueue;
import edu.rit.util.GraphSpec;
import edu.rit.util.Instance;

/**
 * Class GraphColorSmp provides a color assignment solution to a given graph
 * with the minimum number of colors.
 *
 * @author  Junan Zhao
 * @version 28-Feb-2019
 */
public class GraphColorSmp extends Task
{
   GraphColorState graphColor;  // Graph color assignment that was found.
        
	/**
	 * Task main program.
	 */
   public void main(String[] args) throws Exception
   {
      if(args.length!=2) usage();// Parse command line arguments.
      String ctor = args[0];
      int threshold = Integer.parseInt(args[1]);		
      WorkQueue<GraphColorState> queue = new WorkQueue<GraphColorState>();  // Set up parallel work queue.	
      AMGraph graph = new AMGraph((GraphSpec)Instance.newInstance(ctor)); // Construct graph spec, set up graph.
      graph.printGraph();
      GraphColorStateSmp.setGraph(graph,threshold,queue);    
      
      for( int i=1; i<=GraphColorStateSmp.V-1; i++)     //try from using only one color all the way to V-1 colors, until found an answer
         {                                   
            queue.add(new GraphColorStateSmp(i)); // Add first work item to work queue.          
            parallelFor( queue).exec( new ObjectLoop<GraphColorState>()   // Search the graph in parallel.
            {
               public void run( GraphColorState state)
	       {
	          GraphColorState solution = state.search();
		  if(solution!=null)
	            {
		       GraphColorState.stop();
		       graphColor = solution;
	            }
               }
	    });
                      
            if(graphColor!=null) break;
         }
      
		// Print results.
      if(graphColor==null)  //worst case, has to color with V colors
        {
           graphColor = new GraphColorStateSmp(GraphColorStateSmp.V);
           graphColor.worstCase();
        }
      System.out.println(graphColor);
   }

	/**
	 * Print an error message and exit.
         * 
         * @param  msg     error message
	 */
   private static void usage( String msg)
   {
      System.err.printf( "GraphColorSmp: %s%n", msg);
      usage();
   }

	/**
	 * Print a usage message and exit.
	 */
   private static void usage()
   {
      System.err.println( "Usage: java pj2 GraphColorSmp \"<ctor>\" <threshold>");
      System.err.println( "<ctor> = GraphSpec constructor expression");
      System.err.println( "<threshold> = Parallel search threshold level");
      terminate( 1);
   }
   
   
   protected static int coresRequired()
   {
      return 84;
   }
}
