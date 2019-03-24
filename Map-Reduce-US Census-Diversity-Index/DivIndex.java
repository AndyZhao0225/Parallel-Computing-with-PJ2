import edu.rit.pjmr.Combiner;
import edu.rit.pjmr.Customizer;
import edu.rit.pjmr.Mapper;
import edu.rit.pjmr.Reducer;
import edu.rit.pjmr.TextFileSource;
import edu.rit.pjmr.TextId;
import edu.rit.pjmr.PjmrJob;
import java.util.*;

/**
 * Compute diversity index(es) for given states or the all the states in the US in 
 * a given year. For every states it displayed, the program will also list the 
 * diversity index for each county in the state, in an descending order of diversity
 * index. 
 *
 * @author  Junan Zhao
 * @version 11-Dec-2018
 */
public class DivIndex extends PjmrJob<TextId,String,Key,CensusVbl>
{
   /**
    * PJMR job main program.
    *
    * @param  args  Command line arguments.
    */
   public void main(String[] args)
   {
       // Parse command line arguments.
      if(args.length<=2) usage();
      String[] nodes = args[0].split(",");
      String file = args[1];
      if(!isNumber(args[2])) usage(); 
      Set<String> stateNameSetMain = new HashSet<String>();   //a hash set helps checking duplicate arguments
      if(args.length>3)
        {   
           for( int i=3; i<=args.length-1; i++)
              {
                 if(stateNameSetMain.contains(args[i])) usage();
                 else stateNameSetMain.add(args[i]);          
              }
        }
      // Determine number of mapper threads.
      int NT = Math.max(threads(),1);

       // Configure mapper tasks.
      for( String node: nodes)
          mapperTask(node).source(new TextFileSource(file)).mapper(NT,MyMapper.class,args);
       
      // Configure reducer task.
      reducerTask().customizer(MyCustomizer.class).reducer(MyReducer.class);

      startJob();
   }

   
   /**
    * To check if a string's content is numeric
    *
    * @param  text       the string to be tested 
    * 
    * @return true/false   true if the string content is numeric, otherwise false.
    */
   private static boolean isNumber(String text)
   {
      for( int i=0; i<=text.length()-1; i++)
          if(!Character.isDigit(text.charAt(i))) return false;
      return true;
   }
   
   
   /**
    * Print a usage message and exit.
   */
   private static void usage()
   {
      System.err.println("Usage: java pj2 jar=<jar> threads=<NT> DivIndex <nodes> <file> <year> [ \"<state>\" ... ]");
      System.err.println("<year> should be an integer between 1 and 10");
      System.err.println("<state> each state name should only appear once");
      terminate(1);
   }
   
   
   
    /**
    * Mapper class.
    */
   private static class MyMapper extends Mapper<TextId,String,Key,CensusVbl>
   {
      private int year;
      private Set<String> stateNameSet = new HashSet<String>();
      
         /**
	 * Start this mapper.
	 *
	 * @param  args      Array of zero or more argument strings.
	 * @param  combiner  Thread-local combiner.
	 */
      public void start( String[] args, Combiner<Key,CensusVbl> combiner)
      {
         year = Integer.parseInt(args[2]); 
         if(args.length>3) 
           {   
              for( int i=3; i<=args.length-1; i++)
                  stateNameSet.add(args[i]);
           }
      }

      
      /**
	 * Map the given record ID and record contents to a (key, value) pair. 
	 *
	 * @param  id        Data record ID.
	 * @param  contents  Data record contents.
	 * @param  combiner  Thread-local combiner.
	 */
      public void map( TextId id, String contents, Combiner<Key,CensusVbl> combiner)
      {
         String[] arguments = contents.split(",");
         CensusVbl tempCensusVbl = createACensusVbl(arguments);        
         if(tempCensusVbl!=null) 
           {
              combiner.add(new Key(arguments[3],arguments[4]),tempCensusVbl);
              combiner.add(new Key(arguments[3],"0"),tempCensusVbl); 
           }
      }
      
    
           /**
    * Create a census record according to a line of data contents 
    *
    * @param  arguments  the array stored contents information.
    *
    * @return StateVbl   a reduction variable of a census class
    */
      private CensusVbl createACensusVbl(String[] arguments)
      {
         if(Integer.parseInt(arguments[5])!=this.year) return null;  //check year
         if(Integer.parseInt(arguments[6])!=0) return null;   //check ageGroup        
         if(stateNameSet.size()>0 && !stateNameSet.contains(arguments[3])) return null;    //check state name     
         Census tempCensus = new Census(); 
         for( int i=0; i<=5; i++)
            {
               int j = 10 + 2*i;
               int male = Integer.parseInt(arguments[j]);
               j++;
               int female = Integer.parseInt(arguments[j]);
               tempCensus.n[i] = male + female;
            }
         tempCensus.update();
         return new CensusVbl(tempCensus);
      }
   }

   
   
    /**
    * Reducer class.
    */
   private static class MyReducer extends Reducer<Key,CensusVbl>
   {
      final String STATE_DIV_ID = "0";   //a special string to indicate any Key points to state information 
                                         //If a Key object's county string equals this, the CensusVbl along with the key stores
                                         //information of the whole state
       
        /**
	 * Reduce the given (key, value) pair. 
         *
	 * @param  key     Key; non-null.
	 * @param  value   Value; may be null.
	 */
      public void reduce( Key key, CensusVbl censusVbl) 
      {
         if(key.countyName.equals(STATE_DIV_ID)) System.out.printf("%s\t\t%.5g%n",key.stateName,censusVbl.census.diversityIndex);
         else System.out.printf ("\t%s\t%.5g%n",key.countyName,censusVbl.census.diversityIndex);     
      }
   }
   
   
   
   /**
    * Reducer task customizer class.
    */
   private static class MyCustomizer extends Customizer<Key,CensusVbl>
   {
      /**
	 * Determine if the first (key, value) pair comes before the second (key,
	 * value) pair in the desired sorted order.
	 * 
	 * @param  key_1    Key from first pair.
	 * @param  value_1  Value from first pair.
	 * @param  key_2    Key from second pair.
	 * @param  value_2  Value from second pair.
	 *
	 * @return  True if the first pair comes before the second pair, false
	 *          otherwise.
	 */ 
      public boolean comesBefore( Key key_1, CensusVbl value_1, Key key_2, CensusVbl value_2)
      {
          if(key_1.stateName.compareTo(key_2.stateName)<0) return true;
          else if(key_1.stateName.equals(key_2.stateName)) 
            {
               if(key_1.countyName.equals("0")) return true;
               if(key_2.countyName.equals("0")) return false;
               if(value_1.census.diversityIndex>value_2.census.diversityIndex) return true;
               else if(value_1.census.diversityIndex<value_2.census.diversityIndex) return false;
               else return (key_1.countyName.compareTo(key_2.countyName)<0);
            }
          else return false;  
      }
   }   
}