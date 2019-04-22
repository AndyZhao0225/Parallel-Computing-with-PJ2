import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import java.io.IOException;

/**
 * Class GraphColorState encapsulates the state of a search for a coloring 
 * assignment in a graph.
 * 
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class GraphColorState extends Tuple
{   // Hidden class data members.   
   static AMGraph graph; // The graph being searched.
   static int V;
   static int threshold;  // Search level threshold at which to switch from BFS to DFS.
   static volatile boolean stop;  // Flag to stop search.
// Hidden instance data members.
   private int[] colors; //colors assigned 
   private int level;  // Search level = index of last vertex in the path.
   private int C;   //number of colors
   
   
// Exported class operations.

   /**
     * Specify the graph to be analyzed.
     *
     * @param  graph  Graph.
     *
     * @exception  NullPointerException
     *     (unchecked exception) Thrown if <TT>graph</TT> is null.
     */
   public static void setGraph(AMGraph graph)
   {
      GraphColorState.graph = graph;
      GraphColorState.V = graph.V();
      GraphColorState.threshold = 0;
      GraphColorState.stop = false;
   }

   
// Exported constructors.

   /**
     * Constructor. Construct a new search state object that supports with 
     * the given number of colors limit. 
     * 
     * @param numOfColors  the limit of number of colors 
     */
   public GraphColorState( int numOfColors)
   {  
      C = numOfColors;
      colors = new int[V];
      for( int i=0; i<=V-1; ++i)
	  colors[i] = -1;
      level = 0;
   }

   
// Exported operations.
   
   /**
     * Clone this search state object.
     *
     * @return  Clone.
     */
   public Object clone()
   { 
      GraphColorState graph = (GraphColorState) super.clone();
      graph.colors = (int[]) this.colors.clone();
      graph.level = level;
      graph.C = C;
      return graph;
   }

   
   /**
     * Stop the search in progress.
     */
   public static void stop()
   {
      stop = true;
   }

   
   /**
     * Returns a string version of this search state object.
     *
     * @return  String version.
     */
   public String toString() 
   {
      StringBuilder b = new StringBuilder();
      b.append("\n\n");
      b.append("Solution\n");
      for( int i=0; i<=V-1; i++)
         {
            b.append("Vertex " + i + " = Color " + colors[i]);
            b.append("\n");
         }
      b.append("\n");
      b.append("Min num of colors = " + C);
      b.append("\n");
      return b.toString();
   }
   

   /**
     * Write this search state object to the given out stream.
     *
     * @param  out  Out stream.
     *
     * @exception  IOException
     *     Thrown if an I/O error occurred.
     */
   public void writeOut(OutStream out) throws IOException
   {
      out.writeIntArray(colors);
      out.writeInt(level);
      out.writeInt(C);
   }

   
   /**
     * Read this search state object from the given in stream.
     *
     * @param  in  In stream.
     *
     * @exception  IOException
     *     Thrown if an I/O error occurred.
     */
   public void readIn(InStream in) throws IOException
   {
      colors = in.readIntArray();
      level = in.readInt();
      C = in.readInt();
      if(colors.length!=V) 
         throw new IOException(String.format("GraphColorState.readIn(): Mismatch: V = %d, colors.length = %d",V,colors.length));
   }
   
   
   /**
     * Have to color with V colors. The worst case: 
     */
   public void worstCase() 
   {
      C = V;
      for( int i=0; i<=V-1; i++)
          colors[i] = i;   
   }
   
   
   /**
     * Search the graph from this state.
     *
     * @return  Search state object containing the color assignment found, or
     *          null if any color solution was not found.
     */
   public GraphColorState search()
   {
      return(level<threshold)? bfs(): dfs();
   }
   
   
// Hidden operations. 
   
   /**
     * Do a breadth first search of the graph from this state.
     *
     * @return  Search state object containing the color solution found, or
     *          null if any color solution was not found.
     */
   private GraphColorState bfs()
   {    
      if(level==V) return this;
      int min = level<C-1? level: C-1;
      for( int i=0; i<=min && !stop; i++)  //try every possible color at this level
         {
            colors[level] = i;
            if(isValid(level))    
              {
                 ++level; 
	         enqueue((GraphColorState)this.clone());
		 --level;
              }
         }
      return null;       
   }

   
   /**
     * Do a depth-first-search of the graph from this state.
     *
     * @return  Search state object containing the color solution found, or
     *          null if any color solution was not found.
     */
   private GraphColorState dfs()
   {       
      if(level==V) return this;  
      int min = level<C-1? level: C-1;
      for( int i=0; i<=min && !stop; i++)
         {
            colors[level] = i;         
            if(isValid(level))
              {
                 level++; 
                 if(dfs()!=null) return this;
                 level--;
              }
            colors[level] = -1;    //don'e know why but this way fixed a bug
         }
      return null;
   }
   
   
   /**
     * Check if the color of the vertex v has any clash with its neighbors.
     *
     * @param  v  the index of the vertex 
     * 
     * @return  true/false  true if no clash found, else false
     */
   private boolean isValid( int v)
   {
      int[] adjacentVtx = graph.getOneVertexAdjacent(v); 
      for( int i=0; i<=adjacentVtx.length-1; i++)
          if(colors[adjacentVtx[i]]==colors[v]) return false;	//clash found, not valid, return false
      return true;  //it's valid
   }
   
   
   /**
     * Enqueue the given search state object during a BFS.
     * <P>
     * The base class <TT>enqueue()</TT> method throws an unsupported operation
     * exception, because the base class does not support BFS. A subclass may
     * override the <TT>enqueue()</TT> method to place the search state object
     * in a queue. 
     *
     * @param  state  Search state object to enqueue.
     */	
   protected void enqueue(GraphColorState state)
   {
      throw new UnsupportedOperationException();
   }
}