package org.sharpx.astgrakn.grakn4jDriver;

import ai.grakn.GraknSession;
import ai.grakn.GraknTx;
import ai.grakn.GraknTxType;
import ai.grakn.Keyspace;
import ai.grakn.concept.Attribute;
import ai.grakn.concept.Entity;
import ai.grakn.concept.Relationship;
import ai.grakn.remote.RemoteGrakn;
import ai.grakn.util.SimpleURI;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.sharpx.astgrakn.Helpers.StaticVariables;
import org.sharpx.astgrakn.Objects.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Grakn4JDriver {

	final static Logger logger = Logger.getLogger(Grakn4JDriver.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	final static Logger resultLog = Logger.getLogger("reportsLogger");

	private Session session;
	private GraknSession grakn_session;

	/**
	 * Neo4JDriver creates and inserts the query to Neo4j instance
	 */
	public Grakn4JDriver(String host, int port, String keyspace) {
		Properties prop = new Properties();
		InputStream input = null;

		grakn_session = RemoteGrakn.session(new SimpleURI(host+":"+port), Keyspace.of(keyspace));

	}

	/**
	 * Inserting AST to Neo4J instance through log file
	 * 
	 * @param query
	 */
	public void insertNeo4JDBLogFile(String query) {

		if (isNeo4jConnectionUp()) {

			try {
				// Insert query on Neo4j graph DB
				session.run(query);
				logger.info("Insertion Query: " + query);

			} catch (Exception e) {
				logger.debug("Excetion : ", e);
				debugLog.debug("Excetion : ", e);
				return;
			}
		} else {
			logger.debug("Driver or Session is down, check the configuration");
			debugLog.debug("Driver or Session is down, check the configuration");
		}

	}

	/**
	 * Inserting AST to Neo4J instance
	 * 
	 * @param fileNodeAST
	 *            (AST root node)
	 */
	public void insertNeo4JDB(FileNodeAST fileNodeAST) {

		if (fileNodeAST == null) {
			logger.debug("AST File Object is null (Probably had parsing error)");
			debugLog.debug("AST File Object is null (Probably had parsing error)");
			return;
		}

		try (GraknTx tx = grakn_session.open(GraknTxType.WRITE)) {
			JavaSchema schema = JavaSchema.load(tx);

			Entity cu = schema.compileunit.addEntity();
			Attribute fileName = schema.name.putAttribute(fileNodeAST.getName());
			Attribute packageName = schema.package_name.putAttribute(fileNodeAST.getPackageName());
			cu.attribute(fileName).attribute(packageName);

			//check if packgee named packagename already exists
			Entity packagee = schema.packagee.addEntity();
			packagee.attribute(schema.name.putAttribute(packageName));
			Relationship include_compileunit = schema.include_compileunit.addRelationship();
			include_compileunit.addRolePlayer(schema.including_compileunit, packagee).addRolePlayer(schema.included_by_package, cu);

			for (ClassNodeAST classNode : fileNodeAST.getClasses()) {
				Entity clazz = schema.clazz.addEntity();
				clazz.attribute(schema.name.putAttribute(classNode.getName()));

				Relationship contain_class = schema.contain_class.addRelationship();
				contain_class.addRolePlayer(schema.containing_class, cu).addRolePlayer(schema.contained_by_compileunit, clazz);
			}

			for (InterfaceNodeAST intfNode : fileNodeAST.getInterfaces()) {
				Entity clazz = schema.clazz.addEntity();
				clazz.attribute(schema.name.putAttribute(intfNode.getName()));

				Relationship contain_class = schema.contain_class.addRelationship();
				contain_class.addRolePlayer(schema.containing_class, cu).addRolePlayer(schema.contained_by_compileunit, clazz);
			}

		}
	}

	public void insertRepoNodeNeo4JDB(String repoURL, long linesOfJavaCode) {

		if (isNeo4jConnectionUp()) {
			// File Node of AST
			String nodeInsertQuery = "CREATE (";
			nodeInsertQuery += "r:" + StaticVariables.repoNodeName + " {";
			// File node properties
			nodeInsertQuery += StaticVariables.URLRepoPropertyName + ":\'"
					+ repoURL + "\',";
			nodeInsertQuery += StaticVariables.linesOfJavaCodeRepoPropertyName
					+ ":" + String.valueOf(linesOfJavaCode) + "";
			nodeInsertQuery += "})";

			nodeInsertQuery += ";";
			logger.info("Insertion Query: " + nodeInsertQuery);
			resultLog.info(nodeInsertQuery);

			// Insert query on Neo4j graph DB
			session.run(nodeInsertQuery);

		} else {
			logger.debug("Driver or Session is down, check the configuration");
			debugLog.debug("Driver or Session is down, check the configuration");
		}
	}

	/*
	 * Close Neo4j Connection
	 */
	public void closeDriverSession() {
		if (session != null)
			session.close();
	}

	public String escapingCharacters(String query) {

		return query;
	}

	/*
	 * Check Neo4j Connection
	 */
	public boolean isNeo4jConnectionUp() {
		return session.isOpen();
	}

}
