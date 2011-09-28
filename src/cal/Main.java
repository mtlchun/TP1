
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cal;

import java.io.*;
import java.lang.Math;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//    import java.util.*;  


/*
cette classe definer de structure de test et,
changer la chaine de caractere
 * a des donnee de test;
 */
class c_read_file {

    private String id;
    private String descrip;
    private double montant;
    private int nombreAnnee;
    private int frequenceRemboursement;
    private double tauxInteret;
    private int frequenceComposition;

    c_read_file() {
    }

    ;

    /*  **********************************
    lecture la fichier ,
    franer au chaine de caractere
    retourse la chaine de caractere
     */
    public String ReadFile(String path) {



        //   String mypath=this.getClass().getClassLoader().getResource("/").getPath()+path;
        File file = new File(path);
        //  System.out.print(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            //ligne par ligne
            while ((tempString = reader.readLine()) != null) {
                //affiche la ligne
                // System.out.println("line"+  line + tempString);
                laststr = laststr + tempString.trim();
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }
        return laststr;
    }

    /*
     *divider la chaine
     * a donnees du test
     */
    void setvaleur(String str) {
        String laststr = str;
        laststr = laststr.replaceAll(" ", "");
        laststr = laststr.substring(1, laststr.length() - 1);
        //  laststr = laststr.replaceAll("\n", "");
        laststr = laststr.trim();
        String[] ss = laststr.split(",");

        int i = 0;

        while (i < 7) {
            ss[i].trim();
            ss[i] = ss[i].substring(ss[i].indexOf(":") + 1, ss[i].length());
//          System.out.println(ss[i]);
            i++;

        }
        this.id = ss[0];
        this.descrip = ss[1];
        this.montant = Integer.parseInt(ss[2]);
        this.nombreAnnee = Integer.parseInt(ss[3]);
        this.frequenceRemboursement = Integer.parseInt(ss[4]);
        this.tauxInteret = Integer.parseInt(ss[5]);
        this.frequenceComposition = Integer.parseInt(ss[6]);
//      i=0;
//      while(i<7){
//          System.out.println(ss[i]);
//          i++;
//      }
//System.out.println(pow(1.06,300));

    }

    /*
    calculer de amortiseement
     */
    String cal_camort(String str) {
        String s_resul = str;
        s_resul = s_resul.replace(",", ",\n");
        s_resul = s_resul.replace("}", ",\n");
        int i, total_periode = this.nombreAnnee * 12;
        //pX(RX(1+R)**N)/((1+R)**N)-1)
        double versementtotal;
        double taux_mois = Math.sqrt(Math.cbrt(1+this.tauxInteret / this.frequenceComposition/100))-1;
        versementtotal = this.montant * taux_mois * pow((1 + taux_mois), total_periode);
        versementtotal = versementtotal / (pow((1 + taux_mois), total_periode) - 1);
        //  System.out.println(versementtotal);

        //calculer par periode
        double debut = this.montant;
        double v_total = versementtotal;
        double v_inte = debut * taux_mois;
        double v_cap = v_total - v_inte;
        double v_rest = debut;
        s_resul = s_resul + "\"versementPeriodique\" : " + Double.toString(versementtotal) + ",\n";
        s_resul = s_resul + "\"amortissement\" : [" + "\n";


        //Boucle pour rest periode

        for (i = 0; i < total_periode - 1; i++) {
            debut = v_rest;
            v_total = versementtotal;
            v_inte = debut * taux_mois;
            v_cap = v_total - v_inte;
            v_rest = debut - v_cap;

            s_resul = s_resul + "{\n" + "periode: " + Integer.toString(i + 1) + "\n";
            s_resul = s_resul + "capitalDebut: " + Double.toString(debut) + "\n";
            s_resul = s_resul + "versementtotal: " + Double.toString(v_total) + "\n";
            s_resul = s_resul + "versementInteret: " + Double.toString(v_inte) + "\n";
            s_resul = s_resul + "versementcapital: " + Double.toString(v_cap) + "\n";
            s_resul = s_resul + "capitalFin: " + Double.toString(v_rest) + "\n";
            s_resul = s_resul + " }," + "\n";
        }

        //la deriere periode
        debut = v_rest;
        v_total = v_rest;
        ;
        v_inte = debut * taux_mois;
        v_cap = v_total - v_inte;
        v_rest = 0;

        s_resul = s_resul + "{\n" + "periode: " + Integer.toString(i + 1) + "\n";
        s_resul = s_resul + "capitalDebut: " + Double.toString(debut) + "\n";
        s_resul = s_resul + "versementtotal: " + Double.toString(v_total) + "\n";
        s_resul = s_resul + "versementInteret: " + Double.toString(v_inte) + "\n";
        s_resul = s_resul + "versementcapital: " + Double.toString(v_cap) + "\n";
        s_resul = s_resul + "capitalFin: " + Double.toString(v_rest) + "\n";
        s_resul = s_resul + " }" + "\n";

        s_resul = s_resul + " ]" + "\n";
        s_resul = s_resul + " }" + "\n";


        //ecrite a la fichier "out.jon"

        try {
            // Create file
            FileWriter fstream = new FileWriter("out.json");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(s_resul);
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        return s_resul;
    }

    /*
    calculer de power
     */
    double pow(double base, int e) {
        double resul = base;
        if (e == 0) {
            return 1;
        }
        if (e == 1) {
            return base;
        }
        int i;
        for (i = 1; i < e; i++) {
            resul = resul * base;
        }
        return resul;
    }
}

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String s = new String("test1.json");
        //     System.out.println(s);

        c_read_file fe = new c_read_file();
        String laststr = (fe.ReadFile(s));
        fe.setvaleur(laststr);
        System.out.println(fe.cal_camort(laststr));
        // System.out.println(laststr);




    }
}
/*
 * import com.google.Gson;
import net.sf.json.JSONObject;

/**
 *
 * @author chun

class Jpret{

private String id;
private String descrip;
private double montant;
private int nombreAnnee;
private int frequenceRemboursement;
private double tauxInteret;
private int frequenceComposition;

};
class amortisse{
pri
}

class JSONObject {

void set() {
String json = "{'name': '呵呵','array':[{'a':'111','b':'222','c':'333'},{},{'a':'999'}],'address':'上海'}";
try {
JSONObject jsonObject = new Gson(json);
String name = jsonObject.getString("name");
String address = jsonObject.getString("address");

System.out.println("name is:" + name);

System.out.println("address is:" + address);

JSONArray jsonArray = jsonObject.getJSONArray("array");

for (int i = 0; i < jsonArray.length(); i++) {
System.out.println("item " + i + " :" + jsonArray.getString(i));
try{
JSONObject jsonObject333 = new JSONObject(jsonArray.getString(i));
String aaaa = jsonObject333.getString("a");
System.out.println("----------"+aaaa);
}catch(Exception e){
}
}
} catch (JSONException e) {
e.printStackTrace();
}


}
}
 */
