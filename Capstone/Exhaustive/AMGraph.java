import edu.rit.util.BitSet;
import edu.rit.util.GraphSpec;

/**
 * Class AMGraph provides an adjacency matrix data structure for a graph with
 * any number of vertices.
 *
 * @author  Alan Kaminsky / Junan Zhao
 * @version 12-Feb-2019
 */
public class AMGraph
	{
// Hidden data members.

	// Number of vertices.
	private int V;

	// The graph's adjacency matrix. adjacent[i] is the set of vertices adjacent
	// to vertex i.
	private BitSet[] adjacent;

// Exported constructors.

	/**
	 * Construct a new uninitialized adjacency matrix.
	 */
	public AMGraph()
		{
		}

        
	/**
	 * Construct a new adjacency matrix for the given graph specification.
	 *
	 * @param  gspec  Graph specification.
	 */
	public AMGraph
		(GraphSpec gspec)
		{
		set (gspec);
		}

// Exported operations.
        
        /**
	 * Get the adjacent vertices of the i-th  i-th vertex 
	 *
	 * @param  i  The vertex index
         * 
         * @return  the adjacent vertices of the given vertex
	 */              
        public int[] getOneVertexAdjacent( int i)
        {
           int[] array = new int[adjacent[i].size()];
           return adjacent[i].toArray(array);
        }        
            
        
	/**
	 * Set this adjacency matrix to the given graph specification.
	 *
	 * @param  gspec  Graph specification.
	 */
	public void set
		(GraphSpec gspec)
		{
		V = gspec.V();
		if (V < 0)
			throw new IllegalArgumentException (String.format
				("AMGraph.set(): V = %d illegal", V));
		if (adjacent == null || adjacent.length != V)
			{
			adjacent = new BitSet [V];
			for (int i = 0; i < V; ++ i)
				adjacent[i] = new BitSet (V);
			}
		else
			{
			for (int i = 0; i < V; ++ i)
				adjacent[i].clear();
			}
		while (gspec.hasNext())
			{
			GraphSpec.Edge edge = gspec.next();
			adjacent[edge.v1].add (edge.v2);
			adjacent[edge.v2].add (edge.v1);
			}
		}

                
	/**
	 * Get the number of vertices in this graph.
	 *
	 * @return  Number of vertices.
	 */
	public int V()
		{
		return V;
		}

        
	/**
	 * Determine if the given vertices are adjacent in this graph.
	 *
	 * @param  v1  First vertex.
	 * @param  v2  Second vertex.
	 *
	 * @return  True if <TT>v1</TT> is adjacent to <TT>v2</TT>, false otherwise.
	 */
	public boolean isAdjacent
		(int v1,
		 int v2)
		{
		return adjacent[v1].contains (v2);
		}

                
	/**
	 * Determine if the given vertex set is a vertex cover for this graph.
	 *
	 * @param  vset  Vertex set.
	 */
	public boolean isVertexCover
		(BitSet vset)
		{
		boolean covered = true;
		for (int i = 0; covered && i < V; ++ i)
			if (! vset.contains (i))
				covered = adjacent[i].isSubsetOf (vset);
		return covered;
		}

                
  //Print the adjacency matrix of the graph                  
  public void printGraph()
  {
     for( int i=0; i<=adjacent.length-1; i++)
        {
           System.out.print("Vertex " + i + " : ");
           int[] array = new int[adjacent[i].size()];
           adjacent[i].toArray(array);
           for( int j=0; j<=array.length-1; j++)
               System.out.print(array[j] + " ");
           System.out.println();         
        }
  }                 
}
