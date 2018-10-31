/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class FeedstockRule {

    public FeedstockRule() {
    }

    public String gettITNBRFIELDSNew(String tITNBRFIELDS)
        {
            String tITNBRFIELDSNew = "";//新的
            if (!"/".equals(tITNBRFIELDS))
            {
                if (tITNBRFIELDS.contains("|"))
                {
                    tITNBRFIELDSNew =  tITNBRFIELDS.substring(1, tITNBRFIELDS.indexOf(";")) + "',";
                    tITNBRFIELDS = tITNBRFIELDS.substring(tITNBRFIELDS.indexOf(";")+1);
                    while (tITNBRFIELDS.length() != 0)
                    {
                        tITNBRFIELDSNew += "'"+ tITNBRFIELDS.substring(1, tITNBRFIELDS.indexOf(";"))+"',";
                        tITNBRFIELDS = tITNBRFIELDS.substring(tITNBRFIELDS.indexOf(";")+1);
                    }
                }
                else
                {
                    tITNBRFIELDSNew = tITNBRFIELDS.substring(0,tITNBRFIELDS.length()-2);
                }
            }
            return tITNBRFIELDSNew.substring(0, tITNBRFIELDSNew.length()-2);
        }

    public String gettITDSCFIELDSNew(String SUPPLYID,String tITDSCFIELDS,String LINE)
        {
            String tITDSCFIELDSNew = "";
            if (!"/".equals(tITDSCFIELDS))
            {
                if (tITDSCFIELDS.contains("|"))
                {
                    tITDSCFIELDSNew = " itdsc like '%" + tITDSCFIELDS.substring(1, tITDSCFIELDS.indexOf(";")) + "%'  ";
                    tITDSCFIELDS = tITDSCFIELDS.substring(tITDSCFIELDS.indexOf(";") + 1);
                    while (tITDSCFIELDS.length() != 0)
                    {
                        tITDSCFIELDSNew += " OR  itdsc like '%" + tITDSCFIELDS.substring(1, tITDSCFIELDS.indexOf(";")) + "%'  ";
                        tITDSCFIELDS = tITDSCFIELDS.substring(tITDSCFIELDS.indexOf(";") + 1);
                    }
                }
                else
                {
                    tITDSCFIELDSNew = " itdsc like '%" + tITDSCFIELDS.substring(0,tITDSCFIELDS.length()-1) + "%'";
                    if ("机组".equals(LINE) && SUPPLYID.equals("SJS00079"))//南通红星
                    {
                        tITDSCFIELDSNew = " itdsc not like '%" + tITDSCFIELDS.substring(2, tITDSCFIELDS.length() - 2) + "%'";//特殊处理
                    }
                }
            }
            return tITDSCFIELDSNew;
        }
    
    
    public String gettITDSCFIELDSNew(String SUPPLYID,String tITDSCFIELDS,String LINE,int a)
        {
            String tITDSCFIELDSNew = "";
            if (!"/".equals(tITDSCFIELDS))
            {
                if (tITDSCFIELDS.contains("|"))
                {
                    tITDSCFIELDSNew = " PRODUCTNAME like '%" + tITDSCFIELDS.substring(1, tITDSCFIELDS.indexOf(";")) + "%'  ";
                    tITDSCFIELDS = tITDSCFIELDS.substring(tITDSCFIELDS.indexOf(";") + 1);
                    while (tITDSCFIELDS.length() != 0)
                    {
                        tITDSCFIELDSNew += " OR  PRODUCTNAME like '%" + tITDSCFIELDS.substring(1, tITDSCFIELDS.indexOf(";")) + "%'  ";
                        tITDSCFIELDS = tITDSCFIELDS.substring(tITDSCFIELDS.indexOf(";") + 1);
                    }
                }
                else
                {
                    tITDSCFIELDSNew = " PRODUCTNAME like '%" + tITDSCFIELDS.substring(0,tITDSCFIELDS.length()-1) + "%'";
                    if ("机组".equals(LINE) && SUPPLYID.equals("SJS00079"))//南通红星
                    {
                        tITDSCFIELDSNew = " PRODUCTNAME not like '%" + tITDSCFIELDS.substring(2, tITDSCFIELDS.length() - 2) + "%'";//特殊处理
                    }
                }
            }
            return tITDSCFIELDSNew;
        }
        public  String getITDSCFIELDS(String SUPPLYID, String tITDSCFIELDSNew, String ITDSCFIELDS,String LINE)
        {
            if (!"".equals(tITDSCFIELDSNew))
            {
                ITDSCFIELDS = " and (" + tITDSCFIELDSNew + ")";
                if ("机组".equals(LINE) && SUPPLYID.equals("SSH00003")) 
                {
                    ITDSCFIELDS = " or  (" + tITDSCFIELDSNew + ")";
                }
            }
            else
            {
                ITDSCFIELDS = "";
            }
            return ITDSCFIELDS;
        }

        public String getITNBRFIELDS(String SUPPLYID, String tITNBRFIELDSNew, String ITNBRFIELDS,String LINE)
        {
            if (!"".equals(tITNBRFIELDSNew))
            {
                ITNBRFIELDS = "and SUBSTRING(A.itnbr,2,1) IN ('" + tITNBRFIELDSNew + "') ";//品号过滤
                if ("机组".equals(LINE) && SUPPLYID.equals("SSH00003"))
                {
                    ITNBRFIELDS = "  SUBSTRING(A.itnbr,2,1) IN ('" + tITNBRFIELDSNew + "') ";//品号过滤
                }
            }
            else
            {
                ITNBRFIELDS = "";
            }
            return ITNBRFIELDS;
        }
        public String getITNBRFIELDS(String SUPPLYID, String tITNBRFIELDSNew, String ITNBRFIELDS,int a,String LINE)
        {
            if (!"".equals(tITNBRFIELDSNew))
            {
                ITNBRFIELDS = "and SUBSTRING(D.PRODUCTID,2,1) IN ('"  + tITNBRFIELDSNew + "') ";//品号过滤
                if ("机组".equals(LINE) && SUPPLYID.equals("SSH00003"))
                {
                    ITNBRFIELDS = " SUBSTRING(D.PRODUCTID,2,1) IN ('" + tITNBRFIELDSNew + "') ";//品号过滤
                }
            }
            else
            {
                ITNBRFIELDS = "";
            }
            return ITNBRFIELDS;
        }

}
