import edu.rit.pj2.Task;
import edu.rit.pj2.LongLoop;
import edu.rit.util.GraphSpec;
import edu.rit.util.Instance;

/**
 * Class MpgSmp provides a color assignment solution to a given graph
 * with an approximate minimum number of colors, by using greedy algorithm.
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class MpgSmp extends Task
{	
   GraphColorVbl graphColorVbl;  //The global search state
   
   /**
     * Task main program.
     */
   public void main(String[] args) throws Exception
   {
      if(args.length!=2) usage();
      final String ctor = args[0];
      final long N = Long.parseLong(args[1]); //number of trails
      
      AMGraph graph = new AMGraph((GraphSpec)Instance.newInstance(ctor));
      graph.printGraph();
      GraphColor.setGraph(graph);
      
      graphColorVbl = new GraphColorVbl();
                     
      parallelFor(1L,N).exec(new LongLoop()
      {
         GraphColorVbl thrGraClrVbl;
         
         public void start()
         {
            thrGraClrVbl = threadLocal(graphColorVbl);
         }
         
         public void run( long i)
         {        
            thrGraClrVbl.graphColor.search(); 		
	 }     
      });
            
      System.out.println(graphColorVbl.graphColor.toString());
   }
   
   
   /**
     * Print a usage message and exit.
     */
   private static void usage()
   {
      System.err.println("Usage: java pj2 MpgSmp <ctor> <N>");
      System.err.println ("<ctor> = GraphSpec constructor expression");
      System.err.println ("<N> = Number of trials");
      terminate(1);
   }
}