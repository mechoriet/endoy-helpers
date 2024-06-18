package dev.endoy.minecraft.helpers.spring.subdomain;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.Set;

@RequiredArgsConstructor
public class SubdomainRequestCondition implements RequestCondition<SubdomainRequestCondition>
{

    private final Set<String> subdomains;

    @Override
    public SubdomainRequestCondition getMatchingCondition( HttpServletRequest request )
    {
        for ( String subdomain : this.subdomains )
        {
            if ( request.getServerName().startsWith( subdomain + "." ) )
            {
                return this;
            }
        }

        return null;
    }

    @Override
    public int compareTo( SubdomainRequestCondition other, HttpServletRequest request )
    {
        return 0;
    }

    @Override
    public SubdomainRequestCondition combine( SubdomainRequestCondition other )
    {
        // not relevant at the moment
        return null;
    }
}
