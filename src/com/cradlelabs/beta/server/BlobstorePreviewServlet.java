package com.cradlelabs.beta.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cradlelabs.beta.server.utils.MiscStorage;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

@SuppressWarnings("serial")
public class BlobstorePreviewServlet extends HttpServlet{

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		final String SCALE_350_PX = "=s350";

		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("image");
		
		if(blobKeys != null){
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			String blobKeyString = blobKeys.get(0).getKeyString();
			BlobKey blobKey = new BlobKey(blobKeyString);
			
			System.out.println("[GET]blobKey => "+blobKeyString);

			String url = imagesService.getServingUrl(blobKey)+SCALE_350_PX;
			
			System.out.println("blobKey => "+blobKeyString+" img => "+url);

			MiscStorage.getInstance().setImgBlobKey(blobKey);
			MiscStorage.getInstance().setImgUrl(url);
			
			resp.getWriter().print("s");
			resp.flushBuffer();
		}else{
			System.out.println("Blob fetch was null");
		}
	
		//super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Came inn ...");


		final String SCALE_350_PX = "=s350";
		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("image");
		
		if(blobKeys != null){
			
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			String blobKeyString = blobKeys.get(0).getKeyString();
			
			System.out.println("[POST]blobKey => "+blobKeyString);

			BlobKey blobKey = new BlobKey(blobKeyString);
			
			String url = imagesService.getServingUrl(blobKey)+SCALE_350_PX;
			
			System.out.println("blobKey => "+blobKeyString+" img => "+url);

			MiscStorage.getInstance().setImgBlobKey(blobKey);
			MiscStorage.getInstance().setImgUrl(url);
			
			resp.getWriter().print("s");
			resp.flushBuffer();
		}else{
			System.out.println("Blob fetch was null");
		}
	}
}
