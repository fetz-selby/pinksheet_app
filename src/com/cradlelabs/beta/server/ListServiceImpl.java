package com.cradlelabs.beta.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.cradlelabs.beta.client.ListService;
import com.cradlelabs.beta.server.utils.DBConnection;
import com.cradlelabs.beta.server.utils.MiscStorage;
import com.cradlelabs.beta.server.utils.Utils;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.BlobstoreModel;
import com.cradlelabs.beta.shared.ConstituencyModel;
import com.cradlelabs.beta.shared.PSUser;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.cradlelabs.beta.shared.PollingStationModel;
import com.cradlelabs.beta.shared.RegionModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ListServiceImpl extends RemoteServiceServlet implements ListService{
	private static Connection con = DBConnection.getConnection();
	
	@Override
	public ArrayList<RegionModel> getAllRegions() {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name from regions");

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<RegionModel> regions = new ArrayList<RegionModel>();
				while(results.next()){

					int id = results.getInt("id");
					String name = results.getString("name");

					RegionModel model = new RegionModel();
					model.setId(id);
					model.setName(name);
					
					regions.add(model);
				}
				return regions;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<ConstituencyModel> getParentConstituencies(int regionId) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name from parent_constituencies where region_id = ?");
			prstmt.setInt(1, regionId);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<ConstituencyModel> constituencies = new ArrayList<ConstituencyModel>();
				while(results.next()){

					int id = results.getInt("id");
					String name = results.getString("name");

					ConstituencyModel model = new ConstituencyModel();
					model.setId(id);
					model.setName(name);
					
					constituencies.add(model);
				}
				return constituencies;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<PollingStationModel> getPollingStations(int constituencyId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select p.id as id,p.name as poll_name,c.name as cons_name,c.id as cons_id from polls as p, constituencies as c where p.cons_id = ? and c.id = p.cons_id and p.year = ?");
			prstmt.setInt(1, getConstituencyId(constituencyId, year));
			prstmt.setString(2, year);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<PollingStationModel> polls = new ArrayList<PollingStationModel>();
				while(results.next()){

					int id = results.getInt("id");
					int consId = results.getInt("cons_id");
					String name = results.getString("poll_name");
					String consName = results.getString("cons_name");

					PollingStationModel model = new PollingStationModel();
					model.setId(id);
					model.setName(name);
					model.setConstituenyId(consId);
					model.setConsName(consName);
					
					polls.add(model);
				}
				return polls;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}
	
	private int getConstituencyId(int parentConstituencyId, String year){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id from constituencies where parent_id = ? and year = ?");
			prstmt.setInt(1, parentConstituencyId);
			prstmt.setString(2, year);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					return results.getInt("id");
				}
				return 0;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public ArrayList<AgentModel> getAgentsWithConsId(int constituencyId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select w.id as id,w.name as name,w.msisdn as msisdn,w.email as email,w.blob_key as blob_key,w.cons_id as cons_id,w.poll_id as poll_id,p.name as polling_station,c.name as constituency from web_agents as w, polls as p, constituencies as c where w.cons_id = ? and w.year = ? and w.poll_id = p.id and c.id = w.cons_id and w.status = 'A' order by id desc");
			prstmt.setInt(1, constituencyId);
			prstmt.setString(2, year);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<AgentModel> agents = new ArrayList<AgentModel>();
				while(results.next()){

					int id = results.getInt("id");
					int consId = results.getInt("cons_id");
					int pollId = results.getInt("poll_id");
					String pollname = results.getString("polling_station");
					String name = results.getString("name");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String blobKey = results.getString("blob_key");
					String constituency = results.getString("constituency");
					
					AgentModel model = new AgentModel();
					model.setId(id);
					model.setName(name);
					model.setConstituencyId(consId);
					model.setPollingStationId(pollId);
					model.setPollingStation(pollname);
					model.setBlobKey(blobKey);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setConstituency(constituency);
					model.setYear(year);
					
					agents.add(model);
				}
				return agents;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<AgentModel> getAgentsWithPollingId(int pollingStationId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select w.id as id,w.name as name,w.msisdn as msisdn,w.email as email,w.blob_key as blob_key,w.cons_id as cons_id,w.poll_id as poll_id,p.name as polling_station,c.name as constituency from web_agents as w, polls as p, constituencies as c where w.poll_id = ? and w.year = ? and w.poll_id = p.id and c.id = p.cons_id and w.status = 'A' order by id desc");
			prstmt.setInt(1, pollingStationId);
			prstmt.setString(2, year);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<AgentModel> agents = new ArrayList<AgentModel>();
				while(results.next()){

					int id = results.getInt("id");
					int consId = results.getInt("cons_id");
					int pollId = results.getInt("poll_id");
					String pollname = results.getString("polling_station");
					String name = results.getString("name");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String blobKey = results.getString("blob_key");
					String constituency = results.getString("constituency");

					
					AgentModel model = new AgentModel();
					model.setId(id);
					model.setName(name);
					model.setConstituencyId(consId);
					model.setPollingStationId(pollId);
					model.setPollingStation(pollname);
					model.setBlobKey(blobKey);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setConstituency(constituency);
					model.setYear(year);
					
					agents.add(model);
				}
				return agents;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}
	
	@Override
	public BlobstoreModel retrieveFromBlobstore(String tmpblobKey) {
		BlobKey blobKey = new BlobKey(tmpblobKey);
		
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
		BlobstoreModel model = new BlobstoreModel();
		model.setBlobkey(tmpblobKey);
		
		//try{
			model.setUrl(imagesService.getServingUrl(blobKey));
			return model;

		//}catch(Exception e){
		//	return null;
		//}
	}
	
	@Override
	public BlobstoreModel requestPreviewImage() {
		
		BlobstoreModel model = new BlobstoreModel();
		model.setBlobkey(MiscStorage.getInstance().getImgBlobKey().getKeyString());
		model.setUrl(MiscStorage.getInstance().getImgUrl());
		
		return model;
	}
	
	@Override
	public String getUploadUrl(String path) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		return blobstoreService.createUploadUrl(path);
	}

	@Override
	public UserModel getUser(String tmpMsisdn) {
		
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			
			if(!tmpMsisdn.contains("+")){
				tmpMsisdn = "+"+tmpMsisdn;
			}
			
			prstmt = (PreparedStatement) con.prepareStatement("select id,name,email,msisdn,perm,blob_key from web_users where msisdn = ? and status = 'A'");
			prstmt.setString(1, tmpMsisdn);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					int id = results.getInt("id");
					String name = results.getString("name");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String blobKey = results.getString("blob_key");
					String perm = results.getString("perm");
					
					UserModel model = new UserModel();
					model.setId(id);
					model.setName(name);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setBlobKey(blobKey);
					model.setPerm(perm);

					return model;
				}
				return null;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public AgentModel getAgent(String tmpMsisdn) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			if(!tmpMsisdn.contains("+")){
				tmpMsisdn = "+"+tmpMsisdn;
			}
			
			prstmt = (PreparedStatement) con.prepareStatement("select a.id as id,a.name as name,a.email as email,a.msisdn as msisdn,a.blob_key as blob_key,a.year as year,a.poll_id as poll_id,a.cons_id as cons_id,c.name as constituency,p.name as polling_station from web_agents as a, constituencies as c, polls as p where a.msisdn = ? and a.status = 'A' and c.id = a.cons_id and p.id = a.poll_id");
			prstmt.setString(1, tmpMsisdn);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					int id = results.getInt("id");
					int pollId = results.getInt("poll_id");
					int consId = results.getInt("cons_id");
					String name = results.getString("name");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String blobKey = results.getString("blob_key");
					String constituency = results.getString("constituency");
					String pollingStation = results.getString("polling_station");
					String year = results.getString("year");
					
					
					AgentModel model = new AgentModel();
					model.setId(id);
					model.setName(name);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setBlobKey(blobKey);
					model.setConstituency(constituency);
					model.setPollingStation(pollingStation);
					model.setYear(year);
					model.setPollingStationId(pollId);
					model.setConstituencyId(consId);

					return model;
				}
				return null;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<UserModel> getUsers(String tmpPerm) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name,msisdn,email,blob_key,perm from web_users where perm = ? and status = 'A' order by id desc");
			prstmt.setString(1, tmpPerm);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<UserModel> users = new ArrayList<UserModel>();
				while(results.next()){

					int id = results.getInt("id");
					String name = results.getString("name");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String blobKey = results.getString("blob_key");
					String perm = results.getString("perm");

					
					UserModel model = new UserModel();
					model.setId(id);
					model.setName(name);
					model.setBlobKey(blobKey);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setPerm(perm);
					
					users.add(model);
				}
				return users;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public PSUser requestLogin(String username, String password) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			String statement = "";
			
			if(Utils.isMsisdnValid(username)){
				if(!username.contains("+")){
					username = "+"+username;
				}
				
				statement = "select id,email,msisdn,type from ps_users where msisdn = ? and password = ? and status = 'A'";
				
			}else{
				statement = "select id,email,msisdn,type from ps_users where email = ? and password = ? and status = 'A'";
			}
			
			prstmt = (PreparedStatement) con.prepareStatement(statement);
			prstmt.setString(1, username);
			prstmt.setString(2, Utils.getSHA256(password));

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					int id = results.getInt("id");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String type = results.getString("type");
					
					PSUser model = new PSUser();
					model.setId(id);
					model.setEmail(email);
					model.setMsisdn(msisdn);
					model.setType(type);

					return model;
				}
				return null;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<PinkSheetModel> getPinkSheets(int pollId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,agent_name,msisdn,email,blob_key,cons_id,poll_id,created_ts,constituency,pollingstation from pinksheets where poll_id = ? and year = ? and status = 'A'");
			prstmt.setInt(1, pollId);
			prstmt.setString(2, year);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<PinkSheetModel> pinkSheets = new ArrayList<PinkSheetModel>();
				while(results.next()){
					int id = results.getInt("id");
					int consId = results.getInt("cons_id");
					String msisdn = results.getString("msisdn");
					String email = results.getString("email");
					String agentName = results.getString("agent_name");
					String blobKey = results.getString("blob_key");
					String createdTs = results.getString("created_ts");
					String constituency = results.getString("constituency");
					String pollingStation = results.getString("pollingstation");
					
					PinkSheetModel pinkSheet = new PinkSheetModel();
					pinkSheet.setId(id);
					pinkSheet.setConstituencyId(consId);
					pinkSheet.setMsisdn(msisdn);
					pinkSheet.setEmail(email);
					pinkSheet.setAgentName(agentName);
					pinkSheet.setBlobKey(blobKey);
					pinkSheet.setDate(createdTs);
					pinkSheet.setConstituency(constituency);
					pinkSheet.setPollingStation(pollingStation);
					pinkSheet.setYear(year);
					pinkSheet.setPollId(pollId);
					
					pinkSheets.add(pinkSheet);
				}
				return pinkSheets;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}
	
	private String getFormattedNumber(String pattern, int number){
		DecimalFormat decimal = new DecimalFormat(pattern);
		return decimal.format(number);
	}

	@Override
	public String agentsCount() {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select count(id) as count from web_agents where year = ? and status = ?");
			prstmt.setString(1, "2016");
			prstmt.setString(2, "A");

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					return getFormattedNumber("###,###", results.getInt("count"));
				}
				return "0";
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return "0";
	}

	@Override
	public String usersCount() {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select count(id) as count from web_users where status = ?");
			prstmt.setString(1, "A");

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					return getFormattedNumber("###,###", results.getInt("count"));
				}
				return "0";
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return "0";
	}

	@Override
	public String pinkSheetsCount() {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select count(id) as count from pinksheets where status = ?");
			prstmt.setString(1, "A");

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					return getFormattedNumber("###,###", results.getInt("count"));
				}
				return "0";
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return "0";
	}

	@Override
	public String pinkSheetsCount(int pollId) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select count(id) as count from pinksheets where poll_id = ? and status = ?");
			prstmt.setInt(1, pollId);
			prstmt.setString(2, "A");

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					return getFormattedNumber("###,###", results.getInt("count"));
				}
				return "0";
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return "0";
	}

}
