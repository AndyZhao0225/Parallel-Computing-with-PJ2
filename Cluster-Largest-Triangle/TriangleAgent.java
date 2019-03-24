/**
 * TriangleAgent class is helping with doing triangle computation.
 * All of its methods are static. 
 * The purpose of this design is to reduce repeated code.
 *
 * @author  Junan Zhao
 * @version 24-Oct-2018
 */
public class TriangleAgent 
{
    /**
      * Compute and store distances between points into a two-dimensional array. 
      *
      * @param  distance  the two dimensional array to store distances between points 
      * @param  points    the points array
      * @param  N         the number of points
      * 
      */
   public static void recordDistance(double[][] distance, Point[] points, int N)
   {
      for( int i=0; i<=N-1; i++)
         {
            for( int j=i+1; j<=N-1; j++)
                distance[i][j] = TriangleAgent.computeDistance(points[i],points[j]);
         }
   }
   

   /**
      * Store generated points information into a point array
      *
      * @param  pointSpec  the object which generate points
      * @param  points     the points array
      * 
      */
   public static void recordPoints( PointSpec pointSpec, Point[] points)
   {
      int i = 0;
      while(pointSpec.hasNext())
         {
            Point temp = pointSpec.next();
            points[i].x = temp.x;
            points[i].y = temp.y;
            i++;
         }
   }
   
   
   /**
    * Compute the distance between two points 
    *
    * @param  a    the first point
    * @param  b    the second point
    *
    * @return  the distance
    */
   public static double computeDistance(Point a, Point b)
   {
      double sqr = (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y); 
      return Math.sqrt(sqr);
   }
   
   
   /**
    * Compute a triangle's area, given the points indexes in points array, and distance array
    *
    * @param   distance  the distance array
    * @param   indexA    the first point index
    * @param   indexB    the second point index  
    * @param   indexC    the third point index
    * 
    * @return  the triangle's area
    */
   public static double computeTriangleArea( double[][] distance, int indexA, int indexB, int indexC)
   {
      double a = distance[indexA][indexB];
      double b = distance[indexB][indexC];
      double c = distance[indexA][indexC];
      double s = (a + b + c)/2;
      return Math.sqrt(s*(s - a)*(s - b)*(s - c));
   }
   
   
   /**
    * Replace the answer's attributes with the given attributes value 
    *
    * @param  answer    the answer whose attributes would be replaced
    * @param  area      the new area value
    * @param  i         the new i value
    * @param  j         the new j value
    * @param  k         the new k value
    */
   public static void replaceTheAnswerWithALargerTriangle( Answer answer, double area, int i, int j, int k)
   { 
      answer.setArea(area);
      answer.setI(i);
      answer.setJ(j);
      answer.setK(k);
   }
   
   
   /**
    * Compare a given answer with a triangle formed from the given indexed points
    *  
    * @param  answer        the answer to be compared with
    * @param  distance      the distance array
    * @param  i             the first point 
    * @param  j             the second point
    * @param  k             the third point
    */
   public static void compareTwoTriangles( Answer answer, double[][] distance, int i, int j, int k)
   {
      double area = TriangleAgent.computeTriangleArea(distance,i,j,k);
      if(area>answer.getArea()) TriangleAgent.replaceTheAnswerWithALargerTriangle(answer,area,i,j,k);
      else if(area==answer.getArea())
        {
           if(i<answer.getI()) TriangleAgent.replaceTheAnswerWithALargerTriangle(answer,area,i,j,k);
           else if(i==answer.getI())
             {
                if(j<answer.getJ()) TriangleAgent.replaceTheAnswerWithALargerTriangle(answer,area,i,j,k);
                else if(j==answer.getJ())
                  {
                     if(k<answer.getK()) TriangleAgent.replaceTheAnswerWithALargerTriangle(answer,area,i,j,k);
                  }
             }
        }  
   }
   
   
   /**
      * Store coordinates into the answer. 
      *
      * @param  points            the points array
      * @param  thrAnsVbl         an answer reduction variable instance
      * 
      */
   public static void recordCoordinates(Point[] points, AnswerVbl thrAnsVbl)
   {
      double ix = points[thrAnsVbl.ans.getI()].x;
      double iy = points[thrAnsVbl.ans.getI()].y;
      double jx = points[thrAnsVbl.ans.getJ()].x;
      double jy = points[thrAnsVbl.ans.getJ()].y;
      double kx = points[thrAnsVbl.ans.getK()].x;
      double ky = points[thrAnsVbl.ans.getK()].y;
      thrAnsVbl.ans.setIX(ix);
      thrAnsVbl.ans.setIY(iy);
      thrAnsVbl.ans.setJX(jx);
      thrAnsVbl.ans.setJY(jy);
      thrAnsVbl.ans.setKX(kx);
      thrAnsVbl.ans.setKY(ky);
   }
   
   
    /**
      * Print the given answer. The points array provide coordinate information
      *
      * @param  answer            the answer to be printed
      * @param  points            the points array
      * 
      */
   public static void printTheAnswer( Answer answer, Point[] points)
   {
      System.out.printf ("%d %.5g %.5g%n", answer.getI(), points[answer.getI()].x, points[answer.getI()].y);
      System.out.printf ("%d %.5g %.5g%n", answer.getJ(), points[answer.getJ()].x, points[answer.getJ()].y);
      System.out.printf ("%d %.5g %.5g%n", answer.getK(), points[answer.getK()].x, points[answer.getK()].y);
      System.out.printf ("%.5g%n", answer.getArea());
   }
   
   
   /**
      * Print the given answer which is encapsulated in the answer reduction variable
      * 
      * @param  answerVbl         the answer reduction variable
      * 
      */
   public static void printTheAnswer(AnswerVbl answerVbl)
   {
      System.out.printf ("%d %.5g %.5g%n", answerVbl.ans.getI(), answerVbl.ans.getIX(), answerVbl.ans.getIY());
      System.out.printf ("%d %.5g %.5g%n", answerVbl.ans.getJ(), answerVbl.ans.getJX(), answerVbl.ans.getJY());
      System.out.printf ("%d %.5g %.5g%n", answerVbl.ans.getK(), answerVbl.ans.getKX(), answerVbl.ans.getKY());
      System.out.printf("%.5g%n", answerVbl.ans.getArea());
   }
}
