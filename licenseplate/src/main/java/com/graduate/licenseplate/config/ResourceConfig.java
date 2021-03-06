package com.graduate.licenseplate.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class ResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceResolver resolver = new ReactResourceResolver();
		registry.addResourceHandler("/**")
	        .resourceChain(true)
	        .addResolver(resolver);

	}
	
	public class ReactResourceResolver implements ResourceResolver{
		// for index.html
		private static final String REACT_DIR="/static/";
		
		// for react static folder
		private static final String REACT_STATIC_DIR="static";
		
		private Resource index = new ClassPathResource(REACT_DIR+"index.html");
		private List<String> rootStaticFiles = Arrays.asList("favicon.ico",
				"asset-manifest.json",
				"logo192.png",
				"logo512.png",
				"manifest.json",
				"robots.txt");
		
		@Override
        public Resource resolveResource(
            HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {

            return resolve(requestPath, locations);
        }

        @Override
        public String resolveUrlPath(
            String resourcePath, 
            List<? extends Resource> locations,
            ResourceResolverChain chain) {
        	
        	System.out.println("[ResolveUrlPath] : "+ resourcePath + " -- " + locations + " -- " + chain);
            Resource resolvedResource = resolve(resourcePath, locations);
            if (resolvedResource == null) {
                return null;
            }
            try {
                return resolvedResource.getURL().toString();
            } catch (IOException e) {
                return resolvedResource.getFilename();
            }
        }
        
        private Resource resolve(String requestPath, List<? extends Resource> locations) {
        	System.out.println("[Resolve] : "+ requestPath + " -- " + locations);
                if (requestPath == null) return null;

                if (rootStaticFiles.contains(requestPath)
                        || requestPath.startsWith(REACT_STATIC_DIR)) {
                    return new ClassPathResource(REACT_DIR + requestPath);
                } else
                    return index;
            }

	}
}
