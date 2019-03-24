import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import java.io.IOException;
import java.util.*;

/**
 * Class GraphColorSearch encapsulates the state of a search for a coloring assignment
 * in a graph.
 * The GraphColorState base class supports depth first search (DFS) in a sequential
 * program. Subclasses support breadth first search (BFS) and DFS in parallel
 * programs.
 *
 * @author  Junan Zhao
 * @version 28-Feb-2018
 */
public class GraphColorSearch extends Tuple
{
   static AMGraph graph;  //the graph to be searched in
   static int V;   //the number of vertices
   static volatile boolean stop;  // flag to stop search.
   static long N; //number of steps 
   static double P;  //the possibility to take a random color choice
   private int C;   //number of colors
   private int[] colors; //colors assigned
   private Set<Integer> clashSet; //a set collected all vertices with clash
  
   /**
     * Constructor. Construct a new search state object that supports with 
     * the given number of colors limit. 
     * 
     * @param numOfColors  the limit of number of colors  
     */
   public GraphColorSearch( int numOfColors)
   {  
      C = numOfColors;
      colors = new int[V];
      for( int i=0; i<=V-1; ++i)
          colors[i] = (int)(Math.random()*C);
      clashSet = new HashSet<Integer>();
   }
  
   
   /**
     * Specify the graph to be analyzed.
     *
     * @param  graph  the graph.
     *
     * @exception  NullPointerException
     *     (unchecked exception) Thrown if <TT>graph</TT> is null.
     */
   public static void setGraph( AMGraph graph)
   {
      GraphColorSearch.graph = graph;
      GraphColorSearch.V = graph.V();
      GraphColorSearch.stop = false;
   }
   
   
   /**
     * Set the number of steps
     *
     * @param  newN  the new number of steps
     */
   public static void setN( long newN)
   {
      N = newN; 
   }
   
   
    /**
     * Set the possibility to take a random color choice
     *
     * @param  newP  the new possibility
     */
   public static void setP( double newP)
   {
      P = newP;
   }
   
   
   /**
     * Clone this search state object.
     *
     * @return  Clone.
     */
   public Object clone()
   { 
      GraphColorSearch state = (GraphColorSearch) super.clone();
      state.colors = (int[]) this.colors.clone();
      state.C = C;
      return state; 
   }
   
   
   /**
     * Search the graph from this state.
     *
     * @return  true/false  true if a valid color solution found, else false
     */
   public boolean search()
   {
      if(isValid()) return true; 
      for( int i=0; i<=N-1; i++)
         {
            int v = pickRandomOneFromSet(clashSet);
            if(v==-1)
              {
                 System.out.println("Something wrong");
                 return false;
              }
            pickAReplaceColor(v);
            if(isValid()) return true;
         } 
      return false;
   }
   
   
   /**
     * Pick a random integer from a set
     *
     * @param  set  the integer set 
     * 
     * @return  a random integer from the set
     */
   public static int pickRandomOneFromSet( Set<Integer> set)
   {
      int size = set.size();
      int index = (int)(Math.random()*size);
      int j = 0;
      for(Integer ele : set)
         {
            if(j==index) return ele;
            j++;
         }         
      return -1; 
   }
   
   
   /**
     * Pick a substitute color for a vertex with color clash 
     *
     * @param  v  the vertex
     */
   private void pickAReplaceColor( int v)
   {
      int originalColor = colors[v];
      int[] clashTable = new int[C];
      Set<Integer> noClashColorSet = new HashSet<Integer>();
      for( int i=0; i<=C-1; i++)
         {
            clashTable[i] = tryColor(v,i,originalColor);
            if(clashTable[i]==0) noClashColorSet.add(i);
         }
      if(noClashColorSet.size()>0)
        {
           int pickedColor = pickRandomOneFromSet(noClashColorSet);
           colors[v] = pickedColor;
        } 
      else if(Math.random()<P) colors[v] = (int)(Math.random()*C);
      else colors[v] = indexWithMinValue(clashTable);
   }
   
   
   /**
     * Find the index of the minimum value within an integer array
     *
     * @param  array  the array of integers 
     * 
     * @return  the index which points to the minimum value
     */
   public static int indexWithMinValue( int[] array)
   {
      int min = array[0];
      int minIndex = 0;
      for( int i=1; i<=array.length-1; i++)
         {
            if(array[i]<min)
              {
                 min = array[i];
                 minIndex = i;
              }
         }
      Set<Integer> minSet = new HashSet<Integer>();
      for( int i=0; i<=array.length-1; i++)
          if(array[i]==min) minSet.add(i);
      minIndex =  pickRandomOneFromSet(minSet);  
      return minIndex;
   }
   
   
   /**
     * Try color c on vertex v and return the count of clashes of the vertex 
     *
     * @param  v  the index of the vertex 
     * @param  c  the color to try
     * @param  originalColor  the original color of the vertex  
     * 
     * @return  the number of clashes found
     */
   private int tryColor( int v, int c, int originalColor)
   {
      colors[v] = c;
      int numOfClash = countClash(v); 
      colors[v] = originalColor; 
      return numOfClash;
   }
   
   
   /**
     * Count color clashes the vertex has with its neighbors
     *
     * @param  v  the index of the vertex 
     * 
     * @return  the number of clashes found
     */
   private int countClash( int v)
   {
      int count = 0;
      int[] adjacentVtx = graph.getOneVertexAdjacent(v); 
      for( int i=0; i<=adjacentVtx.length-1; i++)
          if(colors[adjacentVtx[i]]==colors[v]) count++;	
      return count;
   }
   
   
   /**
     * Check if the current color solution is valid 
     * 
     * @return  true/false  true if no clash found, else false
     */
   private boolean isValid()
   {
      clashSet.clear();
      boolean judge = true;
      for( int i=0; i<=V-1; i++)
         { 
            if(!isValid(i)) 
              {
                 judge = false; 
                 clashSet.add(i);
              }
         }
      return judge;
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
     * Stop the search in progress.
     */
   public static void stop()
   {
      stop = true;
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
     * Returns a string version of this search state object.
     *
     * @return  String version.
     */
   public String toString() 
   {
      StringBuilder b = new StringBuilder();
      b.append("\n");
      b.append("Min num of colors = " + C);
      b.append("\n\n");
      b.append("Solution\n");
      for( int i=0; i<=V-1; i++)
         {
            b.append("Vertex " + i + " = Color " + colors[i]);
            b.append("\n");
         }
      return b.toString();
   }
   
    
   /**
     * Read this search state object from the given in stream.
     *
     * @param  in  In stream.
     *
     * @exception  IOException
     *     Thrown if an I/O error occurred.
     */
   public void readIn( InStream in) throws IOException
   {
      colors = in.readIntArray();
      C = in.readInt();
      if(colors.length!=V) 
         throw new IOException(String.format("GraphColorState.readIn(): Mismatch: V = %d, colors.length = %d",V,colors.length));
   } 
   
   
   /**
     * Write this search state object to the given out stream.
     *
     * @param  out  Out stream.
     *
     * @exception  IOException
     *     Thrown if an I/O error occurred.
     */
   public void writeOut( OutStream out) throws IOException
   {
      out.writeIntArray(colors);
      out.writeInt(C); 
   }
}