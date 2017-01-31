/**
 * #%L
 * sensiNact IoT Gateway - Core
 * %%
 * Copyright (C) 2015 CEA
 * %%
 * sensiNact - 2015
 *
 * CEA - Commissariat a l'energie atomique et aux energies alternatives
 * 17 rue des Martyrs
 * 38054 Grenoble
 * France
 *
 * Copyright(c) CEA
 * All Rights Reserved
 * #L%
 */
package eu.airpublic.osgi;

import java.io.FileOutputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

import eu.airpublic.RawAirQualityProtocolStackConnector;
import eu.airpublic.RawAirQualityReadingHttpRequestHandler;
import fr.cea.sna.gateway.generic.core.SnaResource;
import fr.cea.sna.gateway.generic.core.SnaService;
import fr.cea.sna.gateway.generic.core.SnaServiceProvider;
import fr.cea.sna.gateway.sthbnd.http.HttpSnaManager;
import fr.cea.sna.gateway.util.mediator.AbstractActivator;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Bundle Activator
 */
public class Activator extends AbstractActivator<AbstractMediator>
{
  private static final Logger LOG = Logger.getLogger(Activator.class.getCanonicalName());
  private static final String ROOT = "/air-quality";
  
  private RawAirQualityProtocolStackConnector connector;  
  private ServiceTracker<HttpService, HttpService> tracker = null;

    /**
	 * @inheritDoc
	 *
	 * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#doStart()
	 */
	@Override
	public void doStart() throws Exception
	{
        this.tracker = new ServiceTracker<HttpService, HttpService>(
        	super.mediator.getContext(), HttpService.class.getName(), 
        		null)
        {
            /**
             * @see ServiceTracker#addingService(org.osgi.framework.ServiceReference)
             */
            public HttpService addingService(ServiceReference<HttpService> serviceRef)
            {
            	HttpService httpService = super.addingService(serviceRef);
                Activator.this.registerServlet(httpService);
                LOG.info("Air Quality servlet registered on " + ROOT + " context");
                return httpService;
            }

            /**
             * @see ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
             */
            public void removedService(ServiceReference<HttpService> ref, 
            		HttpService service)
            {
            	Activator.this.unregisterServlet(service);
                super.removedService(ref, service);
                LOG.info("Air Quality servlet unregistered");
            }
        };
        this.tracker.open(true); 
        
        this.connector = new RawAirQualityProtocolStackConnector(mediator);        
        HttpSnaManager manager = new HttpSnaManager(super.mediator, "resource.xml", null);		
		manager.setServiceType(SnaService.class);
		manager.setResourceType(SnaResource.class);
		manager.setServiceProviderType(SnaServiceProvider.class);
		manager.setStartAtInitializationTime(true);
		manager.setBuildDynamically(false);
		connector.connect(manager);
		
	}

	/**
	 * @inheritDoc
	 *
	 * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#
	 * doStop()
	 */
	@Override
	public void doStop() throws Exception
	{
		this.connector.stop();
		if(!this.tracker.isEmpty())
		{
			HttpService[] services = this.tracker.getServices(
				new HttpService[0]);
			int index = 0;
			int length = services.length;
			for(;index < length; index++)
			{
				unregisterServlet(services[index]);
			}
		}
		this.tracker.close();
		
	}

    /**
     * @inheritDoc
     *
     * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#
     * doInstantiate(org.osgi.framework.BundleContext, int, java.io.FileOutputStream)
     */
    public AbstractMediator doInstantiate(BundleContext context, int i, 
    		FileOutputStream outputStream)
            throws InvalidSyntaxException 
    {
        return new AbstractMediator(context, i, outputStream)
        {
			@Override
            public void doDeactivate()
            {
				LOG.log(Level.CONFIG, "Mediator deactivated");
            }
		};
    }

	private void registerServlet(HttpService httpService) 
    {       
        Dictionary<String, String> initParams = new Hashtable<String, String>();
        initParams.put("servlet-name", "AirQuality");

        HttpContext context = httpService.createDefaultHttpContext();
        
		RawAirQualityReadingHttpRequestHandler handler = 
			new RawAirQualityReadingHttpRequestHandler(mediator, connector);
        try 
        { 	
            httpService.registerServlet(ROOT, handler, initParams,context);

        } catch (ServletException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } catch (NamespaceException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void unregisterServlet(HttpService httpService) 
    {
        if (httpService != null)
        {
            httpService.unregister(ROOT);
        }
    }

}
