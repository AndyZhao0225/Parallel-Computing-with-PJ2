import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.io.Streamable;
import java.io.IOException;
import java.util.*;

/**
 * Class GraphColor encapsulates the state of a search for a coloring assignment
 * in a graph. It applies the iterative greedy algorithm in its search.
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class GraphColor implements Cloneable, Streamable
{
   static AMGraph graph; // The graph being searched.
   static int V;
   static long S;    //the number of steps 
   int[] minColors;  //the array stored the best solution found
   int minC;  //the min num of colors
   int[] colors; //the solution 
   int C; //number of colors
   
   
   /**
    * Constructor. Construct a new search state GraphColor object 
    */
   public GraphColor()
   {
      colors = new int[V];
      for( int i=0; i<=V-1; ++i)
          colors[i] = -1;
      C = Integer.MAX_VALUE;
      minColors = colors.clone();
      minC = C;    
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
     * Set the number of steps
     *
     * @param  newN  the new number of steps
     */
   public static void setS( long newS)
   {
      S = newS;
   }
   
   
   /**
    * Clear this state. All the fields are set to default values.
    */
   public void clear()
   {
      for( int i=0; i<=V-1; ++i)
          colors[i] = -1;
      C = Integer.MAX_VALUE;
      minColors = colors.clone();
      minC = C;
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
      this.minColors = graClr.minColors==null? null: (int[]) graClr.minColors.clone();
      this.minC = graClr.minC;
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
      iterativeGreedy();
      
   }
   
   
   /**
    *  The iterative greedy algorithm
    */
   private void iterativeGreedy()
   {
      for( int i=0; i<=S-1; i++)
         {
            countColors();
            List<Set> colorList = buildColorList();
            List<Integer> setOrder = shuffleSetOrder();
            List<Integer> order = createVerticesOrder(colorList,setOrder); 
            greedy(order);
         } 
      
   }
   
   
   /**
    * Create a vertices order
    *
    * @param  colorList  a list contains all color sets.
    * @param  order      the order of color sets
    */
   private List<Integer> createVerticesOrder( List<Set> colorList, List<Integer> order) 
   {
      List<Integer> vOrder = new ArrayList();     
      for( int i=0; i<=C-1; i++)
         {
            int setIndex = order.get(i);
            Iterator<Integer> it = colorList.get(setIndex).iterator();
            while(it.hasNext()) vOrder.add(it.next());
         }
      return vOrder;
   }
   
   
   /**
    * Create a shuffled order of the existed color sets
    */
   private List<Integer> shuffleSetOrder()
   {
      List<Integer> order = new ArrayList();
      for( int i=0; i<=C-1; ++i)
          order.add(i);
      Collections.shuffle(order);
      return order;
   }
   
   
   /**
    * Build a color list which contains all color sets 
    */
   private List<Set> buildColorList()
   {
      List<Set> colorList = new ArrayList<Set>();
      for( int i=0; i<=C-1; i++)
         {
            Set<Integer> colorSet = new HashSet<Integer>();
            colorList.add(colorSet);
         }
      for( int j=0; j<=V-1; j++)
          colorList.get(colors[j]).add(j);
      return colorList;
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
      if(C<minC)
        {
           minC = C;
           minColors = colors.clone();
        } 
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
      if(this.minC>graClr.minC) this.copy(graClr); 
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
            b.append("\n\n");
         }
      b.append("\n");
      b.append("Min num of colors = " + minC);
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
      out.writeIntArray(minColors);
      out.writeInt(minC);
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
      minColors = in.readIntArray();
      minC = in.readInt();
      if(colors.length!=V || minColors.length!=V/*|| order.size()!=V*/) 
         throw new IOException(String.format("GraphColorState.readIn(): Mismatch: V = %d, colors.length = %d",V,colors.length));
   } 
}