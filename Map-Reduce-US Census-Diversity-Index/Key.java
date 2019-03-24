import java.io.*;

/**
 * Key is a class that used to pair state name and county name, 
 * so that a pair of (stateName,countyName) could be served as 
 * a key in any map data structure.  
 *
 * @author  Junan Zhao
 * @version 11-Dec-2018
 */
public class Key implements Serializable 
{
   String stateName;
   String countyName;
   
   
   /**
    * Construct a new key with the given number state name and county name.
    *
    * @param  newStateName       Name of a state
    * @param  newCountyName      Name of a county 
    */
   public Key( String newStateName, String newCountyName)
   { 
      stateName = newStateName;
      countyName = newCountyName;
   }   
   
   
     /**
    * Returns the hash code of this object t
    * 
    * @return the hash code of this object
    */
   public int hashCode()
   {
      return (stateName + " " + countyName).hashCode();
   }
   
    /**
    * Construct a new key with the given number state name and county name.
    *
    * @param  key      the key to be tested 
    * 
    * @return  true/false        true if the given key equals with this one 
    */
   public boolean equals(Object obj)
   {
      return(obj instanceof Key) && (this.stateName.equals(((Key)obj).stateName)) && (this.countyName.equals(((Key)obj).countyName));
   }
}
