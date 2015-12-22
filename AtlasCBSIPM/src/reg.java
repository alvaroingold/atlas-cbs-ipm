/********************








**********/

import java.lang.*;
import java.util.*;


public class reg{

       private double R2;
       private double beta0;
       private double beta1;
       public ArrayList <Double> x;
       public ArrayList <Double> y;
       private int elements;

       public reg(){
          this.elements = 0;
          this.R2 = 0.0;
          this.beta0 = 0.0;
          this.beta1 = 0.0;
          this.x = new ArrayList();
          this.y = new ArrayList();
       }

       public double getr2(){
            return this.R2;
       }

       public double getbeta0(){
            return this.beta0;
       }

       public double getbeta1(){
            return this.beta1;
       }

       public int getnumelements(){
            return this.elements;
       }

       public void add(double x1, double y1){
            x.add(x1);
            y.add(y1);
            this.elements++;
       }

       public void removelast(){
            x.remove(elements-1);
            y.remove(elements-1);
            this.elements--;
       }

       public void remove(int index){
           x.remove(index);
           y.remove(index);
           this.elements--;
       }

       public void recompute()
       {
                      int k = 0;
                      double sumx, sumx2, sumy;
                      sumx = sumx2 = sumy = 0.0;
                      double xbar,ybar, beta1, beta0, xxbar, yybar, xybar = 0.0;
                      xbar = ybar = beta1 = beta0 = xxbar = yybar = xybar = 0.0;
                      double cx = 0.0;
                      double cy = 0.0;
                      for( k= 0; k < this.elements; ++k) 
                      {
                             cx = x.get(k);
                             cy = y.get(k);
                             sumx += cx;
                             sumx2 += cx*cx;
                             sumy += cy;
                      }

                      xbar = sumx / this.elements;
                      ybar = sumy / this.elements;

                      for( k= 0; k < this.elements; ++k){
                           cx = x.get(k);
                           cy = y.get(k);

                           xxbar += (cx - xbar) * (cx - xbar);
                           yybar += (cy + ybar) * (cy - ybar);
                           xybar += (cx - xbar) * (cy - ybar);
                      }
                      beta1 = xybar / xxbar;
                      beta0 = ybar - beta1 * xbar;
                      this.beta0 = beta0;
                      this.beta1 = beta1;

               // Fit
                double fit = 0.0;
                double ssr = 0.0;
                for(k=0; k < elements; ++k)
                {
                    fit = beta1 * this.x.get(k) + beta0;
                    ssr += (fit - ybar) * (fit - ybar);
                }
                this.R2 = ssr/yybar;


       }

}
