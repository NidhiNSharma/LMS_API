package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Fetch_Data_From_Properties_File {
	private LMSPojo lmsPojo;

	public Fetch_Data_From_Properties_File(String API_Endpoint) {
		this.lmsPojo = new LMSPojo();
		try {
			Properties prop = readPropertiesFile("./src/test/resources/config/credentials.properties");
			this.lmsPojo.setStr_baseURL(prop.getProperty("URL"));
			this.lmsPojo.setUserName(prop.getProperty("Username"));
			this.lmsPojo.setPassword(prop.getProperty("Pwd"));
			this.lmsPojo.setStr_DBURL(prop.getProperty("SQLDatabaseURL"));
			this.lmsPojo.setStr_DBUserName(prop.getProperty("DBUname"));
			this.lmsPojo.setStr_DBPWD(prop.getProperty("BDPWD"));
			switch (API_Endpoint) {
			case "UserSkills":
				prop = readPropertiesFile("./src/test/resources/config/UserSkills.properties");
				this.lmsPojo.setExcelPath(prop.getProperty("ExcelPath"));
				this.lmsPojo.setNumericColumns(prop.getProperty("numeric.coloms"));
				this.lmsPojo.setGET_SchemaFilePath(prop.getProperty("Get_Filepath"));
				this.lmsPojo.setGET_AllSchemaFilePath(prop.getProperty("Get_all_Filepath"));
				this.lmsPojo.setPOST_SchemaFilePath(prop.getProperty("POST_Resp_Filepath"));
				
				break;
			case "Users":
				prop = readPropertiesFile("./src/test/resources/config/Users.properties");
				this.lmsPojo.setExcelPath(prop.getProperty("ExcelPath"));
				this.lmsPojo.setNumericColumns(prop.getProperty("numeric.coloms"));
				this.lmsPojo.setGET_SchemaFilePath(prop.getProperty("Get_Filepath"));
				this.lmsPojo.setGET_AllSchemaFilePath(prop.getProperty("Get_all_Filepath"));
				this.lmsPojo.setPOST_SchemaFilePath(prop.getProperty("POST_Filepath"));
				break;
			case "Skills":
				prop = readPropertiesFile("./src/test/resources/config/Skills.properties");
				this.lmsPojo.setExcelPath(prop.getProperty("ExcelPath"));
				this.lmsPojo.setNumericColumns(prop.getProperty("numeric.coloms"));
				this.lmsPojo.setStr_GETSkillsSchema(prop.getProperty("Skills_GET_Filepath"));
				this.lmsPojo.setStr_GETidSkillsSchema(prop.getProperty("Skills_GET_id_Filepath"));
				this.lmsPojo.setStr_POSTSkillsSchema(prop.getProperty("Skills_POST_Filepath"));
				break;
			case "UserSkillsMap":
				prop = readPropertiesFile("./src/test/resources/config/UserSkillMap.properties");
				this.lmsPojo.setExcelPath(prop.getProperty("ExcelPath"));
				this.lmsPojo.setUser_Skill_SchemaFilePath(prop.getProperty("User_skill_GET_Filepath"));
				this.lmsPojo.setSkill_User_SchemaFilePath(prop.getProperty("Skill_user_GET_Filepath"));
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Properties readPropertiesFile(String FilePath) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			
			fis = new FileInputStream(FilePath);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	public LMSPojo getLmsPojo() {
		return lmsPojo;
	}

	public void setLmsPojo(LMSPojo lmsPojo) {
		this.lmsPojo = lmsPojo;
	}

	// Example of how to call in main method
	/*
	 * public static void main(String[] args) throws IOException { Properties prop =
	 * readPropertiesFile("credentials.properties"); String
	 * str_User=prop.getProperty("username"); String
	 * str_password=prop.getProperty("Password");
	 * 
	 * }
	 */

}
