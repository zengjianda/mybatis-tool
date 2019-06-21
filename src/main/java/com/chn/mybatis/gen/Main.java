package com.chn.mybatis.gen;

import com.chn.mybatis.gen.def.TableMetadata;
import com.chn.mybatis.gen.trans.TableTrans;
import com.chn.mybatis.gen.utils.DBUtils;
import org.apache.commons.io.FileUtils;
import org.bee.tl.core.GroupTemplate;
import org.bee.tl.core.Template;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class Main {

	private static final String EOL = System.getProperty("line.separator");
	public static final String ROOT_FILE_PATH = Main.class.getResource("/").getPath().replace("%20", " ");
	public static final String PACKAGE_PATH = ROOT_FILE_PATH + "com/chn/mybatis/gen/tpl";

	//数据库连接
	public static String dbUrl = "jdbc:mysql://127.0.0.1:3306/read?characterEncoding=utf-8&allowMultiQueries=true";
	public static String username = "root";
	public static String password = "admin";
	public static String driver = "com.mysql.jdbc.Driver";

	//要初始化的表
	public static String[] tables = {"sys_dic"};


	public static File GEN_SERVICE_FOLDER = new File("F:/idea_workspace/myself/read-all/read-business/src/main/java");//生成service文件目录

	public static File GEN_SERVICE_IMPL_FOLDER = new File("F:/idea_workspace/myself/read-all/read-business-impl/src/main/java");//生成service实现类文件目录

	public static File GEN_XML_FOLDER = new File("F:/idea_workspace/myself/read-all/read-business-impl/src/main/resources");//生成mapper文件目录


	public static final String GEN_PACKAGE = "com.read";//项目根目录
	public static final String GEN_PERSISTENCE = "mapper";//持久化额外加的目录
	public static final String GEN_MODEL = "model";//Pojo类路径
	public static final String GEN_SERVICE = "business.interfaces";//接口额外加的目录
	public static final String GEN_SERVICE_IMPL = "business.impl";//接口额外加的目录
	public static final String PERSISTENCE_SUFFIX = "Mapper";//持久化文件后缀
	public static final String SERVICE_SUFFIX = "Manager";//接口后缀
	public static final String SERVICE_IMPL_SUFFIX = "ManagerImpl";//接口后缀


	public static final GroupTemplate group = new GroupTemplate(new File(PACKAGE_PATH));
	
	public static void main(String[] args) throws Exception {
		group.setCharset("UTF-8");
		Connection conn = DBUtils.getConn();
		DatabaseMetaData dbmd = DBUtils.getDatabaseMetaData(conn);
		String dbType = dbmd.getDatabaseProductName();
		DBUtils.loadMetadata(dbmd);
		for (String tableName : TableMetadata.getAllTables().keySet()) {
			generateXml(tableName, dbType);
			generateDao(tableName, dbType);
			generateEntity(tableName, dbType);
			generateService(tableName, dbType);
			generateServiceImpl(tableName, dbType);
//			generateWeb(tableName, dbType);
//			generateDatatableHtml(tableName, dbType);
//			generateDialogInfoHtml(tableName, dbType);
//			generateDialogUpdateHtml(tableName, dbType);
		}
	}

	private static void generateDialogInfoHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dialog-info-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("title", "tableName管理");
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-info.jsp");
		FileUtils.write(new File(GEN_SERVICE_IMPL_FOLDER, "/list/" + trans.getLowerStartClassName() + "-info.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void generateDialogUpdateHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dialog-update-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("title", "tableName管理");
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-update.jsp");
		FileUtils.write(new File(GEN_SERVICE_IMPL_FOLDER, "/list/" + trans.getLowerStartClassName() + "-update.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void writeTag(TableTrans trans, String basePath) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\"	pageEncoding=\"UTF-8\"%>").append(EOL);
		buffer.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>").append(EOL);
		buffer.append("<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\"%>").append(EOL);
		buffer.append("<c:set var=\"ctx\" value=\"${pageContext.request.contextPath}\" />").append(EOL);
		FileUtils.write(new File(GEN_SERVICE_IMPL_FOLDER, basePath), buffer.toString());
	}

	private static void generateDatatableHtml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-datatable-html.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		template.set("startTag_", "${");
		template.set("endTag_", "}");
		template.set("title", "tableName管理");
		template.set("addEntity", "添加");
		template.set("startTime", "开始时间");
		template.set("endTime", "结束时间");
		template.set("searchText", "searchText");
		template.set("searchButton", "searchButton");
		template.set("export", "导出");
		template.set("name", "操作");
		template.set("infoName", "详细");
		template.set("deleteName", "删除");
		template.set("updateName", "更新");

		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		writeTag(trans, "/list/" + trans.getLowerStartClassName() + "-list.jsp");
		FileUtils.write(new File(GEN_SERVICE_IMPL_FOLDER, "/list/" + trans.getLowerStartClassName() + "-list.jsp"), template.getTextAsString(), "UTF-8", true);
	}

	private static void generateXml(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dao-xml.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("persistence", GEN_PERSISTENCE);
		template.set("model",GEN_MODEL);
		template.set("persistenceSuffix", PERSISTENCE_SUFFIX);
		File file = new File(GEN_XML_FOLDER, "/mapper/" +trans.getLowerStartClassName() + "/"+ trans.getUpperStartClassName() + "Mapper.xml");
		FileUtils.write(file, template.getTextAsString(), "UTF-8");
	}

	private static void generateDao(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-dao-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("persistence", GEN_PERSISTENCE);
		template.set("model", GEN_MODEL);
		template.set("persistenceSuffix", PERSISTENCE_SUFFIX);
		String path = GEN_SERVICE_IMPL_FOLDER +"/" + GEN_PACKAGE.replace(".","/") + "/" +GEN_PERSISTENCE.replace(".","/") + "/"+  trans.getLowerStartClassName()+ "/" +trans.getUpperStartClassName() + PERSISTENCE_SUFFIX+".java";
		File file = new File(path);
		FileUtils.write(file, template.getTextAsString(), "UTF-8");
	}



	private static void generateWeb(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-web-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		FileUtils.write(new File(GEN_SERVICE_IMPL_FOLDER, "/controller/" + trans.getUpperStartClassName() + "Controller.java"), template.getTextAsString(),
				"UTF-8");
	}

	private static void generateService(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-service-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("model", GEN_MODEL);
		template.set("service", GEN_SERVICE);
		template.set("serviceSuffix", SERVICE_SUFFIX);
		String path =  GEN_SERVICE_FOLDER + "/" + GEN_PACKAGE.replace(".","/") + "/" + GEN_SERVICE.replace(".","/") + "/"+trans.getLowerStartClassName() + "/" + trans.getUpperStartClassName() + SERVICE_SUFFIX+ ".java";
		FileUtils.write(new File(path), template.getTextAsString(), "UTF-8");
	}
	private static void generateServiceImpl(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-service-impl-java.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("serviceSuffix", SERVICE_SUFFIX);
		template.set("serviceImpl", GEN_SERVICE_IMPL);
		template.set("serviceImplSuffix", SERVICE_IMPL_SUFFIX);
		template.set("model", GEN_MODEL);
		template.set("persistence", GEN_PERSISTENCE);
		template.set("service", GEN_SERVICE);
		template.set("persistenceSuffix", PERSISTENCE_SUFFIX);
		String path =  GEN_SERVICE_IMPL_FOLDER + "/" + GEN_PACKAGE.replace(".","/") + "/" + GEN_SERVICE_IMPL.replace(".","/") + "/"+trans.getLowerStartClassName() + "/" + trans.getUpperStartClassName() + SERVICE_IMPL_SUFFIX+ ".java";
		FileUtils.write(new File(path), template.getTextAsString(), "UTF-8");
	}

	private static void generateEntity(String tableName, String dbType) throws Exception {
		Template template = group.getFileTemplate(dbType + "-domain.txt");
		if (template == null)
			throw new RuntimeException(String.format("未支持的数据库类型【%s】", dbType));
		TableTrans trans = TableTrans.find(tableName);
		template.set("package", GEN_PACKAGE);
		template.set("table", trans);
		template.set("model", GEN_MODEL);
		String path = GEN_SERVICE_FOLDER + "/" + GEN_PACKAGE.replace(".","/") + "/" + GEN_MODEL.replace(".","/") + "/"+trans.getLowerStartClassName() + "/" + trans.getUpperStartClassName() + ".java";
		FileUtils.write(new File(path), template.getTextAsString());
	}
}
