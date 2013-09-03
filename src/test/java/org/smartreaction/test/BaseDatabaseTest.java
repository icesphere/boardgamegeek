package org.smartreaction.test;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDatabaseTest
{
    private static EntityManagerFactory entityManagerFactory;
    protected static EntityManager entityManager;
    protected static IDatabaseConnection databaseConnection;

    protected static boolean dataSetInitialized;

    @BeforeClass
    public static void setupEntityManager() throws DatabaseUnitException, SQLException
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("TestPU");
        entityManager = entityManagerFactory.createEntityManager();

        Connection connection = entityManager.unwrap(java.sql.Connection.class);
        databaseConnection = new DatabaseConnection(connection);
        databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
    }

    @Before
    public void setup()
    {
        if (!dataSetInitialized) {
            try {
                FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
                flatXmlDataSetBuilder.setColumnSensing(true);
                IDataSet dataSet = flatXmlDataSetBuilder.build(this.getClass().getResourceAsStream(this.getClass().getSimpleName()+".xml"));

                DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);

                dataSetInitialized = true;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterClass
    public static void closeEntityManager()
    {
        entityManager.close();
        entityManagerFactory.close();
    }
}
