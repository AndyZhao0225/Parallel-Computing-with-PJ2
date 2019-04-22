import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.io.Streamable;
import java.io.IOException;
import java.util.*;

/**
 * Class GraphColor encapsulates the state of a search for a coloring assignment
 * in a graph. It applies the greedy algorithm in its search.
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class GraphColor implements Cloneable, Streamable
{
   static AMGraph graph; // The graph being searched.
   static int V;
   int[] colors; //the solution 
   int C; //number of colors used
   
   
   public GraphColor()
   {
      colors = new int[V];
      for( int i=0; i<=V-1; ++i)
          colors[i] = -1;   
      C = Integer.MAX_VALUE;
   }


   /**
    * Construct a new search state that is a deep copy of the given state.
    *
    * @param  graClr  GraphColor to copy.
    */
   public GraphColor(GraphColor graClr)
   {
      copy(graClr);
   }

   
   /**
     * Specify the graph to be analyzed.
     *
     * @param  graph  the graph.
     */   
   public static void setGraph( AMGraph graph)
   {
      GraphColor.graph = graph;
      GraphColor.V = graph.V();
   }
    
   
   /**
    * Clear this state. All the fields are set to default values.
    */ 
   public void clear()
   {
      for( int i=0; i<=V-1; ++i)
          colors[i] = -1;    
      C = Integer.MAX_VALUE;
   }

   
   /**
    * Make this search state be a deep copy of the given state.
    *
    * @param  graClr  the search state GraphColor to copy.
    *
    * @return  This search state.
    */
   public GraphColor copy( GraphColor graClr)
   {
      this.colors = graClr.colors==null? null: (int[]) graClr.colors.clone();
      this.C = graClr.C;
      return this;
   }

   
   /**
     * Clone this search state object.
     *
     * @return  Clone.
     */
   public Object clone()
   {
      try
        {
           GraphColor graClr = (GraphColor)super.clone();
           graClr.copy(this);
           return graClr;
        }
      catch(CloneNotSupportedException exc)
        {
           throw new RuntimeException ("Shouldn't happen", exc);
        }
   }

   
   /** 
    * Search the graph from this state.
    */
   public void search()
   {
      List<Integer> order = createOriOrder();
      Collections.shuffle(order);
      greedy(order);
      countColors();
   }
   
   
   /**
    * Count how many colors have been used in the current state
    */
   private void countColors()
   {
      Set<Integer> colorsSet = new HashSet<Integer>(); 
      for( int i=0; i<=V-1; ++i)
          colorsSet.add(colors[i]);
      C = colorsSet.size();
   }
   
   
   /**
    * Create the default order of vertices
    */
   private List<Integer> createOriOrder()
   {
      List<Integer> order = new ArrayList();
      for( int i=0; i<=V-1; ++i)
          order.add(i);
      return order;
   }
   
   
   /**
    *  The greedy algorithm to the given order of vertices
    *
    *  @param  order   the order of vertices
    */
   private void greedy( List<Integer> order)
   {
      for( int i=0; i<=V-1; i++)
         {
            int v = order.get(i);         
            Set<Integer> forbiddenColors = new HashSet<Integer>();
            int[] adjacentVtx = graph.getOneVertexAdjacent(v);           
            for( int j=0; j<=adjacentVtx.length-1; j++)
                forbiddenColors.add(colors[adjacentVtx[j]]);
            for( int c=0; c<=V-1; c++)
               {
                  if(!forbiddenColors.contains(c))
                    {
                       colors[v] = c;
                       break;
                    }
               }
         }
   }
   
   
   /**
    * Fuse the given search state to this state. The method compares the 
    * given search state with this one. It will copy the given state
    * if the given state has less colors used; otherwise no change.
    *
    * @param  graClr  The GraphColor search state to fuse.
    */ 
   public void fuse( GraphColor graClr)
   {
      if(this.C>graClr.C) this.copy(graClr); 
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
   public void writeOut( OutStream out) throws IOException
   {
      out.writeIntArray(colors);
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
   public void readIn( InStream in) throws IOException
   {
      colors = in.readIntArray();
      C = in.readInt();
      if(colors.length!=V) 
         throw new IOException(String.format("GraphColorState.readIn(): Mismatch: V = %d, colors.length = %d",V,colors.length));
   } 
}