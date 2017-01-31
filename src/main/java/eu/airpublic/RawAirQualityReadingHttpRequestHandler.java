package eu.airpublic;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.cea.sna.gateway.sthbnd.http.HttpPacket;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Created by john on 27/01/17.
 */
public class RawAirQualityReadingHttpRequestHandler extends HttpServlet
{

    AbstractMediator mediator;
    RawAirQualityProtocolStackConnector connector;

    public RawAirQualityReadingHttpRequestHandler(AbstractMediator mediator,
    		RawAirQualityProtocolStackConnector connector)
    {
        this.mediator = mediator;
        this.connector = connector;
    }

    /**
     * @throws IOException 
     * @inheritDoc
     *
     * @see javax.servlet.http.HttpServlet#
     * doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws IOException
    {
        this.mediator.warn("Airpublic: process HTTP request");
        
		int read = 0;
		int length = 0;
		byte[] content = new byte[length];
		byte[] buffer = new byte[60*1024];

		try
		{
			InputStream input = req.getInputStream();	
			int contentLength = req.getContentLength();
			
			while(true)
			{
				if(contentLength > -1 && length >= contentLength)
				{
					break;
				}
				read = input.read(buffer);
				if(read == -1)
				{
					break;
				}
				byte[] newContent = new byte[length+read];
				if(length > 0)
				{
					System.arraycopy(content, 0, newContent, 0, length);
				}
				System.arraycopy(buffer, 0, newContent, length, read);
				content = newContent;
				newContent = null;
				length+=read;
			}	
            this.connector.process(new HttpPacket(content));
            res.setStatus(200);
            
		} catch(Exception e)
		{
			mediator.error(e.getMessage(), e);
			res.setStatus(520, e.getMessage());
			
		} finally
		{
			res.flushBuffer();
		}
    }
}
