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
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

import fr.cea.sna.gateway.util.mediator.AbstractActivator;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Bundle Activator
 */
public class Activator extends AbstractActivator<AbstractMediator>
{		
    /**
	 * @inheritDoc
	 *
	 * @see fr.cea.sna.gateway.util.mediator.AbstractActivator#doStart()
	 */
	@Override
	public void doStart() throws Exception
	{    		
		
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
		return new AbstractMediator(context,logMode,output)
		{
			public void doDeactivate(){};
		};
	}	
}
