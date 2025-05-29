package util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class Fetch_Data_From_SQL {
	// Declaration of the variables

	// call to method to fetch data from property file will be there instead of
	// declataion of variables here. This will be in main class. Its here just for
	// testing purpose
	/*
	 * private final String str_URL = "jdbc:postgresql://localhost/LMS_DB"; private
	 * final String str_UserName = "postgres"; private final String str_Pwd =
	 * "postgres"; private final String fname=null;
	 */

	// Method to initalize connection to the database and execute query

	@SuppressWarnings("finally")
	public static Map<String, String> connect(String str_URL, String str_UserName, String str_Pwd, String str_Query) {
		PreparedStatement pst = null;
		Connection conn = null;
		Map<String, String> resultMap = new HashMap<>();
		try {
			DriverManager.registerDriver(
					(Driver) Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance());
			conn = DriverManager.getConnection(str_URL, str_UserName, str_Pwd);
			{
				if (conn != null) {
					pst = conn.prepareStatement(str_Query);
					ResultSet rs = pst.executeQuery();

					while (rs.next()) {

						ResultSetMetaData resultsetMetaData = rs.getMetaData();
						for (int i = 1; i <= resultsetMetaData.getColumnCount(); i++) {
							String str_Column_Name = resultsetMetaData.getColumnLabel(i);
							String str_Column_Value = rs.getString(str_Column_Name);
							resultMap.put(str_Column_Name, str_Column_Value);

						}
					}

				} else {
					System.out.println("Failed to connect");
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (pst != null) {

					pst.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {
			}

			return resultMap;
		}
	}

	@SuppressWarnings("finally")
	public static Boolean connect_delete(String str_URL, String str_UserName, String str_Pwd, String str_Query,
			String id) {

		Boolean QueryResult = true;
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			DriverManager.registerDriver(
					(Driver) Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance());
			conn = DriverManager.getConnection(str_URL, str_UserName, str_Pwd);
			{
				if (conn != null) {
					// Call to class where reading from excel for query as per TCID will used in
					// this place
					pst = conn.prepareStatement(str_Query);
					ResultSet rs = pst.executeQuery();

					int count = 0;

					while (rs.next()) { // get the column count
						// user for loop to get the column names & column value
						// store that in an array

						++count;

						ResultSetMetaData resultsetMetaData = rs.getMetaData();
						int int_ColumnCount = resultsetMetaData.getColumnCount();
						// arr_QueryResultArray = new String[int_ColumnCount];
						for (int i = 1; i <= int_ColumnCount; i++) {
							String str_Column_Name = resultsetMetaData.getColumnLabel(i);
							String str_Column_Value = rs.getString(str_Column_Name);
							System.out.println(str_Column_Name + " : " + str_Column_Value);

							// arr_QueryResultArray[i - 1] = str_Column_Value;
							if (str_Column_Name == id) {

								QueryResult = false;
								return QueryResult;
							}

						}

					}

					if (count == 0) {
						System.out.println("No records found");
					}

				} else {
					System.out.println("Failed to connect");
				}

			}
		} catch (Exception e) {
		} finally {
			try {
				if (pst != null) {

					pst.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {
			}

			return QueryResult;
		}
	}
	// This will how it will be called from the main class under API_LMS_Main

//	public static void main(String[] args) {
//
//		String str_DBURL = "jdbc:postgresql://localhost:5432/LMS_DB";
//		String str_DBUserName = "postgres";
//		String str_DBPWD = "postgres";
//		String str_userskillsid = "US03";
//		String str_Query = "select user_skill_id,user_id,skill_Id,months_of_exp from tbl_lms_userskill_map where user_skill_id='"
//				+ str_userskillsid + "'";
//		cls_Fetch_Data_From_SQL app = new cls_Fetch_Data_From_SQL();
//		app.connect(str_DBURL, str_DBUserName, str_DBPWD, str_Query);
//		String[] OutputArray = app.connect(str_DBURL, str_DBUserName, str_DBPWD, str_Query);
//
//		printOutput(OutputArray);
//	}
//	private static void printOutput(String[] OutputArray) {
//		System.out.println("~~~~~~~~ START Printing final output ~~~~~~~~~~~");
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < OutputArray.length; i++) {
//			sb.append(OutputArray[i]);
//			
//			if (i < OutputArray.length-1) {
//				sb.append(",");
//			}
//		}
//		System.out.println(sb.toString());
//		
//		System.out.println("~~~~~~~~ END Printing final output ~~~~~~~~~~~");
//	}

}
