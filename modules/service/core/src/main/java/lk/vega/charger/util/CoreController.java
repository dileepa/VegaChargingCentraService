package lk.vega.charger.util;

import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import lk.vega.charger.util.connection.ConnectionPool;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by dileepa on 3/20/15.
 */
public class CoreController
{
    public static ConnectionPool pool = null;
    public static int AUTH_KEY_EXPIRE_TIMEOUT_VAL = 5; //value of seconds for expire transaction or not.
    public static String AUTH_KEY_EXPIRE_TIMEOUT_KEY = "AUTH_KEY_EXPIRE_TIMEOUT";


    public static void init()
    {
        try
        {
            pool = CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }


    public static ChgResponse save(  Object obj )
    {
        ChgResponse err = new ChgResponse( ChgResponse.SUCCESS, "Sucessfully Saved....." );

        try
        {
            Connection con = pool.getConnection();
            try
            {
                con.setAutoCommit( false );
                Savable savObj;
                if( obj instanceof Savable )
                {
                    savObj = (Savable) obj;
                    int status = savObj.getStatus();
                    savObj.save( con );

                }
                else
                {
                    err = new ChgResponse( ChgResponse.ERROR, "Unknown Object Type '" + obj.getClass().getName() );
                }

                con.commit();
            }
            catch( SavingSQLException se )
            {
                try
                {
                    con.rollback();
                }
                catch( SQLException e )
                {
                    e.printStackTrace();
                }
                err = new ChgResponse( ChgResponse.ERROR,se.getMessage());

            }
            catch( Exception ne )
            {
                try
                {
                    con.rollback();
                }
                catch( SQLException e )
                {
                    e.printStackTrace();
                }
                err = new ChgResponse( ChgResponse.ERROR, ne.getMessage() );
            }
            finally
            {
                DBUtility.close( con );
            }
        }
        catch( SQLException se )
        {
            err = new ChgResponse( ChgResponse.ERROR, "ERROR: in connecting to database!" );
        }
        return err;
    }


    public static void loadServiceConfigurations ()
    {
        if ( System.getProperty("vega.config.path") != null )
        {
            String confFile = System.getProperty("vega.config.path");
            Properties configurations = new Properties();
            try
            {
                configurations.load(new FileInputStream(confFile));
                AUTH_KEY_EXPIRE_TIMEOUT_VAL = Integer.parseInt( configurations.getProperty( AUTH_KEY_EXPIRE_TIMEOUT_KEY ) );
            }
            catch( Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
