package nl.tudelft.pds.granula;

/**
 * Created by wlngai on 21-9-15.
 */
public class test {
    public static void main(String[] args) {
        String x= "attempt_1442829866554_0002_m_000000_0";

        String[] y = x.split("_");
        String z = y[1] + "_" + y[2];
        String z2 = y[3] + "_" + y[4] + "_" + y[5];
        System.out.println(z);
        System.out.println(z2);


        String second = "job_1442829866554_0002";
        String[] yy = second.split("_");
        String yyy = yy[1] + "_" + yy[2];
        System.out.println(yyy);

        String third = "appattempt_1442836555411_0002_000001";
        String[] zz3 = third.split("_");
        String zzz = zz3[1] + "_" + zz3[2];
        System.out.println(zzz);
    }
}
