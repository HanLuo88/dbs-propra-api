package de.hhu.cs.dbs.propra.presentation.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Jasonize
{
    public static String makeJsonString(ResultSet rs, String[] keys) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();

        List<String> rows = new ArrayList<>();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next())
        {
            StringBuilder sbRow = new StringBuilder();
            sbRow.append("\n\t{");
            for (int i = 1; i <= columnsNumber; i++)
            {
                String columnValue = rs.getString(i);
                sbRow.append("\n\t\t");

                String key = keys[i - 1];
                sbRow.append("\"" + key + "\":\"" + columnValue + "\"");
                if (i < columnsNumber)
                {
                    sbRow.append(",");
                }
                sbRow.append("\n");
            }
            sbRow.append("\t}");
            rows.add(sbRow.toString());
        }


        StringBuilder sbArray = new StringBuilder();
        sbArray.append("[");
        for (int i = 0; i < rows.size(); i++)
        {
            sbArray.append(rows.get(i));
            if (i < rows.size() - 1)
            {
                sbArray.append(",");
            }
        }
        sbArray.append("\n]");
        return sbArray.toString();
    }
}
