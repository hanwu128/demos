package until;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceMapping extends MappingSqlQuery {

    public ResourceMapping(DataSource dataSource,String resourceQuery){
        super(dataSource,resourceQuery);
        compile();
    }

    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        String url = rs.getString(1);
        String role = rs.getString(2);
        Resource resource = new Resource(url,role);
        return resource;
    }
}
