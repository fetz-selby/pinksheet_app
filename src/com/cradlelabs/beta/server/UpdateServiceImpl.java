package com.cradlelabs.beta.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cradlelabs.beta.client.UpdateService;
import com.cradlelabs.beta.server.utils.DBConnection;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UpdateServiceImpl extends RemoteServiceServlet implements UpdateService{
	private static Connection con = DBConnection.getConnection();

	@Override
	public void deleteOrphanBlob(String tmpblobKey) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		BlobKey blobKey = new BlobKey(tmpblobKey);
		blobstoreService.delete(blobKey);	
	}

	@Override
	public int removeAgent(AgentModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update web_agents set status = 'D' where id = ? and year = ? and poll_id = ?");
			prstmt.setInt(1, model.getId());
			prstmt.setString(2, model.getYear());
			prstmt.setInt(3, model.getPollingStationId());

			int success = prstmt.executeUpdate();
			System.out.println("[Agent] success is "+success);
			if(success > 0){
				return 1;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return 0;
	}

	@Override
	public int updateUser(UserModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update web_users set name = ?, perm = ?, blob_key = ? where id = ? and status = 'A'");
			prstmt.setString(1, model.getName());
			prstmt.setString(2, model.getPerm());
			prstmt.setString(3, model.getBlobKey());
			prstmt.setInt(4, model.getId());

			int success = prstmt.executeUpdate();
			System.out.println("[User] success is "+success);
			if(success > 0){
				return 1;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return 0;
	}

}
