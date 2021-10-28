package cbd.cass;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("video_sharing");

        //ADDING

        session.execute("INSERT INTO Users (email, name, reg_time, username) VALUES ('manel23@mail.com', 'Manel', '2020-12-12', 'MegaManel23');");
        System.out.println("Inserted user MegaManel23");

        ResultSet result = session.execute("SELECT * FROM users;");
    
        System.out.println(result.all());

        //UPDATING

        session.execute("UPDATE Users SET name='Manel Lopes' WHERE email='manel23@mail.com';");
        System.out.println();
        System.out.println("Updated user MegaManel23");

        result = session.execute("SELECT * FROM users;");
    
        System.out.println(result.all());

        //DELETING

        session.execute("DELETE FROM Users WHERE email='manel23@mail.com';");
        System.out.println();
        System.out.println("Deleted user MegaManel23");

        result = session.execute("SELECT * FROM users;");
    
        System.out.println(result.all());


        //queries
        System.out.println();
        System.out.println("Videos with tag Aveiro:");
        result = session.execute("SELECT * FROM Videos WHERE tags CONTAINS 'Aveiro';");
        System.out.println(result.all());

        System.out.println();
        System.out.println("Tags from video 6:");
        result = session.execute("SELECT tags FROM Videos WHERE id = 6;");
        System.out.println(result.all());

        System.out.println();
        System.out.println("Followers from video 1:");
        result = session.execute("Select * from Followers WHERE video_id = 1;");
        System.out.println(result.all());

    }
}
