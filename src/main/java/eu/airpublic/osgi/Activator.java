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

import eu.airpublic.RawAirQualityProtocolStackConnector;
import eu.airpublic.RawAirQualityReadingHttpRequestHandler;
import eu.airpublic.RawAirQualityReadingPacket;
import eu.airpublic.Server;
import fr.cea.sna.gateway.core.model.Resource;
import fr.cea.sna.gateway.generic.core.*;
import fr.cea.sna.gateway.sthbnd.http.HttpPacket;
import fr.cea.sna.gateway.sthbnd.http.HttpSnaProcessor;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import fr.cea.sna.gateway.sthbnd.http.HttpSnaManager;


import fr.cea.sna.gateway.util.mediator.AbstractActivator;

/**
 * Bundle Activator
 */
public class Activator extends AbstractActivator<AbstractMediator>
{

  //private HttpProtocolStackConnector connector = null;
  private HttpSnaManager manager = null;
  private RawAirQualityReadingHttpRequestHandler handler = null;
  private Server<RawAirQualityReadingPacket> server = null;

    /**
	 * @inheritDoc
	 *
	 * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#doStart()
	 */
	@Override
	public void doStart() throws Exception
	{
        RawAirQualityProtocolStackConnector connector = new RawAirQualityProtocolStackConnector(mediator);

		manager = new HttpSnaManager(super.mediator,  "resource.xml", null);
		manager.setServiceType(SnaService.class);
		manager.setResourceType(SnaResource.class);
		manager.setServiceProviderType(SnaServiceProvider.class);
		manager.setStartAtInitializationTime(true);
		manager.setUpdatePolicy(Resource.UpdatePolicy.AUTO);

        SnaProcessor<HttpPacket> processor = manager.connect(connector);

        handler = new RawAirQualityReadingHttpRequestHandler(mediator, processor);
        server = new Server<RawAirQualityReadingPacket>(mediator, handler, processor);


        server.start("127.0.0.1", 8789);
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
		if (this.server != null) {
			this.server.stop();
		}
		this.server = null;
		this.handler = null;
		this.manager = null;
	}

	/**
	 * @inheritDoc
	 *
	 * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#
	 * doInstantiate(org.osgi.framework.BundleContext, int, java.io.FileOutputStream)
	 */
	@Override
	public AbstractMediator doInstantiate(BundleContext context, int logMode,
										 FileOutputStream output) throws InvalidSyntaxException
	{
		return new AbstractMediator(context, logMode, output) {
		    @Override
            public void doDeactivate() {};
		};
  }
}
