import edu.rit.pj2.Task;
import edu.rit.pj2.LongLoop;
import edu.rit.util.GraphSpec;
import edu.rit.util.Instance;

/**
 * Class MpslsSmp provides a color assignment solution to a given graph
 * with an approximate minimum number of colors, by using a stochastic
 * local search algorithm.
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class MpslsSmp extends Task
{	
   GraphColorSearch graphColor;  //The global search state
   int k;   //current number of colors used 
   
   /**
     * Task main program.
     */
   public void main(String[] args) throws Exception
   {
      if(args.length!=4) usage();
      final String ctor = args[0];
      final long N = Long.parseLong(args[1]); //number of repetitions for k-color
      final long S = Long.parseLong(args[2]); //number of steps in each step
      final double P = Double.parseDouble(args[3]);
      
      AMGraph graph = new AMGraph((GraphSpec)Instance.newInstance(ctor));
      int V = graph.V();
      graph.printGraph();
      GraphColorSearch.setGraph(graph);
      GraphColorSearch.setS(S);
      GraphColorSearch.setP(P);
              
      for( k=1; k<=V-1; k++) 
         {         
            parallelFor(1L,N).exec(new LongLoop()
            {
	       public void run( long i)
               {        
                  GraphColorSearch solution = new GraphColorSearch(k);
		  if(solution.search())
	            {    
		       GraphColorSearch.stop();
		       graphColor = solution;         
	            } 			
	       }     
            });
            
            if(graphColor!=null) break;
         }
      
      if(graphColor==null)  //worst case, has to color with V colors
        {
           graphColor = new GraphColorSearch(GraphColorSearch.V);
           graphColor.worstCase();
        }
      System.out.println(graphColor);
   }
   
   
   /**
     * Print a usage message and exit.
     */
   private static void usage()
   {
      System.err.println("Usage: java pj2 MpslsSmp <ctor> <N> <S> <P>");
      System.err.println("<ctor> = GraphSpec constructor expression");
      System.err.println("<N> = Number of repetitions for k-color search");
      System.err.println("<S> = Number of steps within each repetition");
      System.err.println("<P> = The possibility to take a random choice color");
      terminate(1);
   }
}