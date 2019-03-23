import edu.rit.pj2.Task;
import edu.rit.util.GraphSpec;
import edu.rit.util.Instance;
import edu.rit.pj2.LongLoop;
import static edu.rit.pj2.Task.terminate;

/**
 * Class MainSmp provides a color assignment solution to a given graph
 * with the minimum number of colors.
 *
 * @author  Junan Zhao
 * @version 15-Feb-2019
 */
public class MainSmp extends Task
{
   AMGraph graph;              //the graph to be searched
   int[] globalColorAssigned;  //the array recorded colors assigned to vertices
   int c;                      //number of colors used
   boolean foundNewSolution ;  //flag to mark whether a solution found

   /**
    * Task main program.
    */
   public void main(String[] args) throws Exception
   {
      if(args.length!=1) usage();
      String ctor = args[0];
      graph = new AMGraph((GraphSpec)Instance.newInstance(ctor));
      graph.printGraph();
      
      initialize();   
      for( c=1; c<=graph.V()-1; c++)
         {
            long numOfTry = (long)Math.pow(c,graph.V());  //total number of configurations with current c number of colors 
            foundNewSolution = false;                     //flag to indicate whether a solution found with current c number of colors
            parallelFor(0,numOfTry-1).exec (new LongLoop()
              {
                 public void run(long i)
                 {
                    int[] colorAssigned = colorVertices(i,c);
                    if(checkClash(colorAssigned))                        //found a solution
                      { 
                         globalColorAssigned = colorAssigned.clone();
                         foundNewSolution = true;
                         stop();
                      }
                 }
              });
            if(foundNewSolution) break;
         }
      if(!foundNewSolution) worstCaseSolution();     //worst case, c = number of vertices
      printSolution();
   } 
   
   
   //Initialize every vertex with null color "-1"
   private void initialize()
   {
      globalColorAssigned = new int[graph.V()];
      for( int i=0; i<=this.graph.V()-1; i++)
          globalColorAssigned[i] = -1;
   }
   
   
   //The solution for the worst case, which is C=V
   private void worstCaseSolution()
   {
      globalColorAssigned = new int[graph.V()];
      for( int i=0; i<=this.graph.V()-1; i++)
          globalColorAssigned[i] = i;
   }
   
   
   /** 
     * Color vertices with c colors according to a given serial number n
     *
     * @param  n  the given serial number
     * @param  c  the number of colors
     * 
     * @return  the color solution according to the given n and c
     */
   private int[] colorVertices( long n, int c)   
   {
      int[] colorAssigned = new int[graph.V()]; 
      int i = graph.V() - 1;
      do
        { 
           long quotient = n/c;
           int reminder = (int)(n%c);
           colorAssigned[i] = reminder;
           i--;
           n = quotient;
        }
      while(n!=0);
      return colorAssigned;
   } 
   

   /** 
     * Check all vertices colors, see any clash with its neighbors.
     *
     * @param  colorAssigned  the color solution to be checked
     * 
     * @return  true/false  return true if everything is fine, false if any clash found,
     */
   private boolean checkClash(int[] colorAssigned)
   {
      for( int i=0; i<=graph.V()-1; i++)
          if(!checkClash(i,colorAssigned)) return false;   //clash found. Return false
      return true;       //no clash found. Solution. Return true;
   }
   
    
   /** 
     * Check a single vertex color, and see any clash with its neighbors.
     *
     * @param  v  the index of the vertex
     * @param  colorAssigned  the color solution to be checked
     * 
     * @return  true/false  Return false if any clash found, return true if everything is fine.
     */
   private boolean checkClash( int v, int[] colorAssigned)   
   {
      int[] adjacentVtx = graph.getOneVertexAdjacent(v); 
      for( int i=0; i<=adjacentVtx.length-1; i++)
          if(colorAssigned[adjacentVtx[i]]==colorAssigned[v]) return false;	//clash found, return false
      return true;   //no clash found for this vertex color
   }
 
   
   //Print the color solution
   public void printSolution()
   {
      System.out.println("At least " + c + " colors"); 
      for( int i=0; i<=this.graph.V()-1; i++)
          System.out.println(i + " with color " + globalColorAssigned[i]);
   }

   
   /**
     * Print a usage message and exit.
     */
   private static void usage()
   {
      System.err.println ("Usage: java pj2 edu.rit.pj2.example.HamCycSmp \"<ctor>\" ");
      System.err.println ("<ctor> = GraphSpec constructor expression");
      terminate(1);
   }
}