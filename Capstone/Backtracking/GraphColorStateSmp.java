import edu.rit.pj2.WorkQueue;

/**
 * Class GraphColorStateSmp encapsulates the state of a search for a graph 
 * coloring solution in a graph in a multi-core parallel program. 
 * Class GraphColorStateSmp supports both breadth first search (BFS) 
 * and depth first search (DFS).
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class GraphColorStateSmp extends GraphColorState
{
   // Hidden class data members.    
   private static WorkQueue<GraphColorState> queue;  //Multicore parallel work queue.

   // Exported class operations.

  /**
    * Specify the graph to be analyzed using BFS and DFS.
    *
    * @param  graph      Graph.
    * @param  threshold  Search level threshold at which to switch from BFS to DFS                 
    * @param  queue      Multi-core parallel work queue.
    *
    * @exception  NullPointerException
    *     (unchecked exception) Thrown if <TT>graph</TT> is null. Thrown if
    *     <TT>queue</TT> is null.
    * @exception  IllegalArgumentException
    *     (unchecked exception) Thrown if <TT>threshold</TT> &lt; 0.
    */
   public static void setGraph( AMGraph graph, int threshold, WorkQueue<GraphColorState> queue)
   {
      if(queue==null) throw new NullPointerException("GraphColorStateSmp.setGraph(): queue is null");
      GraphColorState.setGraph (graph);
      GraphColorState.threshold = threshold;
      GraphColorStateSmp.queue = queue;
   }

  
// Exported constructors.

  /**
    * Construct a new search state object that supports BFS and DFS. All
    * instances of class GraphColorStateSmp will analyze the graph specified
    * in the {@link #setGraph(AMGraph,int,WorkQueue) setGraph()} method.
    */
  public GraphColorStateSmp( int numOfColors)
  {
     super(numOfColors);
  }

  
// Hidden operations.

  /**
    * Enqueue the given search state object during a BFS.
    *
    * @param  state  Search state object to enqueue.
    */
  protected void enqueue(GraphColorState state)
  {
     queue.add (state);
  }
}
