import java.io.*;

public class Fraction {

   private int numerator, denominator;

   public Fraction(){
      numerator = denominator = 0;
   }

   public void print() {
      System.out.print(numerator + "/" + denominator);
   }

   public void setNumerator(int n) {
      numerator = n;
   }

   public void setDenominator(int d) {
      denominator = d;
   }

   public int getDenominator() {
      return denominator;
   }

   public int getNumerator() {
      return numerator;
   }

   public static void main(String args[]) {
      try {
         Fraction frac = new Fraction();

         // Check if exactly two arguments are provided
         if (args.length != 2) {
            System.out.println("Usage: java Fraction <numerator> <denominator>");
            return;
         }

         // Parse arguments
         int num = Integer.parseInt(args[0]);
         int denom = Integer.parseInt(args[1]);

         // set the values
         frac.setNumerator(num);
         frac.setDenominator(denom);

         // print it
         System.out.print("The fraction is: ");
         frac.print();
         System.out.println("");

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}

