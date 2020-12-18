import org.glassfish.jersey.server.Uri;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class MyUriInfo implements UriInfo
{

    URI classuri;

    public MyUriInfo(URI u)
    {
        classuri = u;
    }

    public MyUriInfo(String host, int port, String path)
    {
        this.classuri = UriBuilder.fromUri(host).scheme("http").port(port).path(path).build();
    }

    public MyUriInfo(String path)
    {
        this.classuri = UriBuilder.fromPath(path).build();
    }

    @Override
    public String getPath()
    {
        return null;
    }

    @Override
    public String getPath(boolean decode)
    {
        return null;
    }

    @Override
    public List<PathSegment> getPathSegments()
    {
        return null;
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode)
    {
        return null;
    }

    @Override
    public URI getRequestUri()
    {
        return null;
    }

    @Override
    public UriBuilder getRequestUriBuilder()
    {
        return null;
    }

    @Override
    public URI getAbsolutePath()
    {
        return classuri;
    }

    @Override
    public UriBuilder getAbsolutePathBuilder()
    {
        UriBuilder ub = JerseyUriBuilder.fromUri(classuri);
        return ub;
    }

    @Override
    public URI getBaseUri()
    {
        return null;
    }

    @Override
    public UriBuilder getBaseUriBuilder()
    {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters()
    {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean decode)
    {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters()
    {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean decode)
    {
        return null;
    }

    @Override
    public List<String> getMatchedURIs()
    {
        return null;
    }

    @Override
    public List<String> getMatchedURIs(boolean decode)
    {
        return null;
    }

    @Override
    public List<Object> getMatchedResources()
    {
        return null;
    }

    @Override
    public URI resolve(URI uri)
    {
        return null;
    }

    @Override
    public URI relativize(URI uri)
    {
        return null;
    }
}

