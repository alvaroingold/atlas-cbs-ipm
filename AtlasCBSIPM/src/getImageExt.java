import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import org.jfree.data.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.jdbc.JDBCXYDataset;



public class getImageExt extends HttpServlet {


    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("image/jpeg");

        OutputStream salida = response.getOutputStream();
        JFreeChart grafica = crearChart(request);

        int ancho = getParamEntero(request,"ancho",600);
        int alto = getParamEntero(request,"alto",300);

        ChartUtilities.writeChartAsJPEG(salida,grafica,ancho,alto);

        salida.close();
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }
   int getParamEntero(HttpServletRequest request,String pNombre, int pDefecto)
    {
            String param = request.getParameter(pNombre);

            if (param == null || param.compareTo("") == 0)
            {
                return pDefecto;
            }

            return Integer.parseInt(param);

    }


    public JFreeChart crearChart(HttpServletRequest request)
    {

        XYSeries series = new XYSeries("Evolucion Sesiones");
        series.add(1, 23);
        series.add(2, 34);
        series.add(3, 51);
        series.add(4, 67);
        series.add(5, 89);
        series.add(6, 121);
        series.add(7, 137);
        XYDataset juegoDatos= new XYSeriesCollection(series);


                try {
                        java.sql.Connection conn;

                        conn = DriverManager.getConnection("jdbc:mysql://localhost/BDB?user=root&password=alvaro");

//                   JDBCXYDataset xyDataset = new JDBCXYDataset(con,"select x as meses,y as sesiones from sesiones");

                String param = request.getParameter("target");
JDBCXYDataset xyDataset = new JDBCXYDataset(conn,"select PHARMACOLOGY.NSEI,PHARMACOLOGY.NBEI2 from PHARMACOLOGY,MOLECULE WHERE PHARMACOLOGY.ID_TARGET=" + param + " AND MOLECULE.ID_MOLECULE=PHARMACOLOGY.ID_MOLECULE");

JFreeChart chart = ChartFactory.createScatterPlot(
"LEI", "nSEI", "nBEI",
xyDataset,
PlotOrientation.VERTICAL,
false,
false,
false
);

                        conn.close();
return chart;
                }catch (SQLException E) {
                }

return null;
    }



}
